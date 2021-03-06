package controllers;

import action.Cors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import mongo.PaginatedQueryResult;
import mongo.QueryResult;
import org.joda.time.DateTime;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import models.Specialite;

import java.util.List;

@Cors
public class Specialites extends Controller {

    public static Result delete(String id){
        if(id == null){
            return notFound(String.format("Specialite %s does not exist.", id));
        }
        if (id.isEmpty()){
            return notFound(String.format("Specialite %s does not exist.", id));
        }
        QueryResult specialite = models.Specialite.findById(id);
        if(specialite.isError()){
            return notFound(String.format("Specialite %s does not exist.", id));
        }
        models.Specialite.remove((Specialite) specialite);
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/specialites/"+id);
        result.put("status", 200);
        result.put("specialite", Json.toJson(specialite));
        return ok(result);
    }

    public static Result list(){
        QueryResult queryResult = models.Specialite.findAll();
        if (queryResult.isError()){
            return notFound(String.format("Specialites does not exist."));
        }
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/specialites/");
        result.put("status", 200);
        result.put("specialite", Json.toJson(((PaginatedQueryResult)queryResult).getResults()));
        return ok(result);
    }

    public static Result read(String id){
        if(id == null){
            return notFound(String.format("Specialite %s does not exist.", id));
        }
        if(id.isEmpty()){
            return notFound(String.format("Specialite %s does not exist.", id));
        }
        QueryResult specialite = models.Specialite.findById(id);
        if(specialite.isError()){
            return notFound(String.format("Specialite %s does not exist.", id));
        }
        return  ok(Json.toJson(specialite));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result create(){
        JsonNode json = request().body().asJson();
        if(json == null){
            return badRequest("Expecting Json data");
        } else {
            String name = json.findPath("name").textValue();
            if(name == null){
                return badRequest("Missing parameter [name]");
            }else {
                models.Specialite specialite = Json.fromJson(json, models.Specialite.class);
                specialite.setCreatedAt(DateTime.now());
                QueryResult queryResult = models.Specialite.save(specialite);
                if (queryResult.isError()){
                    return notFound(String.format("Specialite does not exist."));
                }
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/specialites/");
                result.put("status", 202);
                result.put("specialite", Json.toJson(specialite));
                return ok(result);
            }
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result update(String id){
        JsonNode json = request().body().asJson();
        if(json == null){
            return badRequest("Expecting Json data");
        } else {
            String name = json.findPath("name").textValue();
            if(name == null){
                return badRequest("Missing parameter [name]");
            }else {
                models.Specialite specialite = Json.fromJson(json, models.Specialite.class);
                if(!specialite.getId().equals(id)){
                    return notFound("Utilisateur not found");
                }
                models.Specialite.update(specialite);
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/specialites/"+id);
                result.put("status", 200);
                result.put("specialite", Json.toJson(specialite));
                return ok(result);
            }
        }
    }
}
