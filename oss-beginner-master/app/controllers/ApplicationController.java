package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

@Security.Authenticated(Secured.class)
public class ApplicationController extends Controller {

    public Result index() {
    	return ok(index.render("welcome"));
    }

}
