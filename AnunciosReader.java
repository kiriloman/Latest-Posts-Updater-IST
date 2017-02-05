public class AnunciosReader {
    public static void main(String[] args) throws Exception {
        new Thread(new Updater()).start();
    }
}
