package db.models;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Game {
  int gameNumber;
  Set<Integer> players;
  Map<Integer, List<Integer>> takes;

  public Game() {
  }

  public Game(Integer gameNumber, Set<Integer> players,
      Map<Integer, List<Integer>> takes) {
    this.gameNumber = gameNumber;
    this.players = players;
    this.takes = takes;
  }

  public Integer getGameNumber() {
    return gameNumber;
  }

  public void setGameNumber(Integer gameNumber) {
    this.gameNumber = gameNumber;
  }

  public Set<Integer> getPlayers() {
    return players;
  }

  public void setPlayers(Set<Integer> players) {
    this.players = players;
  }

  public Map<Integer, List<Integer>> getTakes() {
    return takes;
  }

  public void setTakes(Map<Integer, List<Integer>> takes) {
    this.takes = takes;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if(!(o instanceof Game))
      return false;

    Game other = (Game) o;
    return this.gameNumber == other.gameNumber
        && this.players.equals(other.players)
        && this.takes.equals(other.takes);
  }

  @Override
  public final int hashCode() {
    return this.gameNumber;
  }
}
