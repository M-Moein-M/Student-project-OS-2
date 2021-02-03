import java.util.ArrayList;
import java.util.Random;

public class Process extends Thread {
    private long pid;
    private ArrayList<Segment> segments;
    private long initTime;
    private static final long MIN_INTERVAL_DELAY = 2000;  // minimum delay between process actions(allocate, deallocate, terminate)
    private Memory memory;

    public Process(long pid, Memory memory){
        this.initTime = System.currentTimeMillis();
        this.memory = memory;
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
                long delay = ((long)(new Random().nextInt(4000)) + Process.MIN_INTERVAL_DELAY)*2;
                Thread.sleep(delay);

                int action = new Random().nextInt(3);   // 0 for allocate, 1 for deallocate, 2 for terminate
                switch (action){
                    // allocate memory
                    case 0:
                        // random size for memory request
                        long requestSize = new Random().nextInt((int)(memory.getMemorySize()/4)) + 5;

                        Segment seg = this.memory.allocate(this.pid, requestSize);
                        if (seg != null){
                            this.segments.add(seg);
                        }else{ // no segment found or requested for space that already is owned by process
                            continue;
                        }
                        break;

                    // deallocate memory
                    case 1:
                        break;

                    // terminate process
                    case 2:
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
