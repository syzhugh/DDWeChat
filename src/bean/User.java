package bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.cookie.Cookie;


import utility.HttpUtils;
import utility.HttpUtils.DefaultCallback;

public class User {

	// error time
	private int num = 0;
	// init
	private String uuid, deviceId;

	// wait
	private int tip = -1;
	private String uri_base, uri_redirect;

	// login
	private String currentUser;
	private String skey, wxsid, wxuin, pass_ticket, baseRequest;

	// getContacts
	private List<Normal> normalList;
	private List<Group> grouplList;

	// send
	private List<Cookie> cookielist;
	private String _webwx_data_ticket;
	private String _webwx_auth_ticket;

	public User() {
		normalList = new ArrayList<Normal>();
		grouplList = new ArrayList<Group>();
	}

	public List<Cookie> getCookielist() {
		return cookielist;
	}

	public void setCookielist(List<Cookie> cookielist) {
		this.cookielist = cookielist;
	}

	public void init(String uuid, String deviceid) {
		this.uuid = uuid;
		this.deviceId = deviceid;
	}

	public void sendMsg() {

	}

	public void sendPic() {

	}

	public void addUser() {

	}

	public void rename() {

	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getTip() {
		return tip;
	}

	public void setTip(int tip) {
		this.tip = tip;
	}

	public String getUri_base() {
		return uri_base;
	}

	public void setUri_base(String uri_base) {
		this.uri_base = uri_base;
	}

	public String getUri_redirect() {
		return uri_redirect;
	}

	public void setUri_redirect(String uri_redirect) {
		this.uri_redirect = uri_redirect;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public String getSkey() {
		return skey;
	}

	public void setSkey(String skey) {
		this.skey = skey;
	}

	public String getWxsid() {
		return wxsid;
	}

	public void setWxsid(String wxsid) {
		this.wxsid = wxsid;
	}

	public String getWxuin() {
		return wxuin;
	}

	public void setWxuin(String wxuin) {
		this.wxuin = wxuin;
	}

	public String getPass_ticket() {
		return pass_ticket;
	}

	public void setPass_ticket(String pass_ticket) {
		this.pass_ticket = pass_ticket;
	}

	public String getBaseRequest() {
		return baseRequest;
	}

	public void setBaseRequest(String baseRequest) {
		this.baseRequest = baseRequest;
	}

	public List<Normal> getNormalList() {
		return normalList;
	}

	public void setNormalList(List<Normal> normalList) {
		this.normalList = normalList;
	}

	public List<Group> getGrouplList() {
		return grouplList;
	}

	public void setGrouplList(List<Group> grouplList) {
		this.grouplList = grouplList;
	}

	public String get_webwx_data_ticket() {
		return _webwx_data_ticket;
	}

	public void set_webwx_data_ticket(String _webwx_data_ticket) {
		this._webwx_data_ticket = _webwx_data_ticket;
	}

	public String get_webwx_auth_ticket() {
		return _webwx_auth_ticket;
	}

	public void set_webwx_auth_ticket(String _webwx_auth_ticket) {
		this._webwx_auth_ticket = _webwx_auth_ticket;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
