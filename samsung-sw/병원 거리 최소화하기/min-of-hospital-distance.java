import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    static int N, M;
    static int[][] map;

    static List<int[]> persons = new ArrayList<>();
    static List<int[]> hospitals = new ArrayList<>();

    static boolean[] selected;

    static int answer = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken()); // 병원을 M개만 남겨야 함

        map = new int[N][N];

        // map 초기화 & 사람, 병원 개수 세기
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                // 빈 칸: 0, 사람: 1, 병원: 2
                map[i][j] = Integer.parseInt(st.nextToken());

                if (map[i][j] == 1) {
                    persons.add(new int[] { i, j });
                } else if (map[i][j] == 2) {
                    hospitals.add(new int[] { i, j });
                }
            }
        }

        selected = new boolean[hospitals.size()];

        selectHospital(0, 0);

        System.out.println(answer);
    }

    // 병원 M개 선택 (조합)
    public static void selectHospital(int idx, int cnt) {

        // 병원 선택 완료
        if (cnt == M) {
            answer = Math.min(answer, calcTotalDist());
            return;
        }

        // 모든 병원 확인함
        if (idx == hospitals.size()) {
            return;
        }

        // 백트래킹

        selected[idx] = true;
        selectHospital(idx + 1, cnt + 1);

        selected[idx] = false;
        selectHospital(idx + 1, cnt);
    }

    // 현재 선택한 병원 기준으로 거리의 총 합 계산
    public static int calcTotalDist() {
        int sumDist = 0;

        for (int[] person : persons) {

            int minDist = Integer.MAX_VALUE;

            for (int i = 0; i < hospitals.size(); i++) {
                if (!selected[i]) {
                    continue;
                }

                minDist = Math.min(minDist, calcDist(person, hospitals.get(i)));
            }

            sumDist += minDist;
        }

        return sumDist;
    }

    public static int calcDist(int[] p1, int[] p2) {
        return Math.abs(p1[0] - p2[0]) + Math.abs(p1[1] - p2[1]);
    }
}
