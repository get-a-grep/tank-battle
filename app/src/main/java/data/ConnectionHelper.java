package data;

import com.mongodb.MongoClientURI;
import org.apache.log4j.Logger;

/**
 * A helper class to establish a connection to the MongoDB container.
 * 
 * @author mads
 */
public class ConnectionHelper {
    private static Logger LOGGER = Logger.getLogger(ConnectionHelper.class);

    String mUser;
    String mPwd;
    String mPort;
    String mHost;

    public ConnectionHelper() {
        loadProperties();
    }

    public MongoClientURI getMongoUri() {
        MongoClientURI clientURI = new MongoClientURI("mongodb://" + mUser + ":" +
                mPwd + "@" + mHost + ":" + mPort);

        return clientURI;
    }

    private void loadProperties() {
        mUser = System.getenv("MONGO_USER");
        mPwd = System.getenv("MONGO_PWD");
        mHost = System.getenv("MONGO_HOST");
        mPort = System.getenv("MONGO_PORT");

        if(mUser == null) {
            LOGGER.warn("Property MONGO_USER could not be loaded.");
        } else if (mPwd == null) {
            LOGGER.warn("Property MONGO_PWD could not be loaded.");
        } else if (mHost == null) {
            LOGGER.warn("Property MONGO_HOST could not be loaded.");
        } else if (mPort == null) {
            LOGGER.warn("Property MONGO_PORT could not be loaded.");
        }
    }
}
