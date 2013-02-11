package bit.scallionchickenrice.raph.DAO;

import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class RaphResultSet {
	private int iterator = -1;
	private LinkedList<Map<String, String>> resultSet = new LinkedList<Map<String, String>>();
	
	public void add(Map<String, String> item) {
		resultSet.add(item);
	}
	
	public Map<String, String> get(int i) {
		return resultSet.get(i);
	}
	
	public int size() {
		return resultSet.size();
	}
	
	public boolean next() {
		iterator++;
		
		if(iterator < resultSet.size()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getString(String key) {
		return resultSet.get(iterator).get(key);
	}
	
	public String getString(int key) {
		for(Entry<String,String> entry : resultSet.get(iterator).entrySet()) {
			return entry.getValue();
		}
		return null;
	}
	
	public int getInt(int key) {
		for(Entry<String,String> entry : resultSet.get(iterator).entrySet()) {
			return Integer.parseInt(entry.getValue());
		}
		return 0;
	}
	
	public boolean getBoolean(String key) {
		return (resultSet.get(iterator).get(key).equals("1")) ? true : false;
	}
	
	public int getInt(String key) {
		return Integer.parseInt(resultSet.get(iterator).get(key));
	}
}
