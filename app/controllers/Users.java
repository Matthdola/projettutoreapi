package controllers;


import action.Cors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.Files;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.joda.time.DateTime;
import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import models.User;
import play.libs.Json;

import mongo.Error;
import security.Security;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Cors
public class Users extends Controller {
    public final static String ACTIVATION_CODE = "activation_code";
    public final static String INVITATION_CODE = "invitation_code";
    public final static String PASSWORD_RESET_CODE = "password_reset_code";

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

        if (mailNode.isMissingNode() || nameNode.isMissingNode() || passwordNode.isMissingNode()) {
            Error error = new Error(Error.MISSING_PARAMETER, "email or name or password is missing in the request body.");

            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        String mail = mailNode.textValue().trim();
        String name = nameNode.textValue().trim();
        String password = passwordNode.textValue();

        if (mail.isEmpty() || name.isEmpty() || password.isEmpty()) {
            Error error = new Error(Error.EMPTY_PARAMETER, "email, name and password can not be empty.");

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

        models.User tempUserResult = User.findByEmail(mail);
        if (tempUserResult != null) {
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

        models.User user = Json.fromJson(json, models.User.class);


        user.setEmail(mail);
        user.setName(name);
        user.setPasswordSalt(passwordSalt);
        user.setPassword(encryptedPassword);
        user.setCreatedAt(DateTime.now());


        return signupAdmin(user);
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
            Error error = new Error(Error.EMPTY_PARAMETER, "the user name and the password can not be empty.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        models.User result1 = models.User.findByName(name);
        if (result1 == null) {

            Error error = new Error(Error.EMPTY_PARAMETER, "the user name and the password can not be empty.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));
            return internalServerError(result);
            /*
            User res = User.findByEmail(name);
            if (res == null) {


                Error error = (Error) result;

                switch (error.getCode()) {
                    case Error.NOT_FOUND:
                        Error err = new Error(Error.INVALID_CREDENTIALS, "invalid credentials.");
                        return unauthorized((new Response(401, request(), err)).toJson());

                    case Error.DATABASE_ACCESS_TIMEOUT:
                        return status(408, (new Response(408, request(), error)).toJson());

                    default:
                        return internalServerError((new Response(500, request(), error)).toJson());
                }

            }
            */
        }

        models.User user = (models.User) result1;
        if (!user.isActivated()) {
            Error error = new Error(Error.PERMISSION_DENIED, "the account is either not yet activated, or the account is deactivated");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 400);
            result.put("error",  Json.toJson(error));

            return badRequest(result);
        }

        String encryptedPassword = Security.generatePBKDF2EncryptedPassword(password, user.getPasswordSalt());

        if (encryptedPassword.isEmpty()) {
            Error error = new Error(Error.UNEXPECTED, "something went wrong, try again later.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 500);
            result.put("error",  Json.toJson(error));

            return internalServerError(result);
        }

        if (!encryptedPassword.equals(user.getPassword())) {
            Error error = new Error(Error.INVALID_CREDENTIALS, "invalid credentials.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 401);
            result.put("error",  Json.toJson(error));

            return unauthorized(result);
        }


        return controllers.Token.generate(user.getId(), request().getHeader("User-Agent"), request().remoteAddress(), remember);
    }

    public static Result logout() {

        models.User user = (models.User) Http.Context.current().args.get("user");
        Http.Request request = Http.Context.current().request();

        String token = request.getHeader("X-Auth-Token");
        //boolean loggedOut = Tokens.removeMatchingUserId(user.getId());
        boolean loggedOut = models.Token.removeMatchingKey(token);
        if (loggedOut) {
            ObjectNode message = Json.newObject();
            message.put("status", 200)
                    .put("uri", request().uri())
                    .put("message", user.getName() + " is logged out.");

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

        models.Token res = models.Token.findByKey(key).get(0);
        if (res == null) {
            Error error = new Error(Error.INVALID_TOKEN, "the activation code is invalid");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 404);
            result.put("error",  Json.toJson(error));

            return notFound(result);
        }

        models.Token token =  res;
        if (token.getType() != models.Token.Type.ACTIVATION) {
            Error error = new Error(Error.INVALID_TOKEN, "the activation code is invalid.");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 404);
            result.put("error",  Json.toJson(error));
            return notFound(result);
        }

        models.User userResult = models.User.findById(token.getUserId());
        if (userResult == null) {
            Error error = new Error(Error.NOT_FOUND, "User not Found");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 404);
            result.put("error",  Json.toJson(error));
            return notFound(result);
        }

        models.User user = (models.User) userResult;

        user.setActivated(true);
        user.setUpdatedAt(DateTime.now());

        try {
            models.User.update(user);
            user = models.User.findByEmail(user.getEmail());
        }catch (Exception e){
            Error error = new Error(Error.NOT_FOUND, "User not Found");
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
                .put("user", Json.toJson(user));

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

        models.User res = models.User.findByEmail(email);
        if (res == null) {
            Error error = new Error(Error.NOT_FOUND, "User not Found");
            result = Json.newObject();
            result.put("uri", request().uri());
            result.put("status", 500);
            result.put("error",  Json.toJson(error));
            return notFound(result);
        }

        models.User user = (models.User) res;

        models.Token token = new models.Token();
        token.setUserId(user.getId());
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

        models.Token tokenResult = models.Token.findByKey(key).get(0);
        if (tokenResult == null) {
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

        models.User userResult = models.User.findById(token.getUserId());
        if (userResult == null) {
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

        QueryResult result = changePassword((models.User) userResult, newPassword);
        if (result.isError()) {
            return status(409, (new Response(409, request(), result)).toJson());
        }
        */

        return ok("Changed");
    }


    private static Result signupAdmin(models.User user) {
        try {
            models.User.save(user);
        } catch (Exception e){
            Error error = new Error(Error.UNEXPECTED, "something went wrong when discarding the password reset code.");
            ObjectNode result = Json.newObject();
            result.put("uri", "/v1/activate/");
            result.put("status", 500);
            result.put("error",  Json.toJson(error));
            return internalServerError(result);
        }

        models.User registered = models.User.findByEmail(user.getEmail());

        models.Token tokenResult = generateActivationToken(registered);
        if (tokenResult == null) {
            Error error = new Error(Error.UNEXPECTED, "something went wrong when discarding the password reset code.");
            ObjectNode result = Json.newObject();
            result.put("uri", "/v1/activate/");
            result.put("status", 500);
            result.put("error",  Json.toJson(error));
            return internalServerError(result);
        }

        models.Token token = tokenResult;

        ObjectNode node = Json.newObject();
        node.put("status", 201)
                .put("uri", request().uri())
                .put(ACTIVATION_CODE, token.getKey())
                .put("user", Json.toJson(registered));

        return created(node);
    }


    private static models.Token generateActivationToken(models.User user) {
        models.Token token = new models.Token();
        token.setUserId(user.getId());
        token.setKey(Security.generateTokenKey(models.Token.DEFAULT_KEY_BYTES_LENGTH));
        token.setType(models.Token.Type.ACTIVATION);
        token.setCreatedAt(DateTime.now());
        token.setExpiresAt(token.getCreatedAt().plusYears(1));

        models.Token.save(token);
        return token;
    }
}
