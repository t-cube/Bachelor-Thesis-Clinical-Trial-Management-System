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

package plugins.base.client;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import plugins.base.BASE_COMMANDS;
import plugins.base.client.listener.BaseMenuListener;
import shared.util.ClinSysException;
import client.controller.Controller;

/**
 * The Controller object (client side) of the Base plugin. It creates the menu
 * items, shows the depending views and controls what has to be done if a action
 * was done. 
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class BaseController extends Controller 
{

	/* Constants ----------------------------------------------------------------*/
	
	
	/* Variables ----------------------------------------------------------------*/	
	
	
	/* Constructor --------------------------------------------------------------*/
	
	public BaseController()
	{		
		super("BaseController");

		createMenus();
	}
	
	
	/* Getter -------------------------------------------------------------------*/
	

	/* Setter -------------------------------------------------------------------*/
		
	
	/* Private ------------------------------------------------------------------*/

	
	/**
	 * Creates the Menu for this plug-in.
	 */
	private void createMenus()
	{
		// start by creating the base menu listener
		BaseMenuListener bml = new BaseMenuListener(this);
		JMenu menu = new JMenu("General");
		
		// set up the settings menu item
		JMenuItem settings = new JMenuItem("Settings");
		settings.setActionCommand(BASE_COMMANDS.SHOW_SETTINGS.get());
		settings.addActionListener(bml);
		
		// and put in a separator just for the style
		JSeparator sep = new JSeparator();
		
		// set up the disconnect menu item
		JMenuItem disconnect = new JMenuItem("Disconnect");
		disconnect.setActionCommand(BASE_COMMANDS.DISCONNECT.get());
		disconnect.addActionListener(bml);
		
		// and for last the close menu item
		JMenuItem close = new JMenuItem("Close");
		close.setActionCommand(BASE_COMMANDS.CLOSE.get());
		close.addActionListener(bml);
		
		// add them all to the menu
		menu.add(settings);
		menu.add(sep);
		menu.add(disconnect);
		menu.add(close);
		
		// and put it to the menu list
		this.menus.put("General", menu);
	}
	
	
	/* Public -------------------------------------------------------------------*/
	
	/**
	 * This shows the settings view to allow the user change some settings.
	 */
	public void showSettings()
	{
		this.clientController.getMainView().pushView(this.views.get(
																"SettingsView"));
	}
	
	
	/**
	 * Closes the client application.
	 * 
	 * @throws ClinSysException Thrown if the ClientController object cannot be 
	 * 							closed, or the AbstractController cannot be closed.
	 */
	public void closeClient() throws ClinSysException
	{
		// try to close the AbstractController super class.
		try 
		{
			super.close();
		} 
		catch (ClinSysException e) 
		{
			throw new ClinSysException("((Client) GeneralController." + 
									   "closeClient()) : Could not close the " +
									   "super class");
		}
		
		// disconnect the client
		disconnect();
		
		// try to close the ClientController (the application)
		try 
		{
			this.clientController.close();
		} 
		catch (ClinSysException e) 
		{
			throw new ClinSysException("((Client) GeneralController." + 
									   "closeClient()) : Could not close the " +
								   	   "ClientController object");
		}
	}
	
	
	/**
	 * Disconnects a client from the server.
	 * 
	 * @throws ClinSysException Thrown if the Client object cannot be closed.
	 */
	public void disconnect() throws ClinSysException
	{
		if (this.client!=null) 
		{
			this.client.close();
		}
		this.clientController.getMainView().pushView(this.clientController.
											getController("LoginController").
												getViews().get("LoginView"));
	}

	
	@Override
	public void afterCreated() 
	{
		// TODO Auto-generated method stub
		
	}
	
}
