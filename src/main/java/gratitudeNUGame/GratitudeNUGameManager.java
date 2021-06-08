package gratitudeNUGame;

import db.dao.GameDAO;
import db.models.Game;
import db.models.Player;
import gratitudeNUGame.models.PlayerGameSummary;
import gratitudeNUGame.models.PlayerGameSummary.GameEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GratitudeNUGameManager {
  private GameDAO gameDAO;

  public GratitudeNUGameManager(GameDAO gameDAO) {
    this.gameDAO = gameDAO;
  }

  public List<Integer> getPlayerIDs() {
    return gameDAO.getPlayerInfo().stream()
        .map(player -> player.getId())
        .collect(Collectors.toList());
  }

  public List<Integer> getGameNumbers() {
    return gameDAO.getGames().stream()
        .map(game -> game.getGameNumber())
        .collect(Collectors.toList());
  }

  public Game getGame(int gameNumber) {
    return gameDAO.getGames().stream()
        .filter(game -> game.getGameNumber() == gameNumber)
        .findAny().orElse(null);
  }

  public PlayerGameSummary getPlayerGameSummary(Player player) {
    List<GameEntry> gameEntries = new ArrayList<>();
    int totalScore = 0;
    int roundsPlayed = 0;
    int gamesWon = 0;

    for(Game game : gameDAO.getGames()) {
      if(!game.getPlayers().contains(player)) {
        continue;
      }

      List<Integer> playerTakes = game.getTakes().get(player.getId());
      int playerScore = playerTakes.stream()
          .reduce(0, Integer::sum);

      boolean playerWon = true;
      for(List<Integer> takes : game.getTakes().values()) {
        if(takes.stream().reduce(0, Integer::sum) > playerScore) {
          playerWon = false;
          break;
        }
      }

      roundsPlayed = roundsPlayed + playerTakes.size();
      totalScore = totalScore + playerScore;
      gameEntries.add(new GameEntry(game.getGameNumber(), playerTakes));
      if(playerWon)
        gamesWon = gamesWon + 1;
    }

    return new PlayerGameSummary(
        player.getName(),
        totalScore,
        (int) Math.round((double) totalScore / roundsPlayed),
        gamesWon,
        gameEntries
    );
  }

  public Player getPlayer(int playerID) {
    return gameDAO.getPlayerInfo().stream()
        .filter(player -> player.getId() == playerID)
        .findAny().orElse(null);
  }

  public void addPlayer(String name) {
    if(name == null
        || name.equals("")
        || gameDAO.getPlayerInfo().stream()
        .filter(player -> player.getName() == name)
        .findFirst().isPresent()) {
      throw new IllegalArgumentException();
    }
    gameDAO.insertPlayer(name);
  }
}
