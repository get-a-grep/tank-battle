package data.dao;

import api.model.BattleParams;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import data.ConnectionHelper;

public class GameDao {

    public void storeGameSession(BattleParams battleParams, int score) {
        //Retrieve configured connection
        ConnectionHelper connHelper = new ConnectionHelper();

        //Connect to DB
        MongoClient mongoClient = new MongoClient(connHelper.getMongoUri());
        DB mongoDB = mongoClient.getDB("tb-test");

        //Retrieve collection
        DBCollection mapCollection = mongoDB.getCollection("games");


    }
}
