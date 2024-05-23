package lol.oce.atlantis.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Data;
import lombok.Getter;
import org.bson.Document;

@Data
public class MongoManager {

    MongoCollection<Document> collection;
    @Getter
    private static MongoManager instance = new MongoManager();

    public void load(String uri, String database) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase db = mongoClient.getDatabase(database);
        collection = db.getCollection(database);
    }


}
