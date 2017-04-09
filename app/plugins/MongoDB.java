package plugins;

import com.mongodb.*;
import play.Application;
import play.Logger;
import play.Play;
import play.Plugin;

import java.nio.charset.Charset;
import java.util.List;

public class MongoDB extends Plugin {
    public final String Tag = "MongoDBPlugin";

    private final Application application;

    public MongoClient c;
    public String host;
    public int port;
    private String database;
    private String defaultDatabase;
    public String trashPrefix;
    String username;
    String password;

    public MongoDB(Application application) {
        this.application = application;
    }

    public static MongoClient getClient() {
        return Play.application().plugin(MongoDB.class).c;
    }

    public static DB getDB() {
        return getClient().getDB(getDBName());
    }

    private static String getDBName() {
        return Play.application().plugin(MongoDB.class).getDatabase();
    }

    public void onStart() {
        host = application.configuration().getString("mongodb.default.host");
        port = application.configuration().getInt("mongodb.default.port");
        database = defaultDatabase = application.configuration().getString("mongodb.default.db");
        username = application.configuration().getString("mongodb.username");
        password = application.configuration().getString("mongodb.password");
        trashPrefix = application.configuration().getString("mongodb.trashPrefix");

        try {
            MongoClientOptions.Builder builder = MongoClientOptions.builder()
                    .autoConnectRetry(true).connectTimeout(15000)
                    .socketKeepAlive(true).socketTimeout(60000);

            MongoClientOptions mco = builder.build();
            c = new MongoClient(host, port);
            MongoClient client = new MongoClient(host, mco);

            bootstrapDatabase();


        } catch (Exception e) {
            Logger.error(Tag + ": " + e.getMessage());
        }
    }

    public static void use(String database) {
        char[] pass ={ 'a', 'b', 'c', '1', '2','3' };
        try {
            MongoClient client = new MongoClient("localhost", 27017);
            client.getDB(database).authenticate("adminUser", pass);
        } catch (Exception e){

        }
        Play.application().plugin(MongoDB.class).setDatabase(database);
    }

    public static void useDefaultDatabase() {
        Play.application().plugin(MongoDB.class).setDatabase(
                Play.application().plugin(MongoDB.class).getDefaultDatabase()
        );
    }

    public static void setup(String database) {
        char[] pass ={ 'a', 'b', 'c', '1', '2','3' };
        try {
            MongoClient client = new MongoClient("localhost", 27017);
            client.getDB(database).authenticate("adminUser", pass);
        } catch (Exception e){

        }
        use(database);

        Play.application().plugin(MongoDB.class).bootstrapDatabase();
    }

    public static DBCollection getCollection(String collectionName) {
        return getDB().getCollection(collectionName);
    }

    public static DBCollection getTrash(String collectionName) {
        String trashPrefix = Play.application().plugin(MongoDB.class).trashPrefix;

        return getDB().getCollection(trashPrefix + collectionName);
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDefaultDatabase() {
        return defaultDatabase;
    }

    public void setDefaultDatabase(String defaultDatabase) {
        this.defaultDatabase = defaultDatabase;
    }

    public void bootstrapDatabase() {
        List<String> collections = application.configuration().getStringList("mongodb.collections");

        for (String collection : collections) {
            buildIndexes(collection);
            buildUniqueIndexes(collection);
            buildUniqueCompoundIndexes(collection);
            buildTextIndexes(collection);
        }
    }

    private void buildIndexes(String collection) {
        try {
            DBCollection coll = c.getDB(database).getCollection(collection);

            List<String> indexes = application.configuration().getStringList("mongodb.indexes." + collection);

            if (indexes != null) {
                for (String index : indexes) {
                    coll.createIndex(new BasicDBObject(index, 1));
                }
            }
        } catch (MongoException e) {
            Logger.error(Tag + ": " + e.getMessage());
        }
    }

    private void buildUniqueIndexes(String collection) {
        try {
            DBCollection coll = c.getDB(database).getCollection(collection);

            List<String> indexes = application.configuration().getStringList("mongodb.uniqueIndexes." + collection);

            if (indexes != null) {
                for (String index : indexes) {
                    coll.createIndex(new BasicDBObject(index, 1), new BasicDBObject("unique", true));
                }
            }
        } catch (MongoException e) {
            Logger.error(Tag + ": " + e.getMessage());
        }
    }

    private void buildUniqueCompoundIndexes(String collection) {
        try {
            DBCollection coll = c.getDB(database).getCollection(collection);

            List<String> indexes = application.configuration().getStringList("mongodb.uniqueCompoundIndexes." + collection);

            if (indexes != null) {
                BasicDBObject object = new BasicDBObject();

                indexes.forEach(index -> object.append(index, 1));

                coll.createIndex(object, new BasicDBObject("unique", true));
            }

        } catch (MongoException e) {
            Logger.error(Tag + ": " + e.getMessage());
        }
    }


    private void buildTextIndexes(String collection) {
        try {
            DBCollection coll = c.getDB(database).getCollection(collection);

            List<String> indexes = application.configuration().getStringList("mongodb.textIndexes." + collection);

            if (indexes != null) {
                indexes.forEach(index -> coll.createIndex(new BasicDBObject(index, "text")));
            }

        } catch (MongoException e) {
            Logger.error(Tag + ": " + e.getMessage());
        }
    }
}
