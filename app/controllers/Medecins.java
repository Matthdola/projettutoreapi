package controllers;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joda.time.DateTime;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class Medecins extends Controller {

    public static Result list(){
        List<models.Medecin> medecins = models.Medecin.findAll();
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/medecins/");
        result.put("status", 200);
        result.put("medecin", Json.toJson(medecins));
        return ok(result);
    }

    public static Result listByCentre(String centre){
        List<models.Medecin> medecins = models.Medecin.listByCentre(centre);
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/medecins/");
        result.put("status", 200);
        result.put("medecin", Json.toJson(medecins));
        return ok(result);
    }


    public static Result listBySpecialite(String specialite){
        List<models.Medecin> medecins = models.Medecin.listBySpecialite(specialite);
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/medecins/");
        result.put("status", 200);
        result.put("medecin", Json.toJson(medecins));
        return ok(result);
    }


    public static Result read(String id){
        if(id == null){
            ObjectNode result = Json.newObject();
            result.put("uri", "/v1/medecins/"+id);
            result.put("status", 404);
            result.put("message", String.format("Medecin %s does not exist.", id));

            return notFound(result);
        }
        if(id.isEmpty()){
            ObjectNode result = Json.newObject();
            result.put("uri", "/v1/medecins/"+id);
            result.put("status", 404);
            result.put("message", String.format("Medecin %s does not exist.", id));

            return notFound(result);
        }
        models.Medecin medecin = models.Medecin.findById(id);
        if(medecin == null){
            ObjectNode result = Json.newObject();
            result.put("uri", "/v1/medecins/"+id);
            result.put("status", 404);
            result.put("message", String.format("Medecin %s does not exist.", id));

            return notFound(result);
        }
        return  ok(Json.toJson(medecin));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result create(){
        JsonNode json = request().body().asJson();
        if(json == null){
            return badRequest("Expecting Json data");
        } else {
            String name = json.findPath("nom").textValue();
            if(name == null){
                return badRequest("Missing parameter nom");
            }else {
                models.Medecin medecin = Json.fromJson(json, models.Medecin.class);
                medecin.setCreatedAt(DateTime.now().toDateTimeISO());
                models.Medecin.save(medecin);
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/medecins/");
                result.put("status", 202);
                result.put("medecin", Json.toJson(medecin).toString());
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
                models.Medecin medecin = Json.fromJson(json, models.Medecin.class);
                if(!medecin.getId().equals(id)){
                    return notFound("User not found");
                }
                models.Medecin.update(medecin);
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/medecins/"+id);
                result.put("status", 200);
                result.put("medecin", Json.toJson(medecin).toString());
                return ok(result);
            }
        }
    }

    public static Result delete(String id){
        if(id == null){
            return notFound(String.format("Medecin %s does not exist.", id));
        }
        if (id.isEmpty()){
            return notFound(String.format("Medecin %s does not exist.", id));
        }
        models.Medecin medecin = models.Medecin.findById(id);
        if(medecin == null){
            return notFound(String.format("Medecin %s does not exist.", id));
        }
        models.Medecin.remove(medecin);
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/medecins/"+id);
        result.put("status", 200);
        result.put("medecin", Json.toJson(medecin).toString());
        return ok(result);
    }
}
