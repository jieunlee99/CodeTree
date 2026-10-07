import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static int[][] seat = new int[4][8];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        for (int i = 0; i < 4; i++) {
            String input = br.readLine();
            for (int j = 0; j < 8; j++) {
                // 0:N, 1:S
                seat[i][j] = input.charAt(j) - '0';
            }
        }

        int k = Integer.parseInt(br.readLine());

        for (int i = 0; i < k; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken()); // 회전시킬 의자 번호
            int d = Integer.parseInt(st.nextToken()); // 1: 시계, -1: 반시계

            rotate(n - 1, d);
        }

        int answer = 0;
        for (int i = 0; i < 4; i++) {
            answer += seat[i][0] * (1 << i);
        }
        System.out.println(answer);
    }

    private static void turn(int n, int d) {
        if (d == 1) {
            int temp = seat[n][7];

            for (int i = 7; i > 0; i--) {
                seat[n][i] = seat[n][i - 1];
            }

            seat[n][0] = temp;

        } else if (d == -1) {
            int temp = seat[n][0];

            for (int i = 0; i < 7; i++) {
                seat[n][i] = seat[n][i + 1];
            }

            seat[n][7] = temp;
        }
    }

    private static void rotate(int n, int d) {

        // 각 바퀴가 어느 방향으로 회전해야 하는지 기록
        int[] dir = new int[4];

        dir[n] = d;

        // 왼쪽 전파 기록
        for (int i = n; i > 0; i--) {

            if (seat[i - 1][2] != seat[i][6]) {
                dir[i - 1] = -dir[i];
            } else {
                break;
            }
        }

        // 오른쪽 전파 기록
        for (int i = n; i < 3; i++) {

            if (seat[i][2] != seat[i + 1][6]) {
                dir[i + 1] = -dir[i];
            } else {
                break;
            }
        }

        // 실제 회전
        for (int i = 0; i < 4; i++) {
            if (dir[i] != 0) {
                turn(i, dir[i]);
            }
        }
    }
}
