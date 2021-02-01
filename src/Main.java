public class Main {
    public static void main(String []args){
        Memory memory = new Memory(256);
        try {
            System.out.println(memory.findOptimumSize(3));
        }catch (NotEnoughMemoryError e){}
    }
}
