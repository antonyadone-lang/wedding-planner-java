import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class WeddingPlannerMain {
    public static void main(String[] args) {
        // ========== CREAZIONE INVITATI E TESTIMONI ==========
        Invitato invitato1 = new Invitato("Mario", "Rossi", "mario.rossi@gmail.com");
        Invitato invitato2 = new Invitato("Rosa","Bianco", "rosa.bianco@yahoo.com");
        Invitato invitato3 = new Invitato("Luca","Dimuccio", "luca.dimuccio@gmail.com");
        Testimone testimone1 = new Testimone("Lisa","Lisi", "lisa.lisi@gmail.com",true );

        // ========== CREAZIONE COMPITI ==========
        Compiti compito1 = new Compiti("Invia inviti");
        Compiti compito2 = new Compiti("Prenotare Chiesa");
        Compiti compito3 = new Compiti("Ordinare Bomboniere");

        // ========== CREAZIONE TAVOLI ==========
        Tavolo tavolo1 = new Tavolo(1, 12);
        Tavolo tavolo2 = new Tavolo(2, 10);
        Tavolo tavolo3 = new Tavolo(3, 14);
        Tavolo tavolo4 = new Tavolo(4,1);

        // ========== INIZIALIZZAZIONE WEDDING MANAGER ==========
        WeddingManager weddingManager = new WeddingManager();

        // Registra invitati nel manager
        weddingManager.aggiungiInvitato(invitato1);
        weddingManager.aggiungiInvitato(invitato2);
        weddingManager.aggiungiInvitato(invitato3);
        weddingManager.aggiungiInvitato(testimone1);

        // ========== ASSEGNAZIONE TAVOLI ==========
        weddingManager.assegnaTavolo(invitato1, tavolo2);
        weddingManager.assegnaTavolo(invitato2, tavolo4);
        weddingManager.assegnaTavolo(invitato3, tavolo4);

        // ========== TEST POLIMORFISMO (presentati) ==========
        testimone1.presentati();
        invitato1.presentati();

        // ========== CREAZIONE FORNITORI ==========
        Fiorista fiorista1 = new Fiorista("Fiori Felici","fiori.felici@gmail.com",550,80);
        Fotografo fotografo1 = new Fotografo("Photo service","photo.service@yahoo.com",3000);
        Dj dj1 = new Dj("Agenzia Divertiamoci","divertiamoci@gmail.com", 2500);
        Catering catering1 = new Catering("Catering Service", "service.catering@gmail.com",200, 100);

        // ========== TEST ARRAY E POLIMORFISMO FORNITORI ==========
        ServiziMatrimonio[] fornitori = {fiorista1, fotografo1, dj1, catering1};
        for(ServiziMatrimonio fornitore : fornitori){
            fornitore.scheda();
            System.out.println("---");
        }

        // ========== REGISTRAZIONE FORNITORI NEL MANAGER ==========
        weddingManager.aggiungiFornitore(fiorista1);
        weddingManager.aggiungiFornitore(fotografo1);
        weddingManager.aggiungiFornitore(dj1);
        weddingManager.aggiungiFornitore(catering1);

        // ========== CALCOLO COSTI ==========
        double costoTotale = weddingManager.calcolaTotaleFornitori();
        double costoLordo = weddingManager.calcolaTotaleLordo();
        System.out.println("Costo Lordo matrimonio: €" + costoLordo);
        System.out.println("Costo totale matrimonio: €" + costoTotale);

        // ========== GESTIONE TRACCIABILI (interface) ==========
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

        // ========== TEST STATIC E FINAL ==========
        System.out.println("\n=== CONTATORE INVITATI ===");
        System.out.println("Totale invitati: " + Invitato.getTotaleInvitati());
        System.out.println("Nome: " + invitato1.getNome() + " Cognome: " + invitato1.getCognome() +  " ID: " + invitato1.getIdInvitato());
        System.out.println("Nome: " + invitato2.getNome() + " Cognome: " + invitato2.getCognome() +  " ID: " + invitato2.getIdInvitato());
        System.out.println("Nome: " + invitato3.getNome() + " Cognome: " + invitato3.getCognome() +  " ID: " + invitato3.getIdInvitato());
        System.out.println("Nome: " + testimone1.getNome() + " Cognome: " + testimone1.getCognome() +  " ID: " + testimone1.getIdInvitato());

        // ========== TEST GENERICS (RisultatoOperazione) ==========
        System.out.println("\n=== TEST RICERCA INVITATO ===");

        // Test 1: Email esistente
        RisultatoOperazione<Invitato> risultato1 =
                weddingManager.cercaInvitatoPerEmail("mario.rossi@gmail.com");
        if(risultato1.isSuccesso()){
            System.out.println("Trovato: " + risultato1.getDato().getNome() + " " + risultato1.getDato().getCognome());
        }else {
            System.out.println(" " + risultato1.getMessaggioErrore());
        }
        // Test 2: Email non esistente
        RisultatoOperazione<Invitato> risultato2 =
                weddingManager.cercaInvitatoPerEmail("test@gmail.com");
        if(risultato2.isSuccesso()){
            System.out.println("Trovato: " + risultato2.getDato().getNome());
        }else {
            System.out.println(risultato2.getMessaggioErrore());
        }
        // ========== TEST WILDCARDS (Upper Bound) ==========
        System.out.println("\n=== TEST WILDCARDS ===");

        // Lista di Invitati generici
        List<Invitato> listaInvitati = new ArrayList<>();
        listaInvitati.add(invitato1);
        listaInvitati.add(invitato2);

        // Lista di SOLO Testimoni
        List<Testimone> listaTestimoni = new ArrayList<>();
        listaTestimoni.add(testimone1);

        // Test 1: Lista di Invitati
        System.out.println("Lista Invitati:");
        weddingManager.stampaNomi(listaInvitati);

        // Test 2: Lista di Testimoni
        System.out.println("\nLista Testimoni:");
        weddingManager.stampaNomi(listaTestimoni);

        // ========== TEST ITERATOR (Rimozione RSVP) ==========
        System.out.println("\n=== TEST RIMOZIONE INVITATI SENZA RSVP ===");
        LocalDate dataLimite = LocalDate.of(2025, 10, 4);
        invitato1.setDataRisposta(LocalDate.of(2025, 8, 20));
        invitato1.setConfermato(true);
        invitato2.setDataRisposta(null);
        invitato3.setDataRisposta(LocalDate.of(2025,10,5));
        testimone1.setDataRisposta(LocalDate.of(2025, 8, 15));
        testimone1.setConfermato(true);
        int rimossi = weddingManager.rimuoviInvitatiSenzaRSVP(dataLimite);
        System.out.println("Invitati Rimossi: " + rimossi);
        System.out.println("\nInvitati Rimanenti: ");
        weddingManager.stampaNomi(weddingManager.getListaInvitati());

        // ========== TEST HASHSET (Duplicati) ==========
        System.out.println("\n=== TEST CONTROLLO DUPLICATI ===");
        Invitato duplicato = new Invitato ("Giovanni", "Verdi", "mario.rossi@gmail.com");
        boolean aggiunto = weddingManager.aggiungiInvitato(duplicato);
        if(!aggiunto){
            System.out.println("Duplicato correttamente bloccato");
        }

        // ========== TEST HASHMAP (Ricerca per ID) ==========
        System.out.println("\n=== TEST HASHMAP (Ricerca per ID) ===");
        System.out.println("ID Fiorista: " + fiorista1.getIdServizio());
        System.out.println("ID Fotografo: " + fotografo1.getIdServizio());
        System.out.println("ID Dj: " + dj1.getIdServizio());
        System.out.println("ID Catering: " + catering1.getIdServizio());

        // ========== TEST LAMBDA EXPRESSIONS ==========
        System.out.println("\n=== TEST LAMBA EXPRESSIONS ===");
        List<Invitato> confermati = weddingManager.filtraInvitati(inv -> inv.isConfermato());
        System.out.println("Invitati confermati: ");
        weddingManager.stampaNomi(confermati);

        System.out.println("\nInvitati che hanno risposto:");
        List<Invitato> conRisposta = weddingManager.filtraInvitati(inv -> inv.haRisposto());
        weddingManager.stampaNomi(conRisposta);

        System.out.println("\nTestimoni:");
        List<Invitato> testimoni = weddingManager.filtraInvitati(inv -> inv instanceof Testimone);
        weddingManager.stampaNomi(testimoni);

        // ========== TEST PREDICATE (Rimozione condizionale) ==========
        System.out.println("\n=== TEST PREDICATE (Rimozione condizionale) ===");
        System.out.println("Invitati prima: " + weddingManager.getListaInvitati().size());

        weddingManager.rimuoviSe(inv -> !inv.isConfermato());

        System.out.println("Invitati dopo rimozione non confermati: " + weddingManager.getListaInvitati().size());
        System.out.println("Invitati rimanenti:");
        weddingManager.stampaNomi(weddingManager.getListaInvitati());

    }
}