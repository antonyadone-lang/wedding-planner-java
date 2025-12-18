public class AutoSaveTask implements Runnable{
    private volatile boolean running = true;

    public void stopRunning(){
        this.running = false;
    }

    @Override
    public void run() {
        while(running) {
                try{
                    Thread.sleep(10 * 1000);
                    if(running) {
                        System.out.println("[System] backup completato!");
                    }
        } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
        }
        System.out.println("[System] Thread di backup arrestato correttamente.");
    }
}
