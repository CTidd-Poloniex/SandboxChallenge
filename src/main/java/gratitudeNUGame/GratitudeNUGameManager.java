package gratitudeNUGame;

import db.dao.GameDAO;
import db.models.Game;
import db.models.Player;
import gratitudeNUGame.models.GameResultSummary;
import gratitudeNUGame.models.PlayerGameSummary;
import gratitudeNUGame.models.PlayerGameSummary.GameEntry;

import javax.ws.rs.NotFoundException;
import java.util.*;
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
    List<Game> games =  gameDAO.getGames();
    return games.stream()
        .map(game -> game.getGameNumber())
        .collect(Collectors.toList());
  }

  public Game getGame(int gameNumber) {
    return gameDAO.getGames().stream()
        .filter(game -> game.getGameNumber() == gameNumber)
        .findAny().orElse(null);
  }

  public GameResultSummary getGameResultSummary(int gameNumber) {
    Game game = getGame(gameNumber);
    if(game == null) {
      throw new NotFoundException("Game not found.");
    }
    List<Player> players = game.getPlayers().stream()
            .map(playerID -> getPlayer(playerID))
            .collect(Collectors.toList());

    return new GameResultSummary(players, game);
  }

  public PlayerGameSummary getPlayerGameSummary(Player player) {
    List<GameEntry> gameEntries = new ArrayList<>();
    int totalScore = 0;
    int roundsPlayed = 0;
    int gamesWon = 0;

    for(Game game : gameDAO.getGames()) {
      if(!game.getPlayers().contains(player.getId())) {
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
        .findAny().orElseThrow(() -> new NotFoundException());
  }

  public void addPlayer(String name) {
    if(name == null
        || name.equals("")
        || gameDAO.getPlayerInfo().stream()
        .filter(player -> player.getName() == name)
        .findFirst().isPresent()) {
      throw new IllegalArgumentException();
    }
    List<Player> heldPlayers = gameDAO.getPlayerInfo();
    int uniqueID = heldPlayers.get(heldPlayers.size() < 1 ? 0 :heldPlayers.size() - 1).getId() + 1;
    gameDAO.insertPlayer(new Player(uniqueID, name));
  }

  public void addGame(List<Integer> players, Map<Integer, List<Integer>> takes) {
    Set<Integer> playerSet = new HashSet<>(players);
    Set<Integer> takesKeys = takes.keySet();

    if(playerSet.size() != players.size()) {
      throw new IllegalArgumentException("Duplicate players in game.");
    }
    if(!takesKeys.equals(playerSet)) {
      throw new IllegalArgumentException("Player set is not set equal to players in takes.");
    }

    Integer prevPlayerTakesRounds = null;
    for(Integer playerID : players) {
      Player player = getPlayer(playerID);
      if(player == null) {
        throw new IllegalArgumentException(String.format("PlayerID %n is unrecognized."));
      }
      int curPlayerTakesRounds = takes.get(playerID).size();
      if(prevPlayerTakesRounds != null && curPlayerTakesRounds != prevPlayerTakesRounds) {
        throw new IllegalArgumentException("Inconsistent amount of rounds played.");
      }
      prevPlayerTakesRounds = curPlayerTakesRounds;
    }

    List<Game> games = gameDAO.getGames();
    int uniqueID = games.get(games.size() < 1 ? 0 :games.size() - 1).getGameNumber() + 1;
    Game newGame = new Game(uniqueID, playerSet, takes);
    gameDAO.insertGame(newGame);
  }
}
