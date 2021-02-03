public class Main {
    public static void main(String []args){
        Memory memory = new Memory(1024);
        ProcessPool pool = new ProcessPool(memory, 2);
        pool.start();  // init logging memory every 5 sec
    }
}
