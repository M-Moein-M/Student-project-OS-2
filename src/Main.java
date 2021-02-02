public class Main {
    public static void main(String []args){
        System.out.println("All of the memory size and address are in KB");
        Memory memory = new Memory(1024);
        ProcessPool pool = new ProcessPool(memory);
    }
}
