import java.util.ArrayList;

public class Memory {
    private final long MEMORY_SIZE;
    private Process [] processes;
    private ArrayList<Segment> segments;
    private final long MIN_SEG_SIZE = 32;

    public Memory(long memorySize){
        this.MEMORY_SIZE = memorySize;
        this.segments = new ArrayList<Segment>();
        // add first memory segment
        this.segments.add(new Segment(memorySize, 0));
    }

    public void allocate(long pid, long requestedSize){
        try{
            // if a proper segment was not found, this index is best candidate ot split to half
            int splitCandidateIndex = -1;
            long splitCandidateSize = Long.MAX_VALUE;
            long neededSegmentSize = this.findOptimumSize(requestedSize);

            for(int i = 0; i < this.segments.size(); i++){
                Segment seg = this.segments.get(i);
                if (seg.isOccupied())
                    continue;

                if (seg.getSize() == neededSegmentSize) {
                    seg.assignSegment(pid, requestedSize);
                    return;
                }

                if (seg.getSize() > neededSegmentSize && seg.getSize() < splitCandidateSize)
                    splitCandidateIndex = i;
                    splitCandidateSize = seg.getSize();
            }

            // no allocatable memory segment found
            if (splitCandidateIndex == -1){
                throw new NotEnoughMemoryError();
            }else{
                // split memory segment
                this.splitMemorySegment(splitCandidateIndex, neededSegmentSize);
                this.segments.get(splitCandidateIndex).assignSegment(pid, requestedSize);
            }
        }
        catch (NotEnoughMemoryError e){
            System.out.println("Memory allocation Failed. Not Enough memory space. Process id: "+pid);
        }
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

    public void printMemory(){
        System.out.println(this.segments);
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
}
