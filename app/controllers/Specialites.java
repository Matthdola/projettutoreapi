package controllers;

import action.Cors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import models.Specialite;

import java.util.List;

//@Cors
public class Specialites extends Controller {

    public static Result delete(String id){
        if(id == null){
            return notFound(String.format("Specialite %s does not exist.", id));
        }
        if (id.isEmpty()){
            return notFound(String.format("Specialite %s does not exist.", id));
        }
        models.Specialite specialite = models.Specialite.findById(id);
        if(specialite == null){
            return notFound(String.format("Specialite %s does not exist.", id));
        }
        models.Specialite.remove(specialite);
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/specialites/"+id);
        result.put("status", 200);
        result.put("specialite", Json.toJson(specialite).toString());
        return ok(result);
    }

    public static Result list(){
        List<Specialite> specialites = models.Specialite.findAll();
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/specialites/");
        result.put("status", 200);
        result.put("specialite", Json.toJson(specialites));
        return ok(result);
    }

    public static Result read(String id){
        if(id == null){
            return notFound(String.format("Specialite %s does not exist.", id));
        }
        if(id.isEmpty()){
            return notFound(String.format("Specialite %s does not exist.", id));
        }
        models.Specialite specialite = models.Specialite.findById(id);
        if(specialite == null){
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
                models.Specialite.save(specialite);
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/specialites/");
                result.put("status", 202);
                result.put("specialite", Json.toJson(specialite).toString());
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
            String name = json.findPath("nom").textValue();
            if(name == null){
                return badRequest("Missing parameter [nom]");
            }else {
                models.Specialite specialite = Json.fromJson(json, models.Specialite.class);
                if(!specialite.getId().equals(id)){
                    return notFound("Utilisateur not found");
                }
                models.Specialite.update(specialite);
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/specialites/"+id);
                result.put("status", 200);
                result.put("specialite", Json.toJson(specialite).toString());
                return ok(result);
            }
        }
    }
}
