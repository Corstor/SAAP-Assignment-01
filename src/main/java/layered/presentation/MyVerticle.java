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
        /* static files by default searched in "webroot" directory */
		router.route("/static/*").handler(StaticHandler.create().setCachingEnabled(false));
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
            reply.put("result", "Ok");
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            reply.put("result", "Error: " + e.getMessage());
        }
        sendReply(context, reply);
    }

    /**
     * Create an object starting from the request body.
     * 
     * @param request the request from which the object will be created.
     */
    protected abstract void create(JsonObject request);

    private void get(RoutingContext context) {
        logger.log(Level.INFO, "Asked to get data about " + context.request().getParam("id") + " " + createdObjectName);
		JsonObject reply = new JsonObject();
		reply.put("result", "ok");
		sendReply(context, reply);
    }

    protected void sendReply(RoutingContext request, JsonObject reply) {
		HttpServerResponse response = request.response();
		response.putHeader("content-type", "application/json");
		response.end(reply.toString());
	}
}
