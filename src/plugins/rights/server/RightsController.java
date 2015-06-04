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

package plugins.rights.server;

import server.controller.Controller;
import server.model.SQLCreator;
import server.model.enumerations.CONCAT;
import server.model.enumerations.RIGHTS;
import server.model.enumerations.SQLITEMTYPE;
import server.model.enumerations.SQLWORD;
import server.model.enumerations.STATEMENTTYPE;
import shared.json.JSONObject;
import shared.util.ClinSysException;
import shared.util.RecordSet;

/**
 * @author torstend
 *
 */
public class RightsController extends Controller {
	public RightsController(){
		super("RightsController");
		this.protocol = new RightsProtocol();
	}
	
	
	
	private int getUserId(String executingUser, String userName) throws ClinSysException{
		SQLCreator sql1 = new SQLCreator();
		RecordSet result;
		int userId = -1;
		
		sql1.reset();
		sql1.setStatementType(STATEMENTTYPE.SELECT);
		sql1.addItem(new String[]{"id","name"}, SQLITEMTYPE.FIELD);
		sql1.addItem(SQLWORD.FROM);		
			sql1.addItem("ictm_user", SQLITEMTYPE.TABLE);
		sql1.addItem(SQLWORD.WHERE);
		sql1.addFieldItemPair("name", "=", userName, SQLITEMTYPE.TEXT);	
	
		result = this.model.executeSQL(sql1, executingUser, this.getFunctionName(), this.pluginName, RIGHTS.READ.get());
		
		if (result.getRecordCount()!=1){
			
		}else{
			userId = (Integer) result.getValues("ictm_user.id").get(0);
		}
	
		return userId;
	}
	
	private int getUsergroupId(String executingUser, String usergroupName) throws ClinSysException{
		SQLCreator sql1 = new SQLCreator();
		RecordSet result;
		int usergroupId = -1;
		
		sql1.reset();
		sql1.setStatementType(STATEMENTTYPE.SELECT);
		sql1.addItem(new String[]{"id","name"}, SQLITEMTYPE.FIELD);
		sql1.addItem(SQLWORD.FROM);
		sql1.addItem("ictm_usergroups", SQLITEMTYPE.TABLE);
		sql1.addItem(SQLWORD.WHERE);
		sql1.addFieldItemPair("name", "=", usergroupName, SQLITEMTYPE.TEXT);	

		result = this.model.executeSQL(sql1, executingUser, this.getFunctionName(), this.pluginName, RIGHTS.READ.get());
		
		if (result.getRecordCount()!=1){
			
		}else{
			usergroupId = (Integer) result.getValues("ictm_user.id").get(0);
		}
		
		return usergroupId;
	}
	
	private int getFunctionId(String executingUser, String functionName, String pluginName) throws ClinSysException{
		SQLCreator sql1 = new SQLCreator();
		SQLCreator sql2 = new SQLCreator();
		SQLCreator sqlJoin = new SQLCreator();
		RecordSet result;
		int functionId = -1;
		
		sql1.setStatementType(STATEMENTTYPE.SELECT);
		sql1.addItem(new String[]{"id","name"}, SQLITEMTYPE.FIELD);
		sql1.addItem(SQLWORD.FROM);
		sql1.addItem("ictm_plugins", SQLITEMTYPE.TABLE);
		sql1.addItem(SQLWORD.WHERE);
		sql1.addFieldItemPair("name", "=", pluginName, SQLITEMTYPE.TEXT);
		
		sql2.setStatementType(STATEMENTTYPE.SELECT);
		sql2.addItem(new String[]{"id","name, plugin_id"}, SQLITEMTYPE.FIELD);
		sql2.addItem(SQLWORD.FROM);
		sql2.addItem("ictm_plugin_functions", SQLITEMTYPE.TABLE);
		sql2.addItem(SQLWORD.WHERE);
		sql2.addFieldItemPair("name", "=", functionName, SQLITEMTYPE.TEXT);
		
		sqlJoin.setLeftPart(sql1);
		sqlJoin.setRightPart(sql2);
		sqlJoin.setConcat(CONCAT.INNER, "Plugins", "Functions");
		sqlJoin.setOnFields(new String[]{"Plugins.id","Functions.plugin_id"});
		
		result = this.model.executeSQL(sqlJoin, executingUser, this.getFunctionName(), this.pluginName, RIGHTS.READ.get());
		
		if (result.getRecordCount()!=1){
			
		}else{
			functionId = (Integer) result.getValues("Functions.id").get(0);
		}
		
		return functionId;
	}
	
	private int getRights(String executingUser, int userId, int functionId, boolean isGroup) throws ClinSysException{
		SQLCreator sql1 = new SQLCreator();
		RecordSet result;
		int rc;
		
		sql1.setStatementType(STATEMENTTYPE.SELECT);
		if (!isGroup){
			sql1.addItem(new String[]{"user_id",  "plugin_function_id", "level"}, SQLITEMTYPE.FIELD);
			sql1.addItem(SQLWORD.FROM);
			sql1.addItem("ictm_user_rights", SQLITEMTYPE.TABLE);
			sql1.addItem(SQLWORD.WHERE);
			sql1.addFieldItemPair("user_id", "=", userId, SQLITEMTYPE.INT);
			sql1.addItem(SQLWORD.AND);
			sql1.addFieldItemPair("plugin_function_id", "=", functionId, SQLITEMTYPE.INT);
		}else{
			sql1.addItem(new String[]{"usergroups_rights",  "plugin_function_id", "level"}, SQLITEMTYPE.FIELD);
			sql1.addItem(SQLWORD.FROM);
			sql1.addItem("ictm_usergroups_rights", SQLITEMTYPE.TABLE);
			sql1.addItem(SQLWORD.WHERE);
			sql1.addFieldItemPair("usergroup_id", "=", userId, SQLITEMTYPE.INT);
			sql1.addItem(SQLWORD.AND);
			sql1.addFieldItemPair("plugin_function_id", "=", functionId, SQLITEMTYPE.INT);
		}
		

		result = this.model.executeSQL(sql1, executingUser, this.getFunctionName(), this.pluginName, RIGHTS.READ.get());
		
		rc = result.getRecordCount();
		
		if (rc==1){
			return (Integer) result.getValues("level").get(0);
		}else if(rc==0){
			return 0;
		}else{
			return -1;
		}

	}
	
	private void changeRights(String executingUser, String userName2set, String functionName2set, String pluginName2set, int level, boolean isGroup) throws ClinSysException{
		SQLCreator sql1 = new SQLCreator();
		int userId = -1;
		int functionId = -1;
		
		if (!isGroup){
			userId = this.getUserId(executingUser, userName2set);
		}else{
			userId = this.getUsergroupId(executingUser, userName2set);
		}
		
		functionId = this.getFunctionId(executingUser, functionName2set, pluginName2set);
		
		sql1.setStatementType(STATEMENTTYPE.UPDATE);
		if (!isGroup){
			sql1.addItem("ictm_user_rights", SQLITEMTYPE.TABLE);
			sql1.addItem(SQLWORD.SET);
			sql1.addFieldItemPair("level", "=", level, SQLITEMTYPE.INT);
			sql1.addItem(SQLWORD.WHERE);
			sql1.addFieldItemPair("user_id", "=", userId, SQLITEMTYPE.INT);
			sql1.addItem(SQLWORD.AND);
			sql1.addFieldItemPair("plugin_function_id", "=", functionId, SQLITEMTYPE.INT);
		}else{
			sql1.addItem("ictm_usergroups_rights", SQLITEMTYPE.TABLE);
			sql1.addItem(SQLWORD.SET);
			sql1.addFieldItemPair("level", "=", level, SQLITEMTYPE.INT);
			sql1.addItem(SQLWORD.WHERE);
			sql1.addFieldItemPair("usergroup_id", "=", userId, SQLITEMTYPE.INT);
			sql1.addItem(SQLWORD.AND);
			sql1.addFieldItemPair("plugin_function_id", "=", functionId, SQLITEMTYPE.INT);
		}			

		this.model.executeSQL(sql1, executingUser, this.getFunctionName(), this.pluginName, RIGHTS.EDIT.get());
	}
	
	
	
	public void setRights(JSONObject requestData) throws ClinSysException{
		SQLCreator sql1 = new SQLCreator();
		int userId;
		int functionId;
		int right;			
		String executingUser = requestData.getString("UserName");
		String userName2set = requestData.getString("User2Change");
		String functionName2set = requestData.getString("Function2Change");
		String pluginName2set = requestData.getString("Plugin2Change");
		int level = requestData.getInt("Level");
		boolean isGroup = requestData.getBoolean("IsGroup");

		if (!isGroup){
			userId = this.getUserId(executingUser, userName2set);
		}else{
			userId = this.getUsergroupId(executingUser, userName2set);
		}
		
		functionId = this.getFunctionId(executingUser, functionName2set, pluginName2set);
		
		right = this.getRights(executingUser, userId, functionId, isGroup);
		
		if (right>0){ // a right level exists
			if (!RIGHTS.hasRight(right, level)){ // it includes not the wanted right
				this.changeRights(executingUser, userName2set, functionName2set, pluginName2set, RIGHTS.mergeRights(right, level), isGroup);
			}
		}else if (right==0) { // no right exists			
			sql1.setStatementType(STATEMENTTYPE.INSERT);
			if (!isGroup){
				sql1.addItem("ictm_user_rights", SQLITEMTYPE.TABLE);
				sql1.addItem(SQLWORD.OPENINGBRACKET);
				sql1.addItem(new String[]{"user_id", "plugin_function_id", "level"}, SQLITEMTYPE.FIELD);
				sql1.addItem(SQLWORD.CLOSINGBRACKET);
			}else{
				sql1.addItem("ictm_usergroups_rights", SQLITEMTYPE.TABLE);
				sql1.addItem(SQLWORD.OPENINGBRACKET);
				sql1.addItem(new String[]{"usergroup_id", "plugin_function_id", "level"}, SQLITEMTYPE.FIELD);
				sql1.addItem(SQLWORD.CLOSINGBRACKET);
			}
			sql1.addItem(SQLWORD.VALUES);
			sql1.addItem(SQLWORD.OPENINGBRACKET);
			sql1.addItem(new int[]{userId, functionId, level}, SQLITEMTYPE.INT);
			sql1.addItem(SQLWORD.CLOSINGBRACKET);			

			this.model.executeSQL(sql1, executingUser, this.getFunctionName(), this.pluginName, RIGHTS.CREATE.get());
		}else{ // we found too much rights!
			
		}
	}
			
	public void unsetRights(JSONObject requestData) throws ClinSysException{
		SQLCreator sql1 = new SQLCreator();
		int userId = -1;
		int functionId = -1;
		int right;
		String executingUser = requestData.getString("UserName");
		String userName2set = requestData.getString("User2Change");
		String functionName2set = requestData.getString("Function2Change");
		String pluginName2set = requestData.getString("Plugin2Change");
		int level = requestData.getInt("Level");
		boolean isGroup = requestData.getBoolean("IsGroup");
		
		if (!isGroup){
			userId = this.getUserId(executingUser, userName2set);
		}else{
			userId = this.getUsergroupId(executingUser, userName2set);
		}
		
		functionId = this.getFunctionId(executingUser, functionName2set, pluginName2set);
		
		right = this.getRights(executingUser, userId, functionId, isGroup);
		
		if (right>=0){ // there are one or no right level but not too much
			right = RIGHTS.extractRights(right, level);
			
			if (right>0){	// after unsetting the right level ther is still a right level 	
				this.changeRights(executingUser, userName2set, functionName2set, pluginName2set, right, isGroup);
			}else{ // there is no right anymore delete the row
				sql1.setStatementType(STATEMENTTYPE.DELETE);
				if (!isGroup){
					sql1.addItem(SQLWORD.FROM);
					sql1.addItem("ictm_user_rights", SQLITEMTYPE.TABLE);
					sql1.addItem(SQLWORD.WHERE);
					sql1.addFieldItemPair("user_id", "=", userId, SQLITEMTYPE.INT);
					sql1.addItem(SQLWORD.AND);
					sql1.addFieldItemPair("plugin_function_id", "=", functionId, SQLITEMTYPE.INT);
				}else{
					sql1.addItem(SQLWORD.FROM);
					sql1.addItem("ictm_usergroups_rights", SQLITEMTYPE.TABLE);
					sql1.addItem(SQLWORD.WHERE);
					sql1.addFieldItemPair("usergroup_id", "=", userId, SQLITEMTYPE.INT);
					sql1.addItem(SQLWORD.AND);
					sql1.addFieldItemPair("plugin_function_id", "=", functionId, SQLITEMTYPE.INT);
				}	
				
				this.model.executeSQL(sql1, executingUser, this.getFunctionName(), this.pluginName, RIGHTS.DELETE.get());
			}
		}else{ // we found too much rights
			
		}		
	}



	@Override
	public void afterCreated() {
		// TODO Auto-generated method stub
		
	}
}
