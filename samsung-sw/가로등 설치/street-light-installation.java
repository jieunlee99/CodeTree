import java.io.*;
import java.util.*;

public class Main {

    static int N, M;

    // 가로등 위치를 오름차순으로 관리
    static TreeSet<Integer> positions = new TreeSet<>();

    // 가로등 번호 -> 위치
    static HashMap<Integer, Integer> lampMap = new HashMap<>();

    // 구간을 길이 내림차순, 시작 위치 오름차순 정렬
    static TreeSet<Gap> gaps = new TreeSet<>((a, b) -> {
        if (a.length != b.length) {
            return Integer.compare(b.length, a.length);
        }
        return Integer.compare(a.left, b.left);
    });

    static class Gap {
        int left, right, length;

        Gap(int left, int right) {
            this.left = left;
            this.right = right;
            this.length = right - left;
        }
    }

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(
            new InputStreamReader(System.in)
        );
        StringBuilder sb = new StringBuilder();

        int Q = Integer.parseInt(br.readLine());

        while (Q-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == 100) {
                N = Integer.parseInt(st.nextToken());
                M = Integer.parseInt(st.nextToken());

                positions.clear();
                lampMap.clear();
                gaps.clear();

                for (int i = 1; i <= M; i++) {
                    int pos = Integer.parseInt(st.nextToken());

                    positions.add(pos);
                    lampMap.put(i, pos);
                }

                Integer prev = null;

                for (int pos : positions) {
                    if (prev != null) {
                        addGap(prev, pos);
                    }
                    prev = pos;
                }
            }

            else if (cmd == 200) {
                Gap gap = gaps.first();

                int newPos = gap.left
                           + (gap.length + 1) / 2;

                addLamp(++M, newPos);
            }

            else if (cmd == 300) {
                int id = Integer.parseInt(st.nextToken());
                removeLamp(id);
            }

            else if (cmd == 400) {
                sb.append(calcMinEnergy()).append('\n');
            }
        }

        System.out.print(sb);
    }

    // 구간 추가
    static void addGap(int left, int right) {
        gaps.add(new Gap(left, right));
    }

    // 구간 제거
    static void removeGap(int left, int right) {
        gaps.remove(new Gap(left, right));
    }

    // 가로등 설치
    static void addLamp(int id, int pos) {

        Integer left = positions.lower(pos);
        Integer right = positions.higher(pos);

        // 기존 구간 제거
        if (left != null && right != null) {
            removeGap(left, right);
        }

        // 새로운 구간 추가
        if (left != null) {
            addGap(left, pos);
        }

        if (right != null) {
            addGap(pos, right);
        }

        positions.add(pos);
        lampMap.put(id, pos);
    }

    // 가로등 제거
    static void removeLamp(int id) {

        int pos = lampMap.remove(id);

        Integer left = positions.lower(pos);
        Integer right = positions.higher(pos);

        // 삭제되는 가로등과 연결된 구간 제거
        if (left != null) {
            removeGap(left, pos);
        }

        if (right != null) {
            removeGap(pos, right);
        }

        // 양쪽 가로등을 새로운 구간으로 연결
        if (left != null && right != null) {
            addGap(left, right);
        }

        positions.remove(pos);
    }

    // 최소 전력 계산
    static int calcMinEnergy() {

        int leftDist = positions.first() - 1;
        int rightDist = N - positions.last();

        int maxGap = gaps.isEmpty()
                   ? 0
                   : gaps.first().length;

        return Math.max(
            maxGap,
            Math.max(leftDist * 2, rightDist * 2)
        );
    }
}