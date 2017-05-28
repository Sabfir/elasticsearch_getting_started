package integration;

import org.junit.Test;

import static javax.servlet.http.HttpServletResponse.SC_OK;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.greaterThan;

public class AddressControllerIT extends BaseControllerIT {

    @Test
    public void getAll() throws Exception {
        given().
        when().
                get("/addresses").
        then().
                statusCode(SC_OK).
                body("size()", greaterThan(0));
    }
}
