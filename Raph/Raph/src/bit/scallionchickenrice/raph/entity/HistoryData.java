package bit.scallionchickenrice.raph.entity;

public class HistoryData {
	private String orderId;
	
	private String HistoryTime;
	
	private String HistoryPrice;
	
	private String TableNumber;
	
	private String HistoryDiscount;
	
	private boolean ifPaid;

	public String getHistoryTime() {
		return HistoryTime;
	}

	public void setHistoryTime(String historyTime) {
		HistoryTime = historyTime;
	}

	public String getHistoryPrice() {
		return HistoryPrice;
	}

	public void setHistoryPrice(String historyPrice) {
		HistoryPrice = historyPrice;
	}

	public String getTableNumber() {
		return TableNumber;
	}

	public void setTableNumber(String tableNumber) {
		TableNumber = tableNumber;
	}

	public String getHistoryDiscount() {
		return HistoryDiscount;
	}

	public void setHistoryDiscount(String historyDiscount) {
		HistoryDiscount = historyDiscount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public boolean isIfPaid() {
		return ifPaid;
	}

	public void setIfPaid(boolean ifPaid) {
		this.ifPaid = ifPaid;
	}
}
