package data.dao;

import api.model.Map;
import api.model.Obstacle;
import business.map.MapInstance;
import com.mongodb.*;
import data.ConnectionHelper;
import data.converter.DBObjConverter;
import org.apache.log4j.Logger;

import java.util.UUID;

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
        try(MongoClient mongoClient = new MongoClient(connHelper.getMongoUri())){
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

    public String storeNewMap(MapInstance mapInstance) {
        //Retrieve configured connection
        ConnectionHelper connHelper = new ConnectionHelper();

        //Connect to DB
        try(MongoClient mongoClient = new MongoClient(connHelper.getMongoUri())){
            DB mongoDB = mongoClient.getDB("tb-test");
            //Retrieve collection
            DBCollection mapCollection = mongoDB.getCollection("maps");

            //Generate unique ID for map
            String id = UUID.randomUUID().toString();

            BasicDBObject outerMap = new BasicDBObject();
            BasicDBList obsList = new BasicDBList();
            for (Obstacle obs : mapInstance.getObstacles()) {
                BasicDBList obsPosition = new BasicDBList();
                obsPosition.add(new BasicDBObject("x", obs.getPosition().getX()));
                obsPosition.add(new BasicDBObject("y", obs.getPosition().getY()));

                obsList.add(new BasicDBObject("position", obsPosition));
                obsList.add(new BasicDBObject("blocksMove", obs.isBlocksMove()));
            }
            outerMap.append("id", id);
            outerMap.append("obstacles", obsList);
            outerMap.append("fieldX", mapInstance.getFieldX());
            outerMap.append("fieldY", mapInstance.getFieldY());

            mapCollection.insert(outerMap);

            return id;
        }
    }
}
