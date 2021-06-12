package db.dao;

import db.models.Game;
import db.models.Player;
import java.util.ArrayList;
import java.util.List;

public class InMemoryGameDAO implements GameDAO{
  private ArrayList<Game> games;
  private ArrayList<Player> playerInfo;

  public InMemoryGameDAO() {
    this.games = new ArrayList<>();
    this.playerInfo = new ArrayList<>();
  }

  @Override
  public List<Game> getGames() {
    return new ArrayList<>(games);
  }

  @Override
  public List<Player> getPlayerInfo() {
    return new ArrayList<>(playerInfo);
  }

  public void insertPlayer(Player player) {
    this.playerInfo.add(player);
  }

  public void insertGame(Game game) {
    this.games.add(game);
  }
}
