import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    static int N, M;
    static int[][] arr;
    static boolean[][] visited;

    static int[] dr = { 1, -1, 0, 0 };
    static int[] dc = { 0, 0, 1, -1 };

    static int answer = -1;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        arr = new int[N][M];
        visited = new boolean[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                visited[i][j] = true;
                dfs(i, j, 1, arr[i][j]);
                visited[i][j] = false;
            }
        }

        System.out.println(answer);
    }

    private static void dfs(int r, int c, int depth, int sum) {

        if (depth == 4) {
            answer = Math.max(answer, sum);
            return;
        }

        for (int i = 0; i < 4; i++) {

            int nr = r + dr[i];
            int nc = c + dc[i];

            if (!inRange(nr, nc) || visited[nr][nc]) {
                continue;
            }

            // ㅗ 모양 처리
            if (depth == 2) {
                // 위치는 r, c에 그대로 있고 nr, nc 값만 추가
                visited[nr][nc] = true;
                dfs(r, c, depth + 1, sum + arr[nr][nc]);
                visited[nr][nc] = false;
            }

            // 일반 DFS
            visited[nr][nc] = true;
            dfs(nr, nc, depth + 1, sum + arr[nr][nc]);
            visited[nr][nc] = false;
        }
    }

    private static boolean inRange(int r, int c) {
        return 0 <= r && r < N && 0 <= c && c < M;
    }

}
