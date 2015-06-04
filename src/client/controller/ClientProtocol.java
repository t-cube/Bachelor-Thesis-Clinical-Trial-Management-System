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

package client.controller;

import client.controller.Controller;
import shared.json.JSONObject;
import shared.system.AbstractController;
import shared.system.Protocol;
import shared.util.Debugger;
import shared.util.ClinSysException;

/**
 * The ClientProtocol class, which implements the Protocol interface, is
 * used as a interpreter/translator for the exchanged network messages. It defines
 * conditions for the messages and what to do if they are fulfilled.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class ClientProtocol implements Protocol 
{
	
	/* Constants ----------------------------------------------------------------*/	
	
	
	/* Variables ----------------------------------------------------------------*/
	
	
	/* Constructor --------------------------------------------------------------*/

	
	/* Getter -------------------------------------------------------------------*/
	
	
	/* Setter -------------------------------------------------------------------*/
	
	
	/* Private ------------------------------------------------------------------*/
	
	
	/* Public -------------------------------------------------------------------*/	
	
	/* (non-Javadoc)
	 * @see shared.system.Protocol#interpretate(org.json.JSONObject, 
	 * shared.system.AbstractController)
	 */
	@Override
	public void interpret(JSONObject msg, AbstractController c) 
				throws ClinSysException 
	{
		// ClientController object, to call the functions that should be executed
		// for a given message
		ClientController cc = null;
		
		// a temporary Controller object, if the receiver of this message is not 
		// the client itself, but a plug-in
		Controller tempController;
		
		// variable to store the receiver (plug-in name)
		String receiver = msg.getString("Receiver");
		
		// if the plug-in name of the given AbstractController object equals
		// "ClientController", then we have been given a ClientController object,
		// which we can use to execute functions depending on the network message
		// else
		// it is not the expected ClientController object, then throw an error
		if (c.getPluginName().equals("ClientController"))
		{
			// cast the given AbstractController into a ClientController object
			cc = (ClientController) c;
		}
		else
		{
			// error message
			throw new ClinSysException("The given AbstractController is not the" + 
									   " expected ClientController object");
		}
		
		// if the message should reach the client itself (that means receiver is 
		// equal to the plug-in-name of the ClientController) then do what must be 
		// done depending on the message
		// else 
		// find the Controller object of the receiver plug-in in the 
		// controller list of the ClientController object and call the interpret
		// method of the associated Protocol object
		if (receiver.equals(cc.getPluginName()))
		{
			/* do something if it has to be done */ 			
		}
		else
		{ 
			Debugger.debug("ClientProtocol - Receiver: " + receiver);
			tempController = cc.getController(receiver);
			tempController.getProtocol().interpret(msg, tempController);
		}
	}

}
