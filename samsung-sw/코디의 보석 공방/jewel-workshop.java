import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

    static class Jewel {
        int idx, w, v;

        public Jewel(int idx, int w, int v) {
            this.idx = idx;
            this.w = w;
            this.v = v;
        }

        @Override
        public String toString() {
            return "Jewel [idx=" + idx + "]";
        }
    }

    static int Q; // 작업의 수
    static List<Jewel> jewels = new ArrayList<>();
    static int jewelIdx;
    static int maxValue;

    public static void main(String[] args) throws NumberFormatException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;

        Q = Integer.parseInt(br.readLine());

        while (Q-- > 0) {
            st = new StringTokenizer(br.readLine());

            int cmd = Integer.parseInt(st.nextToken());

            // 1. 보석 준비
            if (cmd == 1) {
                int N = Integer.parseInt(st.nextToken());

                for (int i = 1; i <= N; i++) {
                    int w = Integer.parseInt(st.nextToken());
                    int v = Integer.parseInt(st.nextToken());

                    jewels.add(new Jewel(i, w, v));
                }

                jewelIdx = N + 1;
            }

            // 2. 보석 입고
            else if (cmd == 2) {
                int w = Integer.parseInt(st.nextToken());
                int v = Integer.parseInt(st.nextToken());
                jewels.add(new Jewel(jewelIdx++, w, v));
            }

            // 3. 보석 판매
            else if (cmd == 3) {
                int idx = Integer.parseInt(st.nextToken()); // 0-base

                int sell = findJewel(idx);

                if (sell == -1) {
                    sb.append(-1).append("\n");
                    continue;
                }

                sb.append(jewels.get(sell).v).append("\n");
                jewels.remove(sell);
            }

            // 4. 진열 - 보석 몇 개를 골라 무게 합이 W 이하면서 가장 큰 가치의 합
            else if (cmd == 4) {
                int W = Integer.parseInt(st.nextToken());

                long[] dp = new long[W + 1];

                for (Jewel jewel : jewels) {
                    for (int w = W; w >= jewel.w; w--) {
                        dp[w] = Math.max(dp[w], dp[w - jewel.w] + jewel.v);
                    }
                }

                sb.append(dp[W]).append("\n");
            }

            // 5. 세트 구성 - 보석 두 개를 골라 무게 차이가 D 이하인 세트의 개수
            else if (cmd == 5) {
                int D = Integer.parseInt(st.nextToken());

                jewels.sort((a, b) -> Integer.compare(a.w, b.w));

                int left = 0;
                long cnt = 0;

                for (int right = 0; right < jewels.size(); right++) {

                    while (jewels.get(right).w - jewels.get(left).w > D) {
                        left++;
                    }

                    cnt += right - left;
                }

                sb.append(cnt).append("\n");
            }
        }

        System.out.print(sb.toString().trim());
    }

    static int findJewel(int idx) {
        for (int i = 0; i < jewels.size(); i++) {
            if (jewels.get(i).idx == idx) {
                return i;
            }
        }
        return -1;
    }
}
