package integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static javax.servlet.http.HttpServletResponse.SC_OK;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.greaterThan;

@Slf4j
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
