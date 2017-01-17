package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joda.time.DateTime;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import models.*;

import java.util.List;

public class Demandes extends Controller {

    public static Result list(){
        List<models.Demande> demandes = models.Demande.findAll();
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/demandes/");
        result.put("status", 200);
        result.put("demandes", Json.toJson(demandes));
        return ok(result);
    }

    public static Result read(String id){
        if(id == null){
            return notFound(String.format("Demande does not exist."));
        }
        if(id.isEmpty()){
            return notFound(String.format("Demande %s does not exist.", id));
        }
        models.Demande demande = models.Demande.findById(id);
        if(demande == null){
            return notFound(String.format("Demande %s does not exist.", id));
        }
        return  ok(Json.toJson(demande));
    }

    public static Result readByPatient(String patientId){
        if(patientId == null){
            return notFound(String.format("Demande does not exist ."));
        }
        if(patientId.isEmpty()){
            return notFound(String.format("Demande does not exist with %s.", patientId));
        }
        List<models.Demande> demandes = models.Demande.listByIdPatient(patientId);
        if(demandes == null){
            return notFound(String.format("Demande does not exist with %s.", patientId));
        }
        return  ok(Json.toJson(demandes));
    }

    public static Result readByEtat(String etat){
        if(etat == null){
            return notFound(String.format("Demande does not exist."));
        }
        if(etat.isEmpty()){
            return notFound(String.format("Demande does not exist with %s .", etat));
        }
        List<models.Demande> demandes = models.Demande.listByEtat(etat);
        if(demandes == null){
            return notFound(String.format("Demande does not exist with %s.", etat));
        }
        return  ok(Json.toJson(demandes));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result readByDate(){
        JsonNode json = request().body().asJson();
        if(json == null){
            return badRequest("Expecting Json data");
        } else {
            String date = json.findPath("date").textValue();
            DateTime dateDemande = DateTime.parse(date);
            List<models.Demande> demandes = models.Demande.listByDate(dateDemande);
            if(demandes == null){
                return notFound(String.format("Demande does not exist with %s.", date));
            }
            return  ok(Json.toJson(demandes));
        }

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
                models.Demande demande = Json.fromJson(json, models.Demande.class);
                models.Demande.save(demande);
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/demandes/");
                result.put("status", 202);
                result.put("demande", Json.toJson(demande).toString());
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
                models.Demande demande = Json.fromJson(json, models.Demande.class);
                if(!demande.getId().equals(id)){
                    return notFound("User not found");
                }
                models.Demande.update(demande);
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/demandes/"+id);
                result.put("status", 200);
                result.put("demande", Json.toJson(demande).toString());
                return ok(result);
            }
        }
    }

    public static Result delete(String id){
        if(id == null){
            return notFound(String.format("Demande %s does not exist.", id));
        }
        if (id.isEmpty()){
            return notFound(String.format("Demande %s does not exist.", id));
        }
        models.Demande demande = models.Demande.findById(id);
        if(demande == null){
            return notFound(String.format("Demande %s does not exist.", id));
        }
        models.Demande.remove(demande);
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/demandes/"+id);
        result.put("status", 200);
        result.put("demande", Json.toJson(demande).toString());
        return ok(result);
    }
}
