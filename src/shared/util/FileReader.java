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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * This class opens and loads a file and then allows to read it line by line or 
 * in complete. Furthermore it has a method to close the file again.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class FileReader 
{
	
	/* Constants ----------------------------------------------------------------*/
	
	
	/* Variables ----------------------------------------------------------------*/	
	
	// stores the path to the file we want to read
	private String filePath = "";
	
	// FileInputStream, DataInputStream, BufferedReader to read out the file. 
	private FileInputStream fis = null;
	private DataInputStream dis = null;
	private BufferedReader br = null;

	
	/* Constructor --------------------------------------------------------------*/
	
	/**
	 * Constructor, which tries to open the file located under the given file path.
	 * 
	 * @param filePath The path to the file, which should be opened.
	 */
	public FileReader(String filePath)
	{
		// set the file path
		this.filePath = filePath;
		
		// try to open the file and data input stream as well as the buffered
		// reader to allow reading the file
		try 
		{
			this.fis = new FileInputStream(this.filePath);
			this.dis = new DataInputStream(this.fis);
			this.br = new BufferedReader(new InputStreamReader(this.dis));
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			Debugger.debug("The file: " + this.filePath + " was not found! " +
							   "Please check the file path!");
			e.printStackTrace();
		}
	}	

	
	/* Getter -------------------------------------------------------------------*/
	

	/* Setter -------------------------------------------------------------------*/
		
	
	/* Private ------------------------------------------------------------------*/
	
	
	/* Public -------------------------------------------------------------------*/
	
	/**
	 * Tries to close the file by closing the Reader and Streams working with the 
	 * file
	 */
	public void close()
	{
		// try to close BufferedReader as well as Data- and FileInputStream
		try 
		{
			this.br.close();
			this.dis.close();
			this.fis.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			Debugger.debug("Could not close either BufferedReader, " +
							"DataInputStream or FileInputStream");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Tries to read out the next line of the file, as long as there is a line.
	 * @return The content of the next line that was read.
	 */
	public String readNextLine()
	{
		// try to read the next line
		try 
		{
			return this.br.readLine();
		} 
		catch (IOException e) 
		{
			Debugger.debug("Could not read the line");
		}
		// only if an error occurs
		return null;
	}
	
	/**
	 * Tries to read the complete file. 
	 * @return An array containing each line of the file.
	 */
	public String[] readFile()
	{
		// open the list to store the result of each read line.
		ArrayList<String> file = new ArrayList<String>();
		
		// temporary stores the content of a line
		String line = "";
		
		// while there is still a line
		while ((line = this.readNextLine()) != null)
		{
			// add the read line to the list of lines
			file.add(line);
		}
		
		// return the list transformed into a string array
		return (String[]) file.toArray();
	}
}
