import java.util.ArrayList;
import java.util.Random;

public class Process extends Thread {
    private long pid;
    private ArrayList<Segment> segments;
    private long initTime;
    private static final long MIN_INTERVAL_DELAY = 1000;  // minimum delay between process actions(allocate, deallocate, terminate)

    public Process(long pid){
        this.initTime = System.currentTimeMillis();
        this.pid = pid;
        this.segments = new ArrayList<>();
    }

    public long getRunTime(){
        return System.currentTimeMillis()-this.initTime;
    }

    public void run(){
        while(true){
            try{
                // generate delay
                long delay = (long)(new Random().nextInt(2000)) + Process.MIN_INTERVAL_DELAY;
                Thread.sleep(delay);

                int action = new Random().nextInt(3);   // 0 for allocate, 1 for deallocate, 2 for terminate
                System.out.println("pid: "+this.pid + "\taction: " + action);
                switch (action){
                    case 0:
                        // allocate memory
                        break;
                    case 1:
                        // deallocate memory
                        break;
                    case 2:
                        // terminate process
                        break;
                }

            }catch (InterruptedException e){
                System.out.println("Error in process run method");
            }
        }
    }

    public long getPid() {
        return pid;
    }

    public void addSegment(Segment memorySeg){

    }

}
