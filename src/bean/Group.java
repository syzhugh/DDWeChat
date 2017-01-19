package bean;

import java.util.List;

public class Group extends Member {

	private String headImgUrl;

	public Group(String userName, String nickName) {
		super(userName, nickName);
	}

	public Group(String userName, String nickName, String headImgUrl) {
		super(userName, nickName);
		this.headImgUrl = headImgUrl;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

}
