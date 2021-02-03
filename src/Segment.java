public class Segment {
    private long size;
    private long initAddress;
    private long usedSize = 0;
    private long pid;

    public Segment(long size, long initAddress){
        this.size = size;
        this.initAddress = initAddress;
        this.usedSize = 0;  // how much of the segment is used by process
    }

    public void assignSegment(long pid, long usedSize){
        System.out.format("Segment(%dKB) assigned to pid: %d\n", this.size, pid);
        this.usedSize = usedSize;
        this.pid = pid;
    }

    public long getPid() {
        return pid;
    }

    public boolean isOccupied(){
        return this.usedSize != 0;
    }

    public long getUsedSize() {
        return this.usedSize;
    }

    public long getAvailableSize(){
        return this.size-this.usedSize;
    }

    public long getSize() {
        return this.size;
    }

    public String toString(){
        return "(SegSize: "+this.size+", "
                +"Seg initAddress: "+this.initAddress+", "
                +"Used size: "+ this.usedSize+", "
                +"pid: "+this.pid
                +")\n";
    }

    public long getInitAddress() {
        return initAddress;
    }
}
