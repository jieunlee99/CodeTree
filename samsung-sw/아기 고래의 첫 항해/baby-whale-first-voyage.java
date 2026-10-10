import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    static int N, r, c, d;
    static int[][] sea;
    static boolean[][] visited;

    // 이동 방향: 상, 하, 좌, 우
    static final int[] dr = { 0, -1, 1, 0, 0 };
    static final int[] dc = { 0, 0, 0, -1, 1 };

    // 직진 -> 좌회전 -> 위회전 -> 180도 회전
    static final int[][] priority = { {}, { 1, 3, 4, 2 }, { 2, 4, 3, 1 }, { 3, 2, 1, 4 }, { 4, 1, 2, 3 } };

    // 최단 경로 이동 시 우선순위: 왼쪽, 아래, 오른쪽, 위
    static final int[] moveOrder = { 3, 2, 4, 1 };

    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        d = Integer.parseInt(st.nextToken());

        sea = new int[N + 1][N + 1];
        visited = new boolean[N + 1][N + 1];

        for (int i = 1; i <= N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= N; j++) {
                sea[i][j] = Integer.parseInt(st.nextToken()); // 0: 바다, 1: 암초
            }
        }

        visited[r][c] = true;
        sb.append(r).append(" ").append(c).append("\n");

        while (true) {
            // 1. 인접 탐험
            if (explore()) {
                continue;
            }

            // 2. 가장 가까운 바다로 이동
            int[] target = findTarget(); // BFS
            if (target == null) {
                break;
            }
            moveToTarget(target);
        }

        System.out.println(sb.toString());
    }

    private static void moveToTarget(int[] target) {

        int tr = target[0];
        int tc = target[1];

        int[][] dist = getDistance(tr, tc);

        while (r != tr || c != tc) {
            for (int nd : moveOrder) {
                int nr = r + dr[nd];
                int nc = c + dc[nd];

                if (!inRange(nr, nc)) {
                    continue;
                }

                if (dist[nr][nc] == dist[r][c] - 1) {
                    move(nr, nc, nd);
                    break;
                }
            }
        }

    }

    private static int[][] getDistance(int tr, int tc) {

        int[][] dist = new int[N + 1][N + 1];

        for (int[] row : dist) {
            Arrays.fill(row, -1);
        }

        Queue<int[]> queue = new ArrayDeque<>();

        queue.offer(new int[] { tr, tc });
        dist[tr][tc] = 0;

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();

            int cr = cur[0];
            int cc = cur[1];

            for (int dir = 1; dir <= 4; dir++) {
                int nr = cr + dr[dir];
                int nc = cc + dc[dir];

                if (!inRange(nr, nc)) {
                    continue;
                }

                if (sea[nr][nc] == 1) {
                    continue;
                }

                if (dist[nr][nc] != -1) {
                    continue;
                }

                dist[nr][nc] = dist[cr][cc] + 1;
                queue.offer(new int[] { nr, nc });
            }
        }

        return dist;
    }

    private static int[] findTarget() {
        Queue<int[]> queue = new ArrayDeque<>();
        boolean[][] checked = new boolean[N + 1][N + 1];

        queue.offer(new int[] { r, c, 0 });
        checked[r][c] = true;

        int minDist = -1;
        int[] target = null;

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();

            int cr = cur[0];
            int cc = cur[1];
            int dist = cur[2];

            // 최소 거리보다 멀면 종료
            if (minDist != -1 && dist > minDist) {
                break;
            }

            if (!visited[cr][cc]) {
                minDist = dist;

                // 거리가 같으면 행 작은 순 -> 열 작은 순
                if (target == null || cr < target[0] || (cr == target[0] && cc < target[1])) {
                    target = new int[] { cr, cc };
                }

                continue;
            }

            for (int dir = 1; dir <= 4; dir++) {
                int nr = cr + dr[dir];
                int nc = cc + dc[dir];

                if (!inRange(nr, nc)) {
                    continue;
                }

                if (sea[nr][nc] == 1) {
                    continue;
                }

                if (checked[nr][nc]) {
                    continue;
                }

                checked[nr][nc] = true;
                queue.offer(new int[] { nr, nc, dist + 1 });
            }
        }

        return target;
    }

    private static boolean explore() {

        for (int nd : priority[d]) {
            int nr = r + dr[nd];
            int nc = c + dc[nd];

            if (!inRange(nr, nc)) {
                continue;
            }

            if (sea[nr][nc] == 1) {
                continue;
            }

            if (visited[nr][nc]) {
                continue;
            }

            move(nr, nc, nd);

            return true;
        }

        return false;
    }

    private static void move(int nr, int nc, int nd) {

        r = nr;
        c = nc;
        d = nd;

        if (!visited[r][c]) {
            visited[r][c] = true;
            sb.append(r).append(" ").append(c).append("\n");
        }

    }

    private static boolean inRange(int r, int c) {
        return 1 <= r && r <= N && 1 <= c && c <= N;
    }

}
