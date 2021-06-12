package resources;

import db.models.Game;
import db.models.Player;
import gratitudeNUGame.GratitudeNUGameManager;
import gratitudeNUGame.models.GameResultSummary;
import gratitudeNUGame.models.PlayerGameSummary;
import gratitudeNUGame.models.PlayerGameSummary.GameEntry;
import io.dropwizard.testing.junit.ResourceTestRule;

import java.util.*;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import resources.requests.AddGameRequest;
import resources.requests.AddPlayerRequest;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

public class GratitudeNUResourceTest {

  public static GratitudeNUGameManager gameManager = mock(GratitudeNUGameManager.class);
  private Integer[] playersIDArray = new Integer[]{3,2,4,5};
  private List<Integer> playerIDList = Arrays.asList(playersIDArray);
  private Integer[] gameNumberArray = new Integer[]{1,4,3,5};
  private List<Integer> gameNumberList = Arrays.asList(gameNumberArray);
  private Player player;
  private Game game;
  private Map<Integer, List<Integer>> takes = new HashMap<>();

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
    takes.put(3, Arrays.asList(4, 8, 1, 20, 5));
    takes.put(2, Arrays.asList(6, 5, 7, 1, 3));
    takes.put(4, Arrays.asList(10, 3, 8, 4, 7));
    takes.put(5, Arrays.asList(10, 3, 8, 4, 8));
    game = new Game(1, new HashSet<>(playerIDList), takes);
    player = new Player(1, "Woody");
  }

  /**
   * GET /playerIds
   */
  @Test
  public void testGetPlayerIDs() {
    when(gameManager.getPlayerIDs()).thenReturn(Arrays.asList(playersIDArray));

    Response r = rule.target("/playerIds")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    assertThat(r.getStatus()).isEqualTo(200);
    Integer[] value = r.readEntity(Integer[].class);
    assertThat(value).isEqualTo(playersIDArray);
  }

  /**
   * GET /gameNumbers
   */
  @Test
  public void testGetGameNumbers() {
    when(gameManager.getGameNumbers()).thenReturn(Arrays.asList(gameNumberArray));

    Response r = rule.target("/gameNumbers")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    assertThat(r.getStatus()).isEqualTo(200);
    Integer[] value = r.readEntity(Integer[].class);
    assertThat(value).isEqualTo(gameNumberArray);
  }

  /**
   *  GET /game
   */
  @Test
  public void testGetGameSummary() {
    HashMap<Integer, List<Integer>> testTakes = new HashMap<>();
    testTakes.put(1, Arrays.asList(3, 5, 1, 2));
    Game testGame = new Game(1, new HashSet<>(Arrays.asList(1)), testTakes);
    GameResultSummary testGameResultSummary = new GameResultSummary(Arrays.asList(player), testGame);
    when(gameManager.getGameResultSummary(1)).thenReturn(testGameResultSummary);

    Response r = rule.target("/game")
        .queryParam("gameId", 1)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    assertThat(r.getStatus()).isEqualTo(200);
    GameResultSummary value = r.readEntity(GameResultSummary.class);
    assertThat(value).isEqualTo(testGameResultSummary);
  }

  @Test
  public void testGetGameSummaryUnknownGame() {
    when(gameManager.getGameResultSummary(1)).thenThrow(new NotFoundException());

    Response r = rule.target("/game")
        .queryParam("gameId", 1)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    assertThat(r.getStatus()).isEqualTo(404);
  }

  /**
   * GET /player
   */
  @Test
  public void testGetPlayerSummary() {
    GameEntry gameEntry = new GameEntry(1, Arrays.asList(4, 8, 1, 20, 5));
    PlayerGameSummary summary = new PlayerGameSummary("Woody", 38, 6, 1, Arrays.asList(gameEntry));
    when(gameManager.getPlayer(1)).thenReturn(player);
    when(gameManager.getPlayerGameSummary(player)).thenReturn(summary);

    Response r = rule.target("/player")
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
    when(gameManager.getPlayer(1)).thenThrow(new NotFoundException());

    Response r = rule.target("/player")
        .queryParam("playerId", 1)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    assertThat(r.getStatus()).isEqualTo(404);
  }

  /**
   * POST /player
   */
  @Test
  public void testAddPlayer() {
    doNothing().when(gameManager).addPlayer(player.getName());

    AddPlayerRequest req = new AddPlayerRequest();
    req.setName(player.getName());

    Response r = rule.target("/player")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.json(req));

    assertThat(r.getStatus()).isEqualTo(201);
  }

  @Test
  public void testAddPlayerEmptyString() {
    doThrow(new IllegalArgumentException()).when(gameManager).addPlayer("");

    AddPlayerRequest req = new AddPlayerRequest();
    req.setName("");

    Response r = rule.target("/player")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.json(req));

    assertThat(r.getStatus()).isEqualTo(400);
  }

  @Test
  public void testAddPlayerExistingName() {
    doThrow(new IllegalArgumentException()).when(gameManager).addPlayer("Existing Name");

    AddPlayerRequest req = new AddPlayerRequest();
    req.setName("Existing Name");

    Response r = rule.target("/player")
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.json(req));

    assertThat(r.getStatus()).isEqualTo(400);
  }

  /**
   * POST /game
   */
  @Test
  public void testAddGame() {
    doNothing().when(gameManager).addGame(playerIDList, takes);

    AddGameRequest req = new AddGameRequest();
    req.setPlayers(playerIDList);
    req.setTakes(takes);

    Response r = rule.target("/game")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.json(req));

    assertThat(r.getStatus()).isEqualTo(201);
  }

  @Test
  public void testAddGameUnequalRoundsPlayer() {
    doThrow(new IllegalArgumentException("Inconsistent amount of rounds played."))
            .when(gameManager).addGame(playerIDList, takes);

    AddGameRequest req = new AddGameRequest();
    req.setPlayers(playerIDList);
    req.setTakes(takes);

    Response r = rule.target("/game")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.json(req));

    String s = r.readEntity(String.class);
    assertThat(r.getStatus()).isEqualTo(400);
    assertThat(s).isEqualTo("Incorrectly structured game. Inconsistent amount of rounds played.");
  }

  @Test
  public void testAddGamePlayerDoesntExist() {
    doThrow(new IllegalArgumentException("PlayerID 3 is unrecognized."))
            .when(gameManager).addGame(playerIDList, takes);

    AddGameRequest req = new AddGameRequest();
    req.setPlayers(playerIDList);
    req.setTakes(takes);

    Response r = rule.target("/game")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.json(req));

    String s = r.readEntity(String.class);
    assertThat(r.getStatus()).isEqualTo(400);
    assertThat(s).isEqualTo("Incorrectly structured game. PlayerID 3 is unrecognized.");
  }

  @Test
  public void testAddGamePlayerSetInequality() {
    doThrow(new IllegalArgumentException("Player set is not set equal to players in takes."))
            .when(gameManager).addGame(playerIDList, takes);

    AddGameRequest req = new AddGameRequest();
    req.setPlayers(playerIDList);
    req.setTakes(takes);

    Response r = rule.target("/game")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.json(req));

    String s = r.readEntity(String.class);
    assertThat(r.getStatus()).isEqualTo(400);
    assertThat(s).isEqualTo("Incorrectly structured game. Player set is not set equal to players in takes.");
  }

  @Test
  public void testAddGamPlayersArentUnique() {
    doThrow(new IllegalArgumentException("Duplicate players in game."))
            .when(gameManager).addGame(playerIDList, takes);

    AddGameRequest req = new AddGameRequest();
    req.setPlayers(playerIDList);
    req.setTakes(takes);

    Response r = rule.target("/game")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.json(req));

    String s = r.readEntity(String.class);
    assertThat(r.getStatus()).isEqualTo(400);
    assertThat(s).isEqualTo("Incorrectly structured game. Duplicate players in game.");
  }
}
