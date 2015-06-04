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

package plugins.login.server;

import plugins.base.BASE_TABLES;
import plugins.login.LOGIN_COMMANDS;
import server.controller.Controller;
import server.model.SQLCreator;
import server.model.enumerations.RIGHTS;
import server.model.enumerations.SQLITEMTYPE;
import server.model.enumerations.SQLWORD;
import server.model.enumerations.STATEMENTTYPE;
import shared.util.ClinSysException;
import shared.util.RecordSet;

/**
 * @author torstend
 *
 */
public class LoginController extends Controller {

	public LoginController() {
		super("LoginController");
		this.protocol = new LoginProtocol();
	}
	
	
	public int login(String userName, String password) throws ClinSysException{
		SQLCreator sql = new SQLCreator();
		RecordSet result;
		
		try{
			sql.setStatementType(STATEMENTTYPE.SELECT);
			sql.addItem("id, name, password", SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.FROM);
			sql.addItem(BASE_TABLES.TBL_USER.get(), SQLITEMTYPE.TABLE);
			sql.addItem(SQLWORD.WHERE);
			sql.addFieldItemPair("name", "=", userName, SQLITEMTYPE.TEXT);
			sql.addItem(SQLWORD.AND);
			sql.addFieldItemPair("password", "=", password, SQLITEMTYPE.TEXT);
			
			result = this.model.executeSQL(sql, userName, LOGIN_COMMANDS.LOGIN.get(), this.getPluginName(), RIGHTS.READ.get());
			
			if (result.getRecordCount()==1){
				return (Integer) result.getValues("id").get(0); 
			}
		}catch (ClinSysException e){
			throw new ClinSysException("((Server) LoginController.login(String userName, String password) : Could not execute the SQL to log the user in.");
		}
		return -1;
	}


	@Override
	public void afterCreated() {
		// TODO Auto-generated method stub
		
	}
	
}
