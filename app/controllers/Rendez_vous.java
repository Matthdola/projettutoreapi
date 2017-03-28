package controllers;


import action.Cors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

//@Cors
public class Rendez_vous extends Controller {

    public static Result list(){
        List<models.RendezVous> rendezVouses = models.RendezVous.findAll();
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/patients/");
        result.put("status", 200);
        result.put("rendez_vous", Json.toJson(rendezVouses));
        return ok(result);
    }

    public static Result read(String id){
        if(id == null){
            return notFound(String.format("Rendez-vous does not exist."));
        }
        if(id.isEmpty()){
            return notFound(String.format("Rendez-vous %s does not exist.", id));
        }
        models.RendezVous rendezVous = models.RendezVous.findById(id);
        if(rendezVous == null){
            return notFound(String.format("Rendez-vous %s does not exist.", id));
        }
        return  ok(Json.toJson(rendezVous));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result create(){
        JsonNode json = request().body().asJson();
        if(json == null){
            return badRequest("Expecting Json data");
        } else {
            String name = json.findPath("nom").textValue();
            if(name == null){
                return badRequest("Missing parameter [nom]");
            }else {
                models.RendezVous rendezVous = Json.fromJson(json, models.RendezVous.class);
                models.RendezVous.save(rendezVous);
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/patients/");
                result.put("status", 202);
                result.put("rendez_vous", Json.toJson(rendezVous).toString());
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
                models.RendezVous rendezVous = Json.fromJson(json, models.RendezVous.class);
                if(!rendezVous.getId().equals(id)){
                    return notFound("Utilisateur not found");
                }
                models.RendezVous.update(rendezVous);
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/patients/"+id);
                result.put("status", 200);
                result.put("rendez_vous", Json.toJson(rendezVous).toString());
                return ok(result);
            }
        }
    }

    public static Result delete(String id){
        if(id == null){
            return notFound(String.format("Patient does not exist."));
        }
        if (id.isEmpty()){
            return notFound(String.format("Patient %s does not exist.", id));
        }
        models.RendezVous rendezVous = models.RendezVous.findById(id);
        if(rendezVous == null){
            return notFound(String.format("Patient %s does not exist.", id));
        }
        models.RendezVous.remove(rendezVous);
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/patients/"+id);
        result.put("status", 200);
        result.put("rendez_vous", Json.toJson(rendezVous).toString());
        return ok(result);
    }
}
