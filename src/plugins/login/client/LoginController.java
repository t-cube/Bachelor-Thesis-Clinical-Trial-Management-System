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

package plugins.login.client;

import plugins.home.client.view.HomeView;
import plugins.login.LOGIN_COMMANDS;
import plugins.login.client.view.LoginView;
import shared.json.JSONObject;
import shared.network.MESSAGE_TYPES;
import shared.util.ClinSysException;
import client.controller.Controller;
import client.network.Client;
import client.view.InnerView;

/**
 * @author torstend
 *
 */
public class LoginController extends Controller {
	
	public LoginController() {
		super("LoginController");
		this.protocol = new LoginProtocol();
		LoginView lv = new LoginView(this);
		this.views.put(lv.getName(), lv);
	}

	
	public void connect() throws ClinSysException{
		this.getClientController().setUserName(this.getUserName());
		this.getClientController().setPassword(this.getPassword());
		this.getClientController().setClient(new Client(this.getClientController()));
	}
	
	public void requestLogin(long tempId) throws ClinSysException{
		JSONObject json = new JSONObject();
		JSONObject requestData = new JSONObject();
		json.put("Receiver", "LoginController");
		json.put("MessageType", MESSAGE_TYPES.REQUEST);
		json.put("Command", LOGIN_COMMANDS.LOGIN);
		json.put("Sender", this.getPluginName());
		json.put("TemporaryId", tempId);
		

		this.clientController.setUserName(this.clientController.getUserName());
		this.clientController.setPassword(this.clientController.getPassword());
		
		requestData.put("UserName", this.clientController.getUserName());
		requestData.put("Password", this.clientController.getPassword());
		
		json.put("RequestData", requestData);
		
		this.client.sendMsg(json);
	}
	
		
	public void login(JSONObject answer){
		if (answer.getBoolean("Result")){
			// show login message and main view
			this.clientController.setLoggedIn(true);
			this.clientController.setUserId(answer.getInt("UserId"));
			this.clientController.getMainView().pushView(new HomeView());
		}else{
			// show reject message and login view, and reset username and pw
			this.clientController.setUserName(null);
			this.clientController.setPassword(null);
			this.clientController.setLoggedIn(false);
			this.clientController.getMainView().pushView(new InnerView("Reject!", "RejectView"));
			//show error message 
			//printErrorMessage(answer.getString("Error");
		}
	}
	
	public String getUserName(){		
		return ((LoginView)this.views.get("LoginView")).getUserName();
	}
	
	public String getPassword(){
		return ((LoginView)this.views.get("LoginView")).getPassword();
	}


	@Override
	public void afterCreated() {
		// TODO Auto-generated method stub
		
	}
}
