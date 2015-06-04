package testcases;

import java.io.File;

import javax.xml.transform.TransformerConfigurationException;

import shared.util.PDFWriter;

public class test_FOP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			PDFWriter pdf = new PDFWriter();
			String s = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><fo:root  xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"><fo:layout-master-set><fo:simple-page-master master-name=\"A4-portrait\" page-height=\"29.7cm\" page-width=\"21.0cm\"><fo:region-body/></fo:simple-page-master></fo:layout-master-set><fo:page-sequence master-reference=\"A4-portrait\"><fo:flow flow-name=\"xsl-region-body\"><fo:block>Hello, Torsten!</fo:block></fo:flow></fo:page-sequence></fo:root>";
			pdf.writeXSLTFileToPDF(new File("takeda_PaymentReminder_V3.fo"), "PaymentReminder_Takeda_2013_07_09_V3.pdf");
			//pdf.writeXSLTStringToPDF(s, "Test2.pdf");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
