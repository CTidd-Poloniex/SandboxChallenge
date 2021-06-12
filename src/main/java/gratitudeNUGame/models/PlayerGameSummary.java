package gratitudeNUGame.models;

import java.util.List;

public class PlayerGameSummary {
  String playerName;
  int totalScore;
  int averageScore;
  int totalWins;
  List<GameEntry> allPlayerTakes;

  public PlayerGameSummary() {
  }

  public PlayerGameSummary(String playerName, int totalScore, int averageScore, int totalWins,
                           List<GameEntry> allPlayerTakes) {
    this.playerName = playerName;
    this.totalScore = totalScore;
    this.averageScore = averageScore;
    this.totalWins = totalWins;
    this.allPlayerTakes = allPlayerTakes;
  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public int getTotalScore() {
    return totalScore;
  }

  public void setTotalScore(int totalScore) {
    this.totalScore = totalScore;
  }

  public int getAverageScore() {
    return averageScore;
  }

  public void setAverageScore(int averageScore) {
    this.averageScore = averageScore;
  }

  public int getTotalWins() {
    return totalWins;
  }

  public void setTotalWins(int totalWins) {
    this.totalWins = totalWins;
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
    return this.playerName.equals(other.playerName)
        && this.totalScore == other.totalScore
        && this.averageScore == other.averageScore
        && this.totalWins == other.totalWins
        && this.allPlayerTakes.equals(other.allPlayerTakes);

  }

  @Override
  public final int hashCode() {
    return this.playerName.hashCode() + this.totalScore + this.averageScore + this.totalWins + this.allPlayerTakes.hashCode();
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
