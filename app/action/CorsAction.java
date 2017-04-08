package action;


import play.Play;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import util.Constants;

public class CorsAction extends Action<Cors> {
    public F.Promise<Result> call(Http.Context context) throws Throwable {
        Http.Response response = context.response();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        if (context.request().method().equals("OPTIONS")) {
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Origin, X-Requested-With, Accept, X-Auth-Token, X-Admin-Auth-Token, X-Working-Project-Id");
//            response.setHeader("Access-Control-Max-Age", "3600");

            return F.Promise.promise(Results::ok);
        }

        return delegate.call(context);
    }

}
