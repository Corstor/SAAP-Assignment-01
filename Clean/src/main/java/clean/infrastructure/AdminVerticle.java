package clean.infrastructure;

import java.io.IOException;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.StaticHandler;

public class AdminVerticle extends MyVerticle {

    public AdminVerticle(int port) {
        super(port, "Admin", "EBike");
    }

    @Override
    protected void additionalSetups() {
        router.route().handler(StaticHandler.create("admin").setCachingEnabled(false));
    }

    @Override
    protected void create(JsonObject request) throws IOException {
        this.bikeFactory.createEBike(
            request.getString("id"),
            Double.parseDouble(request.getString("x")),
            Double.parseDouble(request.getString("y"))
        );
    }

    @Override
    protected void load(String id, JsonObject reply) throws IOException {
        var bike = this.bikeFactory.getEBikeWithId(id);
        reply.put("id", id);
        reply.put("state", bike.getEBikeSnapshot().state());
        reply.put("location", bike.getEBikeSnapshot().location());
        reply.put("direction", bike.getEBikeSnapshot().direction());
        reply.put("speed", bike.getEBikeSnapshot().speed());
        reply.put("batteryLevel", bike.getEBikeSnapshot().batteryLevel());
    }
    
}
