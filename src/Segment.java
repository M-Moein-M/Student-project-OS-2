public class Segment {
    private long size;
    private long initAddress;
    private long usedSize = 0;

    public Segment(long size, long initAddress){
        this.size = size;
        this.initAddress = initAddress;
        this.usedSize = 0;  // how much of the segment is used by process
    }

    public void assignSegment(long pid, long usedSize){
        System.out.println("Segment assigned to pid: "+pid);
        this.usedSize = usedSize;
    }

    public boolean isOccupied(){
        return this.usedSize != 0;
    }

    public long getUsedSize() {
        return this.usedSize;
    }

    public long getSize() {
        return this.size;
    }

    public String toString(){
        return "(SegSize: "+this.size+", "
                +"Seg initAddress: "+this.initAddress+", "
                +"Used size: "+ this.usedSize
                +")\n";
    }

    public long getInitAddress() {
        return initAddress;
    }
}
