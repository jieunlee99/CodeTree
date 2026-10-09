import java.io.*;
import java.util.*;

public class Main {

    static int N, M;
    static int[][] board;
    static List<Box> boxes = new ArrayList<>();

    static class Box {
        int id, r, c, h, w;
        boolean active;

        Box(int id, int h, int w, int c) {
            this.id = id;
            this.r = 0;
            this.c = c;
            this.h = h;
            this.w = w;
            this.active = true;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        board = new int[N][N];

        // 1. 모든 택배 투입
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int id = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken()) - 1;

            Box box = new Box(id, h, w, c);
            boxes.add(box);

            drop(box);
        }

        // 2. 좌측, 우측 번갈아 하차
        for (int i = 0; i < M; i++) {
            boolean left = (i % 2 == 0);

            Box target = findTarget(left);

            sb.append(target.id).append('\n');

            // 3. 선택된 택배 제거
            remove(target);

            // 4. 남은 택배에 중력 적용
            applyGravity();
        }

        System.out.print(sb);
    }

    // 특정 택배의 영역을 값으로 채우기
    static void fill(Box box, int value) {
        for (int r = box.r; r < box.r + box.h; r++) {
            for (int c = box.c; c < box.c + box.w; c++) {
                board[r][c] = value;
            }
        }
    }

    // 한 칸 아래로 이동할 수 있는지 확인
    static boolean canDrop(Box box) {
        int nextRow = box.r + box.h;

        if (nextRow >= N) {
            return false;
        }

        for (int c = box.c; c < box.c + box.w; c++) {
            if (board[nextRow][c] != 0) {
                return false;
            }
        }

        return true;
    }

    // 택배를 가능한 가장 아래까지 떨어뜨리기
    static void drop(Box box) {

        // 기존 위치에 있던 택배를 지우기
        fill(box, 0);

        while (canDrop(box)) {
            box.r++;
        }

        // 최종 위치에 택배 기록
        fill(box, box.id);
    }

    // 좌측 또는 우측으로 하차할 수 있는지 검사
    static boolean canUnload(Box box, boolean left) {

        for (int r = box.r; r < box.r + box.h; r++) {

            if (left) {
                // 택배 왼쪽 영역 검사
                for (int c = 0; c < box.c; c++) {
                    if (board[r][c] != 0) {
                        return false;
                    }
                }
            } else {
                // 택배 오른쪽 영역 검사
                for (int c = box.c + box.w; c < N; c++) {
                    if (board[r][c] != 0) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    // 하차 가능한 택배 중 번호가 가장 작은 택배
    static Box findTarget(boolean left) {
        Box target = null;

        for (Box box : boxes) {
            if (!box.active) {
                continue;
            }

            if (!canUnload(box, left)) {
                continue;
            }

            if (target == null || box.id < target.id) {
                target = box;
            }
        }

        return target;
    }

    // 택배 제거
    static void remove(Box box) {
        fill(box, 0);
        box.active = false;
    }

    // 모든 택배에 중력 적용
    static void applyGravity() {

        List<Box> remaining = new ArrayList<>();

        for (Box box : boxes) {
            if (box.active) {
                remaining.add(box);
            }
        }

        // 아래에 있는 택배부터 처리
        remaining.sort((a, b) ->
                Integer.compare(b.r + b.h, a.r + a.h));

        for (Box box : remaining) {
            drop(box);
        }
    }
}
