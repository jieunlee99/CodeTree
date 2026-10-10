import java.io.*;
import java.util.*;

public class Main {

    static int N, M, K;

    static int[][] sea;
    static int[][] occupied;

    static Turtle[] turtles;
    static Volcano[] volcanoes;

    // 우, 하, 좌, 상
    static final int[] dr = { 0, 1, 0, -1 };
    static final int[] dc = { 1, 0, -1, 0 };

    static class Turtle {
        int r, c;
        int status; // 0: 생존, 1: 탈출, 2: 화석
        int answer = -1;

        Turtle(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    static class Volcano {
        int r, c;
        int limit; // 분출 임계치
        int pressure; // 현재 압력
        boolean erupted; // 이번 턴 분출 여부

        Volcano(int r, int c, int limit) {
            this.r = r;
            this.c = c;
            this.limit = limit;
        }
    }

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        sea = new int[N][N];
        occupied = new int[N][N];

        turtles = new Turtle[M + 1];
        volcanoes = new Volcano[K + 1];

        // 바다 정보
        for (int r = 0; r < N; r++) {
            st = new StringTokenizer(br.readLine());

            for (int c = 0; c < N; c++) {
                sea[r][c] = Integer.parseInt(st.nextToken());
            }
        }

        // 바다거북 정보
        for (int i = 1; i <= M; i++) {
            st = new StringTokenizer(br.readLine());

            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());

            turtles[i] = new Turtle(r, c);
            occupied[r][c] = i;
        }

        // 해저화산 정보
        for (int i = 1; i <= K; i++) {
            st = new StringTokenizer(br.readLine());

            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int p = Integer.parseInt(st.nextToken());

            volcanoes[i] = new Volcano(r, c, p);
        }

        // 최대 100턴 진행
        for (int turn = 1; turn <= 100; turn++) {

            // 1. 거북이 이동
            moveTurtles(turn);

            // 2. 화산 압력 증가
            increasePressure();

            // 3. 화산 분출과 연쇄 반응
            eruptVolcanoes();

            // 4. 환경 초기화
            resetTurn();
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 1; i <= M; i++) {
            sb.append(turtles[i].answer).append('\n');
        }

        System.out.print(sb);
    }

    // 1. 바다거북을 번호순으로 이동
    static void moveTurtles(int turn) {

        for (int i = 1; i <= M; i++) {
            Turtle t = turtles[i];

            if (t.status != 0)
                continue;

            // 시작부터 안식처에 있는 경우
            if (t.r == N - 1 && t.c == N - 1) {
                t.status = 1;
                t.answer = turn;
                occupied[t.r][t.c] = 0;
                continue;
            }

            int[] next = findNextStep(t.r, t.c);

            // 최단 경로가 없으면 제자리 유지
            if (next == null)
                continue;

            occupied[t.r][t.c] = 0;

            t.r = next[0];
            t.c = next[1];

            if (t.r == N - 1 && t.c == N - 1) {
                t.status = 1;
                t.answer = turn;
            } else {
                occupied[t.r][t.c] = i;
            }
        }
    }

    // 출발점 BFS + parent 경로 복원
    static int[] findNextStep(int sr, int sc) {

        boolean[][] visited = new boolean[N][N];

        // 직전 칸의 행과 열 저장
        int[][] parentR = new int[N][N];
        int[][] parentC = new int[N][N];

        Queue<int[]> q = new ArrayDeque<>();

        q.offer(new int[] { sr, sc });
        visited[sr][sc] = true;

        int tr = N - 1;
        int tc = N - 1;

        while (!q.isEmpty()) {

            int[] cur = q.poll();
            
            int r = cur[0];
            int c = cur[1];

            if (r == tr && c == tc) {
                break;
            }

            // 우, 하, 좌, 상 순서
            for (int d = 0; d < 4; d++) {

                int nr = r + dr[d];
                int nc = c + dc[d];

                if (!inRange(nr, nc))
                    continue;
                if (visited[nr][nc])
                    continue;
                if (sea[nr][nc] == 1)
                    continue;
                if (occupied[nr][nc] != 0)
                    continue;

                visited[nr][nc] = true;

                parentR[nr][nc] = r;
                parentC[nr][nc] = c;

                q.offer(new int[] { nr, nc });
            }
        }

        // 목적지까지 도달할 수 없음
        if (!visited[tr][tc]) {
            return null;
        }

        // 목적지에서 시작점 방향으로 역추적
        int r = tr;
        int c = tc;

        while (true) {

            int pr = parentR[r][c];
            int pc = parentC[r][c];

            // 시작점 바로 다음 칸 발견
            if (pr == sr && pc == sc) {
                return new int[] { r, c };
            }

            r = pr;
            c = pc;
        }
    }

    // 2. 모든 화산의 압력 10 증가
    static void increasePressure() {

        for (int i = 1; i <= K; i++) {
            volcanoes[i].pressure += 10;
        }
    }

    // 3. 화산 분출 및 연쇄 반응
    static void eruptVolcanoes() {

        int[][] heat = new int[N][N];

        List<Integer> current = new ArrayList<>();

        // 최초 분출 대상
        for (int i = 1; i <= K; i++) {

            Volcano v = volcanoes[i];

            if (v.pressure >= v.limit) {
                v.erupted = true;
                current.add(i);
            }
        }

        // 단계별 연쇄 분출
        while (!current.isEmpty()) {

            // 현재 단계의 화산들이 모두 분출
            for (int id : current) {
                spreadHeat(volcanoes[id], heat);
            }

            List<Integer> next = new ArrayList<>();

            // 누적 열기로 다음 단계의 분출 대상 결정
            for (int i = 1; i <= K; i++) {

                Volcano v = volcanoes[i];

                if (v.erupted)
                    continue;

                if (v.pressure + heat[v.r][v.c] >= v.limit) {
                    v.erupted = true;
                    next.add(i);
                }
            }

            current = next;
        }

        // 모든 연쇄 반응 종료 후 화석화 판정
        for (int i = 1; i <= M; i++) {

            Turtle t = turtles[i];

            if (t.status != 0)
                continue;

            if (heat[t.r][t.c] >= 20) {

                t.status = 2;

                // 화석은 그 자리에 남는 장애물
                // occupied를 지우지 않는다.
            }
        }
    }

    // 화산 하나가 발생시키는 열기 전파
    static void spreadHeat(Volcano v, int[][] heat) {

        // 화산 중심
        heat[v.r][v.c] += v.limit;

        // 우, 하, 좌, 상 방향
        for (int d = 0; d < 4; d++) {

            int nr = v.r + dr[d];
            int nc = v.c + dc[d];

            int power = v.limit / 2;

            while (inRange(nr, nc) && sea[nr][nc] != 1 && power > 0) {

                heat[nr][nc] += power;

                power /= 2;

                nr += dr[d];
                nc += dc[d];
            }
        }
    }

    // 4. 분출 화산 초기화
    static void resetTurn() {

        for (int i = 1; i <= K; i++) {

            Volcano v = volcanoes[i];

            if (v.erupted) {
                v.pressure = 0;
                v.erupted = false;
            }
        }
    }

    static boolean inRange(int r, int c) {
        return 0 <= r && r < N && 0 <= c && c < N;
    }
}
