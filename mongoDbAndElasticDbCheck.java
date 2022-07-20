import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import io.restassured.response.Response;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.mongodb.client.model.Filters.eq;
import static io.restassured.RestAssured.given;

public class MongoAndElasticDbVerification extends PostTests {

    @BeforeClass
    public void init() {
        basePath = "";
        body = "";
    }

    @Test
    public void testMongoDbVerification() {
        String userName = "";
        String pwd = "";
        String host = "";
        String port = "";
        String mongoUri = "mongodb://" + userName + ":" + pwd + "@" + host + ":" + port + "/?authSource=" + userName;
        MongoClientURI uri = new MongoClientURI(mongoUri);

        //Creating a MongoDB client
        MongoClient mongo = new MongoClient(uri);
        //Connecting to the database
        MongoDatabase database = mongo.getDatabase("databaseName");
        //Creating a collection object
        MongoCollection<Document> collection = database.getCollection("collectionName");

        //filter
        Bson equalComparison = eq("code", "0118U");

        //Retrieving the documents
        FindIterable<Document> iterDoc = collection.find(equalComparison);

        MongoCursor<Document> str = iterDoc.iterator();

        if (str.hasNext()) {
            for (Document document : iterDoc) {
                softly.then(document.get("code")).isEqualTo("0118U");
            }
        } else
            softly.fail("No matching code found");

        mongo.close();
        System.out.println("connection closed");
        softly.assertAll();
    }

    @Test
    public void elasticSearch() {

        Response re = given().auth().basic("UN", "PWD").
                when().get("hostURI").
                then().extract().response();
        System.out.println(re.toString());

    }

}
