package controllers;


import action.Cors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Utilisateur;
import mongo.PaginatedQueryResult;
import mongo.QueryResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.joda.time.DateTime;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.libs.Json;

import mongo.Error;
import security.Security;

import java.util.List;

@Cors
public class Utilisateurs extends Controller {
    public final static String ACTIVATION_CODE = "activation_code";
    public final static String INVITATION_CODE = "invitation_code";
    public final static String PASSWORD_RESET_CODE = "password_reset_code";

    public static Result list(){
        QueryResult queryResult = models.Utilisateur.findAll();
        ObjectNode result = Json.newObject();
        if (queryResult.isError()){
            Error error = new Error(Error.INVALID_CONTENT_TYPE, "'Content-Type' header must be set to 'application/json' for this request.");

            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        result.put("uri", "/v1/users/");
        result.put("status", 200);
        result.put("utilisateurs", Json.toJson( (  (PaginatedQueryResult)queryResult).getResults()));
        return ok(result);
    }


    @BodyParser.Of(BodyParser.Json.class)
    public static Result signup() {
        JsonNode json = request().body().asJson();
        ObjectNode result = Json.newObject();

        if (json == null) {
            Error error = new Error(Error.INVALID_CONTENT_TYPE, "'Content-Type' header must be set to 'application/json' for this request.");

            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        JsonNode mailNode = json.findPath("email");
        JsonNode nameNode = json.findPath("name");
        JsonNode passwordNode = json.findPath("password");
        JsonNode typeNode = json.findPath("type");

        if (mailNode.isMissingNode() || nameNode.isMissingNode() || passwordNode.isMissingNode() || typeNode.isMissingNode()) {
            Error error = new Error(Error.MISSING_PARAMETER, "email or name or password or type is missing in the request body.");

            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        String mail = mailNode.textValue().trim();
        String name = nameNode.textValue().trim();
        String password = passwordNode.textValue();
        String type = typeNode.textValue();

        if (mail.isEmpty() || name.isEmpty() || password.isEmpty() || type.isEmpty()) {
            Error error = new Error(Error.EMPTY_PARAMETER, "email, name and password or type can not be empty.");

            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        if (!type.equals("patient") && !type.equals("medecin") && !type.equals("assureur")) {
            Error error = new Error(Error.EMPTY_PARAMETER, "type should have one of the following values (patient, medecin, assureur).");

            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        if(!EmailValidator.getInstance().isValid(mail)){
            Error error = new Error(Error.INVALID_EMAIL, "The given email is not valid.");

            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        QueryResult tempUserResult = Utilisateur.findByEmail(mail);
        if (tempUserResult.isError()) {
            Error error = new Error(Error.DUPLICATE_KEY, "The given email is already used.");

            result.put("uri", request().uri());
            result.put("status", 409);
            result.put("error",  Json.toJson(error));

            return unauthorized(result);
        }

        if (!StringUtils.isAlphanumeric(name)) {
            Error error = new Error(Error.INVALID_USERNAME, "the username may only contain alphanumeric characters.");

            result.put("uri", request().uri());
            result.put("status", 409);
            result.put("error",  Json.toJson(error));
        }

        String passwordSalt = Security.generateRandomPasswordSalt();
        String encryptedPassword = Security.generatePBKDF2EncryptedPassword(password, passwordSalt);

        if (passwordSalt.isEmpty() || encryptedPassword.isEmpty()) {
            Error error = new Error(Error.UNEXPECTED, "something went wrong, try again later.");

            result.put("uri", request().uri());
            result.put("status", 500);
            result.put("error",  Json.toJson(error));
        }
        Utilisateur utilisateur = null;

        if(type.equals("patient")){
            utilisateur = Json.fromJson(json, models.Patient.class);
        } else if(type.equals("medecin")){
            utilisateur = Json.fromJson(json, models.Medecin.class);
        } else if(type.equals("assureur")){
            utilisateur = Json.fromJson(json, models.Assureur.class);
        }

        if(utilisateur != null) {
            utilisateur.setType(Utilisateur.Type.fromValue(type));
            utilisateur.setEmail(mail);
            utilisateur.setName(name);
            utilisateur.setPasswordSalt(passwordSalt);
            utilisateur.setPassword(encryptedPassword);
            utilisateur.setCreatedAt(DateTime.now());
            //utilisateur.setActivated(true);
            return signupAdmin(utilisateur);
        }
        return null;
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result login() {
        JsonNode json = request().body().asJson();
        ObjectNode result = Json.newObject();

        if (json == null) {
            Error error = new Error(Error.INVALID_CONTENT_TYPE, "'Content-Type' header must be set to 'application/json' for this request.");
            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        JsonNode credentials = json.findPath("credentials");
        JsonNode rememberNode = json.findPath("remember");

        if (credentials.isMissingNode()) {
            Error error = new Error(Error.MISSING_PARAMETER, "credentials is missing from the request body.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        JsonNode nameNode = credentials.findPath("name");
        JsonNode passwordNode = credentials.findPath("password");

        if (nameNode.isMissingNode() || passwordNode.isMissingNode()) {
            Error error = new Error(Error.MISSING_PARAMETER, "name and/or password is missing from the credentials.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        String name = nameNode.textValue().trim();
        String password = passwordNode.textValue();
        boolean remember = !rememberNode.isMissingNode() && rememberNode.booleanValue();

        if (name.isEmpty() || password.isEmpty()) {
            Error error = new Error(Error.EMPTY_PARAMETER, "the utilisateur name and the password can not be empty.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        QueryResult result1 = Utilisateur.findByName(name);
        if (result1.isError()) {

            Error error = new Error(Error.EMPTY_PARAMETER, "the utilisateur name and the password can not be empty.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));
            return internalServerError(result);
        }

        Utilisateur utilisateur = (Utilisateur) result1;
        if (!utilisateur.isActivated()) {
            Error error = new Error(Error.PERMISSION_DENIED, "the account is either not yet activated, or the account is deactivated");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        String encryptedPassword = Security.generatePBKDF2EncryptedPassword(password, utilisateur.getPasswordSalt());

        if (encryptedPassword.isEmpty()) {
            Error error = new Error(Error.UNEXPECTED, "something went wrong, try again later.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 500);
            result.put("error",  Json.toJson(error));

            return internalServerError(result);
        }

        if (!encryptedPassword.equals(utilisateur.getPassword())) {
            Error error = new Error(Error.INVALID_CREDENTIALS, "invalid credentials.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 401);
            result.put("error",  Json.toJson(error));

            return unauthorized(result);
        }


        return controllers.Token.generate(utilisateur.getId(), request().getHeader("Utilisateur-Agent"), request().remoteAddress(), remember);
    }

    public static Result logout() {

        //Utilisateur utilisateur = (Utilisateur) Http.Context.current().args.get("utilisateur");
        Http.Request request = Http.Context.current().request();

        String token = request.getHeader("X-Auth-Token");
        //boolean loggedOut = Tokens.removeMatchingUserId(utilisateur.getId());
        boolean loggedOut = models.Token.removeMatchingKey(token);
        if (loggedOut) {
            ObjectNode message = Json.newObject();
            message.put("status", 200)
                    .put("uri", request().uri())
                    .put("message",   "user is logged out.");

            return ok(message);

        } else {
            Error error = new Error(Error.UNEXPECTED, "something went wrong, try again later.");
            ObjectNode result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 500);
            result.put("error",  Json.toJson(error));

            return internalServerError(result);
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result activate() {
        JsonNode json = request().body().asJson();
        ObjectNode result = Json.newObject();
        if (json == null) {
            Error error = new Error(Error.INVALID_CONTENT_TYPE, "'Content-Type' header must be set to 'application/json' for this request.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        JsonNode keyNode = json.findPath(ACTIVATION_CODE);

        if (keyNode.isMissingNode()) {
            Error error = new Error(Error.MISSING_PARAMETER, "activation_code is missing from the request body.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        String key = keyNode.textValue().trim();
        if (key.isEmpty()) {
            Error error = new Error(Error.EMPTY_PARAMETER, "the activation code can not be empty.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        QueryResult res =  models.Token.findByKey(key);
        if (res.isError()) {
            Error error = new Error(Error.INVALID_TOKEN, "the activation code is invalid");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 404);
            result.put("error",  Json.toJson(error));

            return notFound(result);
        }

        models.Token token =  (models.Token) res;
        if (token.getType() != models.Token.Type.ACTIVATION) {
            Error error = new Error(Error.INVALID_TOKEN, "the activation code is invalid.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 404);
            result.put("error",  Json.toJson(error));
            return notFound(result);
        }

        QueryResult userResult = Utilisateur.findById(token.getUserId());
        if (userResult.isError()) {
            Error error = new Error(Error.NOT_FOUND, "Utilisateur not Found");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 404);
            result.put("error",  Json.toJson(error));
            return notFound(result);
        }

        Utilisateur utilisateur = (Utilisateur) userResult;

        utilisateur.setActivated(true);
        utilisateur.setUpdatedAt(DateTime.now());

        try {
            Utilisateur.update(utilisateur);
            res = Utilisateur.findByEmail(utilisateur.getEmail());
            if (!res.isError()) {
                utilisateur = (Utilisateur) res;
            }
        }catch (Exception e){
            Error error = new Error(Error.NOT_FOUND, "Utilisateur not Found");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 404);
            result.put("error",  Json.toJson(error));
            return notFound(result);
        }

        if (!models.Token.removeMatchingKey(token.getKey())) {
            Error error = new Error(Error.UNEXPECTED, "something went wrong during the activation process.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 500);
            result.put("error", Json.toJson(error));
            return notFound(result);
        }


        ObjectNode message = Json.newObject();
        message.put("status", 200)
                .put("uri", request().uri())
                .put("utilisateur", Json.toJson(utilisateur));

        return ok(message);
    }

    public static Result initPasswordReset(String email) {
        JsonNode json = request().body().asJson();
        ObjectNode result = Json.newObject();
        if (json == null) {
            Error error = new Error(Error.INVALID_CONTENT_TYPE, "'Content-Type' header must be set to 'application/json' for this request.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        if (email.trim().isEmpty()) {
            Error error = new Error(Error.EMPTY_PARAMETER, "the email parameter can not be empty.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        if(!EmailValidator.getInstance().isValid(email)){
            Error error = new Error(Error.INVALID_EMAIL, "The given email is not valid.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        QueryResult res = Utilisateur.findByEmail(email);
        if (res.isError()) {
            Error error = new Error(Error.NOT_FOUND, "Utilisateur not Found");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 500);
            result.put("error",  Json.toJson(error));
            return notFound(result);
        }

        Utilisateur utilisateur = (Utilisateur) res;

        models.Token token = new models.Token();
        token.setUserId(utilisateur.getId());
        token.setKey(Security.generateTokenKey(models.Token.DEFAULT_KEY_BYTES_LENGTH));
        token.setType(models.Token.Type.PASSWORD_RESET);
        token.setCreatedAt(DateTime.now());
        token.setExpiresAt(token.getCreatedAt().plusYears(1));

        models.Token.save(token);

        ObjectNode response = Json.newObject();
        response.put("status", 200)
                .put("uri", request().uri())
                .put(PASSWORD_RESET_CODE, token.getKey());

        return ok(response);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result resetPassword() {
        JsonNode json = request().body().asJson();
        ObjectNode result = Json.newObject();
        if (json == null) {
            Error error = new Error(Error.INVALID_CONTENT_TYPE, "'Content-Type' header must be set to 'application/json' for this request.");
            result = Json.newObject();
            result.put("uri", "/v1/resetpassword/");
            result.put("status", 400);
            result.put("error",  Json.toJson(error));
            return badRequest(result);
        }

        JsonNode keyNode = json.findPath(PASSWORD_RESET_CODE);
        JsonNode passwordNode = json.findPath("new_password");

        if (keyNode.isMissingNode() || passwordNode.isMissingNode()) {
            Error error = new Error(Error.MISSING_PARAMETER, "password_reset_code and/or new_password is missing from the request body.");
            result = Json.newObject();
            result.put("uri", "/v1/resetpassword/");
            result.put("status", 400);
            result.put("error",  Json.toJson(error));
            return badRequest(result);
        }

        String key = keyNode.textValue().trim();
        String newPassword = passwordNode.textValue();

        if (key.isEmpty() || newPassword.isEmpty()) {
            Error error = new Error(Error.EMPTY_PARAMETER, "the password reset code and the new password can not be empty.");
            result = Json.newObject();
            result.put("uri", "/v1/resetpassword/");
            result.put("status", 400);
            result.put("error",  Json.toJson(error));
            return badRequest(result);
        }

        QueryResult tokenResult = models.Token.findByKey(key);
        if (tokenResult.isError()) {
            Error error = new Error(Error.INVALID_TOKEN, "the password reset code is invalid.");
            result = Json.newObject();
            result.put("uri", "/v1/resetpassword/");
            result.put("status", 400);
            result.put("error",  Json.toJson(error));
            return badRequest(result);
        }

        models.Token token = (models.Token) tokenResult;
        if (token.getType() != models.Token.Type.PASSWORD_RESET) {
            Error error = new Error(Error.INVALID_TOKEN, "the password reset code is invalid.");
            result = Json.newObject();
            result.put("uri", "/v1/resetpassword/");
            result.put("status", 400);
            result.put("error",  Json.toJson(error));
            return badRequest(result);
        }

        QueryResult userResult = Utilisateur.findById(token.getUserId());
        if (userResult.isError()) {
            Error error = new Error(Error.INVALID_TOKEN, "the password reset code is invalid.");
            result = Json.newObject();
            result.put("uri", "/v1/resetpassword/");
            result.put("status", 404);
            result.put("error",  Json.toJson(error));
            return notFound(result);
        }

        if (!models.Token.removeMatchingKey(token.getKey())) {
            Error error = new Error(Error.UNEXPECTED, "something went wrong when discarding the password reset code.");
            result = Json.newObject();
            result.put("uri", "/v1/resetpassword/");
            result.put("status", 404);
            result.put("error",  Json.toJson(error));
            return notFound(result);
        }
        /*

        QueryResult result = changePassword((models.Utilisateur) userResult, newPassword);
        if (result.isError()) {
            return status(409, (new Response(409, request(), result)).toJson());
        }
        */

        return ok("Changed");
    }


    private static Result createPatient(models.Patient patient){
        models.Patient.save(patient);
        return null;
    }

    private static Result createMedecin(models.Medecin medecin){
        models.Medecin.save(medecin);
        return null;
    }

    private static Result createAssureur(models.Assureur assureur){

        return null;
    }


    private static Result signupAdmin(Utilisateur utilisateur) {
        try {
            Utilisateur.save(utilisateur);
        } catch (Exception e){
            Error error = new Error(Error.UNEXPECTED, "something went wrong when discarding the password reset code.");
            ObjectNode result = Json.newObject();
            result.put("uri", "/v1/users/activate");
            result.put("status", 500);
            result.put("error",  Json.toJson(error));
            return internalServerError(result);
        }

        QueryResult registered = Utilisateur.findByEmail(utilisateur.getEmail());

        if (!registered.isError()) {
            models.Token tokenResult = generateActivationToken((Utilisateur) registered);
            if (tokenResult == null) {
                Error error = new Error(Error.UNEXPECTED, "something went wrong when discarding the password reset code.");
                ObjectNode result = Json.newObject();
                result.put("uri", "/v1/users/activate");
                result.put("status", 500);
                result.put("error", Json.toJson(error));
                return internalServerError(result);
            }

            models.Token token = (models.Token) tokenResult;

            ObjectNode node = Json.newObject();
            node.put("status", 201)
                    .put("uri", request().uri())
                    .put(ACTIVATION_CODE, token.getKey())
                    .put("utilisateur", Json.toJson(registered));

            return created(node);
        }
        ObjectNode node = Json.newObject();
        node.put("status", 201)
                .put("uri", request().uri())
                .put("utilisateur", Json.toJson(utilisateur));

        return created(node);
    }


    private static models.Token generateActivationToken(Utilisateur utilisateur) {
        models.Token token = new models.Token();
        token.setUserId(utilisateur.getId());
        token.setKey(Security.generateTokenKey(models.Token.DEFAULT_KEY_BYTES_LENGTH));
        token.setType(models.Token.Type.ACTIVATION);
        token.setCreatedAt(DateTime.now());
        token.setExpiresAt(token.getCreatedAt().plusYears(1));

        models.Token.save(token);
        return token;
    }
}
