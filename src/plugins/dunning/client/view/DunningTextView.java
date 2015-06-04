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

package plugins.dunning.client.view;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import client.view.InnerView;

/**
 * @author torstend
 *
 */
public class DunningTextView extends InnerView {
	private static final long serialVersionUID = -870213662640471044L;
	private JLabel lblLanguage;
	private JComboBox<String> cbxLanguage;
	private JButton btnNewText;
	private JButton btnSaveText;
	private JLabel lblDunningText;
	private JLabel lblFields;
	private JScrollPane scrollDunningText;
	private JTextArea txtDunningText;
	private JScrollPane scrollFields;
	private JList<String> lstFields;
	private Container content;
	
	public DunningTextView() {
		super("Dunning texts", "DunningTextView");
		
		setUp();
	}
	
	/**
	 * @return the lblLanguage
	 */
	public JLabel getLblLanguage() {
		return lblLanguage;
	}

	/**
	 * @return the cbxLanguage
	 */
	public JComboBox<String> getCbxLanguage() {
		return cbxLanguage;
	}

	/**
	 * @return the btnNewText
	 */
	public JButton getBtnNewText() {
		return btnNewText;
	}

	/**
	 * @return the btnSaveText
	 */
	public JButton getBtnSaveText() {
		return btnSaveText;
	}

	/**
	 * @return the lblDunningText
	 */
	public JLabel getLblDunningText() {
		return lblDunningText;
	}

	/**
	 * @return the lblFields
	 */
	public JLabel getLblFields() {
		return lblFields;
	}

	/**
	 * @return the scrollDunningText
	 */
	public JScrollPane getScrollDunningText() {
		return scrollDunningText;
	}

	/**
	 * @return the txtDunningText
	 */
	public JTextArea getTxtDunningText() {
		return txtDunningText;
	}

	/**
	 * @return the scrollFields
	 */
	public JScrollPane getScrollFields() {
		return scrollFields;
	}

	/**
	 * @return the lstFields
	 */
	public JList<String> getLstFields() {
		return lstFields;
	}

	/**
	 * @return the content
	 */
	public Container getContent() {
		return content;
	}


	private void setUp(){
		SpringLayout l = new SpringLayout();
		
		this.content = this.getContentPane();

		this.lblDunningText = new JLabel("Dunning Text");
		this.lblFields = new JLabel("Fields");
		this.lblLanguage = new JLabel("Language");
		
		this.btnNewText = new JButton("New Text");
		this.btnSaveText = new JButton("Save Text");
		
		this.cbxLanguage = new JComboBox<String>();
		this.cbxLanguage.setPreferredSize(new Dimension(50, (int) this.cbxLanguage.getPreferredSize().getHeight()));
		this.cbxLanguage.setEditable(true);
		this.cbxLanguage.setToolTipText("In ISO 639-1/IETF-Format: e.g. English -> en, German -> de, etc.");
		
		this.txtDunningText = new JTextArea();

		this.scrollDunningText = new JScrollPane(this.txtDunningText);
		
		this.lstFields = new JList<String>();
		this.lstFields.setPreferredSize(new Dimension(200, 600));
		
		this.scrollFields = new JScrollPane(this.lstFields);
		
		l.putConstraint(SpringLayout.NORTH, this.lblLanguage, 5, SpringLayout.NORTH, this.content);
		l.putConstraint(SpringLayout.NORTH, this.cbxLanguage, 5, SpringLayout.NORTH, this.content);

		l.putConstraint(SpringLayout.NORTH, this.lblDunningText, 15, SpringLayout.SOUTH, this.cbxLanguage);
		l.putConstraint(SpringLayout.NORTH, this.lblFields, 15, SpringLayout.SOUTH, this.cbxLanguage);
		
		l.putConstraint(SpringLayout.NORTH, this.scrollDunningText, 5, SpringLayout.SOUTH, this.lblDunningText);
		l.putConstraint(SpringLayout.NORTH, this.scrollFields, 5, SpringLayout.SOUTH, this.lblFields);
		
		l.putConstraint(SpringLayout.SOUTH, this.scrollDunningText, 0, SpringLayout.SOUTH, this.scrollFields);
		
		l.putConstraint(SpringLayout.NORTH, this.btnNewText, 15, SpringLayout.SOUTH, this.scrollDunningText);
		l.putConstraint(SpringLayout.NORTH, this.btnSaveText, 15, SpringLayout.SOUTH, this.scrollDunningText);
		
		l.putConstraint(SpringLayout.WEST, this.lblLanguage, 5, SpringLayout.WEST, this.content);
		l.putConstraint(SpringLayout.WEST, this.cbxLanguage, 15, SpringLayout.EAST, this.lblLanguage);
		
		l.putConstraint(SpringLayout.EAST, this.scrollFields, -5, SpringLayout.EAST, this.content);
		l.putConstraint(SpringLayout.WEST, this.lblFields, 0, SpringLayout.WEST, this.scrollFields);
		
		l.putConstraint(SpringLayout.WEST, this.lblDunningText, 15, SpringLayout.WEST, this.content);
		l.putConstraint(SpringLayout.WEST, this.scrollDunningText, 15, SpringLayout.WEST, this.content);
		
		l.putConstraint(SpringLayout.EAST, this.scrollDunningText, -15, SpringLayout.WEST, this.scrollFields);
		
		l.putConstraint(SpringLayout.WEST, this.btnNewText, 50, SpringLayout.WEST, this.content);
		l.putConstraint(SpringLayout.WEST, this.btnSaveText, 50, SpringLayout.EAST, this.btnNewText);
		
		this.content.setLayout(l);
		
		this.content.add(this.lblLanguage);
		this.content.add(this.cbxLanguage);
		this.content.add(this.lblDunningText);
		this.content.add(this.lblFields);
		this.content.add(this.scrollDunningText);
		this.content.add(this.scrollFields);
		this.content.add(this.btnNewText);
		this.content.add(this.btnSaveText);
	}
}
