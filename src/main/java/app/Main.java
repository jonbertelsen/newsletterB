package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {
    public static void main(String[] args)
    {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler ->  handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7071);

        // Routing

        app.get("/", ctx ->  ctx.render("index.html"));
        app.get("/login", ctx -> ctx.render("login.html"));
        app.post("/login", ctx -> handleLogin(ctx));
    }

    private static void handleLogin(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        if (username.equals("admin") && password.equals("1234")) {
            ctx.attribute("message", "Velkommen, " + username);
            ctx.attribute("username", username);
            ctx.render("index.html");
            return;
        }
        ctx.redirect("/login");
    }

}