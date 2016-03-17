package models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;

import play.data.validation.Constraints;

@Entity
public class AppUser extends Model {
	
	@Id
	public Long id;

	/**
	 * ユーザー名
	 */
	@Column(nullable = false, length = 100, unique = true)
	@Constraints.Required
	@Constraints.MaxLength(100)
	public String username;

	/**
	 * パスワード
	 * ユーザー入力は100文字までだが、SHA-256でハッシュ化するのでカラム長は256
	 */
	@Column(nullable = false, length = 256)
	@Constraints.Required
	@Constraints.MinLength(8)
	@Constraints.MaxLength(100)
	public String password;
	
	/**
	 * ソルト
	 * 暗号化前にパスワードの先頭に付与
	 */
	@Column(nullable = false, columnDefinition = "CHAR(10)")
	public String salt;
	
	/**
	 * 登録済みのユーザーかを判定
	 * @return 登録されている場合True
	 */
	public boolean isAuthenticated() {
		AppUser user = Ebean.find(AppUser.class).where().eq("username", this.username).findUnique();
		if (user == null) return false;
		return hashStr(user.salt + this.password).equals(user.password);
	}
	
	/**
	 * 保存の前に暗号化処理を行う
	 */
	@Override
	public void save() {
		encrypt(); 
		super.save();
	}
	
	/**
	 * 乱数からソルトを生成する
	 * @return
	 */
	private void encrypt() {
		this.salt = String.format("%010d", (int) (Math.random() * 1.e10));
		this.password = hashStr(this.salt + this.password); 
	}
	
	/**
	 * SHA-256で文字列をハッシュ化する
	 * @param str 対象の文字列
	 * @return ハッシュ化された文字列
	 */
	private static String hashStr(String str) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		
		md.update(str.getBytes());
		
		StringBuilder sb = new StringBuilder();
		for (byte m : md.digest()) {
			sb.append(String.format("%02x", m));
		}
		
		return sb.toString();
	}
}
