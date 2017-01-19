package bean;

public class Normal extends Member {

	private String alias;
	private String remarkName;
	private String headImgUrl;
	private String signature;
	private String province;
	private String city;

	public Normal(String userName, String nickName) {
		super(userName, nickName);
	}

	public Normal(String userName, String nickName, String alias, String remarkName, String headImgUrl, String signature, String province, String city) {
		super(userName, nickName);
		this.alias = alias;
		this.remarkName = remarkName;
		this.headImgUrl = headImgUrl;
		this.signature = signature;
		this.province = province;
		this.city = city;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getRemarkName() {
		return remarkName;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
