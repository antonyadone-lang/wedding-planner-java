package com.wedding.util;

import com.wedding.service.IWeddingManager;

import java.io.IOException;

public class AutoSaveTask implements Runnable {
    private volatile boolean running = true;
    private IWeddingManager manager;

    public AutoSaveTask(IWeddingManager manager) {
        this.manager = manager;
    }

    public void stopRunning(){
        this.running = false;
    }

    @Override
    public void run() {
        while(running) {
                try{
                    Thread.sleep(10 * 1000);
                    if(running) {
                        manager.salvaInvitatiSuFile("autosave_invitati.csv");
                        // Commentiamo la stampa per non disturbare l'input utente
                        // System.out.println("[System] AutoSave: Backup su file completato con successo!");
                    }
        } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }catch (IOException e){
                    System.err.println("[AutoSave ERROR] Impossibile salvare il file di backup: " + e.getMessage());
                }
        }
        System.out.println("[System] Thread di backup arrestato correttamente.");
    }
}
