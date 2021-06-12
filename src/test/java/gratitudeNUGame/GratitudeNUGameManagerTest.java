package gratitudeNUGame;

import db.dao.GameDAO;
import db.models.Game;
import db.models.Player;
import gratitudeNUGame.models.PlayerGameSummary;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class GratitudeNUGameManagerTest {

    @Mock
    GameDAO gameDAO = mock(GameDAO.class);

    private GratitudeNUGameManager gameManager;

    private List<Integer> playerIDList = Arrays.asList(1,2,4,5);
    private List<Integer> gameNumberList = Arrays.asList(1,4,3,5);
    private Player player;
    private Game game;
    private Map<Integer, List<Integer>> takes = new HashMap<>();
    private List<Game> games;
    private List<Player> players;

    @Before
    public void setup() {
        gameDAO = mock(GameDAO.class);
        takes.put(1, Arrays.asList(4, 8, 1, 20, 5));
        takes.put(2, Arrays.asList(6, 5, 7, 1, 3));
        takes.put(4, Arrays.asList(10, 3, 8, 4, 7));
        takes.put(5, Arrays.asList(10, 3, 8, 4, 8));
        game = new Game(1, new HashSet<>(playerIDList), takes);
        player = new Player(1, "Woody");
        games = Arrays.asList(game);

        players = Arrays.asList(player);

        gameManager = new GratitudeNUGameManager(gameDAO);
    }

    @Test
    public void testGetPlayerIds() {
        when(gameDAO.getPlayerInfo()).thenReturn(players);
        assertThat(gameManager.getPlayerIDs()).isEqualTo(players);
    }

    @Test
    public void testGetGameNumbers() {
        when(gameDAO.getGames()).thenReturn(games);
        assertThat(gameManager.getGameNumbers()).isEqualTo(Arrays.asList(1));
    }

    @Test
    public void testGetGameSummary() {
        when(gameDAO.getGames()).thenReturn(games);
        assertThat(gameManager.getGame(1)).isEqualTo(game);
    }

    @Test
    public void testGetPlayerSummary() {
        PlayerGameSummary.GameEntry gameEntry = new PlayerGameSummary.GameEntry(1, Arrays.asList(4, 8, 1, 20, 5));
        PlayerGameSummary summary = new PlayerGameSummary("Woody", 38, 8, 1, Arrays.asList(gameEntry));
        when(gameDAO.getGames()).thenReturn(games);
        assertThat(gameManager.getPlayerGameSummary(player)).isEqualTo(summary);
    }

    @Test
    public void testAddPlayer() {
        PlayerGameSummary.GameEntry gameEntry = new PlayerGameSummary.GameEntry(1, Arrays.asList(4, 8, 1, 20, 5));
        PlayerGameSummary summary = new PlayerGameSummary("Woody", 38, 8, 1, Arrays.asList(gameEntry));
        when(gameDAO.getGames()).thenReturn(games);
        assertThat(gameManager.getPlayerGameSummary(player)).isEqualTo(summary);
    }
}
