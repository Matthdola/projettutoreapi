package controllers;

import action.Cors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

@Cors
public class Patients extends Controller {
    
    public static Result list(){
        List<models.Utilisateur> patients = models.Utilisateur.findAllPatient();
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/patients/");
        result.put("status", 200);
        result.put("patients", Json.toJson(patients));
        return ok(result);
    }

    public static Result read(String id){
        if(id == null){
            return notFound(String.format("Patients %s does not exist.", id));
        }
        if(id.isEmpty()){
            return notFound(String.format("Patients %s does not exist.", id));
        }
        models.Patient patient = models.Patient.findById(id);
        if(patient == null){
            return notFound(String.format("Patients %s does not exist.", id));
        }
        return  ok(Json.toJson(patient));
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
                models.Patient patient = Json.fromJson(json, models.Patient.class);
                models.Patient.save(patient);
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/patients/");
                result.put("status", 202);
                result.put("patient", Json.toJson(patient).toString());
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
                models.Patient patient = Json.fromJson(json, models.Patient.class);
                if(!patient.getId().equals(id)){
                    return notFound("Utilisateur not found");
                }
                models.Patient.update(patient);
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/patients/"+id);
                result.put("status", 200);
                result.put("patient", Json.toJson(patient).toString());
                return ok(result);
            }
        }
    }

    public static Result delete(String id){
        if(id == null){
            return notFound(String.format("Patients %s does not exist.", id));
        }
        if (id.isEmpty()){
            return notFound(String.format("Patients %s does not exist.", id));
        }
        models.Patient patient = models.Patient.findById(id);
        if(patient == null){
            return notFound(String.format("Patients %s does not exist.", id));
        }
        models.Patient.remove(patient);
        ObjectNode result = Json.newObject();
        result.put("uri", "/v1/patients/"+id);
        result.put("status", 200);
        result.put("patient", Json.toJson(patient).toString());
        return ok(result);
    }
}
