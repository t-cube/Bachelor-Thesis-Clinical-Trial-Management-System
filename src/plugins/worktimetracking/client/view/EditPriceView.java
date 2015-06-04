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
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import plugins.worktimetracking.WORKTIME_FUNCTIONS;
import plugins.worktimetracking.client.WorktimeTrackingController;
import plugins.worktimetracking.client.listener.EditViewActionListener;
import client.view.InnerView;

/**
 * @author torstend
 *
 */
public class EditPriceView extends InnerView {
	private static final long serialVersionUID = -6299430022380872080L;
	private JScrollPane pricePane;
	private JScrollPane discountPane;
	private JTable priceTable;
	private JTable discountTable;
	private Container content;
	private WorktimeTrackingController wtc;
	
	public EditPriceView(WorktimeTrackingController wtc){
		super("Edits employee prices", "EditPriceView");
		this.wtc = wtc;
		setUp();
	}
	
	
	
	
	
	/**
	 * @return the pricePane
	 */
	public JScrollPane getPricePane() {
		return pricePane;
	}





	/**
	 * @return the discountPane
	 */
	public JScrollPane getDiscountPane() {
		return discountPane;
	}





	/**
	 * @return the priceTable
	 */
	public JTable getPriceTable() {
		return priceTable;
	}





	/**
	 * @return the discountTable
	 */
	public JTable getDiscountTable() {
		return discountTable;
	}





	/**
	 * @return the content
	 */
	public Container getContent() {
		return content;
	}





	/**
	 * @return the wtc
	 */
	public WorktimeTrackingController getWtc() {
		return wtc;
	}





	/**
	 * @param pricePane the pricePane to set
	 */
	public void setPricePane(JScrollPane pricePane) {
		this.pricePane = pricePane;
	}





	/**
	 * @param discountPane the discountPane to set
	 */
	public void setDiscountPane(JScrollPane discountPane) {
		this.discountPane = discountPane;
	}





	/**
	 * @param priceTable the priceTable to set
	 */
	public void setPriceTable(JTable priceTable) {
		this.priceTable = priceTable;
	}





	/**
	 * @param discountTable the discountTable to set
	 */
	public void setDiscountTable(JTable discountTable) {
		this.discountTable = discountTable;
	}





	/**
	 * @param content the content to set
	 */
	public void setContent(Container content) {
		this.content = content;
	}





	/**
	 * @param wtc the wtc to set
	 */
	public void setWtc(WorktimeTrackingController wtc) {
		this.wtc = wtc;
	}





	private void setUp(){
		this.priceTable = new JTable();
		this.discountTable = new JTable();
		
		this.priceTable.setFillsViewportHeight(true);
		this.priceTable.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), WORKTIME_FUNCTIONS.DELETE_PRICE.get());
		this.priceTable.getActionMap().put(WORKTIME_FUNCTIONS.DELETE_PRICE.get(), new EditViewActionListener(this.wtc, WORKTIME_FUNCTIONS.DELETE_PRICE));
		this.discountTable.setFillsViewportHeight(true);
		this.discountTable.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), WORKTIME_FUNCTIONS.DELETE_DISCOUNT.get());
		this.discountTable.getActionMap().put(WORKTIME_FUNCTIONS.DELETE_DISCOUNT.get(), new EditViewActionListener(this.wtc, WORKTIME_FUNCTIONS.DELETE_DISCOUNT));
		
		this.pricePane = new JScrollPane(this.priceTable);
		this.discountPane = new JScrollPane(this.discountTable);
		
		this.content = this.getContentPane();
		this.content.setLayout(new GridLayout(2,1));
		this.content.add(this.pricePane);
		this.content.add(this.discountPane);
	}
}
