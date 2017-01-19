package utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.SwingWorker;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.sun.corba.se.impl.orbutil.DenseIntMapImpl;

import bean.Member;
import bean.User;

import sun.org.mozilla.javascript.internal.ast.ForInLoop;

public class HttpUtils {

	public static final int DOGET = 0;
	public static final int DOPOST = 1;

	public interface DefaultCallback {
		void result(int i, boolean bool, String result);
	}

	public static void getUUID(DefaultCallback callback) {
		System.out.println("----------------------getUUID");
		String str_url = "https://login.wx.qq.com/jslogin";

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		BasicNameValuePair param1 = new BasicNameValuePair("appid", "wx782c26e4c19acffb");
		BasicNameValuePair param2 = new BasicNameValuePair("redirect_uri", "https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage");
		BasicNameValuePair param3 = new BasicNameValuePair("fun", "new");
		BasicNameValuePair param4 = new BasicNameValuePair("lang", "zh_CN");
		BasicNameValuePair param5 = new BasicNameValuePair("_", String.valueOf(System.currentTimeMillis()));

		list.add(param1);
		list.add(param2);
		list.add(param3);
		list.add(param4);
		list.add(param5);

		InputStream is = null;
		BufferedReader br = null;
		String result = null;
		try {
			HttpClient client = new SSLClient();
			String param_str = EntityUtils.toString(new UrlEncodedFormEntity(list, Consts.UTF_8));
			HttpGet get = new HttpGet(str_url + "?" + param_str);
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					is = entity.getContent();
					br = new BufferedReader(new InputStreamReader(is, Consts.UTF_8));
					String body = null;
					StringBuilder builder = new StringBuilder();
					while ((body = br.readLine()) != null) {
						builder.append(body);
					}
					result = builder.toString();
					System.out.println(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (result == null) {
			callback.result(0, false, "");
		} else {
			callback.result(0, true, result);
		}

	}

	public static void getImg(DefaultCallback callback, String uuid) {
		System.out.println("----------------------getImg");
		String url = "https://login.weixin.qq.com/qrcode/" + uuid;

		HttpClient client = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		boolean result = false;

		try {
			client = new SSLClient();
			HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					is = entity.getContent();
					bis = new BufferedInputStream(is);
					bos = new BufferedOutputStream(new FileOutputStream(new File(Constants.qrcodeImg)));
					int length = -1;
					byte[] buf = new byte[1024];
					while ((length = bis.read(buf)) > -1) {
						bos.write(buf, 0, length);
					}
					result = true;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.flush();
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		callback.result(1, result, "");
	}

	public static void login(DefaultCallback callback, User user) {
		System.out.println("----------------------login");
		String str_url = "https://login.wx.qq.com/cgi-bin/mmwebwx-bin/login";

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		BasicNameValuePair param1 = new BasicNameValuePair("loginicon", "true");
		BasicNameValuePair param2 = new BasicNameValuePair("uuid", user.getUuid());
		BasicNameValuePair param3 = new BasicNameValuePair("tip", String.valueOf(user.getTip()));
		BasicNameValuePair param4 = new BasicNameValuePair("_", String.valueOf(System.currentTimeMillis()));

		list.add(param1);
		list.add(param2);
		list.add(param3);
		list.add(param4);

		InputStream is = null;
		BufferedReader br = null;
		String result = null;
		try {
			HttpClient client = new SSLClient();
			String param_str = EntityUtils.toString(new UrlEncodedFormEntity(list, Consts.UTF_8));
			HttpGet get = new HttpGet(str_url + "?" + param_str);
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					is = entity.getContent();
					br = new BufferedReader(new InputStreamReader(is, Consts.UTF_8));
					String body = null;
					StringBuilder builder = new StringBuilder();
					while ((body = br.readLine()) != null) {
						builder.append(body);
					}
					result = builder.toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (result == null) {
			callback.result(2, false, "");
		} else {
			callback.result(2, true, result);
		}
	}

	public static void getKey(DefaultCallback callback, User user) {
		System.out.println("----------------------getKey");
		String str_url = user.getUri_redirect() + "&version=v2";

		InputStream is = null;
		BufferedReader br = null;
		String result = null;
		try {
			HttpClient client = new SSLClient();
			HttpGet get = new HttpGet(str_url);
			HttpResponse response = client.execute(get);

			List<Cookie> cookielist = ((AbstractHttpClient) client).getCookieStore().getCookies();
			if (cookielist.size() > 0) {
				user.setCookielist(cookielist);
			}

			for (int i = 0; i < cookielist.size(); i++) {
				if ("webwx_auth_ticket".equals(cookielist.get(i).getName())) {
					user.set_webwx_auth_ticket(cookielist.get(i).getValue());
				} else if ("webwx_data_ticket".equals(cookielist.get(i).getName())) {
					user.set_webwx_data_ticket(cookielist.get(i).getValue());
				}
			}

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					is = entity.getContent();
					br = new BufferedReader(new InputStreamReader(is, Consts.UTF_8));
					String body = null;
					StringBuilder builder = new StringBuilder();
					while ((body = br.readLine()) != null) {
						builder.append(body);
					}
					result = builder.toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (result == null) {
			callback.result(3, false, "");
		} else {
			callback.result(3, true, result);
		}
	}

	public static void initWeChat(DefaultCallback callback, User temp) {
		System.out.println("----------------------initWeChat");
		String str_url = String.format(temp.getUri_base() + "/webwxinit?lang=%s&pass_ticket=%s", "zh_CN", temp.getPass_ticket());

		InputStream is = null;
		BufferedReader br = null;
		String result = null;
		try {
			HttpClient client = new SSLClient();
			HttpPost post = new HttpPost(str_url);

			JSONObject jsonParam = new JSONObject();
			jsonParam.put("BaseRequest", new JSONObject(temp.getBaseRequest()));
			StringEntity body = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题

			post.setHeader("ContentType", "application/json; charset=UTF-8");
			post.setEntity(body);

			HttpResponse response = client.execute(post);

			List<Cookie> cookielist = ((AbstractHttpClient) client).getCookieStore().getCookies();
			if (cookielist.size() > 0) {
				temp.setCookielist(cookielist);
			}

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					is = entity.getContent();
					br = new BufferedReader(new InputStreamReader(is, Consts.UTF_8));
					String str = null;
					StringBuilder builder = new StringBuilder();
					while ((str = br.readLine()) != null) {
						builder.append(str);
					}
					result = builder.toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (result == null) {
			callback.result(4, false, "");
		} else {
			callback.result(4, true, result);
		}

	}

	public static void getContacts(DefaultCallback callback, User temp) {
		System.out.println("----------------------getContacts");
		String str_url = temp.getUri_base() + "/webwxgetcontact";

		List<NameValuePair> list = new ArrayList<NameValuePair>();
		BasicNameValuePair param3 = new BasicNameValuePair("r", String.valueOf(System.currentTimeMillis()));
		BasicNameValuePair param4 = new BasicNameValuePair("seq", String.valueOf(0));
		BasicNameValuePair param5 = new BasicNameValuePair("skey", temp.getSkey());

		list.add(param3);
		list.add(param4);
		list.add(param5);

		InputStream is = null;
		BufferedReader br = null;
		String result = null;
		try {
			HttpClient client = new SSLClient();
			CookieStore cookieStore = ((AbstractHttpClient) client).getCookieStore();
			for (int i = 0; i < temp.getCookielist().size(); i++) {
				cookieStore.addCookie(temp.getCookielist().get(i));
				System.out.println("getName:" + temp.getCookielist().get(i).getName() + "  getValue:" + temp.getCookielist().get(i).getValue());
			}

			String param_str = EntityUtils.toString(new UrlEncodedFormEntity(list, Consts.UTF_8));
			HttpGet get = new HttpGet(str_url + "?" + param_str);
			get.setHeader("Host", "wx2.qq.com");
			get.setHeader("Connection", "keep-alive");
			get.setHeader("Accept", "application/json, text/plain, */*");
			get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
			get.setHeader("Referer", "https://wx2.qq.com/");

			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					is = entity.getContent();
					br = new BufferedReader(new InputStreamReader(is, Consts.UTF_8));
					String body = null;
					StringBuilder builder = new StringBuilder();
					while ((body = br.readLine()) != null) {
						builder.append(body);
					}
					result = builder.toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (result == null) {
			callback.result(5, false, "");
		} else {
			callback.result(5, true, result);
		}
	}

	public static void sendMsg(String text, List<Member> list, DefaultCallback callback) {
		int num = 0;
		User temp = User.getInstance();
		System.out.println("----------------------sendMsg");

		for (int i = 0; i < list.size(); i++) {
			String str_url = String.format(temp.getUri_base() + "/webwxsendmsg?lang=%s&pass_ticket=%s", "zh_CN", temp.getPass_ticket());

			InputStream is = null;
			BufferedReader br = null;
			String result = null;
			try {
				HttpClient client = new SSLClient();
				HttpPost post = new HttpPost(str_url);

				JSONObject jsonParam = new JSONObject();

				JSONObject baseRequest = new JSONObject(temp.getBaseRequest());
				JSONObject msg = new JSONObject();
				msg.put("ClientMsgId", String.valueOf(System.currentTimeMillis()));
				msg.put("LocalID", String.valueOf(System.currentTimeMillis()));
				msg.put("Content", text);
				msg.put("FromUserName", temp.getCurrentUser());
				msg.put("ToUserName", list.get(i).getUserName());
				msg.put("Type", 1);

				jsonParam.put("BaseRequest", baseRequest);
				jsonParam.put("Msg", msg);
				jsonParam.put("Scene", 0);

				StringEntity body = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
				post.setHeader("ContentType", "application/json; charset=UTF-8");
				post.setEntity(body);

				HttpResponse response = client.execute(post);
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						is = entity.getContent();
						br = new BufferedReader(new InputStreamReader(is, Consts.UTF_8));
						String str = null;
						StringBuilder builder = new StringBuilder();
						while ((str = br.readLine()) != null) {
							builder.append(str);
						}
						result = builder.toString();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println(result);
			if (result == null) {
				// 连接失败
			} else {
				// ret判断是否发送成功
			}
		}
	}

	public static void sendMedia(String filePath, List<Member> list, DefaultCallback callback) {
		int num = 0;
		User temp = User.getInstance();
		File file = new File(filePath);
		System.out.println("----------------------sendMedia");

		String mimetype = null;
		String mediatype = null;
		boolean isPic = true;

		if (file.getName().endsWith(".doc")) {
			mimetype = "application/msword";
			mediatype = "doc";
			isPic = false;
		}

		if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg") || file.getName().endsWith(".jpe")) {
			mimetype = "image/jpeg";
			mediatype = "pic";
		}

		if (file.getName().endsWith(".png")) {
			mimetype = "image/png";
			mediatype = "pic";
		}

		JSONObject uploadmediarequest = new JSONObject();
		uploadmediarequest.put("BaseRequest", new JSONObject(temp.getBaseRequest()));
		uploadmediarequest.put("ClientMediaId", System.currentTimeMillis());
		uploadmediarequest.put("StartPos", 0);
		uploadmediarequest.put("DataLen", file.length());
		uploadmediarequest.put("TotalLen", file.length());
		uploadmediarequest.put("FromUserName", temp.getCurrentUser());
		uploadmediarequest.put("ToUserName", list.get(0).getUserName());
		uploadmediarequest.put("UploadType", 2);
		uploadmediarequest.put("MediaType", 4);
		uploadmediarequest.put("FileMd5", Utils.file_md5(file));

		System.out.println(uploadmediarequest.toString());

		String str_url = "https://file.wx2.qq.com/cgi-bin/mmwebwx-bin/webwxuploadmedia?f=json";

		InputStream is = null;
		BufferedReader br = null;
		String result = null;
		try {

			Date fileDate = new Date(file.lastModified());
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss ", Locale.ENGLISH);

			HttpEntity httpEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
					.setContentType(ContentType.MULTIPART_FORM_DATA).setBoundary("----WebKitFormBoundarytIXnZjhq2suKAdTd").setCharset(Consts.UTF_8)
					.addTextBody("id", "WU_FILE_0").addTextBody("name", file.getName()).addTextBody("type", mimetype)
					.addTextBody("lastModifieDate", sdf.format(fileDate) + "GMT+0800 (中国标准时间)").addTextBody("size", String.valueOf(file.length()))
					.addTextBody("mediatype", mediatype).addTextBody("uploadmediarequest", uploadmediarequest.toString())
					.addTextBody("webwx_data_ticket", temp.get_webwx_data_ticket()).addTextBody("pass_ticket", temp.getPass_ticket())
					.addBinaryBody("filename", new FileInputStream(file), ContentType.DEFAULT_BINARY, file.getName()).build();

			HttpClient client = new SSLClient();

			HttpPost post = new HttpPost(str_url);

			post.setHeader("Accept", "*/*");
			post.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
			post.setHeader("Accept-Encoding", "gzip, deflate, br");
			post.setHeader("Connection", "keep-alive");
			post.setHeader("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundarytIXnZjhq2suKAdTd");
			post.setHeader("Host", "file.wx2.qq.com");
			post.setHeader("Referer", "https://wx2.qq.com/");
			post.setHeader("Origin", "https://wx2.qq.com/");
			post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");

			post.setEntity(httpEntity);

			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					is = entity.getContent();
					br = new BufferedReader(new InputStreamReader(is, Consts.UTF_8));
					String str = null;
					StringBuilder builder = new StringBuilder();
					while ((str = br.readLine()) != null) {
						builder.append(str);
					}
					result = builder.toString();
					System.out.println(result);
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getJSONObject("BaseResponse").getInt("Ret") == 0) {
						String MediaId = jsonObject.getString("MediaId");
						System.out.println(MediaId);
						if (isPic) {
							send(list, MediaId);
						} else {
							sendFile(list, MediaId, file);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void sendFile(List<Member> list, String mediaId, File file) {
		User temp = User.getInstance();
		System.out.println("----------------------sendFile");

		for (int i = 0; i < list.size(); i++) {
			String str_url = String.format(temp.getUri_base() + "/webwxsendappmsg?fun=async&f=json&lang=zh_CN&pass_ticket=%s", temp.getPass_ticket());

			InputStream is = null;
			BufferedReader br = null;
			String result = null;
			try {
				HttpClient client = new SSLClient();
				CookieStore cookieStore = ((AbstractHttpClient) client).getCookieStore();
				for (int j = 0; j < temp.getCookielist().size(); j++) {
					cookieStore.addCookie(temp.getCookielist().get(j));
				}

				HttpPost post = new HttpPost(str_url);

				JSONObject jsonParam = new JSONObject();
				JSONObject msg = new JSONObject();

				String format = "<appms appid='wx782c26e4c19acffb' sdkver=''><title>%s</title><des></des><action></action><type>6</type><content></content><url></url><lowurl></lowurl><appattach><totallen>%s</totallen><attachid>%s</attachid><fileext>doc</fileext></appattach><extinfo></extinfo></appmsg>";
				String content = String.format(format, file.getName(), String.valueOf(file.length()), mediaId);

				msg.put("ClientMsgId", String.valueOf(System.currentTimeMillis()));
				msg.put("LocalID", String.valueOf(System.currentTimeMillis()));
				msg.put("Content", "");
				msg.put("FromUserName", temp.getCurrentUser());
				msg.put("ToUserName", list.get(i).getUserName());
				msg.put("Type", 6);

				jsonParam.put("BaseRequest", new JSONObject(temp.getBaseRequest()));
				jsonParam.put("Msg", msg);
				jsonParam.put("Scene", 0);
				System.out.println("***" + jsonParam.toString().replace("\"Content\":\"\"", String.format("\"Content\":\"%s\"", content)));
				StringEntity body = new StringEntity(jsonParam.toString().replace("\"Content\":\"\"", String.format("\"Content\":\"%s\"", content)), "utf-8");
				post.setHeader("Accept", "application/json, text/plain, */*");
				post.setHeader("Accept-Encoding", "gzip, deflate, br");
				post.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
				post.setHeader("Connection", "keep-alive");
				post.setHeader("ContentType", "application/json; charset=UTF-8");
				post.setHeader("Host", "wx2.qq.com");
				post.setHeader("Origin", "https://wx2.qq.com/");
				post.setHeader("Referer", "https://wx2.qq.com/");
				post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
				post.setEntity(body);

				HttpResponse response = client.execute(post);
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						is = entity.getContent();
						br = new BufferedReader(new InputStreamReader(is, Consts.UTF_8));
						String str = null;
						StringBuilder builder = new StringBuilder();
						while ((str = br.readLine()) != null) {
							builder.append(str);
						}
						result = builder.toString();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println(result);
			if (result == null) {
				// 连接失败
			} else {
				// ret判断是否发送成功
			}
		}
	}

	private static void send(List<Member> list, String mediaId) {
		User temp = User.getInstance();
		System.out.println("----------------------send");
		String str_url = String.format(temp.getUri_base() + "/webwxsendmsgimg?fun=async&lang=zh_CN&pass_ticket=%s", temp.getPass_ticket());

		for (int i = 0; i < list.size(); i++) {
			InputStream is = null;
			BufferedReader br = null;
			String result = null;
			try {
				HttpClient client = new SSLClient();
				HttpPost post = new HttpPost(str_url);

				JSONObject jsonParam = new JSONObject();
				jsonParam.put("BaseRequest", new JSONObject(temp.getBaseRequest()));
				JSONObject msg = new JSONObject();
				msg.put("ClientMsgId", String.valueOf(System.currentTimeMillis()));
				String content = "";
				msg.put("Content", content);
				msg.put("FromUserName", temp.getCurrentUser());
				msg.put("LocalID", String.valueOf(System.currentTimeMillis()));
				msg.put("MediaId", mediaId);
				msg.put("ToUserName", list.get(i).getUserName());
				msg.put("Type", 3);
				jsonParam.put("Msg", msg);
				jsonParam.put("Scene", 0);

				StringEntity body = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
				post.setHeader("ContentType", "application/json; charset=UTF-8");
				post.setEntity(body);

				HttpResponse response = client.execute(post);
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						is = entity.getContent();
						br = new BufferedReader(new InputStreamReader(is, Consts.UTF_8));
						String str = null;
						StringBuilder builder = new StringBuilder();
						while ((str = br.readLine()) != null) {
							builder.append(str);
						}
						result = builder.toString();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println(result);
		}
	}

	private static void showCookie(HttpClient client) {
		List<Cookie> cookielist = ((AbstractHttpClient) client).getCookieStore().getCookies();
		System.out.println("cookies:" + cookielist.size());
		for (int i = 0; i < cookielist.size(); i++) {
			System.out.println(cookielist.get(i).getName());
			System.out.println(cookielist.get(i).getValue());
		}
	}

}

class SSLClient extends DefaultHttpClient {
	public SSLClient() throws Exception {
		super();
		SSLContext ctx = SSLContext.getInstance("TLS");
		X509TrustManager tm = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		ctx.init(null, new TrustManager[] { tm }, null);
		SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		ClientConnectionManager ccm = this.getConnectionManager();
		SchemeRegistry sr = ccm.getSchemeRegistry();
		sr.register(new Scheme("https", 443, ssf));
	}
}
