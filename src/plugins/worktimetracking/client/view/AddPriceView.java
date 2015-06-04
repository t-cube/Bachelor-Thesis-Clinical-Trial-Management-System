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
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import plugins.worktimetracking.WORKTIME_FUNCTIONS;
import plugins.worktimetracking.client.WorktimeTrackingController;
import plugins.worktimetracking.client.listener.AddPriceViewActionListener;

import client.view.InnerView;

/**
 * @author torstend
 *
 */
public class AddPriceView extends InnerView {
	private static final long serialVersionUID = -1403211609153570247L;
	private JPanel employeePanel;
	private JPanel pricePanel;
	private JPanel discountPanel;
	private JComboBox<String> employee;
	private JComboBox<String> study;
	private JTextField price;
	private JTextField discount;
	private JButton btnAddPrice;
	private JButton btnAddDiscount;
	private Container content;
	private WorktimeTrackingController wtc;
	
	public AddPriceView(WorktimeTrackingController wtc){
		super("Add price/discount for an employee", "AddPriceView");
		this.wtc = wtc;
		setUp();
	}
	
	
	/**
	 * @return the pricePanel
	 */
	public JPanel getPricePanel() {
		return pricePanel;
	}

	/**
	 * @return the discountPanel
	 */
	public JPanel getDiscountPanel() {
		return discountPanel;
	}

	/**
	 * @return the employee
	 */
	public JComboBox<String> getEmployee() {
		return employee;
	}

	/**
	 * @return the study
	 */
	public JComboBox<String> getStudy() {
		return study;
	}

	/**
	 * @return the price
	 */
	public JTextField getPrice() {
		return price;
	}

	/**
	 * @return the discount
	 */
	public JTextField getDiscount() {
		return discount;
	}

	/**
	 * @return the btnAddPrice
	 */
	public JButton getBtnAddPrice() {
		return btnAddPrice;
	}

	/**
	 * @return the btnAddDiscount
	 */
	public JButton getBtnAddDiscount() {
		return btnAddDiscount;
	}

	/**
	 * @return the content
	 */
	public Container getContent() {
		return content;
	}



	/**
	 * @param pricePanel the pricePanel to set
	 */
	public void setPricePanel(JPanel pricePanel) {
		this.pricePanel = pricePanel;
	}

	/**
	 * @param discountPanel the discountPanel to set
	 */
	public void setDiscountPanel(JPanel discountPanel) {
		this.discountPanel = discountPanel;
	}

	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(JComboBox<String> employee) {
		this.employee = employee;
	}

	/**
	 * @param study the study to set
	 */
	public void setStudy(JComboBox<String> study) {
		this.study = study;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(JTextField price) {
		this.price = price;
	}

	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(JTextField discount) {
		this.discount = discount;
	}

	/**
	 * @param btnAddPrice the btnAddPrice to set
	 */
	public void setBtnAddPrice(JButton btnAddPrice) {
		this.btnAddPrice = btnAddPrice;
	}

	/**
	 * @param btnAddDiscount the btnAddDiscount to set
	 */
	public void setBtnAddDiscount(JButton btnAddDiscount) {
		this.btnAddDiscount = btnAddDiscount;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(Container content) {
		this.content = content;
	}





	private void setUp(){
		AddPriceViewActionListener al = new AddPriceViewActionListener(this.wtc);
		
		this.content = this.getContentPane();
		this.content.setLayout(new GridLayout(3,1));
		
		this.employeePanel = new JPanel();
		this.pricePanel = new JPanel();
		this.discountPanel = new JPanel();
		
		this.employee = new JComboBox<String>();
		this.study = new JComboBox<String>();
		
		this.price = new JTextField("Preis");
		this.discount = new JTextField("Rabattpreis");
		
		this.btnAddPrice = new JButton("Add Price");
		this.btnAddDiscount = new JButton("Add Discount");
		
		this.btnAddPrice.addActionListener(al);
		this.btnAddPrice.setActionCommand(WORKTIME_FUNCTIONS.ADD_PRICE.get());
		this.btnAddDiscount.addActionListener(al);
		this.btnAddDiscount.setActionCommand(WORKTIME_FUNCTIONS.ADD_DISCOUNT.get());

		this.employeePanel.setLayout(new FlowLayout());
		this.pricePanel.setLayout(new FlowLayout());
		this.discountPanel.setLayout(new FlowLayout());
		
		this.employeePanel.add(this.employee);
		
		this.pricePanel.add(this.price);
		this.pricePanel.add(this.btnAddPrice);
		
		this.discountPanel.add(this.study);
		this.discountPanel.add(this.discount);
		this.discountPanel.add(this.btnAddDiscount);
		
		this.content.add(this.employeePanel);
		this.content.add(this.pricePanel);
		this.content.add(this.discountPanel);
	}
}
