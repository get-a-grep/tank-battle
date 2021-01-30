package data.dao;

import api.model.Map;
import com.mongodb.*;
import data.ConnectionHelper;
import data.converter.DBObjConverter;
import org.apache.log4j.Logger;

public class MapDao {
    private static final Logger LOGGER = Logger.getLogger(MapDao.class);

    public Map getMapForId(final String id) {
        //Retrieve configured connection
        ConnectionHelper connHelper = new ConnectionHelper();

        //Connect to DB
        MongoClient mongoClient = new MongoClient(connHelper.getMongoUri());
        DB mongoDB = mongoClient.getDB("tb-test");

        //Retrieve collection
        DBCollection mapCollection = mongoDB.getCollection("maps");

        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        DBObject mapObj = mapCollection.findOne();
        LOGGER.info("Returning following map for id: " + mapObj.toString());

        if(mapObj != null) {
            Map map = DBObjConverter.mapFromDbObj(mapObj);
            return map;
        } else {
            return null;
        }
    }
}
