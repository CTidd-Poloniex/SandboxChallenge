package gratitudeNUGame.models;

import db.models.Game;
import db.models.Player;

import java.util.List;

public class GameResultSummary {
    List<Player> players;
    Game game;

    public GameResultSummary() {
    }

    public GameResultSummary(List<Player> players, Game game) {
        this.players = players;
        this.game = game;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if(!(o instanceof GameResultSummary))
            return false;

        GameResultSummary other = (GameResultSummary) o;
        return this.players.equals(other.players)
                && this.game.equals(other.game);
    }

    @Override
    public final int hashCode() {
        return this.game.hashCode() + this.players.hashCode();
    }
}
