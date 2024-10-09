package layered.presentation;

import java.util.logging.Level;
import java.util.logging.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import layered.business.EBikeCreation;
import layered.business.EBikeCreationImpl;

public class AdminGUI extends AbstractVerticle {
    private final int port;
    private final EBikeCreation bikeCreator;
    private static Logger logger = Logger.getLogger("Adming GUI");

    public AdminGUI(final int port) {
        this.port = port;
        this.bikeCreator = new EBikeCreationImpl();

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(this);
    }

    @Override
    public void start() {
        logger.log(Level.INFO, "Admin web server initializing...");
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        /* static files by default searched in "webroot" directory */
		router.route("/static/*").handler(StaticHandler.create().setCachingEnabled(false));
		router.route().handler(BodyHandler.create());

        router.route(HttpMethod.POST, "/api/admin/addBike").handler(this::AddEBike);
        router.route(HttpMethod.GET, "/api/admin").handler(this::SeeEBikes);

        server
		.requestHandler(router)
		.listen(port);

		logger.log(Level.INFO, "Web server ready - port: " + port);
    }

    private void AddEBike(RoutingContext context) {
        logger.log(Level.INFO, "Request to create a bike");
        JsonObject request = context.body().asJsonObject();
        JsonObject reply = new JsonObject();
        try {
            this.bikeCreator.createEbike(
                request.getString("id"), 
                request.getDouble("x"), 
                request.getDouble("y")
            );
            reply.put("result", "Ok");
        } catch (Exception e) {
            reply.put("result", "Error: " + e.getMessage());
        }
        sendReply(context, reply);
    }

    private void SeeEBikes(RoutingContext context) {
        logger.log(Level.INFO, "New request - query " + context.currentRoute().getPath());
		JsonObject reply = new JsonObject();
		reply.put("result", "ok");
		sendReply(context, reply);
    }

    private void sendReply(RoutingContext request, JsonObject reply) {
		HttpServerResponse response = request.response();
		response.putHeader("content-type", "application/json");
		response.end(reply.toString());
	}
}
