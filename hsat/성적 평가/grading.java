import java.util.*;

public class Main {

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();

        int[][] scores = new int[3][n];

        for (int contest = 0; contest < 3; contest++) {
            for (int i = 0; i < n; i++) {
                scores[contest][i] = sc.nextInt();
            }
        }

        int[] result = new int[n];

        // 3개 대회 순위
        for (int contest = 0; contest < 3; contest++) {

            Integer[] sorted = new Integer[n];

            for (int i = 0; i < n; i++) {
                sorted[i] = scores[contest][i];
                result[i] += scores[contest][i];
            }

            Arrays.sort(sorted, Collections.reverseOrder());

            // <점수, 순위>
            Map<Integer, Integer> rankMap = new HashMap<>();

            for (int i = 0; i < n; i++) {
                rankMap.putIfAbsent(sorted[i], i + 1);
            }

            for (int i = 0; i < n; i++) {
                sb.append(rankMap.get(scores[contest][i])).append(" ");
            }

            sb.append("\n");
        }

        // 최종 점수 순위
        Integer[] sorted = new Integer[n];

        for (int i = 0; i < n; i++) {
            sorted[i] = result[i];
        }

        Arrays.sort(sorted, Collections.reverseOrder());

        Map<Integer, Integer> rankMap = new HashMap<>();

        for (int i = 0; i < n; i++) {
            rankMap.putIfAbsent(sorted[i], i + 1);
        }

        for (int i = 0; i < n; i++) {
            sb.append(rankMap.get(result[i])).append(" ");
        }

        System.out.println(sb);
    }
}