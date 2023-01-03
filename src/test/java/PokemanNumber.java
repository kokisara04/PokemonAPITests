import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PokemanNumber {
    //To Confirm Pokémon number 812 is Rillaboom
    @Test
    void getnumber(){

        RestAssured.baseURI = "https://pokeapi.co/api/v2";
        RequestSpecification request = RestAssured.given();
        Response response = request.request(Method.GET,"/pokemon-species/812");
        String responsebody = response.getBody().asString();//getting the reponse from JSON format to String format
        System.out.println ("Response is" +responsebody);
        int statusCode = response.getStatusCode();
        System.out.println("StatusCode is"+ statusCode);
        Assert.assertEquals(statusCode,200);
        JsonPath js = new JsonPath(responsebody); //for parsing Json
        String name = js.getString("name");
        System.out.println(name);
        Assert.assertEquals(name, "rillaboom");

    }
    @Test
    //Check that the 5th generation of games is set in Unova, and there are two resources in the version groups array
    void getgeneration(){
        RestAssured.baseURI = "https://pokeapi.co/api/v2";
        RequestSpecification genrequest = RestAssured.given();
        Response genresponse = genrequest.request(Method.GET,"/generation/5");
        String resbody = genresponse.getBody().asString();
        System.out.println(resbody);
        JsonPath gs = new JsonPath(resbody);
        String comp = gs.getString("main_region.name");
        System.out.println(comp);
        Assert.assertEquals(comp,"unova");
        List<Object> vGroups= gs.getList("version_groups");
        Assert.assertEquals(2,vGroups.size());
        String version = gs.getString("version_groups");
        System.out.println(version);

    }
    @Test
    //Check the 1st generation is Kanto and it has 151 Pokemon Species
    void getkantodex(){
        RestAssured.baseURI = "https://pokeapi.co/api/v2";
        RequestSpecification genrequest = RestAssured.given();
        Response genresponse = genrequest.request(Method.GET,"/generation/1");
        String resbody = genresponse.getBody().asString();
        System.out.println(resbody);
        JsonPath kd = new JsonPath(resbody);
        String mregion = kd.getString("main_region.name");
        Assert.assertEquals(mregion,"kanto");
        List<String> kdex= kd.getList("pokemon_species.name");
        Assert.assertEquals(kdex.size(),151);

            for(String value:kdex) {
            System.out.println(value);

        }

    }
    @Test
    // Check the berry contains sweet in one of its flavours.
    void getstring(){
        RestAssured.baseURI = "https://pokeapi.co/api/v2";
        RequestSpecification request = RestAssured.given();
        Response response = request.request(Method.GET, "berry/1");
        ResponseBody resbody =response.getBody();
       // System.out.println(resbody.asString());
        JsonPath comparison = resbody.jsonPath();
        String flavors = comparison.getString("flavors.flavor.name");
        System.out.println(flavors);
        Assert.assertTrue(flavors.contains("sweet"), "Response body contains sweet");

    }
    @Test
// Check all the berry's flavors using soft assertion
    void getsoftassert(){
        RestAssured.baseURI = "https://pokeapi.co/api/v2";
        RequestSpecification request = RestAssured.given();
        Response response = request.request(Method.GET, "berry/1");
        ResponseBody resbody =response.getBody();
        // System.out.println(resbody.asString());
        JsonPath comparison = resbody.jsonPath();
        List<String> flavors = comparison.getList("flavors.flavor.name");
        System.out.println(flavors);
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(flavors.get(0),"spicy");
        soft.assertEquals(flavors.get(1),"dry");
        soft.assertEquals(flavors.get(2),"sweet");
        soft.assertEquals(flavors.get(3),"bitter");
        soft.assertEquals(flavors.get(4),"sour");
        soft.assertAll();
    }
    @Test
    // To Check the response time of Pokemon API using Hamcrest Matchers.
    void getresponsetime() {
        RestAssured.baseURI = "https://pokeapi.co/api/v2";
        RequestSpecification genrequest = RestAssured.given();
        Response genresponse = genrequest.request(Method.GET, "/generation/1");
        //String resbody = genresponse.getBody().asString();
        long reponsetimeinMS = genresponse.getTime();
        System.out.println("Response time in milliseconds "+ reponsetimeinMS);
        long reponsetimeinSeconds = genresponse.getTimeIn(TimeUnit.SECONDS);
        System.out.println("Response time in Seconds "+ reponsetimeinSeconds);
        genresponse.then().time(Matchers.lessThan(1000L));

    }

    @Test
    // To check all the description and particular item of Pokemon's growth rate
    void getgrowthrate() {
        RestAssured.baseURI = "https://pokeapi.co/api/v2";
        RequestSpecification genrequest = RestAssured.given();
        Response genresponse = genrequest.request(Method.GET, "growth-rate/1");
        ResponseBody resbody = genresponse.getBody();
        JsonPath grResponse = resbody.jsonPath();
        List<String> language = grResponse.getList("descriptions.language.name");
        System.out.println(language);
        assertThat(language, contains("fr", "de", "en"));
        assertThat(language, hasItem("fr"));
    }

    @Test
    // To check pokemon species for id-1 shows the list of all the species name.
    void PokemanName() {
        RestAssured.baseURI = "https://pokeapi.co/api/v2";
        RequestSpecification genrequest = RestAssured.given();
        Response genresponse = genrequest.request(Method.GET, "growth-rate/1");
        ResponseBody resbody = genresponse.getBody();
        JsonPath grResponse = resbody.jsonPath();
        List<String> Species = grResponse.getList("pokemon_species.name");
        System.out.println(Species.size());
        for (String sp : Species) {
            System.out.println(sp);
            assertThat(Species, hasItem(sp));
        }
    }
@Test
    void Stats() {
    RestAssured.baseURI = "https://pokeapi.co/api/v2";
    RequestSpecification request = RestAssured.given();
    Response response = request.request(Method.GET, "/pokemon/35");
    JsonPath stats = response.jsonPath();
    Integer[] number = new Integer[6];
    int i=0;
    while (i<6) {
    number[i]=stats.getInt("stats["+i+"].base_stat");
        i++;
    }
    assertThat(number,arrayContainingInAnyOrder(70,45,48,60,65,35));
            assertThat(number,arrayWithSize(6));
     }

    @Test
// To check the header, Content-type & Encoding of Pokemon API using Hamcrest Matchers.
    public void getheaderss() {
        RestAssured.baseURI = "https://pokeapi.co/api/v2";
        RequestSpecification request = RestAssured.given();
        Response response = request.request(Method.GET, "/pokemon-species/812");
        Headers allheaders = response.getHeaders();
        //System.out.println(allheaders);
        String ConType = response.getContentType();
        System.out.println(ConType);
        assertThat(ConType, equalToIgnoringCase("application/json; charset=utf-8"));
        String Server = response.header("Server");
        System.out.println(Server);
        assertThat(Server, endsWith("flare"));
        String ConEncode = response.header("Content-Encoding");
        System.out.println(ConEncode);
        assertThat(ConEncode, startsWith("gz"));


    }
}

