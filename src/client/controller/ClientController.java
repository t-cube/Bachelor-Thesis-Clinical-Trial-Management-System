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

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.AbstractButton;

import client.network.Client;
import client.view.MainView;
import client.controller.Controller;
import shared.system.AbstractController;
import shared.system.PlugInLoader;
import shared.system.Protocol;
import shared.util.Debugger;
import shared.util.ClinSysException;

/**
 * The ClientController class extends the Controller class of the client.controller
 * package and manages the the functionality of the client. That means it is the 
 * "brain" of the client, loading the plug-ins for the additional functionality,
 * starting the network connection and creating the GUI.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class ClientController extends Controller 
{
	
	/* Constants ----------------------------------------------------------------*/
	
	
	
	/* Variables ----------------------------------------------------------------*/
	
	// stores the main view object to allow showing the plug-in views
	private MainView mainView = null;  
	
	// stores all loaded plug-in controller objects
	private HashMap<String, Controller> controller = null;
	
	// stores the user information: ID, name and password
	private String userName = null;
	private String password = null;
	private int userId = -1;
	
	// stores if the user is logged in
	private boolean loggedIn = false;
	
	
	/* Constructor --------------------------------------------------------------*/
	
	/**
	 * "Empty" constructor of the class, the standard constructor.
	 * Initiates the super class with the plugin-name, sets the protocol to a newly
	 * created ClientProtocol object, creates an empty HashMap to store the loaded 
	 * plug-in controller, instantiates the main view and starts the 
	 * ClientController, that means loads the client plug-ins.
	 * 
	 * Writes a debug message in the console, that the ClientController Object was
	 * created.
	 * 
	 * @throws ClinSysException Thrown if the start function has any problems. 
	 * 							Most common are problems during the loading of the 
	 * 						    plug-ins.
	 */
	public ClientController() throws ClinSysException 
	{		
		super("ClientController");
		
		this.clientController = this;
		this.protocol = new ClientProtocol();
		this.controller = new HashMap<String, Controller>();
		this.mainView = new MainView(this);
		
		Debugger.debug("Created ClientController Object");
		
		this.start();		
	}
	
	
	/**
	 * Full constructor, expects a Protocol, a HashMap<String, Controller> and a 
	 * MainView object as arguments. 
	 * Assigns the arguments to the class attributes and starts the 
	 * ClientController, that means loads the client plug-ins.
	 * 
	 * Writes a debug message in the console, that the ClientController Object was
	 * created.
	 * 
	 * @param protocol An object interpretating the Protocol interface. Commonly a
 *					   ClientProtocol object. 			
	 * @param controller A HashMap containing the plug-in Controller objects 
	 * 					 indexed by the plug-in name.
	 * @param mainView A MainView object, showing the GUI and used to show the 
	 * 				   plug-in views.
	 * 
	 * @throws ClinSysException Thrown if the start function has any problems. 
	 * 							Most common are problems during the loading of the 
	 * 						    plug-ins
	 */
	public ClientController(Protocol protocol, 
					HashMap<String, Controller> controller, MainView mainView) 
					throws ClinSysException
	{
		super("ClientController");
		
		this.clientController = this;
		this.protocol = protocol;
		this.controller = controller;
		this.mainView = mainView;
		
		Debugger.debug("Created ClientController Object");
		
		this.start();
	}

	
	
	/* Getter -------------------------------------------------------------------*/
	
	/**
	 * Returns the MainView object so that plug-ins can call the pushView function
	 * to show their views.
	 * 
	 * @return MainView object alias the main GUI of the application.
	 */
	public MainView getMainView() 
	{
		return mainView;
	}

	
	/**
	 * Returns the HashMap object containing the Controller objects of the
	 * plug-ins, which are indexed by the plug-in name. Allows access to every 
	 * loaded client plug-in.
	 * 
	 * @return HashMap object containing the loaded plug-in Controller 
	 *         objects indexed by the plug-in name.
	 */
	public HashMap<String, Controller> getController() 
	{
		return controller;
	}
	
	
	/**
	 * Returns the name of the user, if he already logged in. If not it returns 
	 * null.
	 * 
	 * @return The name of the logged in user.
	 */
	public String getUserName() 
	{
		return userName;
	}

	
	/**
	 * Returns the password of the user, if he already logged in. If not it returns
	 * null.
	 * 
	 * ! CARE: It has to be evaluated, if the password have to be stored. 
	 * 
	 * @return The password of the logged in user.
	 */
	public String getPassword() 
	{
		return password;
	}

	
	/**
	 * Returns the ID of the user, if he already logged in. If not it returns -1.
	 * 
	 * @return The ID of the logged in user.
	 */
	public int getUserId()
	{
		return this.userId;
	}
	
	
	/**
	 * Returns if the user is logged in or not. Before a successful connection and
	 * login or after a successful logout the returned value will be false. After a
	 * successful login and during a stable connection it will be true. 
	 * 
	 * @return Boolean indicating if the user is logged in.
	 */
	public boolean isLoggedIn() 
	{
		return loggedIn;
	}		
	
	
	/* Setter -------------------------------------------------------------------*/
		

	/**
	 * Sets the MainView object alias the GUI. Which allow the plug-in controller 
	 * to show their views. 
	 * 
	 * @param mainView The MainView object, which should be set.
	 */
	public void setMainView(MainView mainView) 
	{
		this.mainView = mainView;
	}
	

	/**
	 * Sets the HashMap object, containing the plug-in controller objects. Used to
	 * grant the access on other plug-ins and their functionality. The controller
	 * objects must be indexed by the plug-in name, because the Controller objects
	 * are the entry point to a plug-in.
	 * 
	 * @param controller HashMap containing the Controller objects of the plug-ins.
	 */
	public void setController(HashMap<String, Controller> controller) 
	{
		this.controller = controller;
	}
	
	
	/**
	 * Stores the name of the user in the userName attribute. Which is later used
	 * for userspecific functions.
	 * 
	 * @param userName A String containing the username.
	 */
	public void setUserName(String userName) 
	{
		this.userName = userName;
	}

	
	/**
	 * Stores the password of the user in the password attribute. Which is later 
	 * used for userspecific functions. 
	 * 
	 * ! CARE: It has to be evaluated, if the password have to be stored. 
	 * 
	 * @param password A string containing the password.
	 */
	public void setPassword(String password) 
	{
		this.password = password;
	}
	
	
	/**
	 * Stores the id of the user in the userId attribute. Which can be later used
	 * for userspecific functions.
	 * 
	 * @param userId The id of the user as integer.
	 */
	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	
	/**
	 * Setting the logged in status of a user. Used to decided, if the start view
	 * should be showed or if the user is allowed to see other views.
	 * 
	 * @param loggedIn Boolean indicating if the user is logged in.
	 */
	public void setLoggedIn(boolean loggedIn) 
	{
		this.loggedIn = loggedIn;
	}

		
	
	/* Private ------------------------------------------------------------------*/
	
	
	/**
	 * Starting the ClientController object. That means it loads the client 
	 * plug-ins and sets the login view.
	 * 
	 * @throws ClinSysException Throwed if an error occurs during loading of the
	 * 							plugins or if the LoginController or View was not 
 * 								loaded.
	 */
	private void start() throws ClinSysException
	{
		Debugger.debug("Loading Client-PlugIns");
		this.loadPlugins();
		Debugger.debug("Client-PlugIns loaded: " + this.controller.size());
		if (this.getController().containsKey("LoginController"))
		{
			Debugger.debug("Set LoginView as MainView");
			if (this.getController("LoginController")
						.getViews().containsKey("LoginView"))
			{
				this.mainView.pushView(null);
			}
			else
			{
				Debugger.debug("LoginView was not loaded!");
				throw new ClinSysException("The Login-Plug-In was not loaded "
					+ "correctly. Please make sure it exists and is not broken.");
			}
		}
		else
		{
			Debugger.debug("LoginController was not loaded!");
			throw new ClinSysException("The Login-Plug-In was not loaded "
					+ "correctly. Please make sure it exists and is not broken.");
		}
	}
	
	
	/* Public -------------------------------------------------------------------*/
	
	
	/**
	 * Returns a single controller of a plug-in, that was specified. Mostly used by
	 * other plug-ins to use functions of a second plug-in.
	 *  
	 * @param pluginName The name of the plug-in, we want the controller of.
	 * @return The controller of the plug-in we selected.
	 */
	public Controller getController(String pluginName)
	{
		return this.controller.get(pluginName);
	}
	

	/**
	 * Sets the network connection for the client to establish the connection to
	 * the server. Stores the Client object in this object, as well as in every
	 * Controller object in the controller list. 
	 * 
	 * @param Client The client object, handling the network connection.
	 */
	public void setClient(Client client)
	{
		super.setClient(client);
		
		for (Controller c : this.controller.values())
		{
			c.setClient(client);
		}
	}
	
	
	/**
	 * Adds a controller object (that means the client side of a plug-in) to the 
	 * list of controller
	 * 
	 * @param controller The controller object (client side of the plug-in) that 
	 * 					 will be added.
	 */
	public void addController(Controller controller)
	{
		Debugger.debug("  |-- Add Controller: " + controller.getClass().getName());
		Debugger.debug("      |-- set ClientController");
		controller.setClientController(this);
		Debugger.debug("      |-- add controller to controllerlist");
		this.controller.put(controller.getPluginName(), controller);
		Debugger.debug("      |-- add menus");
		for (AbstractButton menu: controller.getMenus().values())
		{
			this.mainView.getMainViewMenuBar().add(menu);
		}		
	}
	
	
	/**
	 * Adds a list of controller to the controller list. For that it calls the 
	 * addController(Controller controller) function, for each controller in the 
	 * given list.
	 * 
	 * @param controller The controller objects (as list) (client side of the 
	 * 				     plug-in) that will be added.
	 */
	public void addController(Controller[] controller)
	{
		for (Controller c: controller)
		{
			this.addController(c);
		}
	}	
	
	
	/**
	 * Loads the the plug-ins for the client (that means only the client part of a
	 * plug-in). It iterates through the plugins folder and loads the classes for 
	 * each plug-in and adds the controller to the controller list.
	 * 
	 * @throws ClinSysException Throws an error if the plug-in could not be loaded.
	 */
	public void loadPlugins() throws ClinSysException
	{
		PlugInLoader pl = new PlugInLoader();
		
		pl.setPart(PlugInLoader.CLIENT);
		pl.setPluginPath("plugins");
		
		for (AbstractController c : pl.loadPlugins())
		{
			Debugger.debug("|-- Loaded: " + c.getPluginName());
			this.addController((Controller) c);
		}
		
		// später durch definierbare reihenfolge ersetzen
		for (AbstractController c : this.controller.values())
		{
			c.afterCreated();
		}
	}
	
	
	/**
	 * Closes the ClientController object and with this the whole client, as well
	 * as the GUI frame.
	 * 
	 * @throws ClinSysException If the super class can't be closed then an error 
	 * 							is thrown.
	 */
	public void close() throws ClinSysException
	{
		try 
		{
			super.close();
		} 
		catch (ClinSysException e) 
		{
			throw new ClinSysException("Could not close the superclass.");
		}
		
	    // this will make sure WindowListener.windowClosing()  will be called.
	    WindowEvent wev = new WindowEvent(this.mainView, 
	    							WindowEvent.WINDOW_CLOSING);
	    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);

	    // this will hide and dispose the frame, so that the application quits by
	    // itself if there is nothing else around. 
	    this.mainView.setVisible(false);
	    this.mainView.dispose();
	    // if you have other similar frames around, you should dispose them, too.
	    
	    // finally, call this to really exit
	    // i/o libraries such as WiiRemoteJ need this 
	    // also, this is what swing does for JFrame.EXIT_ON_CLOSE
	    System.exit(0); 
	}

	
	/* (non-Javadoc)
	 * @see shared.system.Protocol#interpretate(org.json.JSONObject, 
	 * shared.system.AbstractController)
	 */
	@Override
	public void afterCreated() 
	{
		// TODO Auto-generated method stub		
	}
}
