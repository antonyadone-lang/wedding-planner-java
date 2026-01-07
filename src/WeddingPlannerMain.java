import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WeddingPlannerMain {
    static void main(String[] args) throws IOException, ClassNotFoundException {
        // ========== CREAZIONE INVITATI E TESTIMONI ==========
        Invitato invitato1 = new Invitato("Mario", "Rossi", "mario.rossi@gmail.com");
        Invitato invitato2 = new Invitato("Rosa", "Bianco", "rosa.bianco@yahoo.com");
        Invitato invitato3 = new Invitato("Luca", "Dimuccio", "luca.dimuccio@gmail.com");
        Testimone testimone1 = new Testimone("Lisa", "Lisi", "lisa.lisi@gmail.com", true);

        // ========== CREAZIONE COMPITI ==========
        Compiti compito1 = new Compiti("Invia inviti");
        Compiti compito2 = new Compiti("Prenotare Chiesa");
        Compiti compito3 = new Compiti("Ordinare Bomboniere");

        // ========== CREAZIONE TAVOLI ==========
        Tavolo tavolo1 = new Tavolo(1, 12);
        Tavolo tavolo2 = new Tavolo(2, 10);
        Tavolo tavolo3 = new Tavolo(3, 14);
        Tavolo tavolo4 = new Tavolo(4, 1);

        // ========== INIZIALIZZAZIONE WEDDING MANAGER ==========
        IWeddingManager weddingManager = WeddingManagerSQL.getInstance();
        weddingManager.setBudgetMassimo(6000);

        // Registra invitati nel manager
        weddingManager.aggiungiInvitato(invitato1);
        weddingManager.aggiungiInvitato(invitato2);
        weddingManager.aggiungiInvitato(invitato3);
        weddingManager.aggiungiInvitato(testimone1);

        // ========== ASSEGNAZIONE TAVOLI ==========
        try {
            weddingManager.assegnaTavolo(invitato1, tavolo2);
            weddingManager.assegnaTavolo(invitato2, tavolo4);
            weddingManager.assegnaTavolo(invitato3, tavolo4);
            weddingManager.assegnaTavolo(testimone1, tavolo2);
        } catch (TavoloPienoException e) {
            System.out.println("ERRORE: " + e.getMessage());
        }

        // ========== TEST POLIMORFISMO (presentati) ==========
        testimone1.presentati();
        invitato1.presentati();

        // ========== CREAZIONE FORNITORI ==========
        Fiorista fiorista1 = new Fiorista("Fiori Felici", "fiori.felici@gmail.com", 550, 80);
        Fotografo fotografo1 = new Fotografo("Photo service", "photo.service@yahoo.com", 3000);
        Dj dj1 = new Dj("Agenzia Divertiamoci", "divertiamoci@gmail.com", 2500);
        Catering catering1 = new Catering("Catering Service", "service.catering@gmail.com", 200, 100);

        // ========== TEST ARRAY E POLIMORFISMO FORNITORI ==========
        ServiziMatrimonio[] fornitori = {fiorista1, fotografo1, dj1, catering1}; // Questo array ora è ridondante, ma lo lasciamo per il test
        for (ServiziMatrimonio fornitore : fornitori) {
            fornitore.scheda();
            System.out.println("---");
        }

        // ========== REGISTRAZIONE FORNITORI NEL MANAGER ==========
        try{
            weddingManager.aggiungiFornitore(fiorista1);
            weddingManager.aggiungiFornitore(fotografo1);
            weddingManager.aggiungiFornitore(dj1);
            weddingManager.aggiungiFornitore(catering1);
        } catch (BudgetSuperatoException e) {
            System.out.println("\n--- ERRORE BUDGET ---");
            System.out.println(e.getMessage());
        }

        // ========== CALCOLO COSTI ==========
        double costoTotale = weddingManager.calcolaTotaleFornitori();
        double costoLordo = weddingManager.calcolaTotaleLordo();
        System.out.println("Costo Lordo matrimonio: €" + costoLordo);
        System.out.println("Costo totale matrimonio: €" + costoTotale);

        // ========== TEST STREAM API (Costo Servizi non Pagati) ==========
        System.out.println("\n=== TEST STREAM API - Servizi Non Pagati ===");
        fotografo1.setPagato(true);
        dj1.setPagato(true);
        double costoNonPagati = weddingManager.calcoloCostoServiziNonPagati();
        System.out.println("Costo Servizi non pagati: €" + costoNonPagati);

        // ========== TEST STREAM API (Invitati per tavolo) ==========
        System.out.println("\n=== TEST STREAM API - Invitati per tavolo ===");
        List<Invitato> invitatiTavolo4 = weddingManager.getInvitatiPerTavolo(4);
        System.out.println("Invitati al Tavolo 4:");
        weddingManager.stampaNomi(invitatiTavolo4);
        List<Invitato> invitatiTavolo2 = weddingManager.getInvitatiPerTavolo(2);
        System.out.println("Invitati al Tavolo 2:");
        weddingManager.stampaNomi(invitatiTavolo2);


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
        System.out.println("Nome: " + invitato1.getNome() + " Cognome: " + invitato1.getCognome() + " ID: " + invitato1.getIdInvitato());
        System.out.println("Nome: " + invitato2.getNome() + " Cognome: " + invitato2.getCognome() + " ID: " + invitato2.getIdInvitato());
        System.out.println("Nome: " + invitato3.getNome() + " Cognome: " + invitato3.getCognome() + " ID: " + invitato3.getIdInvitato());
        System.out.println("Nome: " + testimone1.getNome() + " Cognome: " + testimone1.getCognome() + " ID: " + testimone1.getIdInvitato());

        // ========== TEST GENERICS (RisultatoOperazione) ==========
        System.out.println("\n=== TEST RICERCA INVITATO ===");

        // Test 1: Email esistente
        RisultatoOperazione<Invitato> risultato1 =
                weddingManager.cercaInvitatoPerEmail("mario.rossi@gmail.com");
        if (risultato1.isSuccesso()) {
            System.out.println("Trovato: " + risultato1.getDato().getNome() + " " + risultato1.getDato().getCognome());
        } else {
            System.out.println(" " + risultato1.getMessaggioErrore());
        }
        // Test 2: Email non esistente
        RisultatoOperazione<Invitato> risultato2 =
                weddingManager.cercaInvitatoPerEmail("test@gmail.com");
        if (risultato2.isSuccesso()) {
            System.out.println("Trovato: " + risultato2.getDato().getNome());
        } else {
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
        invitato1.setStato(StatoInvitato.CONFERMATO);
        invitato2.setDataRisposta(null);
        invitato3.setDataRisposta(LocalDate.of(2025, 10, 5));
        testimone1.setDataRisposta(LocalDate.of(2025, 8, 15));
        testimone1.setStato(StatoInvitato.CONFERMATO);
        int rimossi = weddingManager.rimuoviInvitatiSenzaRSVP(dataLimite);
        System.out.println("Invitati Rimossi: " + rimossi);
        System.out.println("\nInvitati Rimanenti: ");
        weddingManager.stampaNomi(weddingManager.getListaInvitati());

        // ========== TEST HASHSET (Duplicati) ==========
        System.out.println("\n=== TEST CONTROLLO DUPLICATI ===");
        Invitato duplicato = new Invitato("Giovanni", "Verdi", "mario.rossi@gmail.com");
        boolean aggiunto = weddingManager.aggiungiInvitato(duplicato);
        if (!aggiunto) {
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

        // ========== TEST EXCEPTION HANDLING - Carica Impostazioni ===
        System.out.println("\n=== TEST EXCEPTION HANDLING ===");
        ImpostazioniMatrimonio impostazioni1 = weddingManager.caricaImpostazioni("impostazioni.txt");
        System.out.println(impostazioni1);
        System.out.println();
        System.out.println("Test file inesistente");
        ImpostazioniMatrimonio impostazioni2 = weddingManager.caricaImpostazioni("file_inesistente.txt");
        System.out.println(impostazioni2);

        // ========== TEST CUSTOM EXCEPTION (TavoloPienoException) ==========
        System.out.println("\n=== TEST CUSTOM EXCEPTION - Tavolo Pieno ===");
        Tavolo tavolotest = new Tavolo(99, 1);
        Invitato test1 = new Invitato("Test1", "Uno", "test1@test.com");
        Invitato test2 = new Invitato("Test2", "Due", "test2@test.com");
        try {
            System.out.println("Tentativo 1: Assegno primo invitato");
            weddingManager.assegnaTavolo(test1, tavolotest); // OK

            System.out.println("Tentativo 2: Assegno secondo invitato");
            weddingManager.assegnaTavolo(test2, tavolotest);
        } catch (TavoloPienoException e) {
            System.out.println("Eccezione catturata correttamente");
            System.out.println("Messaggio: " + e.getMessage());
        }
        // ========== TEST SALVATAGGIO/CARICAMENTO CSV ==========
        System.out.println("\n=== TEST SALVATAGGIO/CARICAMENTO CSV ===");

        System.out.println("\n --- Creazione e Salvataggio ---");
        Invitato csv1 = new Invitato("Paolo", "Giallo", "paolo.giallo@gmail.com");
        csv1.setStato(StatoInvitato.CONFERMATO);
        weddingManager.aggiungiInvitato(csv1);

        Invitato csv2 = new Invitato("Marta", "Verde", "marta.verde@gmail.com");
        csv2.setStato(StatoInvitato.CONFERMATO);
        weddingManager.aggiungiInvitato(csv2);

        Invitato csv3 = new Invitato("Giacomo", "Pinko", "giacomo.pinko@gmail.com");
        csv3.setStato(StatoInvitato.CONFERMATO);
        weddingManager.aggiungiInvitato(csv3);

        weddingManager.salvaInvitatiSuFile("invitati.csv");

        System.out.println("Lista invitati PRIMA dello svuotamento");
        for (Invitato inv : weddingManager.getListaInvitati()) {
            System.out.println(inv.getNome() + " " + inv.getCognome() + " - Confermato: " + inv.isConfermato());
        }

        System.out.println("\n--- Simulazione chiusura programma ---");
        weddingManager.svuotaTutto();

        System.out.println("Lista invitati DOPO lo svuotamento:");
        if (weddingManager.getListaInvitati().isEmpty()) {
            System.out.println("(Lista vuota - simulazione chiusura programma)");
        }

        weddingManager.caricaInvitatiDaFile("invitati.csv");

        System.out.println("Lista invitati DOPO il caricamento:");
        for (Invitato inv : weddingManager.getListaInvitati()) {
            System.out.println(inv.getNome() + " " + inv.getCognome() + " - Confermato: " + inv.isConfermato());
        }

        // ========== TEST SERIALIZZAZIONE BINARIA ==========
        System.out.println("\n=== TEST SERIALIZZAZIONE BINARIA ===");
        System.out.println("Creazione e salvataggio binario");

        Invitato bin1 = new Invitato("Anna", "Neri", "anna.neri@test.com");
        bin1.setStato(StatoInvitato.CONFERMATO);
        weddingManager.aggiungiInvitato(bin1);

        Invitato bin2 = new Invitato("Marco", "Blu", "marco.blue@test.com");
        bin2.setStato(StatoInvitato.RIFIUTATO);
        weddingManager.aggiungiInvitato(bin2);

        Fotografo fotoTest = new Fotografo("Studio foto", "foto@test.com", 1500);

        Dj djTest = new Dj("DJ Music", "dj@test.com", 800);

        try {
            weddingManager.aggiungiFornitore(fotoTest);
            weddingManager.aggiungiFornitore(djTest);
        }catch (BudgetSuperatoException e) {
            System.out.println("Errore aggiunta fornitori test binario: " + e.getMessage());
        }

        weddingManager.salvaDatiBinari("backup.dat");

        System.out.println("Invitati in memoria: " + weddingManager.getListaInvitati().size());
        ServiziMatrimonio fornitoreCercato = weddingManager.cercaFornitorePerId(fotoTest.getIdServizio());
        if (fornitoreCercato != null) {
            System.out.println("Fornitori in memoria: " + fornitoreCercato.getNomeFornitore());
        } else {
            System.out.println("Fornitori in memoria: [Dato non disponibile su SQL]");
        }

        System.out.println("\n=== Simulazione Chiusura programma ===");

        weddingManager.getListaInvitati().clear();
        weddingManager.svuotaTutto();

        System.out.println("Invitati dopo svuotamento: " + weddingManager.getListaInvitati().size());
        if (weddingManager.getListaInvitati().isEmpty()) {
            System.out.println("Liste vuote - simulazione chiusura programma");
        }

        System.out.println("\n=== Caricamento da File Binario ===");
        weddingManager.caricaDatiBinari("backup.dat");
        System.out.println("Invitati dopo caricamento: " + weddingManager.getListaInvitati().size());
        System.out.println("Lista invitati:");
        for (Invitato inv : weddingManager.getListaInvitati()) {
            System.out.println("  -  " + inv.getNome() + " " + inv.getCognome());
        }

        System.out.println("\n=== TEST RICOSTRUZIONE HASHMAP ===");
        ServiziMatrimonio fornitoreRecuperato = weddingManager.cercaFornitorePerId(fotoTest.getIdServizio());
        if (fornitoreRecuperato != null) {
            System.out.println("Hashmap ricostruita correttamente!");
            System.out.println("Fornitore trovato per ID " + fotoTest.getIdServizio());
            System.out.println("Costo: €" + fornitoreRecuperato.calcoloCosto());
        } else {
            System.out.println("Errore: Hashmap non ricostruita");
        }
        // ========== TEST SINGLETON PATTERN ==========
        System.out.println("\n=== TEST SINGLETON PATTERN ===");
        WeddingManager manager2 = WeddingManager.getInstance();
        System.out.println("Stessa istanza? " + (weddingManager == manager2));
        System.out.println("HashCode manager1: " + weddingManager.hashCode());
        System.out.println("HashCode manager2: " + manager2.hashCode());

        // ========== TEST BUILDER PATTERN ==========
        System.out.println("\n=== TEST BUILDER PATTERN ===");
        Invitato builderTest1 = new Invitato.Builder("Fabrizio", "Signorini", "fabrizio.signorini@gmail.com")
                .stato(StatoInvitato.CONFERMATO)
                .dietaSpeciale("Vegetariana")
                .allergie("Glutine, Lattosio")
                .numeroTelefono("+39 1234567890")
                .build();
        System.out.println("Invitato 1 (completo):");
        System.out.println(" Nome: " + builderTest1.getNome() + " " + builderTest1.getCognome());
        System.out.println(" Confermato: " + builderTest1.isConfermato());
        System.out.println(" Dieta: " + builderTest1.getDietaSpeciale());
        System.out.println(" Allergie: " + builderTest1.getAllergie());
        System.out.println(" Telefono: " + builderTest1.getNumeroTelefono());

        // ========== TEST FACTORY METHOD PATTERN ==========
        System.out.println("\n=== TEST FACTORY METHOD PATTERN ===");

        ServiziMatrimonio fotoFactory = FornitoreFactory.creareFornitore(
                TipoFornitore.FOTOGRAFO,
                "Studio Fotografico Elite",
                "elite.foto@gmail.com",
                2000
        );

        ServiziMatrimonio djFactory = FornitoreFactory.creareFornitore(
                TipoFornitore.DJ,
                "DJ party",
                "djparty@gmail.com",
                1200
        );

        ServiziMatrimonio cateringFactory = FornitoreFactory.creareFornitore(
                TipoFornitore.CATERING,
                "Catering Deluxe",
                "cateringdeluxe@gmail.com",
                60,
                120
        );

        ServiziMatrimonio fioristaFactory = FornitoreFactory.creareFornitore(
                TipoFornitore.FIORISTA,
                "Fiori & co",
                "infofiori@gmail.com",
                600,
                100
        );

        System.out.println("Fornitori creati con Factory:");
        fotoFactory.scheda();
        System.out.println("Costo: €" + fotoFactory.calcoloCosto());
        System.out.println();

        djFactory.scheda();
        System.out.println("Costo: €" + djFactory.calcoloCosto());
        System.out.println();

        cateringFactory.scheda();
        System.out.println("Costo: €" + cateringFactory.calcoloCosto());
        System.out.println();

        fioristaFactory.scheda();
        System.out.println("Costo: €" + fioristaFactory.calcoloCosto());

        // ========== TEST STRATEGIE DI ORDINAMENTO ==========

        // Test ordiamento per cognome
        System.out.println("\n=== Ordinamento per cognome ===");
        weddingManager.stampaListaInvitati(new OrdinamentoPerCognome());

        // Test ordinamento per stato di conferma
        System.out.println("\n=== Ordinamento per stato di conferma ===");
        weddingManager.stampaListaInvitati(new OrdinamentoPerStato());

        // ========== TEST MULTI THREAD ==========
        System.out.println("\n=== TEST MULTI THREAD ===");
        AutoSaveTask task = new AutoSaveTask(weddingManager);
        Thread nuovoThread = new Thread(task);

        nuovoThread.start();

        Scanner scanner = new Scanner(System.in);
        int scelta = -1;

        System.out.println("--- Wedding Manager Pro ---");

        while(scelta != 0) {
            System.out.println("\nMenu:");
            System.out.println("1. Aggiungi Invitato (Simulazione)");
            System.out.println("2. Conferma Invitato");
            System.out.println("0. Esci e ferma il sistema");
            System.out.println("Scelta: ");

            scelta = scanner.nextInt();
            scanner.nextLine();

            if (scelta == 1) {
                System.out.println("Inserisci il nome: ");
                String nome = scanner.nextLine();
                System.out.println("Inserisci il cognome: ");
                String cognome = scanner.nextLine();
                System.out.println("Inserisci l'email: ");
                String email = scanner.nextLine();

                System.out.println("Invitato aggiunto correttamente!");
            }else if (scelta == 2) {
                System.out.println("Inserisci l'email dell'invitato: ");
                String emailDaConfermare = scanner.nextLine();
                try {
                    weddingManager.confermaInvitato(emailDaConfermare);
                }catch (InvitatoNonTrovatoException e) {
                    System.out.println("ERRORE: " + e.getMessage());
                    System.out.println("Controlla la lista degli invitato e riprova.");
                }
            }else if (scelta != 0) {
                System.out.println("Scelta non valida.");
            }
        }
        task.stopRunning();
        nuovoThread.interrupt();
        System.out.println("Sistema in chiusura...");

        try {
            nuovoThread.join();
        }catch (InterruptedException e) {
            System.out.println("Errore durante l'attesa del thread.");
        }
        System.out.println("Sistema terminato.");

        // ========== TEST DATABASE (DAO) ==========
        System.out.println("\n=== TEST DATABASE (DAO) ===");
        InvitatoDAO dao = new InvitatoDAO();

        Invitato dbInv =new Invitato("Giuseppe", "Verdi", "giuseppe.verdi@db.com");
        dbInv.setStato(StatoInvitato.CONFERMATO);

        try {
            dao.salva(dbInv);
            System.out.println("Salvataggio OK!");

            List<Invitato> dalDb = dao.caricaTutti();
            System.out.println("Invitati nel DB: " + dalDb.size());
            for (Invitato i : dalDb) {
                System.out.println(" - " + i.getNome() + " " + i.getCognome() + " (" + i.getStato() + ")");
            }
        }catch (Exception e) {
            System.out.println("Errore DB: " + e.getMessage());
            e.printStackTrace();
        }
    }
}