package com.wedding.model;

public class ImpostazioniMatrimonio {
    private final double budgetMassimo;
    private final String dataEvento;
    private final String location;

    public ImpostazioniMatrimonio(double budgetMassimo, String dataEvento, String location) {
        this.budgetMassimo = budgetMassimo;
        this.dataEvento = dataEvento;
        this.location = location;
    }

    public double getBudgetMassimo() {
        return budgetMassimo;
    }

    public String getDataEvento() {
        return dataEvento;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "=== IMPOSTAZIONI MATRIMONIO ===\n" +
                "Budget Massimo: €" + budgetMassimo + "\n" +
                "Data Evento: " + dataEvento + "\n" +
                "Location: " + location;
    }
}
