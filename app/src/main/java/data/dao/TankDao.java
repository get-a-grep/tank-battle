package data.dao;

import api.model.Tank;
import com.mongodb.*;
import data.ConnectionHelper;
import data.converter.DBObjConverter;
import org.apache.log4j.Logger;

/**
 * Data access object that stores and retrieves Tanks in/from the 'tank' collection.
 *
 * @author mads
 */
public class TankDao {
    Logger LOGGER = Logger.getLogger(TankDao.class);

    /**
     * Retrieves a Tank from the db for a given id.
     *
     * @param id
     *  The ID to be searched for.
     * @return
     *  The Tank model object.
     */
    public Tank getTankForId(final String id) {
        //Retrieve configured connection
        ConnectionHelper connHelper = new ConnectionHelper();

        //Connect to DB
        MongoClient mongoClient = new MongoClient(connHelper.getMongoUri());
        DB mongoDB = mongoClient.getDB("tb-test");

        //Retrieve collection
        DBCollection tankCollection = mongoDB.getCollection("tanks");

        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        DBObject tankObj = tankCollection.findOne(query);


        if(tankObj != null) {
            Tank tank = DBObjConverter.tankFromDbObj(tankObj);
            LOGGER.info("Returning following tank for id: " + tankObj.toString());
            return tank;
        } else {
            return null;
        }
    }
}
