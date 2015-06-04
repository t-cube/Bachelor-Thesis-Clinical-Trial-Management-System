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

package plugins.contacts.server;

import plugins.contacts.CONTACTS_COMMANDS;
import server.controller.Controller;
import shared.json.JSONObject;
import shared.network.MESSAGE_TYPES;
import shared.system.AbstractController;
import shared.system.Protocol;
import shared.util.ClinSysException;

/**
 * The server side protocol of the contacts plug-in. Interpreting incoming 
 * requests. 
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class ContactsProtocol implements Protocol 
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
	 * 							shared.system.AbstractController)
	 */
	@Override
	public void interpret(JSONObject msg, AbstractController c) 
				throws ClinSysException 
	{
		ContactsController cc = new ContactsController();	
		Controller tempController;
		MESSAGE_TYPES type = MESSAGE_TYPES.valueOf(msg.getString("Type"));
		CONTACTS_COMMANDS cmd = CONTACTS_COMMANDS.valueOf(
														msg.getString("Command"));
		String receiver = msg.getString("Receiver");
		String sender = msg.getString("Sender");
		String executingUser = msg.getString("UserName");
		JSONObject answer = new JSONObject();
		
		if (c.getPluginName().equals(cc.getPluginName()))
		{
			cc = (ContactsController) c;
		}
		else
		{
			//error message
		}
		
		if (receiver.equals(cc.getPluginName()))
		{
			switch(type){
			case ANSWER:
				break;
			case NOTIFY:
				break;
			case REQUEST:
				switch(cmd){
				case CANCEL_CONTACT:
					break;
				case EDIT_CONTACT:
					break;
				case SAVE_CONTACT:
					cc.saveContact(msg);
					break;
				case SHOW_CONTACTS:
					cc.getContactsData(msg);
					break;
				default:
					break;				
				}
				break;
			default:
				break;
				
			}
		}
		else
		{
			tempController = cc.getServerController().getController(receiver);
			tempController.getProtocol().interpret(msg, tempController);
		}
	}

}
