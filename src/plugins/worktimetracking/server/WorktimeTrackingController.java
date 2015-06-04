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

import java.sql.Date;

import plugins.worktimetracking.WORKTIME_FUNCTIONS;
import server.controller.Controller;
import server.model.SQLCreator;
import server.model.enumerations.CONCAT;
import server.model.enumerations.RIGHTS;
import server.model.enumerations.SQLITEMTYPE;
import server.model.enumerations.SQLWORD;
import server.model.enumerations.STATEMENTTYPE;
import shared.json.JSONArray;
import shared.json.JSONObject;
import shared.util.ClinSysException;
import shared.util.RecordSet;

/**
 * @author torstend
 *
 */
public class WorktimeTrackingController extends Controller {

	public WorktimeTrackingController() {
		super("WorktimeTrackingController");
		this.protocol = new WorktimeTrackingProtocol();
	}
	
	/* Private -------------------------------------------------------------- */
	
	
	
	/* Worktime -------------------------------------------------------------- */
	
	public JSONObject getActivities(JSONObject request) throws ClinSysException{
		SQLCreator sql = new SQLCreator();
		String executingUser = request.getString("ExecutingUser");
		int departmentId = -1;
		int groupId = -1;
		String activityName = null;
		JSONArray fields = null;
		RecordSet result = null;
		
		if (request.has("DepartmentId")){
			departmentId = request.getInt("DepartmentId"); 
		}
		if (request.has("GroupId")){
			groupId = request.getInt("GroupId"); 
		}
		if (request.has("ActivityName")){
			activityName = request.getString("ActivityName");
		}
		if (request.has("Fields")){
			fields = request.getJSONArray("Fields");
		}
		
		sql.setStatementType(STATEMENTTYPE.SELECT);
		sql.addItem(new String[]{"id", "name", "department_id", "group_id"}, SQLITEMTYPE.FIELD);
		sql.addItem(SQLWORD.FROM);
		sql.addItem("worktime_activities", SQLITEMTYPE.TABLE);
		
		if ((departmentId!=-1)||(groupId!=-1)||(activityName!=null)){
			sql.addItem(SQLWORD.WHERE);
		}
		
		if (departmentId != -1){
			sql.addFieldItemPair("department_id", "=", departmentId, SQLITEMTYPE.INT);
		}
		
		if (groupId != -1){
			if (departmentId!=-1){
				sql.addItem(SQLWORD.AND);
			}
			sql.addFieldItemPair("group_id", "=", groupId, SQLITEMTYPE.INT);
		}
		
		if (activityName!=null){
			if (groupId!=-1){
				sql.addItem(SQLWORD.AND);
			}
			sql.addFieldItemPair("name", "=", activityName, SQLITEMTYPE.TEXT);
		}

		result = this.model.executeSQL(sql, executingUser, WORKTIME_FUNCTIONS.GET_ACTIVITIES.get(), this.pluginName, RIGHTS.READ.get());
		
		if (fields!=null){
			return result.toJSON(fields);
		}
		
		return result.toJSON();
	}
	
	public JSONObject getDepartments(JSONObject request) throws ClinSysException{
		SQLCreator sql = new SQLCreator();
		String executingUser = request.getString("ExecutingUser");
		String departmentName = null;
		JSONArray fields = null;
		RecordSet result = null;
		
		if (request.has("DepartmentName")){
			departmentName = request.getString("DepartmentName"); 
		}
		if (request.has("Fields")){
			fields = request.getJSONArray("Fields");
		}
		
		sql.setStatementType(STATEMENTTYPE.SELECT);
		sql.addItem(new String[]{"id", "name"}, SQLITEMTYPE.FIELD);
		sql.addItem(SQLWORD.FROM);
		sql.addItem("worktime_departments", SQLITEMTYPE.TABLE);
		
		if (departmentName != null){
			sql.addItem(SQLWORD.WHERE);
			sql.addFieldItemPair("name", "=", departmentName, SQLITEMTYPE.TEXT);
		}

		result = this.model.executeSQL(sql, executingUser, WORKTIME_FUNCTIONS.GET_DEPARTMENTS.get(), this.pluginName, RIGHTS.READ.get());

						
		if (fields!=null){
			return result.toJSON(fields);
		}
		
		return result.toJSON();
	}
	
	public JSONObject getStudies(JSONObject request) throws ClinSysException{
		SQLCreator sql = new SQLCreator();
		String executingUser = request.getString("ExecutingUser");
		int sponsorId = -1;
		String studyName = null;
		JSONArray fields = null;
		RecordSet result = null;
		
		if (request.has("SponsorId")){
			sponsorId = request.getInt("SponsorId"); 
		}
		if (request.has("StudyName")){
			studyName = request.getString("StudyName");
		}
		if (request.has("Fields")){
			fields = request.getJSONArray("Fields");
		}
		
		sql.setStatementType(STATEMENTTYPE.SELECT);
		sql.addItem(new String[]{"id", "name", "sponsor_id"}, SQLITEMTYPE.FIELD);
		sql.addItem(SQLWORD.FROM);
		sql.addItem("worktime_studies", SQLITEMTYPE.TABLE);
		

		if ((sponsorId!=-1)||(studyName!=null)) {
			sql.addItem(SQLWORD.WHERE);
		}
		
		if (sponsorId != -1){
			sql.addFieldItemPair("sponsor_id", "=", sponsorId, SQLITEMTYPE.INT);				
		}
		
		if (studyName!=null){
			if (sponsorId!=-1){
				sql.addItem(SQLWORD.AND);
			}
			sql.addFieldItemPair("name", "=", studyName, SQLITEMTYPE.TEXT);
		}

		result = this.model.executeSQL(sql, executingUser, WORKTIME_FUNCTIONS.GET_STUDIES.get(), this.pluginName, RIGHTS.READ.get());
				
		if (fields!=null){
			return result.toJSON(fields);
		}
		
		return result.toJSON();
	}
	
	public JSONObject getSponsors(JSONObject request) throws ClinSysException{
		SQLCreator sql = new SQLCreator();
		String executingUser = request.getString("ExecutingUser");
		String sponsorName = null;
		JSONArray fields = null;
		RecordSet result = null;
		
		if (request.has("SponsorName")){
			sponsorName = request.getString("SponsorName");
		}
		if (request.has("Fields")){
			fields = request.getJSONArray("Fields");
		}
		
		sql.setStatementType(STATEMENTTYPE.SELECT);
		sql.addItem(new String[]{"id", "name"}, SQLITEMTYPE.FIELD);
		sql.addItem(SQLWORD.FROM);
		sql.addItem("worktime_sponsors", SQLITEMTYPE.TABLE);
		
		if (sponsorName != null){
			sql.addItem(SQLWORD.WHERE);
			sql.addFieldItemPair("name", "=", sponsorName, SQLITEMTYPE.TEXT);
		}

		result = this.model.executeSQL(sql, executingUser, WORKTIME_FUNCTIONS.GET_SPONSORS.get(), this.pluginName, RIGHTS.READ.get());
							
		if (fields!=null){
			return result.toJSON(fields);
		}
		
		return result.toJSON();
	}
	
	public JSONObject getEmployees(JSONObject request) throws ClinSysException{
		SQLCreator sql = new SQLCreator();
		String executingUser = request.getString("ExecutingUser");
		String userName = null;
		String functionName;
		JSONArray fields = null;
		RecordSet result = null;
		
		if (request.has("UserName")){
			userName = request.getString("UserName");
		}
		if (request.has("Fields")){
			fields = request.getJSONArray("Fields");
		}
		
		sql.setStatementType(STATEMENTTYPE.SELECT);
		sql.addItem(new String[]{"id", "username", "firstname", "lastname", "employment_type", "worktime"}, SQLITEMTYPE.FIELD);
		sql.addItem(SQLWORD.FROM);
		sql.addItem("worktime_employees", SQLITEMTYPE.TABLE);
		if (userName!=null){
			sql.addItem(SQLWORD.WHERE);
			sql.addFieldItemPair("username", "=", userName, SQLITEMTYPE.TEXT);
		}
		
		
		if (userName!=null){
			functionName = WORKTIME_FUNCTIONS.GET_USER_EMPLOYEES.get();
		}else{
			functionName = WORKTIME_FUNCTIONS.GET_All_EMPLOYEES.get();
		}
		
	
		result = this.model.executeSQL(sql, executingUser, functionName, this.pluginName, RIGHTS.READ.get());
	
						
		if (fields!=null){
			return result.toJSON(fields);
		}
		
		return result.toJSON();
	}
	
	public JSONObject getWorkTime(JSONObject request) throws ClinSysException{
		SQLCreator sqlHours = new SQLCreator();
		SQLCreator sqlSponsors = new SQLCreator();
		SQLCreator sqlStudies = new SQLCreator();
		SQLCreator sqlEmployees = new SQLCreator();
		SQLCreator sqlDepartments = new SQLCreator();
		SQLCreator sqlActivities = new SQLCreator();
		SQLCreator sqlDepActJoin = new SQLCreator();
		SQLCreator sqlSpoStuJoin = new SQLCreator();
		SQLCreator sqlHoursActivitiesJoin = new SQLCreator();
		SQLCreator sqlHoursStudiesJoin = new SQLCreator();
		SQLCreator sqlHoursEmployeesJoin = new SQLCreator();
		String userName = null;
		String functionName;
		String executingUser = request.getString("ExecutingUser");
		RecordSet result = null;
		JSONArray fieldNames = null;
		
		if (request.has("UserName")){
			userName = request.getString("UserName");
		}
		
		sqlEmployees.setStatementType(STATEMENTTYPE.SELECT);
		sqlEmployees.addItem("id, firstname, username", SQLITEMTYPE.FIELD);
		sqlEmployees.addItem(SQLWORD.FROM);
		sqlEmployees.addItem("worktime_employees", SQLITEMTYPE.TABLE);
		
		if (userName!=null){
			sqlEmployees.addItem(SQLWORD.WHERE);
			sqlEmployees.addFieldItemPair("username", "=", userName, SQLITEMTYPE.TEXT);
		}
		
		sqlDepartments.setStatementType(STATEMENTTYPE.SELECT);
		sqlDepartments.addItem(new String[]{"id", "name"}, SQLITEMTYPE.FIELD);
		sqlDepartments.addItem(SQLWORD.FROM);
		sqlDepartments.addItem("worktime_departments", SQLITEMTYPE.TABLE);

		sqlActivities.setStatementType(STATEMENTTYPE.SELECT);
		sqlActivities.addItem(new String[]{"id", "name", "department_id", "group_id"}, SQLITEMTYPE.FIELD);
		sqlActivities.addItem(SQLWORD.FROM);
		sqlActivities.addItem("worktime_activities", SQLITEMTYPE.TABLE);
		
		sqlDepActJoin.setLeftPart(sqlActivities);
		sqlDepActJoin.setRightPart(sqlDepartments);
		sqlDepActJoin.setConcat(CONCAT.LEFT, "Activities", "Departments");
		sqlDepActJoin.setOnFields(new String[]{"Activities.department_id","Departments.id"});
		
		sqlSponsors.setStatementType(STATEMENTTYPE.SELECT);
		sqlSponsors.addItem(new String[]{"id", "name"}, SQLITEMTYPE.FIELD);
		sqlSponsors.addItem(SQLWORD.FROM);
		sqlSponsors.addItem("worktime_sponsors", SQLITEMTYPE.TABLE);
		
		sqlStudies.setStatementType(STATEMENTTYPE.SELECT);
		sqlStudies.addItem(new String[]{"id", "name", "sponsor_id"}, SQLITEMTYPE.FIELD);
		sqlStudies.addItem(SQLWORD.FROM);
		sqlStudies.addItem("worktime_studies", SQLITEMTYPE.TABLE);
		
		sqlSpoStuJoin.setLeftPart(sqlStudies);
		sqlSpoStuJoin.setRightPart(sqlSponsors);
		sqlSpoStuJoin.setConcat(CONCAT.LEFT, "Studies", "Sponsors");
		sqlSpoStuJoin.setOnFields(new String[]{"Studies.sponsor_id","Sponsors.id"});
					
		sqlHours.setStatementType(STATEMENTTYPE.SELECT);
		sqlHours.addItem(new String[]{"id","employee_id", "study_id", "activity_id", "workday", "hours"}, SQLITEMTYPE.FIELD);
		sqlHours.addItem(SQLWORD.FROM);
		sqlHours.addItem("worktime_hours", SQLITEMTYPE.TABLE);			
		
		sqlHoursActivitiesJoin.setLeftPart(sqlHours);
		sqlHoursActivitiesJoin.setRightPart(sqlDepActJoin);
		sqlHoursActivitiesJoin.setConcat(CONCAT.LEFT, "Hours", "DepAct");
		sqlHoursActivitiesJoin.setOnFields(new String[]{"Hours.activity_id","DepAct.Activities.id"});
		
		sqlHoursStudiesJoin.setLeftPart(sqlHoursActivitiesJoin);
		sqlHoursStudiesJoin.setRightPart(sqlSpoStuJoin);
		sqlHoursStudiesJoin.setConcat(CONCAT.LEFT, "HoursActivities", "SpoStu");
		sqlHoursStudiesJoin.setOnFields(new String[]{"HoursActivities.Hours.study_id","SpoStu.Studies.id"});
		
		sqlHoursEmployeesJoin.setLeftPart(sqlEmployees);
		sqlHoursEmployeesJoin.setRightPart(sqlHoursStudiesJoin);
		sqlHoursEmployeesJoin.setConcat(CONCAT.INNER, "Employees", "HoursActivitiesStudies");
		sqlHoursEmployeesJoin.setOnFields(new String[]{"Employees.id", "HoursActivitiesStudies.HoursActivities.Hours.employee_id"});
		
		if (userName!=null){
			functionName = WORKTIME_FUNCTIONS.GET_USER_WORKTIME.get();
		}else{
			functionName = WORKTIME_FUNCTIONS.GET_ALL_WORKTIME.get();
		}
		
		
		result = this.model.executeSQL(sqlHoursEmployeesJoin, executingUser, functionName, this.pluginName, RIGHTS.READ.get());
	
		
		if (result.getRecordCount()>0){
			// add the normal columns of the hours table
			result.changeFieldName("HoursActivitiesStudies.HoursActivities.Hours.id", "id");
			result.changeFieldName("HoursActivitiesStudies.HoursActivities.Hours.employee_id", "employee_id");
			result.changeFieldName("HoursActivitiesStudies.HoursActivities.Hours.study_id", "study_id");
			result.changeFieldName("HoursActivitiesStudies.HoursActivities.Hours.activity_id", "activity_id");
			result.changeFieldName("HoursActivitiesStudies.HoursActivities.Hours.workday", "workday");
			result.changeFieldName("HoursActivitiesStudies.HoursActivities.Hours.hours", "hours");
			// add the additional name columns
			result.changeFieldName("HoursActivitiesStudies.SpoStu.Sponsors.id", "sponsor_id");
			result.changeFieldName("HoursActivitiesStudies.HoursActivities.DepAct.Departments.id", "department_id");
			result.changeFieldName("HoursActivitiesStudies.SpoStu.Sponsors.name", "sponsor");
			result.changeFieldName("HoursActivitiesStudies.SpoStu.Studies.name", "study");
			result.changeFieldName("HoursActivitiesStudies.HoursActivities.DepAct.Departments.name", "department");
			result.changeFieldName("HoursActivitiesStudies.HoursActivities.DepAct.Activities.name", "activity");
			
			fieldNames = new JSONArray();
			fieldNames.put("id");
			fieldNames.put("employee_id");
			fieldNames.put("study_id");
			fieldNames.put("activity_id");
			fieldNames.put("workday");
			fieldNames.put("hours");
			fieldNames.put("sponsor_id");
			fieldNames.put("department_id");
			fieldNames.put("sponsor");
			fieldNames.put("study");
			fieldNames.put("department");
			fieldNames.put("activity");

			// add full name of employee if it was a get all function call
			if (userName==null){
				result.changeFieldName("firstname", "Employees.firstname");
				result.changeFieldName("lastname", "Employees.lastname");

				fieldNames.put("firstname");
				fieldNames.put("lastname");
			}
		}else{
			//error message
		}
		
		return result.toJSON(fieldNames);
	}
	
	public JSONObject addWorkTime(JSONObject request) throws ClinSysException{
		JSONObject answer = new JSONObject();
		SQLCreator sql = new SQLCreator();
		
		int activityId = request.getInt("ActivityId");
		int studyId = request.getInt("StudyId");
		Date workday = new Date(request.getLong("Workday"));
		double hours = request.getDouble("Hours");
		String executingUser = request.getString("ExecutingUser");		
		
		int employeeId = request.getInt("EmployeeId");
				
		sql.setStatementType(STATEMENTTYPE.INSERT);
		sql.addItem("worktime_hours", SQLITEMTYPE.TABLE);
		sql.addItem(SQLWORD.OPENINGBRACKET);
			sql.addItem(new String[]{"employee_id", "study_id", "activity_id", "workday", "hours"}, SQLITEMTYPE.FIELD);
		sql.addItem(SQLWORD.CLOSINGBRACKET);
		sql.addItem(SQLWORD.VALUES);
		sql.addItem(SQLWORD.OPENINGBRACKET);
			sql.addItem(employeeId, SQLITEMTYPE.INT);
			sql.addItem(SQLWORD.COMMA);
			sql.addItem(studyId, SQLITEMTYPE.INT);
			sql.addItem(SQLWORD.COMMA);
			sql.addItem(activityId, SQLITEMTYPE.INT);
			sql.addItem(SQLWORD.COMMA);
			sql.addItem(workday, SQLITEMTYPE.DATE);
			sql.addItem(SQLWORD.COMMA);
			sql.addItem(hours, SQLITEMTYPE.DOUBLE);
		sql.addItem(SQLWORD.CLOSINGBRACKET);
	
		this.model.executeSQL(sql, executingUser, WORKTIME_FUNCTIONS.ADD_WORKTIME.get(), this.pluginName, RIGHTS.CREATE.get());
		
		answer.put("Result", true);
		return answer;
	}
	
	public JSONObject editWorkTime(JSONObject request) throws ClinSysException{
		JSONObject answer = new JSONObject();
		SQLCreator sql = new SQLCreator();
		
		String executingUser = request.getString("ExecutingUser");
		int worktimeId = request.getInt("WorktimeId");
		int activityId = -1;
		int employeeId = -1;
		int studyId = -1;
		Date workday = null;
		double hours = -1.0;
		
		if ((request.has("ActivityId"))||(request.has("EmployeeId"))||
				(request.has("StudyId"))||(request.has("ActivityId"))||(request.has("Hours"))){			
			
			sql.setStatementType(STATEMENTTYPE.UPDATE);
			sql.addItem("worktime_hours", SQLITEMTYPE.TABLE);
			sql.addItem(SQLWORD.SET);
			if (request.has("ActivityId")){
				activityId = request.getInt("ActivityId");
				sql.addFieldItemPair("activity_id", "=", activityId, SQLITEMTYPE.INT);
			}
			if (request.has("EmployeeId")){
				if (activityId!=-1){
					sql.addItem(SQLWORD.COMMA);
				}
				employeeId = request.getInt("EmployeeId");
				sql.addFieldItemPair("employee_id", "=", employeeId, SQLITEMTYPE.INT);
			}
			if (request.has("StudyId")){
				if ((activityId!=-1)||(employeeId!=-1)){
					sql.addItem(SQLWORD.COMMA);
				}
				studyId = request.getInt("StudyId");
				sql.addFieldItemPair("study_id", "=", studyId, SQLITEMTYPE.INT);
			}
			if (request.has("Workday")){
				if ((activityId!=-1)||(employeeId!=-1)||(studyId!=-1)){
					sql.addItem(SQLWORD.COMMA);
				}
				workday = new Date(request.getLong("Workday"));
				sql.addFieldItemPair("workday", "=", workday, SQLITEMTYPE.DATE);
			}
			if (request.has("Hours")){
				if ((activityId!=-1)||(employeeId!=-1)||(studyId!=-1)||(workday!=null)){
					sql.addItem(SQLWORD.COMMA);
				}
				hours = request.getDouble("Hours");
				sql.addFieldItemPair("hours", "=", hours, SQLITEMTYPE.DOUBLE);
			}
			
			sql.addItem(SQLWORD.WHERE);
			sql.addFieldItemPair("id", "=", worktimeId, SQLITEMTYPE.INT);
	
			this.model.executeSQL(sql, executingUser, WORKTIME_FUNCTIONS.EDIT_WORKTIME.get(), this.pluginName, RIGHTS.EDIT.get());
				
		}else{
			answer.put("Result", false);
		}
		
		answer.put("Result", true);
		return answer;
	}
	
	public JSONObject deleteWorkTime(JSONObject request) throws ClinSysException{
		JSONObject answer = new JSONObject();
		SQLCreator sql = new SQLCreator();
		
		String executingUser = request.getString("ExecutingUser");
		int worktimeId = request.getInt("WorktimeId");
	
		sql.setStatementType(STATEMENTTYPE.DELETE);
		sql.addItem("worktime_hours", SQLITEMTYPE.TABLE);
		sql.addItem(SQLWORD.WHERE);
		sql.addFieldItemPair("id", "=", worktimeId, SQLITEMTYPE.INT);
	
		this.model.executeSQL(sql, executingUser, WORKTIME_FUNCTIONS.DELETE_WORKTIME.get(), this.pluginName, RIGHTS.DELETE.get());
	
		
		answer.put("Result", true);
		answer.put("WorktimeId", worktimeId);
		return answer;
	}
	
	
	/* Prices -------------------------------------------------------------- */
	
	public JSONObject getPrice(JSONObject request) throws ClinSysException{
		SQLCreator sqlPrices = new SQLCreator();
		SQLCreator sqlEmployee = new SQLCreator();
		SQLCreator sqlJoin = new SQLCreator();
		RecordSet result = null;
		JSONArray fields = null;
		String functionName;
		
		String executingUser = request.getString("ExecutingUser");
		int employeeId = -1;		
		
		sqlPrices.setStatementType(STATEMENTTYPE.SELECT);
		sqlPrices.addItem(new String[]{"employee_id","price"}, SQLITEMTYPE.FIELD);
		sqlPrices.addItem(SQLWORD.FROM);
		sqlPrices.addItem("worktime_prices", SQLITEMTYPE.TABLE);
		
		sqlEmployee.setStatementType(STATEMENTTYPE.SELECT);
		sqlEmployee.addItem(new String[]{"id","username","firstname","lastname","employment_type","worktime"}, SQLITEMTYPE.FIELD);
		sqlEmployee.addItem(SQLWORD.FROM);
		sqlEmployee.addItem("worktime_employees", SQLITEMTYPE.TABLE);
		
		if (request.has("EmployeeId")){
			employeeId = request.getInt("EmployeeId");
			sqlEmployee.addItem(SQLWORD.WHERE);
			sqlEmployee.addFieldItemPair("id", "=", employeeId, SQLITEMTYPE.INT);
		}
		
		sqlJoin.setLeftPart(sqlEmployee);
		sqlJoin.setRightPart(sqlPrices);
		sqlJoin.setConcat(CONCAT.LEFT, "Employee", "Prices");
		sqlJoin.setOnFields(new String[]{"Employee.id", "Prices.employee_id"});			
		
		
		if (employeeId!=-1){
			functionName = WORKTIME_FUNCTIONS.GET_USER_PRICE.get();
		}else{
			functionName = WORKTIME_FUNCTIONS.GET_ALL_PRICE.get();
		}
		
		
		result = this.model.executeSQL(sqlJoin, executingUser, functionName, this.pluginName, RIGHTS.READ.get());
		
		
		if (result.getRecordCount()>0){
			result.changeFieldName("Employee.id", "employee_id");
			result.changeFieldName("Employee.username", "username");
			result.changeFieldName("Employee.firstname", "firstname");
			result.changeFieldName("Employee.lastname", "lastname");
			result.changeFieldName("Employee.employment_type", "employment_type");
			result.changeFieldName("Employee.worktime", "worktime");
			result.changeFieldName("Prices.price", "price");
		}
		
		
		if (request.has("Fields")){
			fields = request.getJSONArray("Fields");
			return result.toJSON(fields);
		}		
		return result.toJSON();
	}
	
	
	public JSONObject addPrice(JSONObject request) throws ClinSysException{
		JSONObject answer = new JSONObject();
		SQLCreator sql = new SQLCreator();
		String executingUser = request.getString("ExecutingUser");
		double price = request.getDouble("Price");
		int employee_id = request.getInt("EmployeeId");
		
		sql.setStatementType(STATEMENTTYPE.INSERT);
		sql.addItem("worktime_prices", SQLITEMTYPE.TABLE);
		sql.addItem(SQLWORD.OPENINGBRACKET);
			sql.addItem(new String[]{"employee_id", "price"}, SQLITEMTYPE.FIELD);
		sql.addItem(SQLWORD.CLOSINGBRACKET);
		sql.addItem(SQLWORD.VALUES);
		sql.addItem(SQLWORD.OPENINGBRACKET);
			sql.addItem(employee_id, SQLITEMTYPE.INT);
			sql.addItem(SQLWORD.COMMA);
			sql.addItem(price, SQLITEMTYPE.DOUBLE);
		sql.addItem(SQLWORD.CLOSINGBRACKET);

		this.model.executeSQL(sql, executingUser, WORKTIME_FUNCTIONS.ADD_PRICE.get(), this.pluginName, RIGHTS.CREATE.get());
	
		
		answer.put("Result", true);
		return answer;
	}
	
	public JSONObject editPrice(JSONObject request) throws ClinSysException{
		JSONObject answer = new JSONObject();
		SQLCreator sql = new SQLCreator();
		String executingUser = request.getString("ExecutingUser");
		double price = request.getDouble("Price");
		int employee_id = request.getInt("EmployeeId");
		
		sql.setStatementType(STATEMENTTYPE.UPDATE);
		sql.addItem("worktime_prices", SQLITEMTYPE.TABLE);
		sql.addItem(SQLWORD.SET);
		sql.addFieldItemPair("price", "=", price, SQLITEMTYPE.DOUBLE);
		sql.addItem(SQLWORD.WHERE);
		sql.addFieldItemPair("employee_id", "=", employee_id, SQLITEMTYPE.INT);

		this.model.executeSQL(sql, executingUser, WORKTIME_FUNCTIONS.EDIT_PRICE.get(), this.pluginName, RIGHTS.EDIT.get());
		
		
		answer.put("Result", true);
		return answer;	
	}
	
	public JSONObject deletePrice(JSONObject request) throws ClinSysException{
		JSONObject answer = new JSONObject();
		SQLCreator sql = new SQLCreator();
		String executingUser = request.getString("ExecutingUser");
		int employee_id = request.getInt("EmployeeId");
		
		sql.setStatementType(STATEMENTTYPE.DELETE);			
		sql.addItem("worktime_prices", SQLITEMTYPE.TABLE);
		sql.addItem(SQLWORD.WHERE);
		sql.addFieldItemPair("employee_id", "=", employee_id, SQLITEMTYPE.INT);
	
		this.model.executeSQL(sql, executingUser, WORKTIME_FUNCTIONS.DELETE_PRICE.get(), this.pluginName, RIGHTS.DELETE.get());
		
		
		answer.put("Result", true);
		return answer;		
	}
	
	
	
	/* Discounts -------------------------------------------------------------- */
	
	public JSONObject getDiscount(JSONObject request) throws ClinSysException{
		SQLCreator sqlDiscounts = new SQLCreator();
		SQLCreator sqlEmployee = new SQLCreator();
		SQLCreator sqlStudies = new SQLCreator();
		SQLCreator sqlSponsors = new SQLCreator();
		SQLCreator sqlSpoStu = new SQLCreator();
		SQLCreator sqlDisSpoStu = new SQLCreator();
		SQLCreator sqlJoin = new SQLCreator();
		RecordSet result = null;
		JSONArray fields = null;
		String functionName;
		
		String executingUser = request.getString("ExecutingUser");
		int employeeId = -1;	
		int studyId = -1;
		
		sqlDiscounts.setStatementType(STATEMENTTYPE.SELECT);
		sqlDiscounts.addItem(new String[]{"employee_id", "study_id", "discount_price"}, SQLITEMTYPE.FIELD);
		sqlDiscounts.addItem(SQLWORD.FROM);
		sqlDiscounts.addItem("worktime_discounts", SQLITEMTYPE.TABLE);
		
		sqlEmployee.setStatementType(STATEMENTTYPE.SELECT);
		sqlEmployee.addItem(new String[]{"id","username","firstname","lastname","employment_type","worktime"}, SQLITEMTYPE.FIELD);
		sqlEmployee.addItem(SQLWORD.FROM);
		sqlEmployee.addItem("worktime_employees", SQLITEMTYPE.TABLE);
		
		if (request.has("EmployeeId")){
			employeeId = request.getInt("EmployeeId");
			sqlEmployee.addItem(SQLWORD.WHERE);
			sqlEmployee.addFieldItemPair("id", "=", employeeId, SQLITEMTYPE.INT);
		}
		
		sqlStudies.setStatementType(STATEMENTTYPE.SELECT);
		sqlStudies.addItem(new String[]{"id","name", "sponsor_id"}, SQLITEMTYPE.FIELD);
		sqlStudies.addItem(SQLWORD.FROM);
		sqlStudies.addItem("worktime_studies", SQLITEMTYPE.TABLE);
		
		if (request.has("StudyId")){
			studyId = request.getInt("StudyId");
			sqlEmployee.addItem(SQLWORD.WHERE);
			sqlEmployee.addFieldItemPair("id", "=", studyId, SQLITEMTYPE.INT);
		}
		
		sqlSponsors.setStatementType(STATEMENTTYPE.SELECT);
		sqlSponsors.addItem(new String[]{"id","name"}, SQLITEMTYPE.FIELD);
		sqlSponsors.addItem(SQLWORD.FROM);
		sqlSponsors.addItem("worktime_sponsors", SQLITEMTYPE.TABLE);
		
		sqlSpoStu.setLeftPart(sqlStudies);
		sqlSpoStu.setRightPart(sqlSponsors);
		sqlSpoStu.setConcat(CONCAT.LEFT, "Studies", "Sponsors");
		sqlSpoStu.setOnFields(new String[]{"Studies.sponsor_id", "Sponsors.id"});
		
		sqlDisSpoStu.setLeftPart(sqlDiscounts);
		sqlDisSpoStu.setRightPart(sqlSpoStu);
		sqlDisSpoStu.setConcat(CONCAT.LEFT, "Discounts", "SpoStu");
		sqlDisSpoStu.setOnFields(new String[]{"Discounts.study_id","SpoStu.Studies.id"});
		
		sqlJoin.setLeftPart(sqlEmployee);
		sqlJoin.setRightPart(sqlDisSpoStu);
		sqlJoin.setConcat(CONCAT.INNER, "Employee", "DisSpoStu");
		sqlJoin.setOnFields(new String[]{"Employee.id", "DisSpoStu.Discounts.employee_id"});			
		
		if ((employeeId!=-1)&&(studyId!=-1)){
			functionName = WORKTIME_FUNCTIONS.GET_USER_STUDY_DISCOUNT.get();
		}else if(employeeId!=-1){
			functionName = WORKTIME_FUNCTIONS.GET_USER_DISCOUNT.get();
		}else if(studyId!=-1){
			functionName = WORKTIME_FUNCTIONS.GET_STUDY_DISCOUNT.get();
		}else{
			functionName = WORKTIME_FUNCTIONS.GET_ALL_PRICE.get();
		}
		
		result = this.model.executeSQL(sqlJoin, executingUser, functionName, this.pluginName, RIGHTS.READ.get());
		
		
		if (result.getRecordCount()>0){
			result.changeFieldName("Employee.id", "employee_id");
			result.changeFieldName("Employee.username", "username");
			result.changeFieldName("Employee.firstname", "firstname");
			result.changeFieldName("Employee.lastname", "lastname");
			result.changeFieldName("Employee.employment_type", "employment_type");
			result.changeFieldName("Employee.worktime", "worktime");
			result.changeFieldName("DisSpoStu.Discounts.study_id", "study_id");
			result.changeFieldName("DisSpoStu.SpoStu.Sponsors.id", "sponsor_id");
			result.changeFieldName("DisSpoStu.SpoStu.Sponsors.name", "sponsor");
			result.changeFieldName("DisSpoStu.SpoStu.Studies.name", "study");
			result.changeFieldName("DisSpoStu.Discounts.discount_price", "discount_price");
		}
		
		
		if (request.has("Fields")){
			fields = request.getJSONArray("Fields");
			return result.toJSON(fields);
		}		
		return result.toJSON();
	}
	
	public JSONObject addDiscount(JSONObject request) throws ClinSysException{
		JSONObject answer = new JSONObject();
		SQLCreator sql = new SQLCreator();
		String executingUser = request.getString("ExecutingUser");
		double discount = request.getDouble("Discount");
		int employee_id = request.getInt("EmployeeId");
		int study_id = request.getInt("StudyId");
		
		sql.setStatementType(STATEMENTTYPE.INSERT);
		sql.addItem("worktime_discounts", SQLITEMTYPE.TABLE);
		sql.addItem(SQLWORD.OPENINGBRACKET);
			sql.addItem(new String[]{"employee_id", "study_id", "discount_price"}, SQLITEMTYPE.FIELD);
		sql.addItem(SQLWORD.CLOSINGBRACKET);
		sql.addItem(SQLWORD.VALUES);
		sql.addItem(SQLWORD.OPENINGBRACKET);
			sql.addItem(employee_id, SQLITEMTYPE.INT);
			sql.addItem(SQLWORD.COMMA);
			sql.addItem(study_id, SQLITEMTYPE.INT);
			sql.addItem(SQLWORD.COMMA);
			sql.addItem(discount, SQLITEMTYPE.DOUBLE);
		sql.addItem(SQLWORD.CLOSINGBRACKET);
	
		this.model.executeSQL(sql, executingUser, WORKTIME_FUNCTIONS.ADD_DISCOUNT.get(), this.pluginName, RIGHTS.CREATE.get());
		
		
		answer.put("Result", true);
		return answer;
	}
	
	public JSONObject editDiscount(JSONObject request) throws ClinSysException{
		JSONObject answer = new JSONObject();
		SQLCreator sql = new SQLCreator();
		String executingUser = request.getString("ExecutingUser");
		double discount = request.getDouble("DiscountPrice");
		int employee_id = request.getInt("EmployeeId");
		int study_id = request.getInt("StudyId");
		
		sql.setStatementType(STATEMENTTYPE.UPDATE);
		sql.addItem("worktime_discounts", SQLITEMTYPE.TABLE);
		sql.addItem(SQLWORD.SET);
		sql.addFieldItemPair("discount_price", "=", discount, SQLITEMTYPE.DOUBLE);
		sql.addItem(SQLWORD.WHERE);
		sql.addFieldItemPair("employee_id", "=", employee_id, SQLITEMTYPE.INT);
		sql.addItem(SQLWORD.AND);
		sql.addFieldItemPair("study_id", "=", study_id, SQLITEMTYPE.INT);

		this.model.executeSQL(sql, executingUser, WORKTIME_FUNCTIONS.EDIT_DISCOUNT.get(), this.pluginName, RIGHTS.EDIT.get());
		
		
		answer.put("Result", true);
		return answer;	
	}
	
	public JSONObject deleteDiscount(JSONObject request) throws ClinSysException{
		JSONObject answer = new JSONObject();
		SQLCreator sql = new SQLCreator();
		String executingUser = request.getString("ExecutingUser");
		int employee_id = request.getInt("EmployeeId");
		int study_id = request.getInt("StudyId");
		
		sql.setStatementType(STATEMENTTYPE.DELETE);			
		sql.addItem("worktime_discounts", SQLITEMTYPE.TABLE);
		sql.addItem(SQLWORD.WHERE);
		sql.addFieldItemPair("employee_id", "=", employee_id, SQLITEMTYPE.INT);
		sql.addItem(SQLWORD.AND);
		sql.addFieldItemPair("study_id", "=", study_id, SQLITEMTYPE.INT);

		this.model.executeSQL(sql, executingUser, WORKTIME_FUNCTIONS.DELETE_DISCOUNT.get(), this.pluginName, RIGHTS.DELETE.get());
	
		
		answer.put("Result", true);
		return answer;		
	}

	@Override
	public void afterCreated() {
		// TODO Auto-generated method stub
		
	}
}
