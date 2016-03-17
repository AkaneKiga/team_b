package controllers;

import models.AppUser;
import play.cache.Cache;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;
import views.html.registration;

public class AuthController extends Controller  {
	/**
	 * ログイン画面表示
	 */
	public Result index() {
    	return ok(login.render(Form.form(AppUser.class)));
    }
	
	/**
	 * ログイン処理
	 */
	public Result login() {
		Form<AppUser> form = Form.form(AppUser.class).bindFromRequest();
		if (form.hasErrors() || form.hasGlobalErrors()) {
			return ok(login.render(form));
		}
		
		AppUser user = form.get();
		if (!user.isAuthenticated()) {
			return redirect(routes.AuthController.index());
		}
		cacheUser(user);
		
		return redirect(routes.ApplicationController.index());
	}
	
	/**
	 * ユーザー登録画面表示
	 */
	public Result register() {
		return ok(registration.render(Form.form(AppUser.class)));
	}
	
	/**
	 * ユーザー登録処理
	 */
	public Result create() {
		Form<AppUser> form = Form.form(AppUser.class).bindFromRequest();
		if (form.hasErrors()) {
			return ok(registration.render(form));
		}
		
		AppUser user = form.get();
		user.save();
		cacheUser(user);

		return redirect(routes.ApplicationController.index());
	}
	
	/**
	 * ログアウト
	 */
	public Result logout() {
		cacheUser(null);
		return redirect(routes.AuthController.index());
	}
	
	private void cacheUser(AppUser user) {
		Cache.set("user", user);
	}
}