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


package server.controller;

import server.controller.Controller;
import shared.json.JSONObject;
import shared.system.AbstractController;
import shared.system.Protocol;
import shared.util.Debugger;
import shared.util.ClinSysException;

/**
 * The ServerProtocol class interprets/handles incoming messages as well as 
 * redirects it to the Protocol object of a plug-in controller object, if the 
 * message was addressed to a plug-in.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class ServerProtocol implements Protocol 
{
	
	/* (non-Javadoc)
	 * @see shared.system.Protocol#interpretate(org.json.JSONObject, 
	 * 											shared.system.AbstractController)
	 */
	@Override
	public void interpret(JSONObject msg, AbstractController c) 
							throws ClinSysException 
	{
		// reference to the ServerController object
		ServerController sc = null;
		
		// reference to the Controller object of the addressed plug-in if the 
		// message was addressed to a plug-in
		Controller tempController;
		
		// the name of the plug-in that should receive the message
		String receiver = msg.getString("Receiver");
		
		// if the given Controller object is a ServerController object
		if (c.getPluginName().equals("ServerController"))
		{ 
			// cast it to a ServerController object
			sc = (ServerController) c;
		}
		// if not throw an error message, because a protocol only handles the
		// messages for its owning Controller object.
		else 
		{
			// error message
			throw new ClinSysException("The given AbstractController is not " +  
									   "the expected ServerController object");
		}
		
		// if the message was addressed to the ServerControlelr
		if (receiver.equals(sc.getPluginName()))
		{
			// do something if it has to 			
		}
		// it was not addressed to the ServerController but to another plug-in
		else
		{  
			Debugger.debug("ClientProtocol - Receiver: " + receiver);
			// give the message to the called plugin
			tempController = sc.getController(receiver);
			tempController.getProtocol().interpret(msg, tempController);
		}
	}

}
