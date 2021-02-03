import java.util.ArrayList;

public class Memory {
    private final long MEMORY_SIZE;
    private ArrayList<Segment> segments;
    private final long MIN_SEG_SIZE = 32;
    private ReadWriteLock lock;

    public Memory(long memorySize){
        this.MEMORY_SIZE = memorySize;
        this.segments = new ArrayList<Segment>();
        // add first memory segment
        this.segments.add(new Segment(memorySize, 0));
        this.lock = new ReadWriteLock();
    }

    // checks if process can use available space in its owned segments
    private boolean checkProcessSpace(long pid, long requestedSize){
        try {
            this.lock.lockRead();

            for (Segment seg: this.segments){
                if (seg.getPid() == pid && seg.getAvailableSize() >= requestedSize){
                    long newUsedSize = seg.getUsedSize()+requestedSize;
                    seg.assignSegment(pid, newUsedSize);
                    return true;
                }
            }
            return false;
        } catch (InterruptedException e) {
            System.out.println("Interrupt exception occurred");
        } finally {
            this.lock.unlockRead();
        }
        return true;
    }

    public Segment allocate(long pid, long requestedSize){
        try{
            boolean processHasSpace = this.checkProcessSpace(pid, requestedSize);
            if (processHasSpace){
                System.out.format("Process %d has available space in owned segments. Allocating " +
                        "new segment(%dKB) declined. Segment used size increased.\n", pid, requestedSize);
                return null;
            }


            // if a proper segment was not found, this index is best candidate ot split to half
            int splitCandidateIndex = -1;
            long splitCandidateSize = Long.MAX_VALUE;
            long neededSegmentSize = this.findOptimumSize(requestedSize);

            this.lock.lockWrite();  // get writer lock

            for(int i = 0; i < this.segments.size(); i++){
                Segment seg = this.segments.get(i);
                if (seg.isOccupied())
                    continue;

                if (seg.getSize() == neededSegmentSize) {
                    seg.assignSegment(pid, requestedSize);
                    return seg;
                }

                if (seg.getSize() > neededSegmentSize && seg.getSize() < splitCandidateSize) {
                    splitCandidateIndex = i;
                    splitCandidateSize = seg.getSize();
                }
            }

            // no allocatable memory segment found
            if (splitCandidateIndex == -1){
                throw new NotEnoughMemoryError();
            }else{
                // split memory segment
                this.splitMemorySegment(splitCandidateIndex, neededSegmentSize);
                this.segments.get(splitCandidateIndex).assignSegment(pid, requestedSize);
                return this.segments.get(splitCandidateIndex);
            }
        }
        catch (InterruptedException e){
            System.out.println("Interrupt exception occurred");
        }
        catch (NotEnoughMemoryError e){
            System.out.format("Not Enough memory space(requested %dKB). Process id: %d.\n",requestedSize, pid);
            this.printMemory();
        } finally {
            try{
                this.lock.unlockWrite();
            }catch (InterruptedException e){
                System.out.println("Interrupt exception occurred");
            }
        }
        return null;
    }

    private void splitMemorySegment(int segmentIndex, long neededSize){
        while (true) {
            Segment candidateSeg = this.segments.get(segmentIndex);
            this.segments.remove(segmentIndex);
            long childSize = candidateSeg.getSize() / 2;
            long parentInitAddress = candidateSeg.getInitAddress();

            // setup first half
            this.segments.add(segmentIndex, new Segment(childSize, parentInitAddress));
            // set up second half
            this.segments.add(segmentIndex, new Segment(childSize, parentInitAddress + childSize));

            if (childSize == neededSize)
                return;
        }
    }

    public void deallocate(long pid, long initAddress){
        try {
            this.lock.lockWrite();

            // find segment to deallocate
            for (Segment seg: this.segments){
                if (seg.getPid() == pid && seg.getInitAddress() == initAddress){
                    seg.releaseSegment();
                    return;
                }
            }

        } catch (InterruptedException e) {
            System.out.println("Interrupt exception occurred");
        } finally {
            try {
                this.lock.unlockWrite();
            } catch (InterruptedException e) {
                System.out.println("Interrupt exception occurred");
            }
        }
    }

    // deallocate all of the process segments
    public void deallocateAll(long pid){
        try {
            this.lock.lockWrite();
            for (Segment seg: this.segments){
                if (seg.getPid() == pid)
                    seg.releaseSegment();
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupt exception occurred");
        }finally {
            try {
                this.lock.unlockWrite();
            } catch (InterruptedException e) {
                System.out.println("Interrupt exception occurred");
            }
        }
    }

    public void printMemory(){
        System.out.println(this.segments);
    }

    public long getMemorySize(){
        return this.MEMORY_SIZE;
    }

    // find nearest suitable size to allocate
    private long findOptimumSize(long size) throws NotEnoughMemoryError {
        if (size > this.MEMORY_SIZE)
            throw new NotEnoughMemoryError();

        long currentSize = this.MEMORY_SIZE;
        while(true){
            if (currentSize <= this.MIN_SEG_SIZE)
                return this.MIN_SEG_SIZE;
            if (currentSize == size || currentSize/2 < size)
                return currentSize;
            else
                currentSize /= 2;
        }
    }

    public long getMemoryUsedSpace(){
        try {
            this.lock.lockRead();
            long usedSpace = 0;
            for (Segment seg: this.segments){
                usedSpace += seg.getUsedSize();
            }
            return usedSpace;
        } catch (InterruptedException e) {
            System.out.println("Interrupt exception occurred");
        } finally {
            this.lock.unlockRead();
        }
        return 0;
    }
}
