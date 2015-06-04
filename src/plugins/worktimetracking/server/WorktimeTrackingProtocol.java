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

package plugins.worktimetracking.server;

import plugins.worktimetracking.WORKTIME_FUNCTIONS;
import server.controller.Controller;
import shared.json.JSONException;
import shared.json.JSONObject;
import shared.network.MESSAGE_TYPES;
import shared.network.RECIPIENT;
import shared.system.AbstractController;
import shared.system.Protocol;
import shared.util.ClinSysException;

/**
 * @author torstend
 *
 */
public class WorktimeTrackingProtocol implements Protocol {

	/* (non-Javadoc)
	 * @see util.Protocol#interpretate(org.json.JSONObject, util.AbstractController)
	 */
	@Override
	public void interpret(JSONObject msg, AbstractController c) throws ClinSysException {
		WorktimeTrackingController wtc = new WorktimeTrackingController();	
		Controller tempController;
		MESSAGE_TYPES type = MESSAGE_TYPES.valueOf(msg.getString("Type"));
		WORKTIME_FUNCTIONS cmd = WORKTIME_FUNCTIONS.valueOf(msg.getString("Command"));
		String receiver = msg.getString("Receiver");
		String sender = msg.getString("Sender");
		JSONObject request = msg.getJSONObject("RequestData");
		String executingUser = request.getString("ExecutingUser");
		JSONObject answer = new JSONObject();
				
		if (c.getPluginName().equals(wtc.getPluginName())){
			wtc = (WorktimeTrackingController) c;
		}else{
			//error message
		}
		
		if (receiver.equals(wtc.getPluginName())){
			switch (type){
			case ANSWER:
				break;
			case NOTIFY:
				break;
			case REQUEST:
				request = msg.getJSONObject("RequestData");
				switch (cmd){
				case ADD_DISCOUNT:
					answer.put("Data", wtc.addDiscount(request));
					break;
				case ADD_PRICE:
					answer.put("Data", wtc.addPrice(request));
					break;
				case ADD_WORKTIME:
					answer.put("Data", wtc.addWorkTime(request));
					break;
				case DELETE_DISCOUNT:
					answer.put("Data", wtc.deleteDiscount(request));
					break;
				case DELETE_PRICE:
					answer.put("Data", wtc.deletePrice(request));
					break;
				case DELETE_WORKTIME:
					answer.put("Data", wtc.deleteWorkTime(request));
					break;
				case EDIT_DISCOUNT:
					answer.put("Data", wtc.editDiscount(request));
					break;
				case EDIT_PRICE:
					answer.put("Data", wtc.editPrice(request));
					break;
				case EDIT_WORKTIME:
					answer.put("Data", wtc.editWorkTime(request));
					break;
				case GET_ACTIVITIES:
					answer.put("Data", wtc.getActivities(request));
					break;
				case GET_ALL_DISCOUNT: 
				case GET_USER_DISCOUNT:
				case GET_STUDY_DISCOUNT:
				case GET_USER_STUDY_DISCOUNT:
					answer.put("Data", wtc.getDiscount(request));
					break;
				case GET_ALL_PRICE:
				case GET_USER_PRICE:
					answer.put("Data", wtc.getPrice(request));
					break;
				case GET_ALL_WORKTIME:
				case GET_USER_WORKTIME:
					answer.put("Data", wtc.getWorkTime(request));
					break;
				case GET_All_EMPLOYEES:
				case GET_USER_EMPLOYEES:
					answer.put("Data", wtc.getEmployees(request));
					break;
				case GET_DEPARTMENTS:
					answer.put("Data", wtc.getDepartments(request));
					break;
				case GET_SPONSORS:
					answer.put("Data", wtc.getSponsors(request));
					break;
				case GET_STUDIES:
					answer.put("Data", wtc.getStudies(request));
				default:
					//error message
					break;
				}
				
				answer.put("Sender", receiver);
				answer.put("Receiver", sender);
				answer.put("Type", MESSAGE_TYPES.ANSWER);
				answer.put("Command", cmd);
				answer.put("View", msg.getString("View"));
				wtc.getServer().sendTo(RECIPIENT.CLIENT, executingUser, answer);

				
				break;			
			default:
				//error message
				break;				
			}			
		}else{
			tempController = wtc.getServerController().getController(receiver);
			tempController.getProtocol().interpret(msg, tempController);
		}
	}

}
