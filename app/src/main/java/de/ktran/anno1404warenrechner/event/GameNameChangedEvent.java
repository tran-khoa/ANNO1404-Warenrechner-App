package de.ktran.anno1404warenrechner.event;

import de.ktran.anno1404warenrechner.data.Game;

public class GameNameChangedEvent {
    private final Game game;

    public GameNameChangedEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
