package de.ktran.anno1404warenrechner.event;

import java.util.List;

import de.ktran.anno1404warenrechner.data.BuildingAlternative;
import de.ktran.anno1404warenrechner.data.ProductionChain;

public class ChainsDetailResultEvent extends DataResponseEvent {
    private final List<BuildingAlternative> result;
    private final ProductionChain chain;

    public ChainsDetailResultEvent(List<BuildingAlternative> result, ProductionChain chain) {
        this.result = result;
        this.chain = chain;
    }

    public ProductionChain getChain() {
        return chain;
    }

    public List<BuildingAlternative> getResult() {

        return result;
    }
}
