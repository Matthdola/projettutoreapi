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
import models.*;

import java.util.List;

@Cors
public class Demandes extends Controller {

    public static Result list(){
        QueryResult demandes = models.Demande.findAll();
        if (demandes.isError()){
            return notFound(String.format("Demandes does not exist."));
        }
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/demandes/");
        result.put("status", 200);
        result.put("demandes", Json.toJson(((PaginatedQueryResult)demandes).getResults()));
        return ok(result);
    }

    public static Result read(String id){
        if(id == null){
            return notFound(String.format("Demande does not exist."));
        }
        if(id.isEmpty()){
            return notFound(String.format("Demande %s does not exist.", id));
        }
        QueryResult demande = models.Demande.findById(id);
        if(demande.isError()){
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
        QueryResult demandes = models.Demande.listByIdPatient(patientId);
        if(demandes.isError()){
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
        QueryResult demandes = models.Demande.listByEtat(etat);
        if(demandes.isError()){
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
            QueryResult demandes = models.Demande.listByDate(dateDemande);
            if(demandes.isError()){
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
            String idPatient = json.findPath("id_patient").textValue();
            if(idPatient == null){
                return badRequest("Missing parameter [id_patient]");
            }else {
                models.Demande demande = Json.fromJson(json, models.Demande.class);
                demande.setDateDemande(DateTime.now());
                demande.setCreatedAt(DateTime.now());
                models.Demande.save(demande);
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/demandes/");
                result.put("status", 202);
                result.put("demande", Json.toJson(demande));
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
                    return notFound("Utilisateur not found");
                }
                models.Demande.update(demande);
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/demandes/"+id);
                result.put("status", 200);
                result.put("demande", Json.toJson(demande));
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
        QueryResult demande = models.Demande.findById(id);
        if(demande.isError()){
            return notFound(String.format("Demande %s does not exist.", id));
        }
        models.Demande.remove((Demande)demande);
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/demandes/"+id);
        result.put("status", 200);
        result.put("demande", Json.toJson(demande));
        return ok(result);
    }
}
