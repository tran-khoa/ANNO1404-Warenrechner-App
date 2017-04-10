package de.ktran.anno1404warenrechner.event;

import java.util.List;

import de.ktran.anno1404warenrechner.data.Game;
import de.ktran.anno1404warenrechner.views.main.GameListAdapter;

/**
 * Result for {@link GameListAdapter}
 */
public class GameListResultEvent extends DataResponseEvent {
    private final List<Game> results;

    public GameListResultEvent(List<Game> results) {
        this.results = results;
    }

    public List<Game> getResults() {
        return results;
    }
}
