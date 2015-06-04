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

package plugins.dunning.client;

import plugins.dunning.DUNNING_COMMANDS;
import client.controller.Controller;
import shared.json.JSONObject;
import shared.network.MESSAGE_TYPES;
import shared.system.AbstractController;
import shared.system.Protocol;
import shared.util.ClinSysException;

/**
 * @author torstend
 *
 */
public class DunningProtocol implements Protocol {

	/* (non-Javadoc)
	 * @see util.Protocol#interpretate(org.json.JSONObject, util.AbstractController)
	 */
	@Override
	public void interpret(JSONObject msg, AbstractController c)
			throws ClinSysException {
		DunningController dc = new DunningController();	
		Controller tempController;
		MESSAGE_TYPES type = MESSAGE_TYPES.valueOf(msg.getString("Type"));
		DUNNING_COMMANDS cmd = DUNNING_COMMANDS.valueOf(msg.getString("Command"));
		String receiver = msg.getString("Receiver");
		
		if (c.getPluginName().equals(dc.getPluginName())){
			dc = (DunningController) c;
		}else{
			//error message
		}
		if (receiver.equals(dc.getPluginName())){
			switch(type){
			case ANSWER:switch(cmd){
			case DELETE_DUNNING_TEXT:
				break;
			case DELETE_INVOICE_CONTACT:
				break;
			case GENERATE_PAYMENT_REMINDER:
				dc.renderPaymentReminder(msg);
				break;
			case IMPORT_INVOICE_DATA:
				break;
			case SAVE_DUNNING_TEXT:
				break;
			case SAVE_INVOICE_CONTACT:
				break;
			case SERVE_DATA:
				break;
			case SET_INVOICE_TO_PAID:
				break;
			case SHOW_DUNNING_TEXTS:
				break;
			case SHOW_INVOICE_CONTACTS:
				break;
			case SHOW_INVOICE_DATA:
				break;
			default:
				break;			
			}
				break;
			case NOTIFY:
				break;
			case REQUEST:				
				break;
			default:
				break;				
			}
		}else{
			tempController = dc.getClientController().getController(receiver);
			tempController.getProtocol().interpret(msg, tempController);
		}
	}

}
