import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        
        int[] arr = new int[n];

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        long answer = 0;

        // ak < ai < aj 인 조합 찾기

        for (int i = 0; i < n; i++) {

            int jCount = 0;

            for (int x = i + 1; x < n; x++) {

                if (arr[x] > arr[i]) { // j 찾기
                    jCount++;
                } else if (arr[x] < arr[i]) { // k 찾기
                    answer += jCount;
                }
            }
        }

        System.out.println(answer);
    }
}