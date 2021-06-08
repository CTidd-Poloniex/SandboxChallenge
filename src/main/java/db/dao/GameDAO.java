package db.dao;

import db.models.Game;
import db.models.Player;
import java.util.List;

public interface GameDAO {
  List<Game> getGames();
  List<Player> getPlayerInfo();
  void insertGame(Game game);
  void insertPlayer(String name);
}
