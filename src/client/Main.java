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

package client;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import shared.util.FLAGS;
import shared.util.Debugger;
import shared.util.Dialog;
import shared.util.Flag;
import client.controller.ClientController;
import client.controller.ClientProtocol;

/**
 * The main class of the client application, containing the main method and 
 * instantiating the GUI, the ClientController and so on.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class Main 
{
	
	/* Constants ----------------------------------------------------------------*/
	

	/* Variables ----------------------------------------------------------------*/
	
	
	/* Constructor --------------------------------------------------------------*/
	
	
	/* Getter -------------------------------------------------------------------*/
	

	/* Setter -------------------------------------------------------------------*/
		
	
	/* Private ------------------------------------------------------------------*/
	
	
	/* Public -------------------------------------------------------------------*/
	
	/**
	 * The main method for the client application. 
	 * 
	 * @param args Start parameter for setting up the application. 
	 */
	public static void main(String[] args) 
	{
		
		// switch the debug mode to on, while in development. Should be commented 
		// out for the release
		Flag.set(FLAGS.DEBUG);
		
		// try to set the GUI style of the application to the system style
		try 
		{			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (ClassNotFoundException e) 
		{
			Dialog.msgBox("The class to set the GUI look and feel was not found.", 
						  "Error: Look and feel class not found", 
						  Dialog.ERROR_MESSAGE);
		} 
		catch (InstantiationException e) 
		{
			Dialog.msgBox("Cannot instantiate look and feel for the GUI.", 
					  "Error: Look and feel not instantiated", 
					  Dialog.ERROR_MESSAGE);
		} 
		catch (IllegalAccessException e) 
		{
			Dialog.msgBox("UIManager was not correctly accessed for the GUI " + 
						  "look and feel.", 
						  "Error: Look and feel not accessed correctly", 
						  Dialog.ERROR_MESSAGE);
		} 
		catch (UnsupportedLookAndFeelException e) 
		{
			Dialog.msgBox("Selected look and feel not supported.", 
					  "Error: Look and feel not supported", 
					  Dialog.ERROR_MESSAGE);
		}
		
		// try to create the ClientController
		try
		{
			Debugger.debug("Started ClientMain");
			new ClientController();
		}
		catch(Exception e) // if the ClientController could not be created
		{
			// open a confirmation box with the error message and ask if the 
			// stack trace should be shown
			if (Dialog.confirmationBox(e.getMessage() + 
					"\n\nDo you wish to see more details?", 
					"Error: Client was not able to start", 
					Dialog.YES_NO_OPTION, Dialog.ERROR_MESSAGE) 
				== Dialog.YES_OPTION)
			{ // if the box was answered with yes
				String s = ""; 
				// append every stack trace to the string s
				for (StackTraceElement trace: e.getStackTrace())
				{
					s += trace.toString() + "\n";
				}
				// and print it in an information box
				Dialog.msgBox(s, "Details", Dialog.INFORMATION_MESSAGE);
			}	
			
			// exit the application
			System.exit(0);
		}		
	}

}
