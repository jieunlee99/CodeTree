import java.util.*;
import java.io.*;

public class Main {

    static int n;
    static int[][] p;
    static boolean[] visited;

    static int minDiff = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        n = Integer.parseInt(br.readLine());

        p = new int[n][n];
        visited = new boolean[n];

        for(int i=0; i<n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for(int j=0; j<n; j++) {
                p[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        dfs(0, 0);

        System.out.println(minDiff);
    }

    private static void dfs(int depth, int start) {
        if(depth == n/2) {

            int morning = 0;
            int evening = 0;

            for(int i=0; i<n; i++) {
                for(int j=i+1; j<n; j++) {

                    if(visited[i] && visited[j]) {
                        morning += p[i][j] + p[j][i];
                    }

                    if(!visited[i] && !visited[j]) {
                        evening += p[i][j] + p[j][i];
                    }
                }
            }

            minDiff = Math.min(minDiff, Math.abs(morning-evening));

            return;
        }

        for(int i=start; i<n; i++) {
            if(!visited[i]) {
                visited[i] = true;
                dfs(depth+1, i+1);
                visited[i] = false;
            }
        }
    }
}