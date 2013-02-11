package bit.scallionchickenrice.leo.entity;

public class ActiveInfo {
	private String activeKey;
	
	private String activeTime;
	
	private String activeType;
	
	private String MACAddress;

	public String getActiveKey() {
		return activeKey;
	}

	public void setActiveKey(String activeKey) {
		this.activeKey = activeKey;
	}

	public String getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(String activeTime) {
		this.activeTime = activeTime;
	}

	public String getActiveType() {
		return activeType;
	}

	public void setActiveType(String activeType) {
		this.activeType = activeType;
	}

	public String getMACAddress() {
		return MACAddress;
	}

	public void setMACAddress(String mACAddress) {
		MACAddress = mACAddress;
	}
}
