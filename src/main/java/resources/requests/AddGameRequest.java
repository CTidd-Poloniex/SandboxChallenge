package resources.requests;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class AddGameRequest {

  @NotNull
  List<Integer> players;
  @NotNull
  Map<Integer, List<Integer>> takes;

  public AddGameRequest() {
  }

  public List<Integer> getPlayers() {
    return players;
  }

  public void setPlayers(List<Integer> players) {
    this.players = players;
  }

  public Map<Integer, List<Integer>> getTakes() {
    return takes;
  }

  public void setTakes(Map<Integer, List<Integer>> takes) {
    this.takes = takes;
  }
}
