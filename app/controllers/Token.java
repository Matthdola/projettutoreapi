package controllers;


import action.Cors;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import play.libs.Json;
import play.mvc.Controller;
import security.Security;
import play.mvc.Result;
import java.util.Timer;
import java.util.TimerTask;

@Cors
public class Token extends Controller {
    public static Result generate(String userId, String userAgent, String origin, boolean remember) {
        models.Token token = createToken(userId, userAgent,origin,remember);
        ObjectNode result = Json.newObject();
        if (token == null){
            result.put("uri", "/v1/tokens/");
            result.put("status", 500);
            result.put("token", "Error dans la creation du token");
            return internalServerError(result);
        }

        result.put("uri", "/v1/users/login");
        result.put("status", 202);
        result.put("token", Json.toJson(token));
        return ok(result);
    }

    private static models.Token createToken(String userId, String userAgent, String origin, boolean remember){
        models.Token token = new models.Token();
        token.setUserId(userId);
        token.setKey(Security.generateTokenKey(models.Token.DEFAULT_KEY_BYTES_LENGTH));
        token.setType(models.Token.Type.AUTHENTICATION);
        token.setUserAgent(userAgent);
        token.setOrigin(origin);
        token.setCreatedAt(DateTime.now());

        if (remember) {
            token.setExpiresAt(token.getCreatedAt().plusYears(1));
        } else {
            token.setExpiresAt(token.getCreatedAt().plusHours(models.Token.DEFAULT_DURATION_HOURS));
        }

        long delay = Seconds.secondsBetween(token.getCreatedAt(), token.getExpiresAt()).getSeconds() * 1000;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           @Override
                           public void run() {
                               models.Token.removeMatchingKey(token.getKey());
                           }
                       },
                delay);

        models.Token.save(token);
        return token;
    }
}
