import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    static int N, L, R;

    static int[][] egg;
    static boolean[][] visited;

    static final int[] dr = { 1, -1, 0, 0 };
    static final int[] dc = { 0, 0, 1, -1 };

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        L = Integer.parseInt(st.nextToken());
        R = Integer.parseInt(st.nextToken());

        egg = new int[N][N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                egg[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        
        int answer = 0;

        while (true) {

            // 하루가 시작될 때 방문 배열 초기화
            visited = new boolean[N][N];

            // 오늘 계란 이동이 발생했는지
            boolean moved = false;

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (!visited[i][j]) {
                        if (bfs(i, j)) {
                            moved = true;
                        }
                    }
                }
            }

            // 오늘 아무 이동도 없었다면 종료
            if (!moved) {
                break;
            }

            answer++;
        }

        System.out.println(answer);
    }

    private static boolean bfs(int r, int c) {

        Queue<int[]> queue = new LinkedList<>();

        // 현재 연합에 포함된 칸 저장
        ArrayList<int[]> list = new ArrayList<>();

        queue.offer(new int[] { r, c });
        list.add(new int[] { r, c });

        visited[r][c] = true;

        int sum = egg[r][c];

        while (!queue.isEmpty()) {

            int[] cur = queue.poll();

            for (int i = 0; i < 4; i++) {
                int nr = cur[0] + dr[i];
                int nc = cur[1] + dc[i];

                if (nr < 0 || nr >= N || nc < 0 || nc >= N) {
                    continue;
                }

                if (visited[nr][nc]) {
                    continue;
                }

                int diff = Math.abs(egg[cur[0]][cur[1]] - egg[nr][nc]);

                if (diff >= L && diff <= R) {

                    visited[nr][nc] = true;

                    queue.offer(new int[] { nr, nc });
                    list.add(new int[] { nr, nc });

                    sum += egg[nr][nc];
                }
            }
        }

        // 연합이 1칸이면 이동 없음
        if (list.size() == 1) {
            return false;
        }

        int avg = sum / list.size();

        // 현재 연합에 속한 칸만 평균값으로 변경
        for (int[] pos : list) {
            egg[pos[0]][pos[1]] = avg;
        }

        return true;
    }
}