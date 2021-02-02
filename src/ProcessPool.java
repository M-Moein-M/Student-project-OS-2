import java.util.ArrayList;
import java.util.Random;

public class ProcessPool {
    private final int MAX_PROCESS_COUNT = 5;
    private ArrayList<Process> processes;

    public ProcessPool(){
        this.processes = new ArrayList<>();

        int numberOfInitialProcess = new Random().nextInt(this.MAX_PROCESS_COUNT) + 1;
        System.out.println("Initializing ProcessPool with "+numberOfInitialProcess+" process/processes");

        for (int i = 0; i < numberOfInitialProcess; i++){
            Process newProcesses = generateProcess(i);
            this.processes.add(newProcesses);
            System.out.println("Process created: "+ newProcesses.getPid());
        }

        for (int i = 0; i < numberOfInitialProcess; i++){
            final Process p = this.processes.get(i);
            p.start();
        }
    }

    public Process generateProcess(int index){
        // generate a number for id based on process index in this.processes
        return new Process(1000+index);
    }

}
