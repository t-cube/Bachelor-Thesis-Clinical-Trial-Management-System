/* 
 * Copyright (C) 2013 Torsten Dietl <Torsten.Dietl@gmx.de>
 *
 * This source code is released under the Microsoft Reference Source License 
 * (MS-RSL).
 *
 * MICROSOFT REFERENCE SOURCE LICENSE (MS-RSL)
 *
 * This license governs use of the accompanying software. 
 * If you use the software, you accept this license. 
 * If you do not accept the license, do not use the software.
 *
 *
 * 1. Definitions
 *
 * The terms "reproduce," "reproduction," and "distribution" have the same 
 * meaning here as under U.S. copyright law.
 * "You" means the licensee of the software.
 * "Your company" means the company you worked for when you downloaded the 
 * software.
 * "Reference use" means use of the software within your company as a reference,
 * in read only form, for the sole purposes of debugging your products, 
 * maintaining your products, or enhancing the interoperability of your products
 * with the software, and specifically excludes the right to distribute the 
 * software outside of your company.
 * "Licensed patents" means any Licensor patent claims which read directly on 
 * the software as distributed by the Licensor under this license.
 * 
 *
 * 2. Grant of Rights
 * 
 * (A) Copyright Grant- Subject to the terms of this license, the Licensor 
 * grants you a non-transferable, non-exclusive, worldwide, royalty-free 
 * copyright license to reproduce the software for reference use.
 * (B) Patent Grant- Subject to the terms of this license, the Licensor grants 
 * you a non-transferable, non-exclusive, worldwide, royalty-free patent license
 * under licensed patents for reference use.
 * 
 *
 * 3. Limitations
 *
 * (A) No Trademark License- This license does not grant you any rights to use 
 * the Licensor’s name, logo, or trademarks.
 * (B) If you begin patent litigation against the Licensor over patents that you
 * think may apply to the software (including a cross-claim or counterclaim in a
 * lawsuit), your license to the software ends automatically.
 * (C) The software is licensed "as-is." You bear the risk of using it. 
 * The Licensor gives no express warranties, guarantees or conditions. You may 
 * have additional consumer rights under your local laws which this license 
 * cannot change. To the extent permitted under your local laws, the Licensor 
 * excludes the implied warranties of merchantability, fitness for a particular 
 * purpose and non-infringement. 
 *
 */


package shared.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

/**
 * The class allows to generate a pdf by an XSLT file or an string containing
 * XSLT statements.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class PDFWriter 
{
	
	/* Constants ----------------------------------------------------------------*/
	
	
	/* Variables ----------------------------------------------------------------*/	
	
	// needed variables, i don't really know what they are doing but somehow they
	// transforming the xslt into a pdf.
	private static FopFactory fopFactory = null;
	private static TransformerFactory transformerFactory = null;
	private static Transformer transformer = null;
	
	// output stream to write the pdf file
	private OutputStream out = null;
	
	// the Fop object, which i think generates the pdf
	private Fop fop = null;
	
	// the result either of the pdf or of the read xslt...
	private Result res = null;
	
	/* Constructor --------------------------------------------------------------*/
	
	// set the references to the objects needed for generating a pdf
	// and set some configurations
	public PDFWriter() throws TransformerConfigurationException, FOPException
	{
		if (PDFWriter.fopFactory == null)
		{
			PDFWriter.fopFactory = FopFactory.newInstance();
		}
		if (PDFWriter.transformerFactory == null)
		{
			PDFWriter.transformerFactory = TransformerFactory.newInstance();
		}
		if (PDFWriter.transformer == null)
		{
			PDFWriter.transformer = PDFWriter.transformerFactory.newTransformer();
		}
		
		DefaultConfiguration cfg = new DefaultConfiguration("fop");
	       
        DefaultConfiguration renderers = new DefaultConfiguration("renderers");
        cfg.addChild(renderers);
       
        DefaultConfiguration renderer = new DefaultConfiguration("renderer");
        renderer.addAttribute("mime", "application/pdf");
        renderers.addChild(renderer);
       
        DefaultConfiguration fonts = new DefaultConfiguration("fonts");
        renderer.addChild(fonts);
       
        DefaultConfiguration autoDetect = new DefaultConfiguration("auto-detect");
        fonts.addChild(autoDetect);
		
		PDFWriter.fopFactory.setUserConfig(cfg);
	}
	
	/* Getter -------------------------------------------------------------------*/
	

	/* Setter -------------------------------------------------------------------*/
		
	
	/* Private ------------------------------------------------------------------*/
	
	/**
	 * Write somehow the pdf file by transforming xslt statements.
	 * 
	 * @param src A Source object containing the xslt.
	 * @param output Name for the pdf file.
	 */
	private void writeXSLTToPDF(Source src, String output)
	{
		// add .pdf to the filename if it's missing
		String[] outputSplit = output.split("\\."); 		
		
		if (!outputSplit[outputSplit.length-1].equalsIgnoreCase("pdf"))
		{
			output += ".pdf";
		}
		
		
		// try to open a BufferedOutputStream to write the pdf file.
		try 
		{
			this.out = new BufferedOutputStream(new FileOutputStream(
														new File(output)));
		} 
		catch (FileNotFoundException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// try to somehow transform the xslt into pdf and write it with the 
		// output stream
		try 
		{			
			this.fop = PDFWriter.fopFactory.newFop(MimeConstants.MIME_PDF, 
													this.out);
			
			this.res = new SAXResult(this.fop.getDefaultHandler());
			
			if ((src != null)&&(this.res != null))
			{
				PDFWriter.transformer.transform(src, this.res);
			}			
		} 
		catch (TransformerConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (FOPException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (TransformerException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally 
		{
			// close the stream if it was opened
			try 
			{
				this.out.close();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	/* Public -------------------------------------------------------------------*/
	
	/**
	 * Writes a pdf file using a given string containing xslt-statements.
	 * 
	 * @param input The xslt-statements, which should be transformed into pdf.
	 * @param output The file name for the pdf.
	 */
	public void writeXSLTStringToPDF(String input, String output)
	{
		this.writeXSLTToPDF(new StreamSource(new StringReader(input)), output);
	}
	
	/**
	 * Writes a pdf file using a given file containing xslt-statements.
	 * 	 * 
	 * @param input The file containing the xslt-statements.
	 * @param output The file name for the pdf.
	 */
	public void writeXSLTFileToPDF(File input, String output)
	{
		this.writeXSLTToPDF(new StreamSource(input), output);		
	}
}
