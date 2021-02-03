import java.util.ArrayList;
import java.util.Random;

public class ProcessPool {
    private final int PROCESS_COUNT;
    private ArrayList<Process> processes;
    private Memory memory;

    public ProcessPool(Memory memory, int processCount){
        this.PROCESS_COUNT = processCount;
        this.processes = new ArrayList<>();
        this.memory = memory;

        System.out.println("Initializing ProcessPool with "+PROCESS_COUNT+" processes");

        for (int i = 0; i < PROCESS_COUNT; i++){
            Process newProcesses = generateProcess(i);
            this.processes.add(newProcesses);
            System.out.println("Process created: "+ newProcesses.getPid());
        }

        for (int i = 0; i < PROCESS_COUNT; i++){
            final Process p = this.processes.get(i);
            p.start();
        }
    }

    public Process generateProcess(int index){
        // generate a number for id based on process index in this.processes
        return new Process(1000+index, this.memory);
    }

}
