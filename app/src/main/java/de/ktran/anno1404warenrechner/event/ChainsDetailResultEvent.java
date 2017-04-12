package de.ktran.anno1404warenrechner.event;

import java.util.List;

import de.ktran.anno1404warenrechner.data.BuildingAlternative;
import de.ktran.anno1404warenrechner.data.Goods;

public class ChainsDetailResultEvent extends DataResponseEvent {
    private final List<BuildingAlternative> result;
    private final Goods goods;

    public ChainsDetailResultEvent(List<BuildingAlternative> result, Goods goods) {
        this.result = result;
        this.goods = goods;
    }

    public Goods getGoods() {
        return goods;
    }

    public List<BuildingAlternative> getResult() {

        return result;
    }
}
