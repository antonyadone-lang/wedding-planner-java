import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrdinamentoPerStato implements OrdinamentoStrategy {
    @Override
    public List<Invitato> ordina(List<Invitato> listaNonOrdinata){
        return listaNonOrdinata.stream()
                .sorted(Comparator.comparing(Invitato::isConfermato).reversed())
                .collect(Collectors.toList());
    }
}
