package controllers;

import action.Cors;
import play.mvc.*;
import play.data.*;

import models.*;


public class Application extends Controller {
  static Form<Task> taskForm = Form.form(Task.class);

  public static Result preflight(String any) {
    return ok();
  }

  public static Result index() {
    return ok("Bienvenue sur l'API medicale");
  }
  
}
