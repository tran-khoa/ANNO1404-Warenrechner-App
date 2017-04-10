package de.ktran.anno1404warenrechner.data;


import java.util.ArrayList;
import java.util.List;

public class BuildingAlternative {
    private final List<ProductionChain> chainList = new ArrayList<>();
    private final Goods producedGood;

    public static BuildingAlternative of(ProductionChain chain) {
        final BuildingAlternative res = new BuildingAlternative(chain.getBuilding().getProduces());
        res.addBuildingAlternative(chain);

        return res;
    }

    public BuildingAlternative(Goods goods) {
        this.producedGood = goods;

    }

    public Goods getProducedGood() {
        return producedGood;
    }


    public void addBuildingAlternative(ProductionChain chain) {
        chainList.add(chain);
    }

    public int getCount() {
        return chainList.size();
    }

    public ProductionChain getChain() {
        return chainList.get(0);
    }

    public ProductionChain getChain(int position) {
        return chainList.get(position);
    }

    @Override
    public String toString() {
        return chainList.toString();
    }
}
