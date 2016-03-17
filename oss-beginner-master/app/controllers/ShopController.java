package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import play.Logger;
import play.Play;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import models.*;

//@Security.Authenticated(Secured.class)
public class ShopController extends Controller {
	
	@Inject WSClient ws;

    public Result index() {
    	String keyword = request().getQueryString("q");
    	
    	if (keyword == null || keyword.equals("")) {
    		// 初期表示
    		return ok(shop.render(keyword, new ArrayList<Shop>()));
    	}
    	// 検索結果表示
    	return ok(shop.render(keyword, search(keyword)));
    }
    
    public Result create() {
    	String shopid = request().body().asFormUrlEncoded().get("shopid")[0];
    	Logger.debug("shopid" + shopid);
    	Shop shop = find(shopid);
    	shop.save();
    	flash("info", shop.name + "を登録しました");
    	return redirect(routes.ShopController.index());
    }
    
    /**
     * 検索キーワードから店舗検索を行う
     */
	private ArrayList<Shop> search(String keyword) {
		String keyid = Play.application().configuration().getString("gnavi.keyid");
		int timeout = Play.application().configuration().getInt("gnavi.timeout");
    	WSResponse res =  ws.url("http://api.gnavi.co.jp/RestSearchAPI/20150630/")
    			.setQueryParameter("keyid", keyid)
    			.setQueryParameter("name", keyword)
    			.setQueryParameter("format", "json")
    			.get()
    			.get(timeout);
    	String json = res.asJson().findPath("rest").toString();
    	try {
			return new ObjectMapper().readValue(json, new TypeReference<List<Shop>>() {});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
	
	/**
	 * 店舗IDから店舗情報を1件取得する
	 */
	private Shop find(String shopid) {
		String keyid = Play.application().configuration().getString("gnavi.keyid");
		int timeout = Play.application().configuration().getInt("gnavi.timeout");
    	WSResponse res =  ws.url("http://api.gnavi.co.jp/RestSearchAPI/20150630/")
    			.setQueryParameter("keyid", keyid)
    			.setQueryParameter("id", shopid)
    			.setQueryParameter("format", "json")
    			.get()
    			.get(timeout);
    	String json = res.asJson().findPath("rest").toString();
    	try {
    		return new ObjectMapper().readValue(json, new TypeReference<Shop>() {}); 
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }

}
