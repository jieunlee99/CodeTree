import java.awt.Robot;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static int N, M, T;
    static int[][] room;

    static int[] dr = { 1, -1, 0, 0 };
    static int[] dc = { 0, 0, 1, -1 };

    static int wind = -1;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken()); // 행
        M = Integer.parseInt(st.nextToken()); // 열
        T = Integer.parseInt(st.nextToken()); // 시간

        room = new int[N][M];

        boolean findWind = false;

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                room[i][j] = Integer.parseInt(st.nextToken());
            }

            // 시공의 돌풍이 설치되어 있는 칸
            if (room[i][0] == -1 && !findWind) {
                wind = i;
                findWind = true;
            }
        }

        while (T-- > 0) {
            // 1. 먼지가 인접한 4방향의 상하좌우 칸으로 확산
            diffuseDust();

            // 2. 청소 시작
            cleanRoom();
        }

        // T초가 지난 후 방에 남아있는 먼지의 양 출력
        int answer = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (room[i][j] == -1)
                    continue;
                answer += room[i][j];
            }
        }
        System.out.println(answer);
    }

    private static int[][] copy(int[][] arr) {
        int[][] result = new int[N][M];

        for (int i = 0; i < N; i++) {
            result[i] = arr[i].clone();
        }

        return result;
    }

    private static void diffuseDust() {
        int[][] temp = new int[N][M];

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < M; c++) {
                int dust = room[r][c] / 5;

                if (dust == 0)
                    continue;

                for (int d = 0; d < 4; d++) {
                    int nr = r + dr[d];
                    int nc = c + dc[d];

                    if (!inRange(nr, nc)) {
                        continue;
                    }

                    if ((nr == wind || nr == wind + 1) && nc == 0) {
                        continue;
                    }

                    temp[nr][nc] += dust;
                    temp[r][c] -= dust;
                }
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                room[i][j] += temp[i][j];
            }
        }
    }

    private static boolean inRange(int r, int c) {
        return 0 <= r && r < N && 0 <= c && c < M;
    }

    private static void cleanRoom() {
        rotateLeft(wind);
        rotateRight(wind + 1);
    }

    // 위쪽 돌풍: 반시계 방향
    private static void rotateLeft(int wind) {
        int[][] temp = copy(room);

        // 왼쪽 열: 아래로 이동
        for (int r = wind - 1; r > 0; r--) {
            temp[r][0] = room[r - 1][0];
        }

        // 위쪽 행: 왼쪽으로 이동
        for (int c = 0; c < M - 1; c++) {
            temp[0][c] = room[0][c + 1];
        }

        // 오른쪽 열: 위로 이동
        for (int r = 0; r < wind; r++) {
            temp[r][M - 1] = room[r + 1][M - 1];
        }

        // 돌풍 행: 오른쪽으로 이동
        for (int c = M - 1; c > 1; c--) {
            temp[wind][c] = room[wind][c - 1];
        }

        temp[wind][1] = 0;
        temp[wind][0] = -1;

        room = temp;
    }

    // 아래쪽 돌풍: 시계 방향
    private static void rotateRight(int wind) {
        int[][] temp = copy(room);

        // 왼쪽 열: 위로 이동
        for (int r = wind + 1; r < N - 1; r++) {
            temp[r][0] = room[r + 1][0];
        }

        // 아래쪽 행: 왼쪽으로 이동
        for (int c = 0; c < M - 1; c++) {
            temp[N - 1][c] = room[N - 1][c + 1];
        }

        // 오른쪽 열: 아래로 이동
        for (int r = N - 1; r > wind; r--) {
            temp[r][M - 1] = room[r - 1][M - 1];
        }

        // 돌풍 행: 오른쪽으로 이동
        for (int c = M - 1; c > 1; c--) {
            temp[wind][c] = room[wind][c - 1];
        }

        temp[wind][1] = 0;
        temp[wind][0] = -1;

        room = temp;
    }
}
