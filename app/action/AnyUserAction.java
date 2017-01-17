package action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Action;

import models.Token;
import mongo.Error;
import play.Play;
import play.libs.F;

import play.mvc.Http;
import play.mvc.Result;
import util.Constants;

import java.util.Map;

public class AnyUserAction extends Action.Simple {
    public final static String AUTH_TOKEN_HEADER = "X-Auth-Token";

    public F.Promise<Result> call(Http.Context context) throws Throwable {
        addCorsHeaders(context.response());

        Http.Request request = context.request();

        if (request.hasHeader(AUTH_TOKEN_HEADER) || request.hasHeader(Constants.ADMIN_AUTH_TOKEN_HEADER)) {
            Map<String, String[]> headers = request.headers();

            String[] authTokensKeys = headers.get(AUTH_TOKEN_HEADER);
            if(authTokensKeys == null || authTokensKeys.length == 0) {
                authTokensKeys = headers.get(Constants.ADMIN_AUTH_TOKEN_HEADER);
            }
            if (authTokensKeys.length > 0) {
                String authTokenKey = authTokensKeys[0];

                Object result = models.Token.findByKey(authTokenKey);

                if (result == null) {
                    mongo.Error error = new Error(Error.INVALID_TOKEN, "invalid token, authentication required.");
                    final ObjectNode res = Json.newObject();
                    res.put("status", 400);
                    res.put("error",  Json.toJson(error));

                    return F.Promise.promise(() -> unauthorized(Json.toJson(res)));
                }

                Token token = (Token) result;

                if (token.isExpired()) {
                    Token.removeMatchingKey(token.getKey());

                    Error error = new Error(Error.TOKEN_EXPIRED, "token expired, authentication required.");
                    final ObjectNode res = Json.newObject();
                    res.put("status", 400);
                    res.put("error",  Json.toJson(error));

                    return F.Promise.promise(() -> unauthorized(Json.toJson(res)));
                }

                Object userQueryResult = models.User.findById(token.getUserId());

                if (userQueryResult != null) {

                    models.User user = (models.User) userQueryResult;

                    context.args.put("user", user);

                    return delegate.call(context);


                }
            }
        }

        Error error = new Error(Error.MISSING_AUTH_TOKEN, "an authentication token is missing.");
        final ObjectNode res = Json.newObject();
        res.put("status", 400);
        res.put("error",  Json.toJson(error));
        return F.Promise.promise(() -> forbidden(Json.toJson(res)));
    }

    public void addCorsHeaders(Http.Response response) {
        response.setHeader("Access-Control-Allow-Origin", Play.application().configuration().getString(Constants.ORIGIN_CONFIG_KEY));
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
