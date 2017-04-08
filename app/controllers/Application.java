package controllers;

import action.Cors;
import play.mvc.*;
import plugins.MongoDB;

@Cors
public class Application extends Controller {

  public static Result preflight(String any) {
    return ok();
  }

  public static Result index() {
    return ok("Bienvenue sur l'API medicale");
  }

}
