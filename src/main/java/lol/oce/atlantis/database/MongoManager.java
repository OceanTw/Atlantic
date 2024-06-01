package lol.oce.atlantis.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.Document;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MongoManager {
    @Getter
    private static final MongoManager instance = new MongoManager();

    private MongoCollection<Document> collection;

    public void load(String uri, String database) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase db = mongoClient.getDatabase(database);
        collection = db.getCollection("Atlantis");
    }
}
