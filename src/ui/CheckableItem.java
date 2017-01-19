package ui;

import bean.Member;
import bean.Normal;

public class CheckableItem {
	private String headImgPath;
	private Member user;
	private boolean isSelected;

	public CheckableItem(Member user) {
		headImgPath = "C:/Users/ZS27/Desktop/20k.jpg";
		this.user = user;
		isSelected = false;
	}

	public String getHeadImgPath() {
		return headImgPath;
	}

	public void setHeadImgPath(String headImgPath) {
		this.headImgPath = headImgPath;
	}

	public Member getUser() {
		return user;
	}

	public void setUser(Normal user) {
		this.user = user;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
