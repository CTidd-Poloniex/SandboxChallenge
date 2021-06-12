package resources;

import db.models.Game;
import gratitudeNUGame.GratitudeNUGameManager;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import resources.requests.AddGameRequest;
import resources.requests.AddPlayerRequest;

@Path("")
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
      @ApiResponse(code = 404, message = "Game not found.")
  })
  public Response getGame(@NotNull @QueryParam("gameId") int gameID) {
    try {
      return Response.ok(gameManager.getGameResultSummary(gameID)).build();
    } catch (NotFoundException e) {
      return Response.status(Status.NOT_FOUND).entity("Game not found.").build();
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
      @ApiResponse(code = 404, message = "Player not found.")
  })
  public Response getPlayerSummary(@NotNull @QueryParam("playerId") int playerID) {
    try {
      return Response.ok(gameManager.getPlayer(playerID)).build();
    } catch (NotFoundException e) {
      return Response.status(Status.NOT_FOUND).entity("playerId").build();
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
      @ApiResponse(code = 400, message = "The player cannot be found.")
  })
  public Response addPlayer(@Valid @NotNull AddPlayerRequest playerRequest) {
    try {
      gameManager.addPlayer(playerRequest.getName());
      return Response.status(201).build();
    } catch (IllegalArgumentException e) {
      return Response.status(Status.BAD_REQUEST).entity("Invalid player name.").build();
    }
  }

  @POST
  @Path("game")
  @ApiOperation(
          value = "Adds a given game.",
          response = Response.class
  )
  @ApiResponses(value = {
          @ApiResponse(code = 201, message = "Successfully added this game!"),
          @ApiResponse(code = 400, message = "Incorrectly structured game.")
  })
  public Response addGame(@Valid @NotNull AddGameRequest addGameRequest) {
    try {
      gameManager.addGame(addGameRequest.getPlayers(), addGameRequest.getTakes());
      return Response.status(201).build();
    } catch (IllegalArgumentException e) {
      return Response.status(Status.BAD_REQUEST).entity(String.format("Incorrectly structured game. %s", e.getMessage())).build();
    }
  }
}
