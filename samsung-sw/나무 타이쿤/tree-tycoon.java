import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    // 8 방향으로 이동
    static int[] dr = { 0, -1, -1, -1, 0, 1, 1, 1 };
    static int[] dc = { 1, 1, 0, -1, -1, -1, 0, 1 };

    static int N, M;
    static int[][] heights;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken()); // 격자의 크기: 3 <= N <= 15
        M = Integer.parseInt(st.nextToken()); // 리브로수를 키우는 총 년 수: 1 <= M <= 100

        heights = new int[N][N]; // 각 리브로수의 높이 저장: 0 <= heights[i][j] <= 100

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                heights[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        Queue<int[]> queue = new LinkedList<>();

        // 초기 특수 영양제 위치 (좌하단 4개의 칸)
        queue.offer(new int[] { N - 1, 0 });
        queue.offer(new int[] { N - 1, 1 });
        queue.offer(new int[] { N - 2, 0 });
        queue.offer(new int[] { N - 2, 1 });

        for (int year = 0; year < M; year++) {
            st = new StringTokenizer(br.readLine());
            int d = Integer.parseInt(st.nextToken()); // 이동 방향
            int p = Integer.parseInt(st.nextToken()); // 이동 칸 수

            boolean[][] used = new boolean[N][N]; // 해당 연도에 영양제가 있던 위치 체

            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int[] cur = queue.poll();

                int nr = (cur[0] + dr[d - 1] * p % N + N) % N;
                int nc = (cur[1] + dc[d - 1] * p % N + N) % N;

                queue.offer(new int[] { nr, nc });

                // 일단 새로 영양제가 온 칸 +1
                heights[nr][nc]++;

                used[nr][nc] = true;
            }

            size = queue.size();

            // 대각선 방향에 1 이상인 리브로수의 개수만큼 증가
            for (int i = 0; i < size; i++) {
                int[] cur = queue.poll();

                int cnt = 0;

                // 대각선 방향 -> 1, 3, 5, 7
                for (int k = 1; k < 8; k += 2) {
                    int nr = cur[0] + dr[k];
                    int nc = cur[1] + dc[k];

                    if (nr < 0 || nr >= N || nc < 0 || nc >= N) {
                        continue;
                    }

                    if (heights[nr][nc] >= 1) {
                        cnt++;
                    }
                }

                heights[cur[0]][cur[1]] += cnt;

                queue.offer(cur);
            }

            // 기존 영양제 제거
            queue.clear();

            // 올해 영양제가 없던 칸 중 높이가 2 이상이면 영양제 추가, 해당 위치 높이 -2
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < N; c++) {

                    if (used[r][c]) {
                        continue;
                    }

                    if (heights[r][c] >= 2) {
                        heights[r][c] -= 2;
                        queue.offer(new int[] { r, c });
                    }
                }
            }
        }

        // M년 이후 남아있는 리브로수의 총 높이의 합 출력
        int heightSum = 0;
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                heightSum += heights[r][c];
            }
        }
        System.out.println(heightSum);
    }

}
