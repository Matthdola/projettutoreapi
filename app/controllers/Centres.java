package controllers;

import action.Cors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import models.*;

import java.util.List;

@Cors
public class Centres extends Controller {
  
    public static Result list(){
        List<models.Centre> centres = models.Centre.findAll();
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/centres/");
        result.put("status", 200);
        result.put("centre", Json.toJson(centres));
        return ok(result);
    }

    public static Result read(String id){
        if(id == null){
            return notFound(String.format("Centre %s does not exist.", id));
        }
        if(id.isEmpty()){
            return notFound(String.format("Centre %s does not exist.", id));
        }
        models.Centre centre = models.Centre.findById(id);
        if(centre == null){
            return notFound(String.format("Centre %s does not exist.", id));
        }
        return  ok(Json.toJson(centre));
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
                models.Centre centre = Json.fromJson(json, models.Centre.class);
                models.Centre.save(centre);
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/centres/");
                result.put("status", 202);
                result.put("centre", Json.toJson(centre).toString());
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
                models.Centre centre = Json.fromJson(json, models.Centre.class);
                if(!centre.getId().equals(id)){
                    return notFound("Utilisateur not found");
                }
                models.Centre.update(centre);
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/centres/"+id);
                result.put("status", 200);
                result.put("centre", Json.toJson(centre).toString());
                return ok(result);
            }
        }
    }

    public static Result delete(String id){
        if(id == null){
            return notFound(String.format("Centre %s does not exist.", id));
        }
        if (id.isEmpty()){
            return notFound(String.format("Centre %s does not exist.", id));
        }
        models.Centre centre = models.Centre.findById(id);
        if(centre == null){
            return notFound(String.format("Centre %s does not exist.", id));
        }
        models.Centre.remove(centre);
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/centres/"+id);
        result.put("status", 200);
        result.put("centre", Json.toJson(centre).toString());
        return ok(result);
    }
}
