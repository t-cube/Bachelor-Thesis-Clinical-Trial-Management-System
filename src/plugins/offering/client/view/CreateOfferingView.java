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

package plugins.offering.client.view;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import plugins.offering.OFFERING_FUNCTIONS;
import plugins.offering.client.OfferingController;
import plugins.offering.client.listener.OfferingActionListener;
import client.view.InnerView;

/**
 * @author Torsten
 *
 */
public class CreateOfferingView extends InnerView {
	private static final long serialVersionUID = -4551859690560997891L;
	private JPanel pl_lblTemplate;
	private JLabel lblTemplate;
	private JPanel pl_cbTemplate;
	private JComboBox<String> cbTemplate;
	private JPanel pl_lblActivities;
	private JLabel lblActivities;
	private JPanel pl_cbActivities;
	private JComboBox<String> cbActivities;
	private JScrollPane paneActivities;
	private JTable tblActivities;
	private JPanel pl_lblCosts;
	private JLabel lblCosts;
	private JScrollPane paneCosts;
	private JTable tblCosts;
	private OfferingController oc;
	private Container content;
	private JButton btnSetActivity;
	private JButton btnCreateOffering;
	
	public CreateOfferingView(OfferingController oc){
		super("Creating an Offering", "CreateOfferingView");
		this.oc = oc;
		this.setUp();
	}
	
	
	
	/**
	 * @return the pl_lblTemplate
	 */
	public JPanel getPl_lblTemplate() {
		return pl_lblTemplate;
	}



	/**
	 * @return the lblTemplate
	 */
	public JLabel getLblTemplate() {
		return lblTemplate;
	}



	/**
	 * @return the pl_cbTemplate
	 */
	public JPanel getPl_cbTemplate() {
		return pl_cbTemplate;
	}



	/**
	 * @return the cbTemplate
	 */
	public JComboBox<String> getCbTemplate() {
		return cbTemplate;
	}



	/**
	 * @return the pl_lblActivities
	 */
	public JPanel getPl_lblActivities() {
		return pl_lblActivities;
	}



	/**
	 * @return the lblActivities
	 */
	public JLabel getLblActivities() {
		return lblActivities;
	}



	/**
	 * @return the pl_cbActivities
	 */
	public JPanel getPl_cbActivities() {
		return pl_cbActivities;
	}



	/**
	 * @return the cbActivities
	 */
	public JComboBox<String> getCbActivities() {
		return cbActivities;
	}



	/**
	 * @return the paneActivities
	 */
	public JScrollPane getPaneActivities() {
		return paneActivities;
	}



	/**
	 * @return the tblActivities
	 */
	public JTable getTblActivities() {
		return tblActivities;
	}



	/**
	 * @return the pl_lblCosts
	 */
	public JPanel getPl_lblCosts() {
		return pl_lblCosts;
	}



	/**
	 * @return the lblCosts
	 */
	public JLabel getLblCosts() {
		return lblCosts;
	}



	/**
	 * @return the paneCosts
	 */
	public JScrollPane getPaneCosts() {
		return paneCosts;
	}



	/**
	 * @return the tblCosts
	 */
	public JTable getTblCosts() {
		return tblCosts;
	}



	/**
	 * @return the oc
	 */
	public OfferingController getOc() {
		return oc;
	}



	/**
	 * @return the content
	 */
	public Container getContent() {
		return content;
	}



	/**
	 * @return the btnSetActivity
	 */
	public JButton getBtnSetActivity() {
		return btnSetActivity;
	}



	/**
	 * @return the btnCreateOffering
	 */
	public JButton getBtnCreateOffering() {
		return btnCreateOffering;
	}



	/**
	 * @param pl_lblTemplate the pl_lblTemplate to set
	 */
	public void setPl_lblTemplate(JPanel pl_lblTemplate) {
		this.pl_lblTemplate = pl_lblTemplate;
	}



	/**
	 * @param lblTemplate the lblTemplate to set
	 */
	public void setLblTemplate(JLabel lblTemplate) {
		this.lblTemplate = lblTemplate;
	}



	/**
	 * @param pl_cbTemplate the pl_cbTemplate to set
	 */
	public void setPl_cbTemplate(JPanel pl_cbTemplate) {
		this.pl_cbTemplate = pl_cbTemplate;
	}



	/**
	 * @param cbTemplate the cbTemplate to set
	 */
	public void setCbTemplate(JComboBox<String> cbTemplate) {
		this.cbTemplate = cbTemplate;
	}



	/**
	 * @param pl_lblActivities the pl_lblActivities to set
	 */
	public void setPl_lblActivities(JPanel pl_lblActivities) {
		this.pl_lblActivities = pl_lblActivities;
	}



	/**
	 * @param lblActivities the lblActivities to set
	 */
	public void setLblActivities(JLabel lblActivities) {
		this.lblActivities = lblActivities;
	}



	/**
	 * @param pl_cbActivities the pl_cbActivities to set
	 */
	public void setPl_cbActivities(JPanel pl_cbActivities) {
		this.pl_cbActivities = pl_cbActivities;
	}



	/**
	 * @param cbActivities the cbActivities to set
	 */
	public void setCbActivities(JComboBox<String> cbActivities) {
		this.cbActivities = cbActivities;
	}



	/**
	 * @param paneActivities the paneActivities to set
	 */
	public void setPaneActivities(JScrollPane paneActivities) {
		this.paneActivities = paneActivities;
	}



	/**
	 * @param tblActivities the tblActivities to set
	 */
	public void setTblActivities(JTable tblActivities) {
		this.tblActivities = tblActivities;
	}



	/**
	 * @param pl_lblCosts the pl_lblCosts to set
	 */
	public void setPl_lblCosts(JPanel pl_lblCosts) {
		this.pl_lblCosts = pl_lblCosts;
	}



	/**
	 * @param lblCosts the lblCosts to set
	 */
	public void setLblCosts(JLabel lblCosts) {
		this.lblCosts = lblCosts;
	}



	/**
	 * @param paneCosts the paneCosts to set
	 */
	public void setPaneCosts(JScrollPane paneCosts) {
		this.paneCosts = paneCosts;
	}



	/**
	 * @param tblCosts the tblCosts to set
	 */
	public void setTblCosts(JTable tblCosts) {
		this.tblCosts = tblCosts;
	}



	/**
	 * @param oc the oc to set
	 */
	public void setOc(OfferingController oc) {
		this.oc = oc;
	}



	/**
	 * @param content the content to set
	 */
	public void setContent(Container content) {
		this.content = content;
	}



	/**
	 * @param btnSetActivity the btnSetActivity to set
	 */
	public void setBtnSetActivity(JButton btnSetActivity) {
		this.btnSetActivity = btnSetActivity;
	}



	/**
	 * @param btnCreateOffering the btnCreateOffering to set
	 */
	public void setBtnCreateOffering(JButton btnCreateOffering) {
		this.btnCreateOffering = btnCreateOffering;
	}



	private void setUp(){		
		OfferingActionListener al = new OfferingActionListener(this.oc);
		
		this.lblTemplate = new JLabel("Study-Templates:");
		this.lblActivities = new JLabel("Activities:");
		this.lblCosts = new JLabel("Costs");
		
		this.cbTemplate = new JComboBox<String>();
		this.cbActivities = new JComboBox<String>();
		
		this.tblActivities = new JTable(new DefaultTableModel(new Object[0][3],new Object[]{"id","name","description"}));
		this.tblCosts = new JTable(new DefaultTableModel(new Object[0][2], new Object[]{"name","costs"}));
		
		this.btnSetActivity = new JButton("Add Activity");
		this.btnSetActivity.setActionCommand(OFFERING_FUNCTIONS.GET_ACTIVITIES.get());
		this.btnSetActivity.addActionListener(al);
		this.btnCreateOffering = new JButton("Create Offering");
		this.btnCreateOffering.setActionCommand(OFFERING_FUNCTIONS.CREATE_OFFERING.get());
		this.btnCreateOffering.addActionListener(al);

		this.pl_lblTemplate = new JPanel();
		this.pl_cbTemplate = new JPanel();
		this.pl_lblActivities = new JPanel();
		this.pl_cbActivities = new JPanel();
		this.pl_lblCosts = new JPanel();
		
		this.pl_lblTemplate.setLayout(new FlowLayout());
		this.pl_cbTemplate.setLayout(new FlowLayout());
		this.pl_lblActivities.setLayout(new FlowLayout());
		this.pl_cbActivities.setLayout(new FlowLayout());
		this.pl_lblCosts.setLayout(new FlowLayout());
		
		this.pl_lblTemplate.add(this.lblTemplate);
		this.pl_cbTemplate.add(this.cbTemplate);
		this.pl_lblActivities.add(this.lblActivities);
		this.pl_cbActivities.add(this.cbActivities);
		this.pl_cbActivities.add(this.btnSetActivity);
		this.pl_lblCosts.add(this.lblCosts);
		this.pl_lblCosts.add(this.btnCreateOffering);
		
		this.paneActivities = new JScrollPane(this.tblActivities);
		this.paneCosts = new JScrollPane(this.tblCosts);
		
		this.content = this.getContentPane();
		this.content.setLayout(new GridLayout(7,1));
		this.content.add(this.pl_lblTemplate);
		this.content.add(this.pl_cbTemplate);
		this.content.add(this.pl_lblActivities);
		this.content.add(this.pl_cbActivities);
		this.content.add(this.paneActivities);
		this.content.add(this.pl_lblCosts);
		this.content.add(this.paneCosts);
	}
}
