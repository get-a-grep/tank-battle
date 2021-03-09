package data.dao;

import api.model.BattleParams;
import api.model.Game;
import api.model.Map;
import api.model.Score;
import com.mongodb.*;
import data.ConnectionHelper;
import data.converter.DBObjConverter;
import org.apache.log4j.Logger;

import java.util.UUID;

/**
 * Data access object that stores and retrieves Games in/from the 'games' collection.
 *
 * @author mads
 */
public class GameDao {
    private static final Logger LOGGER = Logger.getLogger(GameDao.class);

    /**
     * Stores BattleParams and a Score from a finished game as a 'game' db-object.
     *
     * @param battleParams
     *  The parameters of the battle that was fought
     * @param score
     *  The score of the battle that was fought
     * @return
     *  The unique ID of the game that was stored in the DB;
     */
    public String storeGameSession(BattleParams battleParams, Score score) {
        //Retrieve configured connection
        ConnectionHelper connHelper = new ConnectionHelper();

        //Connect to DB
        try(MongoClient mongoClient = new MongoClient(connHelper.getMongoUri())) {
            DB mongoDB = mongoClient.getDB("tb-test");

            //Retrieve collection
            DBCollection gameCollection = mongoDB.getCollection("games");

            //Generate unique ID for game
            String id = UUID.randomUUID().toString();

            BasicDBObject game = new BasicDBObject();
            game.append("id", id);
            game.append("tankIds", battleParams.getTankIds());
            game.append("mapId", battleParams.getMapId());
            game.append("scoreT1", score.getTank1Points());
            game.append("scoreT2", score.getTank2Points());
            game.append("winner", score.getWinner());

            gameCollection.insert(game);

            return id;
        }
    }

    /**
     * Retrieves a finished game for the given ID.
     *
     * @param gameId
     *  The gameId to be searched for
     * @return
     *  A Game model object.
     */
    public Game getGameForId(String gameId) {
        //Retrieve configured connection
        ConnectionHelper connHelper = new ConnectionHelper();

        //Connect to DB
        try(MongoClient mongoClient = new MongoClient(connHelper.getMongoUri())){
            DB mongoDB = mongoClient.getDB("tb-test");

            //Retrieve collection
            DBCollection gameCollection = mongoDB.getCollection("games");

            BasicDBObject query = new BasicDBObject();
            query.put("id", gameId);
            DBObject gameObj = gameCollection.findOne();

            if(gameObj != null) {
                Game game = DBObjConverter.gameFromDbObj(gameObj);
                LOGGER.info("Returning following game for id: " + gameObj.toString());
                return game;
            } else {
                return null;
            }
        }
    }
}
