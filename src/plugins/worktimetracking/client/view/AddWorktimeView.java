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

package plugins.worktimetracking.client.view;

import java.awt.Container;
import java.awt.FlowLayout;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import plugins.worktimetracking.WORKTIME_FUNCTIONS;
import plugins.worktimetracking.client.WorktimeTrackingController;
import plugins.worktimetracking.client.listener.AddWorktimeViewActionListener;

import client.view.InnerView;

/**
 * @author torstend
 *
 */
public class AddWorktimeView extends InnerView {
	private static final long serialVersionUID = -3678951685947404027L;
	private JTextField workday;
	private JComboBox<String> sponsor;
	private JComboBox<String> study;
	private JComboBox<String> department;
	private JComboBox<String> activity;
	private JTextField hours;
	private JButton btnAdd;
	private Container content;
	private WorktimeTrackingController wtc;
	
	public AddWorktimeView(WorktimeTrackingController wtc){
		super("Add work time(s)", "AddWorktimeView");
		this.wtc = wtc;
		setUp();
	}
	
	
	
	/**
	 * @return the workday
	 */
	public JTextField getWorkday() {
		return workday;
	}



	/**
	 * @return the sponsor
	 */
	public JComboBox<String> getSponsor() {
		return sponsor;
	}



	/**
	 * @return the study
	 */
	public JComboBox<String> getStudy() {
		return study;
	}



	/**
	 * @return the department
	 */
	public JComboBox<String> getDepartment() {
		return department;
	}



	/**
	 * @return the activity
	 */
	public JComboBox<String> getActivity() {
		return activity;
	}



	/**
	 * @return the hours
	 */
	public JTextField getHours() {
		return hours;
	}



	/**
	 * @return the btnAdd
	 */
	public JButton getBtnAdd() {
		return btnAdd;
	}



	/**
	 * @return the content
	 */
	public Container getContent() {
		return content;
	}



	/**
	 * @param workday the workday to set
	 */
	public void setWorkday(JTextField workday) {
		this.workday = workday;
	}



	/**
	 * @param sponsor the sponsor to set
	 */
	public void setSponsor(JComboBox<String> sponsor) {
		this.sponsor = sponsor;
	}



	/**
	 * @param study the study to set
	 */
	public void setStudy(JComboBox<String> study) {
		this.study = study;
	}



	/**
	 * @param department the department to set
	 */
	public void setDepartment(JComboBox<String> department) {
		this.department = department;
	}



	/**
	 * @param activity the activity to set
	 */
	public void setActivity(JComboBox<String> activity) {
		this.activity = activity;
	}



	/**
	 * @param hours the hours to set
	 */
	public void setHours(JTextField hours) {
		this.hours = hours;
	}



	/**
	 * @param btnAdd the btnAdd to set
	 */
	public void setBtnAdd(JButton btnAdd) {
		this.btnAdd = btnAdd;
	}



	/**
	 * @param content the content to set
	 */
	public void setContent(Container content) {
		this.content = content;
	}



	private void setUp(){
		this.workday = new JTextField();
		this.sponsor = new JComboBox<String>();
		this.study = new JComboBox<String>();
		this.department = new JComboBox<String>();
		this.activity = new JComboBox<String>();
		this.hours = new JTextField();
		this.btnAdd = new JButton("Add Worktime");
		
		this.workday.setText(DateFormat.getDateInstance().format( new Date() ));
		this.hours.setText("Stunden");
		this.btnAdd.setActionCommand(WORKTIME_FUNCTIONS.ADD_WORKTIME.get());
		this.btnAdd.addActionListener(new AddWorktimeViewActionListener(this.wtc));
		
		this.content = this.getContentPane();
		this.content.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		this.content.add(this.workday);
		this.content.add(this.sponsor);
		this.content.add(this.study);
		this.content.add(this.department);
		this.content.add(this.activity);
		this.content.add(this.hours);		
		this.content.add(this.btnAdd);
	}
}
