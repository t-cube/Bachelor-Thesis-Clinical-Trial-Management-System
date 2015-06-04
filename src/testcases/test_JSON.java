package testcases;

import shared.json.JSONObject;

public class test_JSON {
	

	public static void main(String[] args) {
		JSONObject jsonObj = new JSONObject();
		Integer i = null;		
		
		jsonObj.put("test", i);
		System.out.println(jsonObj.isNull("test"));
		
		//System.out.println(jsonObj.getInt("test"));
	}
	
}
