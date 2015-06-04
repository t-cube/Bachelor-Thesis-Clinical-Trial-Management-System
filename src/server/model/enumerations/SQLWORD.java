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


package server.model.enumerations;

/** 
 * @author Torsten Dietl
 * @version 1.0.0a
 * Enumeration of sql prereserved words
 * List of included sql prereserved words:
 * FROM - The "FROM" word of a select statement
 * GROUP - The "GROUP BY" word of a sql statement
 * ORDER - The "ORDER BY" word of a sql statement
 * VALUES - The "VALUES" word of an insert statement
 * ON - The "ON" word of an join statement
 * AND - The "AND" word of a criteria
 * OR - The "OR" word of a criteria part
 * WHERE - The "WHERE" word for a criteria part
 * OPENINGBRACKET - An opening Bracket: "("
 * CLOSINGBRACKET - An closing Bracket: ")"
 * NOT - The "NOT" word to negate a criteria
 * IS - The "IS" word to check if a field is null
 * SET - The "SET" word for an update statement
 * COMMA - An comma: ","
 * RETURNING - The Returning part to get values of inserted rows back
 * IN - For the In-Part of a criteria
 */
public enum SQLWORD {
	FROM ("FROM"),
	GROUP ("GROUP BY"),
	ORDER ("ORDER BY"),
	VALUES ("VALUES"),
	ON ("ON"),
	AND ("AND"),
	OR ("OR"),
	WHERE ("WHERE"),
	OPENINGBRACKET ("("),
	CLOSINGBRACKET (")"),
	NOT ("NOT"),
	IS ("IS"),
	SET ("Set"),
	COMMA (","),
	RETURNING ("RETURNING"),
	IN ("IN");
	
	private String sqlWord;
	
	/**
	 * The Constructor, which sets the string representation of the enumeration-object.
	 * @param String s
	 */
	SQLWORD(String s){
		this.sqlWord = s;
	}
	
	/**
	 * Returns the string representation of the enumeration-object.
	 * @return String
	 */
	public String get(){
		return this.sqlWord;
	}
}
