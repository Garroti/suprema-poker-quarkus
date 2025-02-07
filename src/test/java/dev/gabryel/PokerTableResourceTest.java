package dev.gabryel;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.jwt.build.Jwt;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class PokerTableResourceTest {

    @Test
    public void testListPokerTables() {
        String token = generateToken("suprema");

        given()
                .header("Authorization", "Bearer " + token)
                .when().get("/poker-tables")
                .then()
                .statusCode(200);
    }

    @Test
    @Transactional
    public void testCreatePokerTable() {
        String token = generateToken("suprema");

        String jsonPokerTable = """
            {
                "name": "Mesa 1"
            }
            """;

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(jsonPokerTable)
                .when().post("/poker-tables")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body("tableId", notNullValue());
    }

    private String generateToken(String username) {
        return Jwt.issuer("suprema")
                .upn(username)
                .groups("user")
                .sign();
    }

}
