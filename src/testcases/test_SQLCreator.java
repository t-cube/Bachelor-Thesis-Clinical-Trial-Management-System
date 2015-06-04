package testcases;

import java.sql.ResultSet;
import java.sql.SQLException;

import server.model.DBConnector;
import server.model.SQLCreator;
import server.model.enumerations.CONCAT;
import server.model.enumerations.DBMS;
import server.model.enumerations.SQLITEMTYPE;
import server.model.enumerations.SQLWORD;
import server.model.enumerations.STATEMENTTYPE;
import shared.util.FLAGS;
import shared.util.ClinSysException;
import shared.util.Flag;


public class test_SQLCreator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//set flags
		Flag.set(FLAGS.DEBUG);
		
		SQLCreator creator = new SQLCreator();
		SQLCreator left = new SQLCreator();
		SQLCreator outer = new SQLCreator();
		SQLCreator outer2 = new SQLCreator();
		SQLCreator test = new SQLCreator();

		try {
			left.setStatementType(STATEMENTTYPE.SELECT);
			left.addItem("A.id, A.val, b.val2", SQLITEMTYPE.FIELD);
			left.addItem(SQLWORD.FROM);
			left.addItem("test", SQLITEMTYPE.TABLE);
			
//			creator.setStatementType(STATEMENTTYPE.SELECT);
//			creator.addItem("*",FIELDTYPE.PARAMTER);
//			//creator.addParameter(new String[]{"val","id"});
//			creator.addItem(SQLWORD.FROM);
			creator.addItem("test2", SQLITEMTYPE.TABLE);
//			creator.addItem(SQLWORD.WHERE);
//			creator.addItem(SQLWORD.OPENINGBRACKET);
//			creator.addFieldItemPair("id", "=", 1, FIELDTYPE.INT);
//			creator.addItem(SQLWORD.OR);
//			creator.addFieldItemPair("id", "=", 2, FIELDTYPE.INT);
//			creator.addItem(SQLWORD.CLOSINGBRACKET);
//			creator.addItem(SQLWORD.AND);
//			creator.addFieldItemPair(true, "id", "=", 3, FIELDTYPE.INT);			
			outer.setLeftPart(left);						
			outer.setRightPart(creator);			
			outer.setConcat(CONCAT.INNER, "A", "B");
			outer.addItem(SQLWORD.ON);
			outer.addFieldItemPair("A.id", "=", "B.id", SQLITEMTYPE.FIELD);
			
			outer2.setStatementType(STATEMENTTYPE.SELECT);
			outer2.addItem("C.id, C.val, D.val2", SQLITEMTYPE.FIELD);
			outer2.addItem(SQLWORD.FROM);
			outer2.addItem("test", SQLITEMTYPE.TABLE);
			
			test.setLeftPart(outer2);
			test.setRightPart(outer);
			test.setConcat(CONCAT.INNER, "C", "D");
			test.addItem(SQLWORD.ON);
			test.addFieldItemPair("C.id", "=", "D.id", SQLITEMTYPE.FIELD);
			
		} catch (ClinSysException e) {
			e.printStackTrace();
		}
				
		DBConnector db = new DBConnector();
		ResultSet rs;
		db.setDbms(DBMS.POSTGRESQL);
		db.setDbName("test");
		db.setDbPassword("1234");
		db.setDbUser("postgres");
		db.setHost("localhost");
		db.setPort(5432);
		try {
			db.connect();
			rs = db.executeStatement(test);
			while (rs.next()){
				System.out.println(rs.getInt(1));
				System.out.println(rs.getString(2));
				System.out.println(rs.getString(3));
			}			
		} catch (ClinSysException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
