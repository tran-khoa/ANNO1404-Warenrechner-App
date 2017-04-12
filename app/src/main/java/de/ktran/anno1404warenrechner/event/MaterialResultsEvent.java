package de.ktran.anno1404warenrechner.event;

import de.ktran.anno1404warenrechner.data.Game;

public class MaterialResultsEvent extends DataResponseEvent {
    private final Game game;

    public MaterialResultsEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
