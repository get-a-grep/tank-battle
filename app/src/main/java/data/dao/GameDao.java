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

public class GameDao {
    private static final Logger LOGGER = Logger.getLogger(GameDao.class);

    public String storeGameSession(BattleParams battleParams, Score score) {
        //Retrieve configured connection
        ConnectionHelper connHelper = new ConnectionHelper();

        //Connect to DB
        MongoClient mongoClient = new MongoClient(connHelper.getMongoUri());
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

    public Game getGameForId(String gameId) {
        //Retrieve configured connection
        ConnectionHelper connHelper = new ConnectionHelper();

        //Connect to DB
        MongoClient mongoClient = new MongoClient(connHelper.getMongoUri());
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
