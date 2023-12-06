package org.acme;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/*
    It’s essential to understand what happened behind the scene:
    1. Quarkus creates the virtual thread to invoke your endpoint (because of the @RunOnVirtualThread annotation).
    2. When the code invokes the rest client, the virtual thread is blocked, BUT the carrier thread is not blocked (that’s the virtual thread magic touch).
    3. Once the first invocation of the rest client completes, the virtual thread is rescheduled and continues its execution.
    4. The second rest client invocation happens, and the virtual thread is blocked again (but not the carrier thread).
    5. Finally, when the second invocation of the rest client completes, the virtual thread is rescheduled and continues its execution.
    6. The method returns the result. The virtual thread terminates.
    7. The result is captured by Quarkus and written in the HTTP response.    *
*/

@Path("/")
public class TheBestPlaceToBeResource {

    static final double VALENCE_LATITUDE = 44.9;
    static final double VALENCE_LONGITUDE = 4.9;

    static final double ATHENS_LATITUDE = 37.9;
    static final double ATHENS_LONGITUDE = 23.7;

    static final double sharm_LATITUDE = 27.915817;
    static final double sharm_LONGITUDE = 34.329950;

    @RestClient WeatherService service;

    @GET
    @RunOnVirtualThread  // Instructs Quarkus to invoke this method on a virtual thread
    public String getTheBestPlaceToBe() {
        var valence = service.getWeather(VALENCE_LATITUDE, VALENCE_LONGITUDE).weather().temperature();
        var athens = service.getWeather(ATHENS_LATITUDE, ATHENS_LONGITUDE).weather().temperature();
        var sharm = service.getWeather(sharm_LATITUDE, sharm_LONGITUDE).weather().temperature();

        // Advanced decision tree
        if (sharm > athens && sharm <= 35) {
            return "Sharm, with temperature: " + sharm + "! (" + Thread.currentThread() + ")";
        } else if (athens > 35) {
            return "Sharm, with temperature: " + sharm + "! (" + Thread.currentThread() + ")";
        } else {
            return "Athens (" + Thread.currentThread() + ")";
        }
    }
}