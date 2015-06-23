package cn.myzchh.YTGuide.util;

public class Message {
	private int type;//指定是哪种类型
	private String value;//值
	private String name;

	public int getFaceResID() {
		return faceResID;
	}

	public void setFaceResID(int faceResID) {
		this.faceResID = faceResID;
	}

	private int faceResID;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
