import java.util.*;

public class Main {

    static boolean[] visited = new boolean[26];
    static char[][] arr = new char[5][5];
    static int[][] pos = new int[26][2];

    static String msg, key;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        msg = sc.next();
        key = sc.next();

        fillArr();

        System.out.println(encrypt());
    }

    static void fillArr() {

        // J는 사용하지 않음
        visited['J' - 'A'] = true;

        int idx = 0;

        // key 채우기
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);

            if (!visited[c - 'A']) {
                visited[c - 'A'] = true;
                arr[idx / 5][idx % 5] = c;
                idx++;
            }
        }

        // 나머지 알파벳 채우기
        for (int i = 0; i < 26; i++) {
            if (!visited[i]) {
                visited[i] = true;
                arr[idx / 5][idx % 5] = (char) ('A' + i);
                idx++;
            }
        }

        // 각 문자 위치 저장
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                char c = arr[i][j];
                pos[c - 'A'][0] = i;
                pos[c - 'A'][1] = j;
            }
        }
    }

    static String encrypt() {

        StringBuilder plain = new StringBuilder();

        int idx = 0;

        while (idx < msg.length()) {

            char first = msg.charAt(idx);

            // 마지막 한 글자
            if (idx == msg.length() - 1) {
                plain.append(first);
                plain.append('X');
                break;
            }

            char second = msg.charAt(idx + 1);

            // 같은 문자면 X 또는 Q 삽입
            if (first == second) {
                plain.append(first);

                if (first == 'X')
                    plain.append('Q');
                else
                    plain.append('X');

                idx++;
            } else {
                plain.append(first);
                plain.append(second);
                idx += 2;
            }
        }

        StringBuilder answer = new StringBuilder();

        for (int i = 0; i < plain.length(); i += 2) {

            char a = plain.charAt(i);
            char b = plain.charAt(i + 1);

            int r1 = pos[a - 'A'][0];
            int c1 = pos[a - 'A'][1];

            int r2 = pos[b - 'A'][0];
            int c2 = pos[b - 'A'][1];

            // 같은 행 -> 오른쪽으로 한 칸 이동
            if (r1 == r2) {
                answer.append(arr[r1][(c1 + 1) % 5]);
                answer.append(arr[r2][(c2 + 1) % 5]);
            }
            // 같은 열 -> 아래쪽으로 한 칸 이동
            else if (c1 == c2) {
                answer.append(arr[(r1 + 1) % 5][c1]);
                answer.append(arr[(r2 + 1) % 5][c2]);
            }
            // 서로 다른 행과 열 -> 두 글자가 위치하는 칸의 열을 서로 교환
            else { 
                answer.append(arr[r1][c2]);
                answer.append(arr[r2][c1]);
            }
        }

        return answer.toString();
    }
}