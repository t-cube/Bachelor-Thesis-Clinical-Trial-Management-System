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
 * the Licensor�s name, logo, or trademarks.
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

package plugins.login.client.view;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import plugins.login.client.LoginController;
import plugins.login.client.listener.LoginActionListener;

import client.view.InnerView;

/**
 * @author torstend
 *
 */
public class LoginView extends InnerView {
	private static final long serialVersionUID = 2039928110467871640L;
	private JTextField txtUserName;
	private JPasswordField txtPassword;
	private JLabel lblUserName;
	private JLabel lblPassword;
	private JButton btnLogin;
	
	public LoginView(LoginController lc){
		super("Login", "LoginView");
		setUp(lc);
	}
	
	private void setUp(LoginController lc){
		Container pane = this.getContentPane();
		JPanel innerPane = new JPanel();
		FlowLayout l = new FlowLayout();
		
		innerPane.setSize(500,500);
		
		this.lblUserName = new JLabel("Username");
		this.lblPassword = new JLabel("Password");
		this.txtUserName = new JTextField();
		this.txtUserName.setColumns(5);
		this.txtPassword = new JPasswordField();
		this.btnLogin = new JButton("Log in");
		this.btnLogin.setActionCommand("Login");
		this.btnLogin.addActionListener(new LoginActionListener(lc));
		
		innerPane.setLayout(new GridLayout(5,1));
		innerPane.add(this.lblUserName);
		innerPane.add(this.txtUserName);
		innerPane.add(this.lblPassword);
		innerPane.add(this.txtPassword);
		innerPane.add(this.btnLogin);		
				
		pane.setLayout(l);
		pane.add(innerPane);
	}
	
	public String getUserName(){
		return this.txtUserName.getText();
	}
	
	public String getPassword(){
		return new String(this.txtPassword.getPassword());
	}
}