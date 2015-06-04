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

package plugins.settings.client.view;

import java.awt.Color;
import java.awt.Container;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;

import client.view.InnerView;

/**
 * @author torstend
 *
 */
public class SettingsView extends InnerView {
	private static final long serialVersionUID = -2804568654398578586L;
	// General components
	private JTabbedPane tabs;
	private JPanel tab1;
	private JPanel tab2;
	private JPanel tab3;
	private Container content;
	// Tab1 components
	private JComboBox<String> cbxDBMS;
	private JButton cmbDriverPath;
	private JLabel lblDriverPath;
	// Tab2 components
	
	// Tab3 components
	private JButton cmbPluginPath;
	private JLabel lblPluginPath;
	private JComboBox<String> cbxPlugins;
	private JLabel lblMenu;
	private JCheckBox chkMenu;	
	
	public SettingsView(){
		super("Settings", "SettingsView");
		setUp();
	}
	
	private void setUp(){
		this.content = this.getContentPane();
		
		this.tab1 = new JPanel();
		setUpTab1();
		this.tab2 = new JPanel();
		setUpTab2();
		this.tab3 = new JPanel();
		setUpTab3();
		
		
		this.tabs = new JTabbedPane();
		this.tabs.addTab("General", tab1);
		this.tabs.addTab("Home View", tab2);
		this.tabs.addTab("Plug-Ins", tab3);
		
		this.content.add(tabs);
	}
	
	private void setUpTab1(){
		SpringLayout l = new SpringLayout();
		
		this.cbxDBMS = new JComboBox<String>();
		this.cmbDriverPath = new JButton("Set path to driver libary");
		this.lblDriverPath = new JLabel("<Driver Path>");
		this.lblDriverPath.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		this.lblDriverPath.setMinimumSize(this.cmbDriverPath.getMinimumSize());
		this.lblDriverPath.setPreferredSize(this.cmbDriverPath.getMinimumSize());
		this.lblDriverPath.setSize(this.cmbDriverPath.getMinimumSize());
		
		this.tab1.add(this.cbxDBMS);		
		this.tab1.add(this.lblDriverPath);
		this.tab1.add(this.cmbDriverPath);
		
		this.tab1.setLayout(l);
		
		l.putConstraint(SpringLayout.WEST, this.cbxDBMS, 50, SpringLayout.WEST, this.tab1);
		l.putConstraint(SpringLayout.NORTH, this.cbxDBMS, 5, SpringLayout.NORTH, this.tab1);
		l.putConstraint(SpringLayout.EAST, this.tab1, 50, SpringLayout.EAST, this.cbxDBMS);

		l.putConstraint(SpringLayout.NORTH, this.cmbDriverPath, 10, SpringLayout.SOUTH, this.cbxDBMS);
		l.putConstraint(SpringLayout.EAST, this.cmbDriverPath, -50, SpringLayout.EAST, this.tab1);
		
		l.putConstraint(SpringLayout.WEST, this.lblDriverPath, 50, SpringLayout.WEST, this.tab1);
		l.putConstraint(SpringLayout.NORTH, this.lblDriverPath, 10, SpringLayout.SOUTH, this.cbxDBMS);
		l.putConstraint(SpringLayout.EAST, this.lblDriverPath, -5, SpringLayout.WEST, this.cmbDriverPath);	
				
		this.tab1.setLayout(l);
	}
	
	private void setUpTab2(){
		
	}
	
	private void setUpTab3(){
		SpringLayout l = new SpringLayout();
		
		this.cmbPluginPath = new JButton("Set path to plug-in folder");
		this.lblPluginPath = new JLabel("<Plug-in folder path>");
		this.lblPluginPath.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		this.lblPluginPath.setMinimumSize(this.cmbPluginPath.getMinimumSize());
		this.lblPluginPath.setPreferredSize(this.cmbPluginPath.getMinimumSize());
		this.lblPluginPath.setSize(this.cmbPluginPath.getMinimumSize());
		this.cbxPlugins = new JComboBox<String>();
		this.lblMenu = new JLabel("Has own menu item");
		this.chkMenu = new JCheckBox();
		
		this.tab3.add(this.cmbPluginPath);	
		this.tab3.add(this.lblPluginPath);
		this.tab3.add(this.cbxPlugins);
		this.tab3.add(this.lblMenu);
		this.tab3.add(this.chkMenu);	
		
		l.putConstraint(SpringLayout.EAST, this.cmbPluginPath, -50, SpringLayout.EAST, this.tab3);
		l.putConstraint(SpringLayout.NORTH, this.cmbPluginPath, 5, SpringLayout.NORTH, this.tab3);
		
		l.putConstraint(SpringLayout.WEST, this.lblPluginPath, 50, SpringLayout.WEST, this.tab3);
		l.putConstraint(SpringLayout.NORTH, this.lblPluginPath, 5, SpringLayout.NORTH, this.tab3);
		l.putConstraint(SpringLayout.EAST, this.lblPluginPath, -5, SpringLayout.WEST, this.cmbPluginPath);
		
		l.putConstraint(SpringLayout.WEST, this.cbxPlugins, 50, SpringLayout.WEST, this.tab3);
		l.putConstraint(SpringLayout.NORTH, this.cbxPlugins, 10, SpringLayout.SOUTH, this.lblPluginPath);
		l.putConstraint(SpringLayout.EAST, this.cbxPlugins, -50, SpringLayout.EAST, this.tab3);
		
		l.putConstraint(SpringLayout.WEST, this.lblMenu, 50, SpringLayout.WEST, this.tab3);
		l.putConstraint(SpringLayout.NORTH, this.lblMenu, 10, SpringLayout.SOUTH, this.cbxPlugins);
		
		l.putConstraint(SpringLayout.WEST, this.chkMenu, 0, SpringLayout.WEST, this.cmbPluginPath);
		l.putConstraint(SpringLayout.NORTH, this.chkMenu, 10, SpringLayout.SOUTH, this.cbxPlugins);

		this.tab3.setLayout(l);
	}
}
