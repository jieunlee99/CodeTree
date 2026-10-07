import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    static int N, M;

    static int[][] map;
    static boolean[][] visited;

    // 북, 동, 남, 서
    static final int[] dr = { -1, 0, 1, 0 };
    static final int[] dc = { 0, 1, 0, -1 };

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new int[N][M];
        visited = new boolean[N][M];

        // 초기 위치, 방향
        st = new StringTokenizer(br.readLine());
        int r = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken());
        int d = Integer.parseInt(st.nextToken());

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                // 0:도로, 1:인도
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int answer = 0;

        while (true) {
            if (!visited[r][c]) {
                visited[r][c] = true;
                answer++;
            }

            boolean moved = false;

            // 네 방향 확인
            for (int i = 0; i < 4; i++) {
                // 왼쪽으로 회전
                d = (d - 1 + 4) % 4;

                int nr = r + dr[d];
                int nc = c + dc[d];

                if (inRange(nr, nc) && map[nr][nc] == 0 && !visited[nr][nc]) {
                    r = nr;
                    c = nc;

                    moved = true;

                    break;
                }
            }

            if (moved) {
                continue;
            }

            // 네 방향 모두 갈 수 없다면 후진
            int backR = r - dr[d];
            int backC = c - dc[d];

            if (!inRange(backR, backC) || map[backR][backC] == 1) {
                break;
            }

            r = backR;
            c = backC;
        }

        System.out.println(answer);
    }

    private static boolean inRange(int r, int c) {
        return 0 <= r && r < N && 0 <= c && c < M;
    }
}
