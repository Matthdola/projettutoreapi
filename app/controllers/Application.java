package controllers;

import play.mvc.*;

public class Application extends Controller {

  public static Result preflight(String any) {
    return ok();
  }

  public static Result index() {
    return ok("Bienvenue sur l'API medicale");
  }
  
}
