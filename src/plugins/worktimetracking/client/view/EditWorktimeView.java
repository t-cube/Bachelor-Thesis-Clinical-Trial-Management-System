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

import java.awt.BorderLayout;
import java.awt.Container;
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
public class EditWorktimeView extends InnerView {
	private static final long serialVersionUID = 8060921676456693462L;
	private JScrollPane scrollPane;
	private JTable table;
	private Container content;
	private WorktimeTrackingController wtc;
	
	public EditWorktimeView(WorktimeTrackingController wtc){
		super("Edits worktime(s)", "EditWorktimeView");
		this.wtc = wtc;
		this.setUp();
	}
	
	/**
	 * @return the scrollPane
	 */
	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	/**
	 * @return the table
	 */
	public JTable getTable() {
		return table;
	}

	/**
	 * @return the content
	 */
	public Container getContent() {
		return content;
	}
	
	/**
	 * @param scrollPane the scrollPane to set
	 */
	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(JTable table) {
		this.table = table;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(Container content) {
		this.content = content;
	}


	private void setUp(){				
		this.table = new JTable();
		this.table.setFillsViewportHeight(true);
		this.table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), WORKTIME_FUNCTIONS.DELETE_WORKTIME.get());
		this.table.getActionMap().put(WORKTIME_FUNCTIONS.DELETE_WORKTIME.get(), new EditViewActionListener(this.wtc, WORKTIME_FUNCTIONS.DELETE_WORKTIME));
		this.table.setRowSelectionAllowed(true);
		
		this.scrollPane = new JScrollPane(this.table);
		this.scrollPane.setBounds(620,160,600,420);
		
		this.content = this.getContentPane();
		this.content.setLayout(new BorderLayout());
		this.content.add(this.scrollPane, BorderLayout.CENTER);				
	}
}
