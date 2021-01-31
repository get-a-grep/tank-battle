package api;

import api.model.*;
import business.game.GameSession;
import com.google.gson.Gson;
import data.dao.GameDao;
import data.dao.MapDao;
import data.dao.TankDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource class for tank-battle v1
 *
 * @author mads
 */
@Path("/v1")
@Api(value = "Battle API V1")
public class BattleResourceV1 {

    @GET
    @Path("/tanks/{tank_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Returns tank-json from DB for a given tankId.", response = Tank.class)
    public Response getTank(@PathParam("tank_id") String tankId) {
        Gson gson = new Gson();
        TankDao tankDao = new TankDao();
        Tank tank = tankDao.getTankForId(tankId);
        return Response.ok().entity(gson.toJson(tank)).build();
    }

    @GET
    @Path("/map/{map_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Returns map-json from DB for a given mapId.", response = Map.class)
    public Response getMap(@PathParam("map_id") String mapId) {
        Gson gson = new Gson();
        MapDao mapDao = new MapDao();
        Map map = mapDao.getMapForId(mapId);
        return Response.ok().entity(gson.toJson(map)).build();
    }

    @POST
    @Path("/simulate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Simulates a battle and returns a game_id for use in score IF.",
            response = String.class)
    public Response simulateBattle(
            @ApiParam(value = "The parameters of the battle", required = true) BattleParams params) {
        Gson gson = new Gson();
        GameSession gameSession = new GameSession();
        String gameId = gameSession.playSession(params);
        return Response.ok().entity(gson.toJson(gameId)).build();
    }

    @GET
    @Path("/score/{game_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Returns a score from the DB for a given gameId.",
            response = Score.class)
    public Response getScore(@PathParam("game_id") String gameId) {
        Gson gson = new Gson();
        GameDao gameDao = new GameDao();
        Game game = gameDao.getGameForId(gameId);
        return Response.ok().entity(gson.toJson(game.getScore())).build();
    }

}
