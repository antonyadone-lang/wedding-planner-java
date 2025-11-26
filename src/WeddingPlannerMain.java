public class WeddingPlannerMain {
    public static void main(String[] args) {
        Invitato invitato1 = new Invitato("Mario", "Rossi", "mario.rossi@gmail.com");
        Invitato invitato2 = new Invitato("Rosa","Bianco", "rosa.bianco@yahoo.com");
        Invitato invitato3 = new Invitato("Luca","Dimuccio", "luca.dimuccio@gmail.com");
        Testimone testimone1 = new Testimone("Lisa","Lisi", "lisa.lisi@gmail.com",true );
        Compiti compito1 = new Compiti("Invia inviti");
        Compiti compito2 = new Compiti("Prenotare Chiesa");
        Compiti compito3 = new Compiti("Ordinare Bomboniere");

        Tavolo tavolo1 = new Tavolo(1, 12);
        Tavolo tavolo2 = new Tavolo(2, 10);
        Tavolo tavolo3 = new Tavolo(3, 14);
        Tavolo tavolo4 = new Tavolo(4,1);

        WeddingManager weddingManager = new WeddingManager();
        weddingManager.assegnaTavolo(invitato1, tavolo2);
        weddingManager.assegnaTavolo(invitato2, tavolo4);
        weddingManager.assegnaTavolo(invitato3, tavolo4);

        testimone1.presentati();
        invitato1.presentati();

        Fiorista fiorista1 = new Fiorista("Fiori Felici","fiori.felici@gmail.com",550,80);
        Fotografo fotografo1 = new Fotografo("Photo service","photo.service@yahoo.com",3000);
        Dj dj1 = new Dj("Agenzia Divertiamoci","divertiamoci@gmail.com", 2500);
        Catering catering1 = new Catering("Catering Service", "service.catering@gmail.com",200, 100);
        ServiziMatrimonio[] fornitori = {fiorista1, fotografo1, dj1, catering1};
        for(ServiziMatrimonio fornitore : fornitori){
            fornitore.scheda();
            System.out.println("---");
        }
        weddingManager.aggiungiFornitore(fiorista1);
        weddingManager.aggiungiFornitore(fotografo1);
        weddingManager.aggiungiFornitore(dj1);
        weddingManager.aggiungiFornitore(catering1);
        double costoTotale = weddingManager.calcolaTotaleFornitori();
        System.out.println("Costo totale matrimonio: €" + costoTotale);

        weddingManager.aggiungiTracciabile(fiorista1);
        weddingManager.aggiungiTracciabile(compito1);
        weddingManager.aggiungiTracciabile(compito2);
        weddingManager.aggiungiTracciabile(compito3);
        System.out.println("\n=== STATO INIZIALE TRACCIABILI ===");
        weddingManager.mostraStatoTracciabili();

        System.out.println("\n=== COMPLETAMENTO ATTIVITÀ ===");
        compito2.segnaCompletato();
        compito3.segnaCompletato();
        fiorista1.segnaCompletato();

        System.out.println("\n=== STATO FINALE ===");
        weddingManager.mostraStatoTracciabili();

    }
}