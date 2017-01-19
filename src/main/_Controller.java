package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import bean.Group;
import bean.Normal;
import bean.User;

import utility.HttpUtils;
import utility.Utils;

public class _Controller implements HttpUtils.DefaultCallback {

	private User user;
	private cCallback view;

	public _Controller(cCallback callback) {
		this.user = User.getInstance();
		user.setDeviceId("e000000000000000");

		this.view = callback;
	}

	public User getUser() {
		return user;
	}

	public boolean getuuid() {
		new Thread() {
			@Override
			public void run() {
				HttpUtils.getUUID(_Controller.this);
			}
		}.start();
		return false;
	}

	public boolean getImg() {
		final String uuid = user.getUuid();
		new Thread() {
			@Override
			public void run() {
				HttpUtils.getImg(_Controller.this, uuid);
			}
		}.start();
		return false;
	}

	public boolean waitForLogin() {
		final User temp = user;
		new Thread() {
			@Override
			public void run() {
				HttpUtils.login(_Controller.this, temp);
			}
		}.start();
		return false;
	}

	public boolean getKeys() {
		final User temp = user;
		new Thread() {
			@Override
			public void run() {
				HttpUtils.getKey(_Controller.this, temp);
			}
		}.start();
		return false;
	}

	public boolean initWeChat() {
		final User temp = user;
		new Thread() {
			@Override
			public void run() {
				HttpUtils.initWeChat(_Controller.this, temp);
			}
		}.start();
		return false;
	}

	public boolean getContacts() {
		final User temp = user;
		new Thread() {
			@Override
			public void run() {
				HttpUtils.getContacts(_Controller.this, temp);
			}
		}.start();
		return false;
	}

	@Override
	public void result(int i, boolean bool, String result) {
		if (!bool) {
			view.result(i, false, result);
		}
		switch (i) {
		case 0:
			backForGetUUid(i, bool, result);
			break;
		case 1:
			backForGetImg(i, bool, result);
			break;
		case 2:
			backForWaitLogin(i, bool, result);
			break;
		case 3:
			backForKey(i, bool, result);
			break;
		case 4:
			backForInitWechat(i, bool, result);
			break;
		case 5:
			backForContacts(i, bool, result);
			break;
		}
	}

	private void backForContacts(int i, boolean bool, String result) {
		JSONObject jsonObject = new JSONObject(result);
		JSONArray jsonArray = jsonObject.getJSONArray("MemberList");
		System.out.println("MemberList:" + jsonArray.length());
		for (int j = 0; j < jsonArray.length(); j++) {
			JSONObject memeber = jsonArray.getJSONObject(j);
			// if (user["VerifyFlag"]>0 or user["UserName"][0] != '@' or
			// user["UserName"] == _currentuser):
			// del mcontacts_list[index]
			// elif user["UserName"][0:2] == '@@':
			// _group.append(user)
			// elif user["UserName"][0] == '@':
			// _normal.append(user)
			if (memeber.getInt("VerifyFlag") > 0 || memeber.getString("UserName").charAt(0) != '@'
					|| memeber.getString("UserName").equals(user.getCurrentUser())) {
				continue;
			} else if (memeber.getString("UserName").substring(0, 2).equals("@@")) {
				int temp = memeber.getString("NickName").lastIndexOf("<span");
				user.getGrouplList().add(
						new Group(memeber.getString("UserName"), temp > -1 ? memeber.getString("NickName").substring(0, temp) : memeber.getString("NickName"),
								memeber.getString("HeadImgUrl")));
			} else if (memeber.getString("UserName").substring(0, 1).equals("@")) {
				int temp = memeber.getString("NickName").lastIndexOf("<span");
				user.getNormalList().add(
						new Normal(memeber.getString("UserName"), temp > -1 ? memeber.getString("NickName").substring(0, temp) : memeber.getString("NickName"),
								memeber.getString("Alias"), memeber.getString("RemarkName"), memeber.getString("HeadImgUrl"), memeber.getString("Signature"),
								memeber.getString("Province"), memeber.getString("City")));
			}
		}

		System.out.println("group:" + user.getGrouplList().size());
		System.out.println("normal:" + user.getNormalList().size());

		view.result(i, bool, result);
	}

	private void backForInitWechat(int i, boolean bool, String result) {
		JSONObject jsonObject = new JSONObject(result);
		user.setCurrentUser(jsonObject.getJSONObject("User").getString("UserName"));
		System.out.println("getcurrentuser:" + user.getCurrentUser());

		getContacts();
	}

	private void backForKey(int i, boolean bool, String result) {
		System.out.println(result);
		System.out.println("xml pull:");
		Utils.xml_analyse(user, result);
		view.result(i, true, "登陆成功");

		JSONObject obj = new JSONObject();
		obj.put("Uin", user.getWxuin());
		obj.put("Sid", user.getWxsid());
		obj.put("Skey", user.getSkey());
		obj.put("DeviceID", user.getDeviceId());
		user.setBaseRequest(obj.toString());

		initWeChat();
	}

	private void backForWaitLogin(int i, boolean bool, String result) {
		String regexStr = "window.code=(\\d+);";
		Pattern firstP = Pattern.compile(regexStr);
		Matcher firstM = firstP.matcher(result);

		String code = null;
		if (firstM.find()) {
			System.out.println("find");
			code = firstM.group(1);
		}

		if (code != null) {
			if ("200".equals(code)) {
				System.out.println("200");
				String regexStr1 = "window.redirect_uri=\"(\\S+?)\";";
				Pattern firstP1 = Pattern.compile(regexStr1);
				Matcher firstM1 = firstP1.matcher(result);
				if (firstM1.find()) {
					String temp = firstM1.group(1) + "&fun=new";
					System.out.println(temp);
					user.setUri_redirect(temp);
					user.setUri_base(temp.substring(0, temp.lastIndexOf("/")));
				}

				System.out.println(user.getUri_redirect());
				System.out.println(user.getUri_base());

				getKeys();

			} else if ("201".equals(code)) {
				System.out.println("成功扫描,请在手机上点击确认以登录");
				user.setTip(0);
				waitForLogin();
			} else if ("408".equals(code)) {
				System.out.println("超时");
				user.setTip(0);
			} else {
				System.out.println("error");
				user.setTip(0);
			}
		}
		// view.result(i, bool, "");
	}

	private void backForGetImg(int i, boolean bool, String result) {
		if (bool) {
			user.setTip(1);
		}
		view.result(i, bool, "");
	}

	private void backForGetUUid(int i, boolean bool, String result) {
		String regexStr = "window.QRLogin.code = (\\d+); window.QRLogin.uuid = \"(\\S+?)\"";
		Pattern firstP = Pattern.compile(regexStr);
		Matcher firstM = firstP.matcher(result);

		String code = null;
		String uuid = null;
		if (firstM.find()) {
			System.out.println("find");
			code = firstM.group(1);
			uuid = firstM.group(2);
		}

		if ("200".equals(code) && null != uuid) {
			user.setUuid(uuid);
			getImg();
		} else {
			view.result(i, false, "初始化失败");
		}
	}

	public interface cCallback {
		public void result(int i, boolean bool, String result);
	}
}
