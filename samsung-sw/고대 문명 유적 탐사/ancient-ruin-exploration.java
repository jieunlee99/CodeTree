import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    static int K, M;
    static int[][] arr = new int[5][5];
    static int[] pieces;
    static int pieceIdx = 0;

    static int[] dr = { 1, -1, 0, 0 };
    static int[] dc = { 0, 0, 1, -1 };

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        K = Integer.parseInt(st.nextToken()); // 반복 횟수
        M = Integer.parseInt(st.nextToken()); // 벽면에 적힌 유물 조각 개수

        for (int i = 0; i < 5; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 5; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken()); // 1 <= arr[i][j] <= 7;
            }
        }

        pieces = new int[M];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < M; i++) {
            pieces[i] = Integer.parseInt(st.nextToken());
        }

        // 탐사 진행 (3x3 격자 선택하여 시계 방향으로 90/180/270도 회전)

        // 3개 이상 연결된 유물 애니팡
        // 사라진 자리에 유물 생겨남
        // 3개 이상 연결된 유물이 없을 때까지 반복

        // 탐사 반복 (K번)

        StringBuilder sb = new StringBuilder();

        for (int turn = 0; turn < K; turn++) {

            int maxScore = 0;
            int[][] bestArr = null;

            // 회전 목표 : 유물 1차 획득가치 최대화 -> 각도 낮은 순 -> 열 낮은 순 -> 행 낮은
            for (int angle = 1; angle <= 3; angle++) {

                for (int c = 1; c <= 3; c++) {
                    for (int r = 1; r <= 3; r++) {

                        int[][] rotated = copy(arr);

                        for (int a = 0; a < angle; a++) {
                            rotate(rotated, r, c);
                        }

                        int score = bfs(rotated, false);

                        if (maxScore < score) {
                            maxScore = score;
                            bestArr = rotated;
                        }
                    }
                }
            }

            // 획득 가능한 유물 x -> 종료
            if (maxScore == 0) {
                break;
            }

            arr = bestArr;

            int totalScore = 0;

            while (true) {
                int score = bfs(arr, true);

                if (score == 0) {
                    break;
                }

                totalScore += score;

                fillBlank();
            }

            sb.append(totalScore).append(" ");
        }

        // 각 턴마다 획득한 유물의 가치의 총합 출력
        System.out.println(sb.toString().trim());
    }

    // 빈 칸 채우기
    private static void fillBlank() {
        for (int c = 0; c < 5; c++) {
            for (int r = 4; r >= 0; r--) {
                if (arr[r][c] == 0) {
                    arr[r][c] = pieces[pieceIdx++];
                }
            }
        }
    }

    // 유물 가치 계산
    private static int bfs(int[][] map, boolean remove) {
        boolean[][] visited = new boolean[5][5];

        int total = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {

                if (visited[i][j] || map[i][j] == 0) {
                    continue;
                }

                Queue<int[]> queue = new LinkedList<>();
                List<int[]> group = new ArrayList<>();

                queue.offer(new int[] { i, j });
                group.add(new int[] { i, j });
                visited[i][j] = true;

                int num = map[i][j];

                while (!queue.isEmpty()) {
                    int[] cur = queue.poll();

                    for (int d = 0; d < 4; d++) {
                        int nr = cur[0] + dr[d];
                        int nc = cur[1] + dc[d];

                        if (nr < 0 || nr >= 5 || nc < 0 || nc >= 5) {
                            continue;
                        }

                        if (visited[nr][nc]) {
                            continue;
                        }

                        if (map[nr][nc] != num) {
                            continue;
                        }

                        queue.offer(new int[] { nr, nc });
                        group.add(new int[] { nr, nc });
                        visited[nr][nc] = true;
                    }
                }

                if (group.size() >= 3) {
                    total += group.size();

                    if (remove) {
                        for (int[] pos : group) {
                            map[pos[0]][pos[1]] = 0;
                        }
                    }
                }
            }
        }

        return total;
    }

    private static int[][] copy(int[][] original) {
        int[][] result = new int[5][5];

        for (int i = 0; i < 5; i++) {
            result[i] = original[i].clone();
        }

        return result;
    }

    // (r,c)를 중심으로 시계 방향 90도 회전
    private static void rotate(int[][] map, int r, int c) {
        int[][] temp = copy(map);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                map[r + i][c + j] = temp[r - j][c + i];
            }
        }
    }
}
