import java.util.ArrayList;

public class Memory {
    private final long MEMORY_SIZE;
    private Process [] processes;
    private ArrayList<Segment> segments;
    private final long MIN_SEG_SIZE = 32;

    public Memory(long memorySize){
        this.MEMORY_SIZE = memorySize;
        this.segments = new ArrayList<>();
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
            }
        }
        catch (NotEnoughMemoryError e){
            System.out.println("Memory allocation Failed. Not Enough memory space. Process id: "+pid);
        }
    }

    private void splitMemorySegment(long segmentIndex, long neededSize){
        // split i-th segment in this.segments to two parts until neededSize is satisfied
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
