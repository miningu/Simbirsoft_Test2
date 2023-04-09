package tests;

import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Description;
import io.restassured.http.ContentType;
import net.minidev.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class Home2 {

    public int getWeight(String pokemonName) {
        String weight = "";
        String pokemonjson = given()
                .baseUri("https://pokeapi.co/api/v2/pokemon")
                .contentType(ContentType.JSON)
                .when()
                .get(pokemonName)
                .asString();
        weight = JsonPath.read(pokemonjson, "$..weight").toString();
        weight = weight.substring(1,weight.length()-1);
        return Integer.parseInt(weight.trim());
    }

    public String getAbility(String pokemonName){
        String ability = "";
        String pokemonjson = given()
                .baseUri("https://pokeapi.co/api/v2/pokemon")
                .contentType(ContentType.JSON)
                .when()
                .get(pokemonName)
                .asString();
        ability = JsonPath.read(pokemonjson, "$..ability.name").toString();
        return ability;
    }
    @Test
    @Description("Проверка, что у покемона rattata вес меньше, чем у pidgeotto")
    public void Case1(){
        int weight1 = getWeight("rattata");
        int weight2 = getWeight("pidgeotto");
        Assert.assertTrue("Вес rattata больше или равно веса pidgeotto",weight1<weight2 );
    }
    @Test
    @Description("Проверка наличия умения побег у покемона rattata")
    public void Case2(){
        String ability = getAbility("rattata");
//        String weight2 = getAbility("pidgeotto");
        System.out.println (ability);
        Assert.assertTrue("Умение побег у покемона rattata отсутсвует",ability.contains("run-away") );
    }
    @Test
    @Description("Проверка отсутствия умения побег у покемона pidgeotto")
    public void Case3(){
        String ability = getAbility("pidgeotto");
        System.out.println (ability);
        Assert.assertFalse("Умение побег у покемона pidgeotto есть",ability.contains("run-away") );
    }

    @Test
    @Description("Проверка отсутствия умения побег у покемона pidgeotto")
    public void Case4(){
        int k = 51;
        String getName = "";
        String pokemonjson = given()
                .baseUri("https://pokeapi.co/api/v2/pokemon")
                .contentType(ContentType.JSON)
                .queryParam("limit",k)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().asString();
        String name = "name";
        int number = (pokemonjson.length() - pokemonjson.replace(name, "").length()) / name.length();
        System.out.println("Количество покемонов = " +number);
        Assert.assertTrue("Количество результатов не соответсвует запросу", number == k );

        getName = JsonPath.read(pokemonjson, "$..name").toString();
        String emptyName= "\"\"";
        int emptyNameCount = 0;
        emptyNameCount = (getName.length() - getName.replace(emptyName, "").length()) / emptyName.length();
        System.out.println("Количество покемонов без имени "+emptyNameCount);
        Assert.assertTrue("Есть покемоны без имени", emptyNameCount == 0 );
    }
}
