package utility;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import bean.User;


public class Utils {

	public static String file_md5() {

		return null;
	}

	public static int file_size() {
		return -1;
	}

	public static boolean xml_analyse(User user, String str) {
		try {
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(new ByteArrayInputStream(str.getBytes("utf-8")), "utf-8");
			int eventCode = parser.getEventType();
			while (eventCode != XmlPullParser.END_DOCUMENT) {
				switch (eventCode) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if ("skey".equals(parser.getName())) {
						user.setSkey(parser.nextText());
					} else if ("wxsid".equals(parser.getName())) {
						user.setWxsid(parser.nextText());
					} else if ("wxuin".equals(parser.getName())) {
						user.setWxuin(parser.nextText());
					} else if ("pass_ticket".equals(parser.getName())) {
						user.setPass_ticket(parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				eventCode = parser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
