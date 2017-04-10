package de.ktran.anno1404warenrechner.event;

import de.ktran.anno1404warenrechner.data.Game;

public class GameOpenedEvent {
    private final Game game;

    public GameOpenedEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
