package gratitudeNUGame.models;

import db.models.Game;
import db.models.Player;
import java.util.List;

public class PlayerGameSummary {
  String name;
  int totalScore;
  int averageTake;
  int gamesWon;
  List<GameEntry> allPlayerTakes;

  public PlayerGameSummary() {
  }

  public PlayerGameSummary(String name, int totalScore, int averageTake, int gamesWon,
      List<GameEntry> allPlayerTakes) {
    this.name = name;
    this.totalScore = totalScore;
    this.averageTake = averageTake;
    this.gamesWon = gamesWon;
    this.allPlayerTakes = allPlayerTakes;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getTotalScore() {
    return totalScore;
  }

  public void setTotalScore(int totalScore) {
    this.totalScore = totalScore;
  }

  public int getAverageTake() {
    return averageTake;
  }

  public void setAverageTake(int averageTake) {
    this.averageTake = averageTake;
  }

  public int getGamesWon() {
    return gamesWon;
  }

  public void setGamesWon(int gamesWon) {
    this.gamesWon = gamesWon;
  }

  public List<GameEntry> getAllPlayerTakes() {
    return allPlayerTakes;
  }

  public void setAllPlayerTakes(
      List<GameEntry> allPlayerTakes) {
    this.allPlayerTakes = allPlayerTakes;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if(!(o instanceof PlayerGameSummary))
      return false;

    PlayerGameSummary other = (PlayerGameSummary) o;
    return this.name.equals(other.name)
        && this.totalScore == other.totalScore
        && this.averageTake == other.averageTake
        && this.gamesWon == other.gamesWon
        && this.allPlayerTakes.equals(other.allPlayerTakes);

  }

  @Override
  public final int hashCode() {
    return this.name.hashCode() + this.totalScore + this.averageTake + this.gamesWon + this.allPlayerTakes.hashCode();
  }

  public static class GameEntry {
    int gameNumber;
    List<Integer> takes;

    public GameEntry() {
    }

    public GameEntry(int gameNumber, List<Integer> takes) {
      this.gameNumber = gameNumber;
      this.takes = takes;
    }

    public int getGameNumber() {
      return gameNumber;
    }

    public void setGameNumber(int gameNumber) {
      this.gameNumber = gameNumber;
    }

    public List<Integer> getTakes() {
      return takes;
    }

    public void setTakes(List<Integer> takes) {
      this.takes = takes;
    }

    @Override
    public boolean equals(Object o) {
      if (o == this)
        return true;
      if(!(o instanceof GameEntry))
        return false;

      GameEntry other = (GameEntry) o;
      return this.gameNumber == other.gameNumber && this.takes.equals(other.takes);

    }

    @Override
    public final int hashCode() {
      return this.gameNumber + this.takes.hashCode();
    }
  }
}
