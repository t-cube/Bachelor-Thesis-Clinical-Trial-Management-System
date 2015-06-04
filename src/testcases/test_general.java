package testcases;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import server.model.enumerations.RIGHTS;


public class test_general {
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<String>> test = new ArrayList<ArrayList<String>> ();
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<String> temp2;
		temp.add("test");
		test.add(temp);
		
		temp2 = test.get(0);
		temp2.add("test2");
		
		System.out.println(test.get(0).get(0));
		System.out.println(test.get(0).get(1));
		
		
		
		Object testObj = new Integer(1);
		System.out.println(testObj.getClass());
		
		InputStream in = new ByteArrayInputStream(new byte[]{1,2});
		
		
		System.out.println(in.getClass().toString());
		
		ArrayList<?> testList = new ArrayList<Integer>();
		ArrayList<Integer> testList2 = new ArrayList<Integer>();
		System.out.println(testList==testList2);
		System.out.println(RIGHTS.values()[0]);
		System.out.println(RIGHTS.values()[1]);
		System.out.println(RIGHTS.values()[2]);
		System.out.println(RIGHTS.values()[3]);
		
		File  f = new File("C:\\");
		
		for (String file : f.list()){
			System.out.println(file);
		}
		
		System.out.println(System.getProperty("user.dir"));
		
		Date d1 = new Date();
		
		System.out.println(d1.getTime() + " => " + new SimpleDateFormat("d.M.y").format(d1));
		
		Date d2 = new Date(d1.getTime());
		
		System.out.println(d2.getTime() + " => " + new SimpleDateFormat("d.M.y").format(d2));
	}

}
