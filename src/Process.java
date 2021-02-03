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
                        if (this.segments.size() == 0)
                            continue;

                        int segmentIndex = new Random().nextInt(this.segments.size());
                        this.memory.deallocate(this.pid, this.segments.get(segmentIndex).getUsedSize());
                        this.segments.remove(segmentIndex);
                        break;

                    // terminate process
                    case 2:
                        // for better simulation skip if no segment is owned. add lower chance of termination
                        if (this.segments.size() == 0 || new Random().nextInt(2) == 0)
                            continue;
                        System.out.format("Terminating process: %d\n", this.pid);
                        this.terminate();
                        return;
                }

                // generate delay
                long delay = (long)(new Random().nextInt(4000)) + Process.MIN_INTERVAL_DELAY;
                Thread.sleep(delay);

            }catch (InterruptedException e){
                System.out.println("Error in process run method");
            }
        }
    }

    public void terminate(){
        this.memory.deallocateAll(this.pid);
    }

    public long getPid() {
        return pid;
    }

}
