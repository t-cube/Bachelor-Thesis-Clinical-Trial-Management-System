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

package client.view;

import java.awt.Toolkit;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import client.controller.ClientController;
import shared.util.Debugger;

/**
 * The MainView object is the frame for the GUI windows of the plug-ins, which will
 * be showed inside the MainView frame. The MainView object handles showed 
 * InnerViews in a stack to allow a "Going-back" like function and show the 
 * previous View.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class MainView extends JFrame 
{	

	/* Constants ----------------------------------------------------------------*/
	
	// a serial, used for serialization
	private static final long serialVersionUID = 1L;

	
	/* Variables ----------------------------------------------------------------*/
	
	// the current InnerView object, which is shown atm
	private InnerView innerView;
	
	// the JMenuBar object, showing the menu items
	private JMenuBar menuBar;
	
	// reference to the ClientController object, to access client-wide 
	// functionality
	private ClientController cc;
	
	// the stack, storing opened views
	private Stack<InnerView> viewStack;
	

	/* Constructor --------------------------------------------------------------*/
	
	/**
	 * The constructor of the MainView object. Requires a reference to the
	 * ClientController, to access client-wide functionality.
	 * 
	 * @param cc Reference to the ClientController, as it is the calling object, 
	 * 			 which created the MainView object.
	 */
	public MainView(ClientController cc)
	{
		// Instantiating the super class JFrame with the title of the application
		super("ClinSys a Clinical Trial Management System - 2013 - Torsten Dietl");
		
		Debugger.debug("Creating MainView object");
		
		// set the reference to the ClientController and create a new Stack object
		this.cc = cc;
		this.viewStack = new Stack<InnerView>();
		
		// set the size of the frame to the ScreenSize (make it "Fullscreen")
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		// position it at the top left corner
		this.setLocation(0, 0);
		
		// !IMPORTANT!: Set the default close action, so that the frame thread
		// exits after the window is closed. Important to close the application.
		// If missing the application will run in the background without a visible
		// GUI frame.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// set up the main frame
		this.setUp();
		
		// make the frame visible 
		this.setVisible(true);
	}

	
	/* Getter -------------------------------------------------------------------*/
	
	/**
	 * Returns the main menu bar object, to add menu items.
	 * @return The menu bar object of the MainView.
	 */
	public JMenuBar getMainViewMenuBar()
	{
		return this.menuBar;
	}
	

	/* Setter -------------------------------------------------------------------*/
		
	
	/* Private ------------------------------------------------------------------*/
	
	/**
	 * Sets up the MainView object, that means it creates the menu bar object and 
	 * assigns it.
	 */
	private void setUp()
	{	
		Debugger.debug("Creating menu bar");
		this.menuBar = new JMenuBar();		
		this.setJMenuBar(menuBar);
	}
	
	
	/* Public -------------------------------------------------------------------*/
	
	/**
	 * Pushes a InnerView object into the stack and shows the given InnerView on 
	 * the screen. Only shows Views that are not the log-in window, if the user is
	 * already logged in.
	 * @param view The InnerView object, which should be shown.
	 */
	public void pushView(InnerView view){
		// if the user is logged in
		if (this.cc.isLoggedIn())
		{
			Debugger.debug("Set " + view.getName() + " as inner view");
			// show the menu bar
			this.menuBar.setVisible(true);
			// push the given InnerView object into the stack
			this.viewStack.push(view);
			// and set it as the active view
			this.innerView = view;			
		}
		else // else if the user is not logged in  
		{
			// print a debug message depending if an InnerView object is given 
			if (view!=null) 
			{
				Debugger.debug("Not logged in setting LoginView instead of " + 
								view.getName() + " as inner view");
			}
			else
			{
				Debugger.debug("Set LoginView as inner view");
			}
			// hide the menu bar
			this.menuBar.setVisible(false);
			// get the login view
			InnerView loginView = this.cc.getController("LoginController")
									.getViews().get("LoginView");
			// push it in the stack
			this.viewStack.push(loginView);
			// and set it as the active view
			this.innerView = loginView; 			
		}
		// remove all inner views
		this.getContentPane().removeAll();
		// add the active view
		this.getContentPane().add(this.innerView);
	}
	
	
	/**
	 * Pops the last shown view out of the stack and shows the previous one.
	 * 
	 * @return The last shown view, respectively the view that is shown while 
	 * 		   calling the method.
	 */
	public InnerView popView()
	{
		// get the last shown/active view
		InnerView lastView = this.viewStack.pop();
		// get the view that was shown before
		InnerView prevView = this.viewStack.lastElement();
		
		Debugger.debug("Close " + lastView.getName());
		Debugger.debug("Set " + prevView.getName() + " as inner view");
		
		// set the previous view as the active view
		this.innerView = prevView;
		// remove the last shown view
		this.getContentPane().removeAll();
		// show the previous view
		this.getContentPane().add(this.innerView);
		
		// return the last shown view, i.e. the view that was shown before calling
		// this method
		return lastView;
	}
}
