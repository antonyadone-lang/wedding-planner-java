public class WeddingPlannerMain {
    public static void main(String[] args) {
        Invitato invitato1 = new Invitato("Mario", "Rossi", "mario.rossi@gmail.com");
        Invitato invitato2 = new Invitato("Rosa","Bianco", "rosa.bianco@yahoo.com");
        Invitato invitato3 = new Invitato("Luca","Dimuccio", "luca.dimuccio@gmail.com");
        Testimone testimone1 = new Testimone("Lisa","Lisi", "lisa.lisi@gmail.com",true );

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
    }
}