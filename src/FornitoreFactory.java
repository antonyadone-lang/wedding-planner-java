public class FornitoreFactory {
    public static ServiziMatrimonio creareFornitore(TipoFornitore tipo, String nome, String contatto, double... parametri){
        switch (tipo){
            case FOTOGRAFO:
                return new Fotografo(nome, contatto, parametri[0]);

            case DJ:
                return new Dj(nome, contatto, parametri[0]);

            case CATERING:
                return new Catering(nome, contatto, parametri[0], (int)parametri[1]);

            case FIORISTA:
                return new Fiorista(nome, contatto, parametri[0], parametri[1]);

            default:
                throw new IllegalArgumentException( "Tipo fornitore non valido: " + tipo);

        }
    }
}
