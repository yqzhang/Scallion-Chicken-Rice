package com.Mickey;

import java.util.List;
import java.util.Map;

import android.app.Application;

public class InfoStore extends  Application{
	private List<Map<String,Object>> lists;
	private Map<String,Object> orderInfo;
	private Map<String,Object> unorderInfo;
	private String cosumptionNo;
	private String userName;
	private String IPAddress;
	private MainPage mainGroup;
	private boolean ifLogin;
	private String deskNo;
	public String getDeskNo() {
		return deskNo;
	}
	public void setDeskNo(String deskNo) {
		this.deskNo = deskNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public boolean isIfLogin() {
		return ifLogin;
	}
	public void setIfLogin(boolean ifLogin) {
		this.ifLogin = ifLogin;
	}
	public MainPage getMainGroup() {
		return mainGroup;
	}
	public void setMainGroup(MainPage mainGroup) {
		this.mainGroup = mainGroup;
	}
	public String getIPAddress() {
		return IPAddress;
	}
	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}
	public String getCosumptionNo() {
		return cosumptionNo;
	}
	public void setCosumptionNo(String cosumptionNo) {
		this.cosumptionNo = cosumptionNo;
	}
	public Map<String, Object> getUnorderInfo() {
		return unorderInfo;
	}
	public void setUnorderInfo(Map<String, Object> unorderInfo) {
		this.unorderInfo = unorderInfo;
	}
	private String[] typeLists;
	private Map<String,Object> discountInfo;
	
	public Map<String, Object> getDiscountInfo() {
		return discountInfo;
	}
	public void setDisAccountInfo(Map<String, Object> discountInfo) {
		this.discountInfo = discountInfo;
	}
	public void setOrderInfo(Map<String,Object> lists) {
		this.orderInfo = lists;
	}
	public Map<String,Object> getOrderInfo() {
		return orderInfo;
	}	
	
	public void setLists(List<Map<String,Object>> lists) {
		this.lists = lists;
	}
	public List<Map<String,Object>> getLists() {
		return lists;
	}
    public void onCreate(){
        super.onCreate();
    }
    public void onTermine(){
        super.onTerminate();
    }
	public void setTypeLists(String[] typeLists) {
		this.typeLists = typeLists;
	}
	public String[] getTypeLists() {
		return typeLists;
	}

}
