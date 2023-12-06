package org.acme;

import io.restassured.RestAssured;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

/*
It is a straightforward test, but at least it will detect if our application is pinning. Run the test with either:
    r in dev mode (using continuous testing)
    ./mvnw test

As you will see, it does not pin - no stack trace.
It is because the reactive REST client is implemented in a virtual-thread-friendly way.
 */

@QuarkusTest
class TheBestPlaceToBeResourceTest {

    @Test
    void verify() {
        RestAssured.get("/")
                .then()
                .statusCode(200);
    }
}