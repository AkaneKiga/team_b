package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties(ignoreUnknown=true)
public class Shop extends Model {
	
	@Id
	@JsonIgnoreProperties
	public Long id;
	
	@Column(nullable = false)
	@JsonProperty("name")
	public String name;
	
	@Column(nullable = false)
	@JsonProperty("url")
	public String url;
	
	@Column(nullable = false)
	@JsonProperty("id")
	public String shopid;
	
	@Override
	public String toString() {
		return "name=" + name + ",url=" + url + ",shopid=" + shopid;
	}
}
