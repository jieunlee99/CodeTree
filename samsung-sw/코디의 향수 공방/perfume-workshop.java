import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

    // 향료
    static class Note {
        int idx, v;

        public Note(int idx, int v) {
            this.idx = idx;
            this.v = v;
        }
    }

    static int Q;
    static List<Note> notes = new ArrayList<>();
    static int noteIdx = 1;

    public static void main(String[] args) throws NumberFormatException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;

        Q = Integer.parseInt(br.readLine());

        while (Q-- > 0) {
            st = new StringTokenizer(br.readLine());
            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == 1) {
                int N = Integer.parseInt(st.nextToken());
                for (int i = 1; i <= N; i++) {
                    int v = Integer.parseInt(st.nextToken());
                    notes.add(new Note(i, v));
                }
                noteIdx = N + 1;
            }

            else if (cmd == 2) {
                int v = Integer.parseInt(st.nextToken());
                notes.add(new Note(noteIdx++, v));
            }

            else if (cmd == 3) {
                int idx = Integer.parseInt(st.nextToken());

                // 이미 폐기된 번호
                int remove = findNote(idx);
                if (remove == -1) {
                    sb.append(-1).append("\n");
                    continue;
                }

                // 폐기한 향료 번호 출력
                sb.append(notes.get(remove).v).append("\n");
                notes.remove(remove);

            }

            else if (cmd == 4) {
                int K = Integer.parseInt(st.nextToken());
                sb.append(blending(K)).append("\n");

            }

            else if (cmd == 5) {
                int K = Integer.parseInt(st.nextToken());
                sb.append(compose(K)).append("\n");
            }
        }

        System.out.println(sb.toString().trim());
    }

    // 정렬 + 투 포인터
    static long compose(int K) {

        int n = notes.size();

        if (n == 0) {
            return 0;
        }

        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            arr[i] = notes.get(i).v;
        }

        Arrays.sort(arr);

        long cnt = 0;

        for (int i = 0; i < n; i++) {

            int right = n;

            for (int j = 0; j < n; j++) {
                while (right > 0 && (long) arr[i] + arr[j] + arr[right - 1] >= K) {
                    right--;
                }

                cnt += n - right;
            }
        }

        return cnt;
    }

    static int blending(int K) {
        final int INF = 1_000_000_000;

        int[] dp = new int[K + 1];
        Arrays.fill(dp, INF);

        dp[0] = 0;

        for (int sum = 1; sum <= K; sum++) {

            for (Note note : notes) {

                if (note.v > sum) {
                    continue;
                }

                if (dp[sum - note.v] == INF) {
                    continue;
                }

                dp[sum] = Math.min(dp[sum], dp[sum - note.v] + 1);
            }
        }

        return dp[K] == INF ? -1 : dp[K];
    }

    static int findNote(int idx) {
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).idx == idx) {
                return i;
            }
        }
        return -1;
    }
}
