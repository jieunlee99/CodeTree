import java.io.*;
import java.util.*;

public class Main {

    static class Score implements Comparable<Score> {
        int idx;
        int score;

        Score(int idx, int score) {
            this.idx = idx;
            this.score = score;
        }

        @Override
        public int compareTo(Score o) {
            return o.score - this.score;
        }
    }

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();

        int n = Integer.parseInt(br.readLine());

        int[] total = new int[n];

        for (int contest = 0; contest < 3; contest++) {

            st = new StringTokenizer(br.readLine());

            Score[] arr = new Score[n];
            int[] rank = new int[n];

            for (int i = 0; i < n; i++) {
                int score = Integer.parseInt(st.nextToken());

                arr[i] = new Score(i, score);
                total[i] += score;
            }

            Arrays.sort(arr); // 점수순 정렬

            for (int i = 0; i < n; i++) {
                if (i == 0) {
                    rank[arr[i].idx] = 1;
                } else if (arr[i].score == arr[i - 1].score) {
                    rank[arr[i].idx] = rank[arr[i - 1].idx];
                } else {
                    rank[arr[i].idx] = i + 1;
                }
            }

            for (int i = 0; i < n; i++) {
                sb.append(rank[i]).append(' ');
            }

            sb.append('\n');
        }

        Score[] arr = new Score[n];
        int[] rank = new int[n];

        for (int i = 0; i < n; i++) {
            arr[i] = new Score(i, total[i]);
        }

        Arrays.sort(arr);

        for (int i = 0; i < n; i++) {
            if (i == 0) {
                rank[arr[i].idx] = 1;
            } else if (arr[i].score == arr[i - 1].score) {
                rank[arr[i].idx] = rank[arr[i - 1].idx];
            } else {
                rank[arr[i].idx] = i + 1;
            }
        }

        for (int i = 0; i < n; i++) {
            sb.append(rank[i]).append(' ');
        }

        System.out.print(sb);
    }
}