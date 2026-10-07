import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {

    static int N;
    static int[][] arr, likeFriend;
    static int[] order;

    static final int[] dr = { 1, -1, 0, 0 };
    static final int[] dc = { 0, 0, 1, -1 };

    static class Seat implements Comparable<Seat> {
        int r, c;
        int likeCnt;
        int emptyCnt;

        public Seat(int r, int c, int likeCnt, int emptyCnt) {
            this.r = r;
            this.c = c;
            this.likeCnt = likeCnt;
            this.emptyCnt = emptyCnt;
        }

        @Override
        public int compareTo(Seat s) {
            if (this.likeCnt == s.likeCnt) {
                if (this.emptyCnt == s.emptyCnt) {
                    if (this.r == s.r) {
                        return this.c - s.c;
                    }
                    return this.r - s.r;
                }
                return s.emptyCnt - this.emptyCnt;
            }
            return s.likeCnt - this.likeCnt;
        }

    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());

        arr = new int[N][N];
        likeFriend = new int[N * N + 1][4];
        order = new int[N * N];

        StringTokenizer st;
        for (int i = 0; i < N * N; i++) {
            st = new StringTokenizer(br.readLine());
            // n0 학생이 좋아하는 학생의 번호:n1, n2, n3, n4
            int student = Integer.parseInt(st.nextToken());

            order[i] = student;

            for (int j = 0; j < 4; j++) {
                likeFriend[student][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int student : order) {
            setSeat(student);
        }

        int answer = calcScore();

        System.out.println(answer);
    }

    private static int calcScore() {
        int sum = 0;

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                int student = arr[r][c];

                int cnt = 0;

                for (int d = 0; d < 4; d++) {
                    int nr = r + dr[d];
                    int nc = c + dc[d];

                    if (!inRange(nr, nc)) {
                        continue;
                    }

                    if (isLike(student, arr[nr][nc])) {
                        cnt++;
                    }
                }

                if (cnt == 1) {
                    sum += 1;
                } else if (cnt == 2) {
                    sum += 10;
                } else if (cnt == 3) {
                    sum += 100;
                } else if (cnt == 4) {
                    sum += 1000;
                }
            }
        }

        return sum;
    }

    private static void setSeat(int student) {
        PriorityQueue<Seat> pq = new PriorityQueue<>();

        // 가능한 자리를 모두 후보로 둔 뒤 가장 우선순위의 자리에 배정

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {

                // 이미 배정된 자리
                if (arr[r][c] != 0) {
                    continue;
                }

                int likeCnt = 0;
                int emptyCnt = 0;

                for (int d = 0; d < 4; d++) {
                    int nr = r + dr[d];
                    int nc = c + dc[d];

                    if (!inRange(nr, nc)) {
                        continue;
                    }

                    if (arr[nr][nc] == 0) {
                        emptyCnt++;
                    } else if (isLike(student, arr[nr][nc])) {
                        likeCnt++;
                    }
                }

                pq.offer(new Seat(r, c, likeCnt, emptyCnt));
            }
        }

        Seat best = pq.poll();

        arr[best.r][best.c] = student;
    }

    private static boolean inRange(int r, int c) {
        return r >= 0 && r < N && c >= 0 && c < N;
    }

    private static boolean isLike(int student, int target) {
        for (int i = 0; i < 4; i++) {
            if (likeFriend[student][i] == target) {
                return true;
            }
        }
        return false;
    }

}
