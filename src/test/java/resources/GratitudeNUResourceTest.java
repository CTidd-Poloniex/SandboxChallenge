package resources;

import db.models.Game;
import db.models.Player;
import gratitudeNUGame.GratitudeNUGameManager;
import gratitudeNUGame.models.PlayerGameSummary;
import gratitudeNUGame.models.PlayerGameSummary.GameEntry;
import io.dropwizard.testing.junit.ResourceTestRule;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import resources.requests.AddPlayerRequest;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

public class GratitudeNUResourceTest {

  public static GratitudeNUGameManager gameManager = mock(GratitudeNUGameManager.class);
  private Integer[] playersIDs= new Integer[]{3,2,3,5};
  private Integer[] gameNumbers= new Integer[]{1,4,3,5};
  Player player;
  Game game;

  @ClassRule
  public static ResourceTestRule rule = ResourceTestRule
      .builder()
      .addResource(new GratitudeNUResource(gameManager))
      .build();

  @After
  public void teardown() {
    reset(gameManager);
  }

  @Before
  public void setup() {
    Set<Integer> players = new HashSet<>();
    players.add(1);
    players.add(2);
    players.add(3);
    Map<Integer, List<Integer>> takes = new HashMap<>();
    takes.put(1, Arrays.asList(4, 8, 1, 20, 5));
    takes.put(2, Arrays.asList(6, 5, 7, 1, 3));
    takes.put(3, Arrays.asList(10, 3, 8, 4, 7));
    game = new Game(1, players, takes);
    player = new Player(1, "Woody");
  }

  @Test
  public void testGetPlayerIDs() {
    when(gameManager.getPlayerIDs()).thenReturn(Arrays.asList(playersIDs));

    Response r = rule.target("/gratitudeNU/playerIds")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    assertThat(r.getStatus()).isEqualTo(200);
    Integer[] value = r.readEntity(Integer[].class);
    assertThat(value).isEqualTo(playersIDs);
  }

  @Test
  public void testGetGameNumbers() {
    when(gameManager.getGameNumbers()).thenReturn(Arrays.asList(gameNumbers));

    Response r = rule.target("/gratitudeNU/gameNumbers")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    assertThat(r.getStatus()).isEqualTo(200);
    Integer[] value = r.readEntity(Integer[].class);
    assertThat(value).isEqualTo(gameNumbers);
  }

  @Test
  public void testGetGameSummary() {
    when(gameManager.getGame(1)).thenReturn(game);

    Response r = rule.target("/gratitudeNU/game")
        .queryParam("gameId", 1)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    assertThat(r.getStatus()).isEqualTo(200);
    Game value = r.readEntity(Game.class);
    assertThat(value).isEqualTo(game);
  }

  @Test
  public void testGetGameSummaryUnknownGame() {
    when(gameManager.getGame(1)).thenReturn(null);

    Response r = rule.target("/gratitudeNU/game")
        .queryParam("gameId", 1)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    assertThat(r.getStatus()).isEqualTo(404);
  }

  @Test
  public void testGetPlayerSummary() {
    GameEntry gameEntry = new GameEntry(1, Arrays.asList(4, 8, 1, 20, 5));
    PlayerGameSummary summary = new PlayerGameSummary("Woody", 38, 6, 1, Arrays.asList(gameEntry));
    when(gameManager.getPlayer(1)).thenReturn(player);
    when(gameManager.getPlayerGameSummary(player)).thenReturn(summary);

    Response r = rule.target("/gratitudeNU/player")
        .queryParam("playerId", 1)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    assertThat(r.getStatus()).isEqualTo(200);
    PlayerGameSummary value = r.readEntity(PlayerGameSummary.class);
    assertThat(value).isEqualTo(summary);
  }

  @Test
  public void testGetPlayerSummaryUnknownPlayer() {
    GameEntry gameEntry = new GameEntry(1, Arrays.asList(4, 8, 1, 20, 5));
    PlayerGameSummary summary = new PlayerGameSummary("Woody", 38, 6, 1, Arrays.asList(gameEntry));
    when(gameManager.getPlayer(1)).thenReturn(null);

    Response r = rule.target("/gratitudeNU/player")
        .queryParam("playerId", 1)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    assertThat(r.getStatus()).isEqualTo(404);
  }

  @Test
  public void testAddPlayer() {
    doNothing().when(gameManager).addPlayer(player.getName());

    AddPlayerRequest req = new AddPlayerRequest();
    req.setName(player.getName());

    Response r = rule.target("/gratitudeNU/player")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.json(req));

    assertThat(r.getStatus()).isEqualTo(201);
  }

  @Test
  public void testAddPlayerEmptyString() {
    doThrow(new IllegalArgumentException()).when(gameManager).addPlayer("");

    AddPlayerRequest req = new AddPlayerRequest();
    req.setName("");

    Response r = rule.target("/gratitudeNU/player")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.json(req));

    assertThat(r.getStatus()).isEqualTo(400);
  }

  @Test
  public void testAddPlayerExistingName() {
    doThrow(new IllegalArgumentException()).when(gameManager).addPlayer("Existing Name");

    AddPlayerRequest req = new AddPlayerRequest();
    req.setName("Existing Name");

    Response r = rule.target("/gratitudeNU/player")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.json(req));

    assertThat(r.getStatus()).isEqualTo(400);
  }
}
