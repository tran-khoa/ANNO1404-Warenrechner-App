package de.ktran.anno1404warenrechner.event;

import de.ktran.anno1404warenrechner.data.Game;


public class PopulationResultEvent extends DataResponseEvent {

    private final Game game;

    public PopulationResultEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
