import java.util.ArrayList;

public class ProcessPool extends Thread{
    private final int PROCESS_COUNT;
    private ArrayList<Process> processes;
    private Memory memory;

    public ProcessPool(Memory memory, int processCount){
        this.PROCESS_COUNT = processCount;
        this.processes = new ArrayList<>();
        this.memory = memory;

        System.out.println("Initializing ProcessPool with "+PROCESS_COUNT+" processes");

        for (int i = 0; i < PROCESS_COUNT; i++){
            Process newProcesses = new Process(1000+i, this.memory);
            this.processes.add(newProcesses);
            System.out.println("Process created: "+ newProcesses.getPid());
        }

        for (int i = 0; i < PROCESS_COUNT; i++){
            final Process p = this.processes.get(i);
            p.start();
        }
    }


    @Override
    public void run() {
        try {
            sleep(100);
            while(true){
                long usedSpace = 0;
                long internalFragmentation = 0;
                boolean allProcessesAreTerminated = true;
                for (Process p: this.processes){
                    if (!p.isTerminated())
                        allProcessesAreTerminated = false;
                    if (p.getSegmentCounts() == 0)
                        continue;
                    else
                        usedSpace += p.getUsedSpace();
                    internalFragmentation += p.getInternalFragmentation();
                }

                log(usedSpace, internalFragmentation);

                if (allProcessesAreTerminated){
                    System.out.println("All processes terminated.");
                    return;
                }

                sleep(5000);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupt exception occurred");
        }

    }

    private void log(long memoryUsedSpace, long internalFrag){
        System.out.println("================================== Memory Log ==================================");

        System.out.println("\tOccupied space: "+ memoryUsedSpace);
        System.out.println("\tFree space: "+ (this.memory.getMemorySize()-memoryUsedSpace));
        System.out.println("\tInternal Frag.: "+ (internalFrag));
        System.out.println();

        for (Process p: this.processes){
            System.out.println("\tProcess: "+p.getPid());
            System.out.println("\t\tOccupied space:"+p.getUsedSpace());
            System.out.println("\t\tRun Time:"+(p.isTerminated() ? p.getRunTime()+" milliseconds" : "Running"));
            if (p.isTerminated())
                System.out.println("\t\tTerminated Time:"+p.getTerminatedTime());
        }

        System.out.println();
        this.memory.printMemory();

        System.out.println("================================================================================");
    }
}
