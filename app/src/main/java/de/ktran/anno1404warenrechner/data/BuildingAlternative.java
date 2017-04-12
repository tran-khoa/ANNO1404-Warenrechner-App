package de.ktran.anno1404warenrechner.data;


import java.util.ArrayList;
import java.util.List;

/**
 * This is unnecessary at the moment as the only good which can
 * be produced by two different buildings is coal.
 *
 * May be necessary for modded ANNO 1404 or ANNO 2070, though.
 */
public class BuildingAlternative {
    private final List<ProductionChain> chainList = new ArrayList<>();
    private final Goods producedGood;

    public static BuildingAlternative of(ProductionChain chain) {
        final BuildingAlternative res = new BuildingAlternative(chain.getBuilding().getProduces());
        res.addBuildingAlternative(chain);

        return res;
    }

    BuildingAlternative(Goods goods) {
        this.producedGood = goods;

    }

    public Goods getProducedGood() {
        return producedGood;
    }


    void addBuildingAlternative(ProductionChain chain) {
        chainList.add(chain);
    }

    public ProductionChain getChain() {
        return chainList.get(0);
    }

    @Override
    public String toString() {
        return chainList.toString();
    }
}
