import java.util.ArrayList;

public class Process {
    private long pid;
    private ArrayList<Segment> segments;
    private long initTime;

    public Process(long pid){
        this.initTime = System.currentTimeMillis();
        this.pid = pid;
        this.segments = new ArrayList<>();
    }

    public long getRunTime(){
        return System.currentTimeMillis()-this.initTime;
    }

    public void addSegment(Segment memorySeg){

    }

}
