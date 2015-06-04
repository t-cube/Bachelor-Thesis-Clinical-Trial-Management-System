package testcases;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import shared.system.AbstractController;
import shared.util.ClinSysException;
import sun.misc.BASE64Encoder;
import client.controller.ClientController;

public class test_Classes {

	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
			Class<?> c = ClientController.class;
			
//			System.out.println(c.isAssignableFrom(AbstractController.class));
//			System.out.println(AbstractController.class.isAssignableFrom(c));
			
//			try {
//				System.out.println(
//					String.format("%040x", 
//								 new BigInteger(1, 
//										 "test blabla".getBytes("UTF-8"))));
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			String input = "das ist ein Teststring! der sogar Sonderzeichen !)§/(: und Zahlen 123456 enthält";
			String output = "";
			String temp;

			output = new BASE64Encoder().encode(input.getBytes());
			
			System.out.println(output);
			
			System.out.println(input.length() + " : " + output.length());
			
//			for (char chr : temp.toCharArray()){
//				output += Integer.toHexString(chr);
//			}
			

			//output = new BASE64Encoder().encode(output.getBytes("UTF-8"));
			//output = new BASE64Encoder().encode(output.getBytes());
			System.out.println(output);
			
			System.out.println(input.length() + " : " + output.length());
	}

}
