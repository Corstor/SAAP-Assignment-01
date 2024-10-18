package layered.presentation;


import java.io.IOException;
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

public abstract class MyVerticle extends AbstractVerticle {
    private HttpServer server;
    private final String name;
    private final String createdObjectName;

    protected final int port;
    protected static Logger logger;
    protected Router router;

    public MyVerticle(int port, String name, String createdObjectName) {
        this.port = port;
        this.name = name;
        this.createdObjectName = createdObjectName;
    }

    public void launch() {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(this);
    }

    @Override
    public void start() {
        initialSetup();

        routerSetup();
        additionalSetups();

        endOfSetup();
    }

    private void initialSetup() {
        logger.log(Level.INFO, name + " web server initializing...");
        server = vertx.createHttpServer();
    }

    private void routerSetup() {
        router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route(HttpMethod.POST, "/api/register").handler(this::post);
        router.route(HttpMethod.GET, "/api/").handler(this::get);
    }

    private void endOfSetup() {
        server
		.requestHandler(router)
		.listen(port);

		logger.log(Level.INFO, name + " web server ready - port: " + port);
    }

    /**
     * Add other routes and their handler to the router if needed
     */
    protected abstract void additionalSetups();

    private void post(RoutingContext context) {
        logger.log(Level.INFO, "Request to create a new " + createdObjectName);
        JsonObject request = context.body().asJsonObject();
        JsonObject reply = new JsonObject();

        try {
            create(request);
            reply.put("result", "A " + createdObjectName + " has been created!");
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            reply.put("result", "Error -> " + e.getMessage());
        }
        sendReply(context, reply);
    }

    /**
     * Create an object starting from the request body.
     * 
     * @param request the request from which the object will be created.
     * @throws IOException 
     */
    protected abstract void create(JsonObject request) throws IOException;

    private void get(RoutingContext context) {
        String id = context.request().getParam("id");
        logger.log(Level.INFO, "Asked to get data about " + id + " " + createdObjectName);
		JsonObject reply = new JsonObject();

        try {
            load(id, reply);
            reply.put("result", "The requested " + createdObjectName + " exists");
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            reply.put("result", "Error -> " + e.getMessage());
        }
		
		sendReply(context, reply);
    }

    protected abstract void load(String id, JsonObject reply) throws IOException;

    protected void sendReply(RoutingContext request, JsonObject reply) {
		HttpServerResponse response = request.response();
		response.putHeader("content-type", "application/json");
		response.end(reply.toString());
	}
}
