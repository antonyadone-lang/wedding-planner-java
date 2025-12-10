public class ImpostazioniMatrimonio {
    private double budgetMassimo;
    private String dataEvento;
    private String location;

    public ImpostazioniMatrimonio(double budgetMassimo,String dataEvento,String location){
        this.budgetMassimo = budgetMassimo;
        this.dataEvento = dataEvento;
        this.location = location;
    }
    public double getBudgetMassimo(){
        return budgetMassimo;
    }
    public String getDataEvento(){
        return dataEvento;
    }
    public String getLocation(){
        return location;
    }
    @Override
    public String toString(){
        return "=== IMPOSTAZIONI MATRIMONIO ===\n"+
                "Budget Massimo: €" + budgetMassimo + "\n"+
                "Data Evento: " + dataEvento + "\n"+
                "Location: " + location;
    }
}
