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

package client.controller;

import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.JMenu;

import client.network.Client;
import client.view.InnerView;
import shared.system.AbstractController;
import shared.system.Protocol;
import shared.util.Debugger;


/**
 * This class is an subclass of the AbstractController class, which extends the 
 * class by adding client specific functions. That means it expects a Client object
 * for the network connection as well as it stores View and Menu objects for the
 * GUI part of the client application respectively the client part of a plug-in.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public abstract class Controller extends AbstractController 
{
	
	/* Constants ----------------------------------------------------------------*/	
	
	
	/* Variables ----------------------------------------------------------------*/
	
	// The Client object, responsible for the network connection to the server
	// application
	protected Client client;
	
	// The ClientController object, mostly used for plug-ins to access functions
	// of the client application itself, or other plug-ins
	protected ClientController clientController;
	
	// A HashMap object containing the menu items of the main menu, which  
	// execute functions of the associated Controller object
	protected HashMap<String, AbstractButton> menus;
	
	// A HashMap object containing all InnerView objects associated with and used
	// for/by this Controller object
	protected HashMap<String, InnerView> views;
	
	
	/* Constructor --------------------------------------------------------------*/
	
	/**
	 * Simplest constructor for a client-sided Controller class. Only expecting the
	 * plug-in name, to identify the object later. Furthermore it creates a new, 
	 * empty HashMap object for either the InnerView and the Menu objects. 
	 * 
	 * @param pluginName The name of the plug-in the controller works for.
	 */
	public Controller(String pluginName) 
	{
		super(pluginName);
		this.views = new HashMap<String, InnerView>();
		this.menus = new HashMap<String, AbstractButton>();
		Debugger.debug("Controller object created");
	}
	
	
	/**
	 * Constructor, which creates a Controller object with empty HashMap objects 
	 * for the InnerView and Menu objects, but with given plug-in name and 
	 * ClientController, Client and Protocol object. 
	 * 
	 * @param pluginName The name of the plug-in the controller works for.
	 * @param clientController The Controller object of the client application. It 
	 * 						   is the Controller object handling all plug-in 
	 * 						   controller.
	 * @param client The Client object establishing and working with the network
	 * 				 connection.
	 * @param protocol The Protocol object, interpreting the incoming messages and
	 * 				   formatting the outgoing messages.
	 */
	public Controller(String pluginName, ClientController clientController, 
					  Client client, Protocol protocol)
	{
		super(pluginName, protocol);
		this.clientController = clientController;
		this.client = client;
		this.views = new HashMap<String, InnerView>();
		this.menus = new HashMap<String, AbstractButton>();
		Debugger.debug("Controller object created");
	}
	
	
	/**
	 * Constructor, which creates a Controller object completely with the given 
	 * parameters.
	 * 
	 * @param pluginName The name of the plug-in the controller works for.
	 * @param clientController The Controller object of the client application. It 
	 * 						   is the Controller object handling all plug-in 
	 * 						   controller.
	 * @param client The Client object establishing and working with the network
	 * 				 connection.
	 * @param protocol The Protocol object, interpreting the incoming messages and
	 * 				   formatting the outgoing messages.
	 * @param menus A HashMap object, either empty or containing already some Menu
	 * 				objects.
	 * @param views A HashMap object, either empty or containing already some 
	 * 				InnerView objects.
	 */
	public Controller(String pluginName, ClientController clientController, 
			          Client client, Protocol protocol, 
			          HashMap<String, AbstractButton> menus, 
			          HashMap<String, InnerView> views)
	{
		super(pluginName, protocol);
		this.clientController = clientController;
		this.client = client;
		this.menus = menus;
		this.views = views;
		Debugger.debug("Controller object created");
	}
	
	
	
	/* Getter ------------------------------------------------------------------ */
	
	/**
	 * Returns the Client object. Used by the controller to send messages to the 
	 * server.
	 * 
	 * @return The used Client object.
	 */
	public Client getClient() 
	{
		return client;
	}
	

	/**
	 * Returns the ClientController object. Used by a plug-in controller to access
	 * and use functions of the ClientController (client application) or other 
	 * plug-ins.
	 * 
	 * @return The ClientController object (the "brain" of the client application).
	 */
	public ClientController getClientController() 
	{
		return clientController;
	}

	
	/**
	 * Returns the HashMap containing the Menu objects. Used by the 
	 * ClientController object (client application) to add the Menu objects of a
	 * plug-in to the application menu bar.
	 * 
	 * @return The HashMap object containing the Menu objects of a plug-in.
	 */
	public HashMap<String, AbstractButton> getMenus() 
	{
		return menus;
	}
	

	/**
	 * Returns the HashMap containing the InnerView objects. Used mostly by a 
	 * Controller object to show an InnerView object by pushing it into the 
	 * InnerView stack of the MainView object.
	 * 
	 * @return The HashMap containing the InnerView objects of a plug-in.
	 */
	public HashMap<String, InnerView> getViews() 
	{
		return views;
	}
			
	
	/* Setter ------------------------------------------------------------------ */
	
	
	/**
	 * Sets the given Client object to be used by this controller, for sending
	 * messages to the server.  
	 * 
	 * @param client Client object, which will be used by this controller.
	 */
	public void setClient(Client client) 
	{
		this.client = client;
	}
	

	/**
	 * Sets the ClientController object used by this controller, to access 
	 * functions of the client application or another plug-in.
	 * 
	 * @param clientController ClientController object, which will be used.
	 */
	public void setClientController(ClientController clientController) 
	{
		this.clientController = clientController;
	}

	/**
	 * Overwrites the existing Menu HashMap with the given one. Can be used to 
	 * easily exchange menus of a plug-in. 
	 * 
	 * @param menus HashMap containing Menu objects, that overwrites the current
	 * 				Menu HashMap of the controller. 
	 */
	public void setMenus(HashMap<String, AbstractButton> menus) 
	{
		this.menus = menus;
	}

	/**
	 * Overwrites the existing InnerView HashMap with the given one. Can be used to 
	 * easily exchange InnerViews of a plug-in. 
	 * 
	 * @param views HashMap containing InnerView objects, that overwrites the 
	 * 				current InnerView HashMap of the controller.
	 */
	public void setViews(HashMap<String, InnerView> views) 
	{
		this.views = views;
	}
	
	
	
	/* Private ----------------------------------------------------------------- */
	
	
	
	
	/* Public ------------------------------------------------------------------ */
	
	
	/**
	 * Adds an InnerView object to the HashMap of InnerViews used by this 
	 * controller. Allows controller to show this InnerView as well as other 
	 * plug-ins can add a InnerView for an existing plug-in to extend it.  
	 * 
	 * @param view InnerView object, that should be added to the HashMap of
	 * 			   InnerView objects of this controller.
	 */
	public void addView(InnerView view)
	{
		Debugger.debug("Add view " + view.getName() + " to " + 
					   this.getPluginName());
		
		this.views.put(view.getName(), view);
	}
		

	/**
	 * Adds a list of InnerView objects to the HashMap of InnerViews used by this 
	 * controller. Allows controller to show this InnerViews as well as other 
	 * plug-ins can add InnerViews for an existing plug-in to extend it.  
	 * 
	 * @param view List of InnerView objects, that should be added to the HashMap 
	 * 			   of InnerView objects of this controller.
	 */
	public void addView(InnerView[] views)
	{
		for (InnerView view : views)
		{
			this.addView(view);
		}
	}
	
	
	/**
	 * Adds an Menu object to the HashMap of Menus used by this 
	 * controller. Allows controller to add this Menu to the menu bar as well as 
	 * other plug-ins can add a Menu for an existing plug-in to extend it.  
	 * 
	 * @param menu Menu object, that should be added to the HashMap of
	 * 			   Menu objects of this controller.
	 */	
	public void addMenu(JMenu menu)
	{
		Debugger.debug("Add menu " + menu.getName() + " to " + 
					   this.getPluginName());
		
		this.menus.put(menu.getName(), menu);
	}
	
	
	/**
	 * Adds a list of Menu objects to the HashMap of Menus used by this 
	 * controller. Allows controller to add this Menus to the menu bar as well as 
	 * other plug-ins can add Menus for an existing plug-in to extend it.  
	 * 
	 * @param menu List of Menu objects, that should be added to the HashMap of
	 * 			   Menu objects of this controller.
	 */		
	public void addMenu(JMenu[] menus)
	{
		for (JMenu menu : menus)
		{
			this.addMenu(menu);
		}
	}
}
