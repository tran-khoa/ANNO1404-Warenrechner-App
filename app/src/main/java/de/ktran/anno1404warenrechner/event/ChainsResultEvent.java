package de.ktran.anno1404warenrechner.event;

import android.support.annotation.NonNull;

import java.util.List;

import de.ktran.anno1404warenrechner.data.Game;
import de.ktran.anno1404warenrechner.data.ProductionChain;

public class ChainsResultEvent extends DataResponseEvent {
    private final List<ProductionChain> result;
    private final Game game;

    public ChainsResultEvent(Game game, List<ProductionChain> result) {
        this.game = game;
        this.result = result;
    }

    public List<ProductionChain> getResult() {
        return result;
    }

    public boolean isGame(@NonNull final Game game) {
        return this.game.equals(game);
    }
}
