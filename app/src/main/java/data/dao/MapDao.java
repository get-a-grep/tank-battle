package data.dao;

import api.model.Map;
import com.mongodb.*;
import data.ConnectionHelper;
import data.converter.DBObjConverter;
import org.apache.log4j.Logger;

/**
 * Data access object that stores and retrieves Maps in/from the 'map' collection.
 *
 * @author mads
 */
public class MapDao {
    private static final Logger LOGGER = Logger.getLogger(MapDao.class);

    /**
     * Retrieves a map for a given id
     *
     * @param id
     *  the id to be searched for
     * @return
     *  the Map model object
     */
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
        DBObject mapObj = mapCollection.findOne(query);

        if(mapObj != null) {
            Map map = DBObjConverter.mapFromDbObj(mapObj);
            LOGGER.info("Returning following map for id: " + mapObj.toString());
            return map;
        } else {
            return null;
        }
    }
}
