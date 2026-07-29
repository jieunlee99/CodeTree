import java.util.*;
import java.io.*;

public class Main {

    static int n;
    static int[] nums, op;

    static int minValue = Integer.MAX_VALUE;
    static int maxValue = Integer.MIN_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        n = Integer.parseInt(br.readLine());

        nums = new int[n];
        op = new int[3]; // 덧셈, 뺄셈, 곱셈의 개수

        StringTokenizer st = new StringTokenizer(br.readLine());
        for(int i=0; i<n; i++) {
            nums[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        for(int i=0; i<3; i++) {
            op[i] = Integer.parseInt(st.nextToken());
        }

        dfs(1, nums[0]);

        System.out.println(minValue + " " + maxValue);
    }

    public static void dfs(int depth, int value) {

        if(depth == n) {
            minValue = Math.min(minValue, value);
            maxValue = Math.max(maxValue, value);
            return;
        }

        if(op[0] > 0) {
            op[0]--;
            dfs(depth+1, value+nums[depth]);
            op[0]++;
        } 

        if(op[1] > 0) {
            op[1]--;
            dfs(depth+1, value-nums[depth]);
            op[1]++;
        }

        if(op[2] > 0) {
            op[2]--;
            dfs(depth+1, value*nums[depth]);
            op[2]++;
        }
    }
}