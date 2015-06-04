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

package plugins.worktimetracking;

/**
 * @author torstend
 *
 */
public enum WORKTIME_FUNCTIONS {
	GET_USER_WORKTIME ("Worktime#Get_User_Worktime"),
	GET_ALL_WORKTIME ("Worktime#Get_All_Worktime"),
	GET_USER_EMPLOYEES("Worktime#Get_User_Employees"),
	GET_All_EMPLOYEES("Worktime#Get_All_Employees"),
	GET_SPONSORS ("Worktime#Get_Sponsors"),
	GET_STUDIES ("Worktime#Get_Studies"),
	GET_DEPARTMENTS ("Worktime#Get_Departments"),
	GET_ACTIVITIES ("Worktime#Get_Activities"),
	ADD_WORKTIME ("Worktime#Add_Worktime"),
	EDIT_WORKTIME ("Worktime#Edit_Worktime"),
	DELETE_WORKTIME ("Worktime#Delete_Worktime"),
	GET_USER_PRICE ("Worktime#Get_User_Price"),
	GET_ALL_PRICE ("Worktime#Get_All_Price"),	
	ADD_PRICE ("Worktime#Add_Price"),
	EDIT_PRICE ("Worktime#Edit_Price"),
	DELETE_PRICE ("Worktime#Delete_Price"),
	GET_USER_STUDY_DISCOUNT ("Worktime#Get_User_Study_Discount"),
	GET_USER_DISCOUNT ("Worktime#Get_User_Discount"),
	GET_STUDY_DISCOUNT ("Worktime#Get_Study_Discount"),
	GET_ALL_DISCOUNT ("Worktime#Get_All_Discount"),	
	ADD_DISCOUNT ("Worktime#Add_Discount"),
	EDIT_DISCOUNT ("Worktime#Edit_Discount"),
	DELETE_DISCOUNT ("Worktime#Delete_Discount");
	
	
	private String functionName;
	
	WORKTIME_FUNCTIONS(String s){
		this.functionName = s;
	}
	
	public String get(){
		return this.functionName;
	}
	
}
