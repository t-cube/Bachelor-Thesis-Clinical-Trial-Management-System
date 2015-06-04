package testcases;

import java.util.ArrayList;

import server.model.DBConnector;
import server.model.DatabaseDictionary;
import server.model.SQLCreator;
import server.model.enumerations.CONCAT;
import server.model.enumerations.DBMS;
import server.model.enumerations.RIGHTS;
import server.model.enumerations.SQLITEMTYPE;
import server.model.enumerations.SQLOBJECTTYPE;
import server.model.enumerations.SQLWORD;
import server.model.enumerations.STATEMENTTYPE;
import shared.util.FLAGS;
import shared.util.ClinSysException;
import shared.util.Flag;
import shared.util.RecordSet;

public class test_DatabaseDictionary {

	/**
	 * @param args
	 * @throws ClinSysException 
	 */
	public static void main(String[] args) throws ClinSysException {
		Flag.set(FLAGS.DEBUG);
		
		// TODO Auto-generated method stub
		DatabaseDictionary dict = new DatabaseDictionary();
		DBConnector db = new DBConnector();
		SQLCreator sql = new SQLCreator();
		RecordSet result = null;
		ArrayList<String> fieldNames;
		ArrayList<?> values;
		
		db.setDbms(DBMS.POSTGRESQL);
		db.setDbName("ICTM");
		db.setDbPassword("1234");
		db.setDbUser("admin");
		db.setHost("localhost");
		db.setPort(5432);
		try {
			db.connect();
		} catch (ClinSysException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			dict.addDbConnector(db);
		} catch (ClinSysException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			sql.setStatementType(STATEMENTTYPE.SELECT);
			sql.addItem("*", SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.FROM);
			sql.addItem("ictm_plugin_functions", SQLITEMTYPE.TABLE);
		} catch (ClinSysException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			result = dict.executeSQL(sql, "torstend", "test", "test", RIGHTS.READ.get());
		} catch (ClinSysException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fieldNames  = result.getFields();
		for (String field : fieldNames){
			values = result.getValues(field);
			for (Object o : values){
				System.out.println(o.toString());
			}
			System.out.println("---Spaltenende---");
		}
		
		sql = new SQLCreator();
		sql.setStatementType(STATEMENTTYPE.CREATE);
		try {
			sql.setSQLObjectType(SQLOBJECTTYPE.TABLE);
		} catch (ClinSysException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sql.addItem("test", SQLITEMTYPE.TABLE);
			sql.addItem(SQLWORD.OPENINGBRACKET);
				sql.addItem("field1 int NOT NULL", SQLITEMTYPE.FIELD);
			sql.addItem(SQLWORD.CLOSINGBRACKET);
		} catch (ClinSysException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//dict.executeSQL(sql, "torstend","Server_Create_Table",  "Server",  RIGHTS.CREATE.get());
		
		
		
		SQLCreator sqlGroup = null;
		SQLCreator sqlUserUsergroups = null;
		SQLCreator sqlUser = null;
		SQLCreator sqlJoinGroup = null;
		SQLCreator sqlJoin = null;
		
		try {
			sqlGroup = new SQLCreator();
			sqlGroup.setStatementType(STATEMENTTYPE.SELECT);
			sqlGroup.addItem(new String[]{"id","name"}, SQLITEMTYPE.FIELD);
			sqlGroup.addItem(SQLWORD.FROM);
			sqlGroup.addItem("ictm_usergroups", SQLITEMTYPE.TABLE);
			sqlGroup.addItem(SQLWORD.WHERE);
			sqlGroup.addFieldItemPair("name", "=" , "test", SQLITEMTYPE.TEXT);
		} catch (ClinSysException e) {
			
		}
		
		try {
			sqlUserUsergroups = new SQLCreator();
			sqlUserUsergroups.setStatementType(STATEMENTTYPE.SELECT);
			sqlUserUsergroups.addItem(new String[]{"user_id","usergroup_id"}, SQLITEMTYPE.FIELD);
			sqlUserUsergroups.addItem(SQLWORD.FROM);
			sqlUserUsergroups.addItem("ictm_user_usergroups", SQLITEMTYPE.TABLE);
		} catch (ClinSysException e) {
			
		}
		
		try {
			sqlJoinGroup = new SQLCreator();
			sqlJoinGroup.setLeftPart(sqlGroup);
			sqlJoinGroup.setRightPart(sqlUserUsergroups);
			sqlJoinGroup.setConcat(CONCAT.INNER, "Group", "UserUsergroup");
			sqlJoinGroup.setOnFields(new String[]{"Group.id", "UserUsergroup.usergroup_id"});
		} catch (ClinSysException e) {
			
		}
		
		try {
			sqlUser = new SQLCreator();
			sqlUser.setStatementType(STATEMENTTYPE.SELECT);
			sqlUser.addItem(new String[]{"id","name"}, SQLITEMTYPE.FIELD);
			sqlUser.addItem(SQLWORD.FROM);
			sqlUser.addItem("ictm_user", SQLITEMTYPE.TABLE);
		} catch (ClinSysException e) {
			
		}
		
		try {
			sqlJoin = new SQLCreator();
			sqlJoin.setLeftPart(sqlUser);
			sqlJoin.setRightPart(sqlJoinGroup);
			sqlJoin.setConcat(CONCAT.INNER, "User", "JoinGroup");
			sqlJoin.setOnFields(new String[]{"User.id", "JoinGroup.user_id"});
		} catch (ClinSysException e) {
			
		}
		

		RecordSet result2 = dict.executeSQL(sqlJoin, "torstend","Server_Create_Table",  "Server",  RIGHTS.READ.get());
		
		for (int i = 0 ; i< result2.getRecordCount(); i++){
			System.out.println(result2.getValues("User.name").get(i));
		}
	}

}
