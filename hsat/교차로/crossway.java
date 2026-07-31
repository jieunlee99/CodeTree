import java.util.*;
import java.io.*;

public class Main {

    static int N; 
    static int[] outTime;
    static Car[] cars;

    static class Car {
        int id;
        int inTime;

        public Car(int id, int inTime) {
            this.id = id;
            this.inTime = inTime;
        }
    } 

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        N = Integer.parseInt(br.readLine());
        
        // 각 차량이 통과한 시간 기록 (통과하지 못한 차량은 -1)
        outTime = new int[N];
        Arrays.fill(outTime, -1);

        // A, B, C, D 방향의 대기 차량
        Queue<Car>[] q = new Queue[4];
        for(int i=0; i<4; i++) {
            q[i] = new ArrayDeque<>();
        }

        for(int i=0; i<N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int t = Integer.parseInt(st.nextToken());
            int dir = st.nextToken().charAt(0) - 'A';
            q[dir].add(new Car(i, t));
        }

        int currentTime = -1;

        while(true) {
            
            // 모든 차량이 통과한 경우 종료
            if(q[0].isEmpty() && q[1].isEmpty() && q[2].isEmpty() && q[3].isEmpty()) {
                break;
            }

            // 현재 시간에 교차로 앞에 대기 중인 차량 여부
            int[] state = new int[4];

            // 아직 도착하지 않은 차량들 중 가장 빠른 시간
            int minTime = Integer.MAX_VALUE;

            for(int i=0; i<4; ++i) {
                // 해당 방향에 차량이 있는 경우
                if(!q[i].isEmpty()) {
                    // 맨 앞 차량 도착 시간과 비교하여 가장 빠른 도착 시간 갱신
                    int t = q[i].peek().inTime;
                    minTime = Math.min(minTime, t);

                    // 현재 시간까지 도착했다면 대기
                    if(t <= currentTime) {
                        state[i] = 1;
                    }
                }
            }

            // 현재 대기 중인 방향의 개수
            int count = 0;
            for(int value:state) {
                count += value;
            }

            // 도착한 차량이 없으면 가장 먼저 도착하는 시간으로 점프
            if(count == 0) {
                currentTime = minTime;
            } 
            
            // 교착 상태 -> 이후 차량 모두 통과 불가능
            else if(count == 4) {
                break;
            } 
            
            else {
                for(int i=0; i<4; ++i) {
                    // 통과 가능 차량 찾기
                    // -> 왼쪽 차량이 없으면 현재 차량은 통과 가능
                    if(state[i] != 0 && state[(i+3)%4] == 0) {
                        outTime[q[i].poll().id] = currentTime;
                    }
                }

                // 1초 경과
                currentTime++;
            }
        }

        for(int i = 0; i<N; ++i) {
            sb.append(outTime[i]).append("\n");
        }
        System.out.println(sb);
    }
}