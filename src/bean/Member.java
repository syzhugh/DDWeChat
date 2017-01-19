package bean;

public class Member {
	private String userName;
	private String nickName;

	public Member(String userName, String nickName) {
		super();
		this.userName = userName;
		this.nickName = nickName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}
