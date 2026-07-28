import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine()); // 휴가 일수 = 외주 작업 개수

        int[] t = new int[n+1];
        int[] p = new int[n+1];

        int[] dp = new int[n+1];

        for(int i = 1; i <= n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            t[i] = Integer.parseInt(st.nextToken()); // 걸리는 기한
            p[i] = Integer.parseInt(st.nextToken()); // 수익
        }

        for(int i = 1; i <= n; i++) {
            dp[i] = Math.max(dp[i], dp[i-1]);

            int end = i + t[i] - 1;
            if(end <= n) {
                dp[end] = Math.max(dp[end], dp[i-1]+p[i]);
            }
        }

        System.out.println(dp[n]);
    }
}