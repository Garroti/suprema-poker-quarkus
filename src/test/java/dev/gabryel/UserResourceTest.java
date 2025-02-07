package dev.gabryel;

import dev.gabryel.Controller.UserController;
import dev.gabryel.Entity.User;
import dev.gabryel.Repository.UserRepository;
import dev.gabryel.Service.UserService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class UserResourceTest {

    @Inject
    UserService userService;

    @InjectMock
    UserRepository userRepository;

    @Inject
    UserController userController;

    @Test
    public void testListUsersWithAuth() {
        String token = generateToken("suprema");

        given()
                .header("Authorization", "Bearer " + token)
                .when().get("/users")
                .then()
                .statusCode(200);
    }

    @Test
    @Transactional
    public void testCreateUser_Success() {
        String jsonUser = """
                {
                    "name": "John Doe",
                    "cpf": "12724351053",
                    "phone": "(31) 91234-5678",
                    "password": "securePass",
                    "username": "johndoe"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(jsonUser)
                .when().post("/users")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body("userId", notNullValue());
    }

    @Test
    public void testCreateUser_Fail_UsernameExists() {
        String jsonUser = """
                {
                    "name": "Jane Doe",
                    "cpf": "12345678901",
                    "phone": "11988888888",
                    "password": "password123",
                    "username": "existingUser"
                }
                """;

        Mockito.when(userRepository.findByUsername("existingUser")).thenReturn(new User());

        given()
                .contentType(ContentType.JSON)
                .body(jsonUser)
                .when().post("/users")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body(equalTo("Username já está em uso!"));
    }

    @Test
    @Transactional
    public void testCreateUser_Fail_CPFAlreadyExists() {
        String jsonUser = """
            {
                "name": "Jane Doe",
                "cpf": "12345678901",
                "phone": "11988888888",
                "password": "password123",
                "username": "janedoe"
            }
            """;

        Mockito.when(userRepository.findByCPF("12345678901")).thenReturn(new User());

        given()
                .contentType(ContentType.JSON)
                .body(jsonUser)
                .when().post("/users")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body(equalTo("CPF ja cadastrado!"));
    }

    @Test
    @Transactional
    public void testCreateUser_Fail_InvalidCPF() {
        String jsonUser = """
            {
                "name": "John Doe",
                "cpf": "12312312300",
                "phone": "11988888888",
                "password": "password123",
                "username": "johndoe"
            }
            """;

        Mockito.when(userRepository.findByCPF("12312312300")).thenReturn(null);

        given()
                .contentType(ContentType.JSON)
                .body(jsonUser)
                .when().post("/users")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body(equalTo("CPF inválido!"));
    }

    @Test
    @Transactional
    public void testCreateUser_Fail_InvalidPhone() {
        String jsonUser = """
            {
                "name": "John Doe",
                "cpf": "12724351053",
                "phone": "1199999",
                "password": "password123",
                "username": "johndoe"
            }
            """;

        Mockito.when(userRepository.findByCPF("12724351053")).thenReturn(null);

        given()
                .contentType(ContentType.JSON)
                .body(jsonUser)
                .when().post("/users")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body(equalTo("Número de telefone inválido!"));
    }

    private String generateToken(String username) {
        return Jwt.issuer("suprema")
                .upn(username)
                .groups("user")
                .sign();
    }

}