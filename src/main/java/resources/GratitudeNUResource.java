package resources;

import static javax.ws.rs.client.Entity.json;

import db.models.Game;
import db.models.Player;
import gratitudeNUGame.GratitudeNUGameManager;
import gratitudeNUGame.models.PlayerGameSummary;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import resources.requests.AddPlayerRequest;

@Path("/gratitudeNU")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GratitudeNUResource {

  private GratitudeNUGameManager gameManager;

  public GratitudeNUResource(GratitudeNUGameManager gameManager) {
    this.gameManager = gameManager;
  }

  @GET
  @Path("playerIds")
  @ApiOperation(
      value = "Retrieves a list of each player's identifier.",
      response = Response.class
  )
  @ApiResponse(code = 200, message = "Player IDs successfully retrieved.")
  public Response getPlayerIDs() {
    return Response.ok(gameManager.getPlayerIDs()).build();
  }

  @GET
  @Path("gameNumbers")
  @ApiOperation(
      value = "Retrieves a list of each games number.",
      response = Response.class
  )
  @ApiResponse(code = 200, message = "Game numbers successfully retrieved.")
  public Response getGameNumbers() {
    return Response.ok(gameManager.getGameNumbers()).build();
  }

  @GET
  @Path("game")
  @ApiOperation(
      value = "Retrieves a given game.",
      response = Game.class
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Players successfully retrieved."),
      @ApiResponse(code = 404, message = "The game cannot be found.")
  })
  public Response getGame(@NotNull @QueryParam("gameId") int gameID) {
    Game game = gameManager.getGame(gameID);
    if(game != null) {
      return Response.ok(game).build();
    } else {
      return Response.status(Status.NOT_FOUND).entity("The game cannot be found.").build();
    }
  }

  @GET
  @Path("player")
  @ApiOperation(
      value = "Retrieves a given player's summary.",
      response = Response.class
  )
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Players successfully retrieved."),
      @ApiResponse(code = 404, message = "The game cannot be found.")
  })
  public Response getPlayerSummary(@NotNull @QueryParam("playerId") int playerID) {
    Player player = gameManager.getPlayer(playerID);
    if(player != null) {
      PlayerGameSummary summary = gameManager.getPlayerGameSummary(player);
      return Response.ok(summary).build();
    } else {
      return Response.status(Status.NOT_FOUND).entity("The game cannot be found.").build();
    }
  }

  @POST
  @Path("player")
  @ApiOperation(
      value = "Adds a given player.",
      response = Response.class
  )
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Successfully added this player!"),
      @ApiResponse(code = 400, message = "The game cannot be found.")
  })
  public Response addPlayer(@Valid @NotNull AddPlayerRequest playerRequest) {
    try {
      gameManager.addPlayer(playerRequest.getName());
      return Response.status(201).build();
    } catch (IllegalArgumentException e) {
      return Response.status(Status.BAD_REQUEST).entity("The game cannot be found.").build();
    }
  }
}
