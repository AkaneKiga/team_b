package controllers;

import models.AppUser;
import play.cache.Cache;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

/**
 * ログイン制限を掛けたいアクションに以下のアノテーションを付ける
 * @Security.Authenticated(Secured.class)
 * getUserNameメソッドが何かしらの文字列を返さない場合、onUnauthorizedがコールされる
 */
public class Secured extends Security.Authenticator {

	@Override
	public String getUsername(Context ctx) {
		AppUser user = (AppUser)Cache.get("user");
		if (user == null) {
				return null;
		}
		return user.username;
	}

	@Override
	public Result onUnauthorized(Context ctx) {
		return redirect(routes.AuthController.index());
	}
}
