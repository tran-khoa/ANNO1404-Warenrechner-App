package de.ktran.anno1404warenrechner.data;

public class ProductionChain {
    private final ProductionBuilding building;
    private final int chains;
    private final float efficiency;

    public static ProductionChain with(ProductionBuilding building, int chains, float efficiency) {
        return new ProductionChain(building, chains, efficiency);
    }

    public ProductionChain(ProductionBuilding building, int chains, float efficiency) {
        this.building = building;
        this.chains = chains;
        this.efficiency = efficiency;
    }

    public ProductionBuilding getBuilding() {
        return building;
    }

    public int getChains() {
        return chains;
    }

    public float getEfficiency() {
        return efficiency;
    }
}
