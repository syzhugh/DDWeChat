package utility;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import bean.User;

public class Utils {

	public static String file_md5(File file) {
		String value = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			BigInteger bi = new BigInteger(1, md5.digest());
			value = bi.toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
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
						user.setWxuin(Integer.parseInt(parser.nextText()));
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
