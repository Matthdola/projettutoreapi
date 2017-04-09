package controllers;
import action.Cors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Medecin;
import mongo.PaginatedQueryResult;
import mongo.QueryResult;
import org.joda.time.DateTime;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

@Cors
public class Medecins extends Controller {

    public static Result list(){
        QueryResult queryResult = models.Medecin.findAllMedecin();
        if (queryResult.isError()){
            return notFound(String.format("Medecin does not exist."));
        }

        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/medecins/");
        result.put("status", 200);
        result.put("medecins", Json.toJson(((PaginatedQueryResult)queryResult).getResults()));
        return ok(result);
    }

    public static Result listByCentre(String centre){
        QueryResult queryResult = models.Medecin.listByCentre(centre);
        if (queryResult.isError()){
            return notFound(String.format("Medecin does not exist with the centre " + centre));
        }
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/medecins/");
        result.put("status", 200);
        result.put("medecin", Json.toJson(((PaginatedQueryResult)queryResult).getResults()));
        return ok(result);
    }

    public static Result listBySpecialite(String specialite){
        QueryResult queryResult = models.Medecin.listBySpecialite(specialite);
        if (queryResult.isError()){
            return notFound(String.format("Medecin does not exist with the specialite " + specialite));
        }
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/medecins/");
        result.put("status", 200);
        result.put("medecin", Json.toJson(((PaginatedQueryResult)queryResult).getResults()));
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
        models.Medecin medecin = (Medecin) models.Medecin.findById(id);
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
            String name = json.findPath("name").textValue();
            if(name == null){
                return badRequest("Missing parameter nom");
            }else {
                models.Medecin medecin = Json.fromJson(json, models.Medecin.class);
                medecin.setCreatedAt(DateTime.now().toDateTimeISO());
                models.Medecin.save(medecin);
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/medecins/");
                result.put("status", 202);
                result.put("medecin", Json.toJson(medecin));
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
                return badRequest("Missing parameter [nom]");
            }else {
                models.Medecin medecin = Json.fromJson(json, models.Medecin.class);
                if(!medecin.getId().equals(id)){
                    return notFound("Utilisateur not found");
                }
                models.Medecin.update(medecin);
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/medecins/"+id);
                result.put("status", 200);
                result.put("medecin", Json.toJson(medecin));
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
        models.Medecin medecin = (Medecin) models.Medecin.findById(id);
        if(medecin == null){
            return notFound(String.format("Medecin %s does not exist.", id));
        }
        models.Medecin.remove(medecin);
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/medecins/"+id);
        result.put("status", 200);
        result.put("medecin", Json.toJson(medecin));
        return ok(result);
    }
}
