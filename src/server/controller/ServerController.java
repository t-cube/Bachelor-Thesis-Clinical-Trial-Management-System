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

package server.controller;

import java.io.IOException;
import java.util.HashMap;

import server.controller.Controller;


import server.model.DatabaseDictionary;
import server.network.Server;
import shared.system.AbstractController;
import shared.system.PlugInLoader;
import shared.system.Protocol;
import shared.util.Debugger;
import shared.util.ClinSysException;


/** 
 * Class that provides the basic functionalities of the server application. It is
 * the "control center" of the application.
 * 
 * @author Torsten Dietl	
 * @version 1.0.0a
 */
// TODO Exceptions müssen noch geschrieben werden!
public class ServerController extends Controller 
{
	
	/* Constants ----------------------------------------------------------------*/

	
	/* Variables ----------------------------------------------------------------*/
	
	// The HashMap object storing the Controller objects of the plug-ins, and with
	// this the "control-center" of the plug-ins, allowing the server application
	// or other plug-ins to use functionality of another plug-in
	private HashMap<String, Controller> controller;
	
	
	/* Constructor --------------------------------------------------------------*/
	
	/**
	 * The constructor of the ServerController. It requires the model and the 
	 * protocol to instantiate itself. For this it instantiates the super class,
	 * sets the model, and the protocol and the ServerController (in this case 
	 * itself), creates the HashMap to store the Controller objects of the plug-ins
	 * and instantiates the Server object and last but not least "starts" the 
	 * server application by calling the start method of the ServerController 
	 * object.
	 * 
	 * @param model The object containing the data or allowing and handling 
	 * 				connections to the database servers.
	 * @param protocol The object interpreting and handling the messages.
	 * @throws ClinSysException Is thrown if the start method fails. 
	 */
	public ServerController(DatabaseDictionary model, Protocol protocol) 
							throws ClinSysException
	{
		// instantiate the super class
		super("ServerController");
		
		// set the attributes
		this.setModel(model);
		this.setProtocol(protocol);
		this.setServerController(this);
		
		// set attributes that have to be created first
		this.controller = new HashMap<String, Controller>();
		this.setServer(new Server(this));
		
		// "start" the ServerController
		this.start();			
	}
	
	
	
	/* Getter ------------------------------------------------------------------ */
	
	/**
	 * Returns the complete HashMap with all plug-in controllers stored.
	 * 
	 * @return controller HashMap with the plug-in controllers stored by plug-in
	 * 					  name.
	 */
	public HashMap<String, Controller> getController() 
	{
		return this.controller;
	}

	/**
	 * Returns the controller specified by the name given.
	 * @param String The name of the plug-in.
	 * @return Controller The Controller object of the named plug-in.
	 */
	public Controller getController(String pluginName)
	{
		return this.controller.get(pluginName);
	}
	
	
	
	
	/* Setter ------------------------------------------------------------------ */
	
	/**
	 * Sets the given HashMap as the used HashMap for storing the controller of 
	 * the plug-ins. Allows to completely exchange plug-ins during the runtime. 
	 * Careful: It hasn't been tested yet.
	 * 
	 * @param controller The HashMap containing the controllers of the plug-ins 
	 * 					 which should be used.
	 */
	public void setController(HashMap<String, Controller> controller)
	{
		this.controller = controller;
	}
	
	
	
	
	
	/* Private ----------------------------------------------------------------- */
		
	/**
	 * "Starts" the ServerController, that means it loads the server side plug-ins 
	 * into the controller HashMap.
	 * @throws ClinSysException Is thrown if a plug-in can't be loaded.
	 */
	private void start() throws ClinSysException
	{
		// try to load all server-side plug-ins
		try
		{
			this.loadPlugIns();
		}
		// if it failed to load the plug-ins throw an error
		catch (ClinSysException e) 
		{
			throw new ClinSysException("(ServerController.start()) : Could not " + 
									   "load the plug-ins");
		}
		
		Debugger.debug("Server-PlugIns loaded: " + this.controller.size());
	}
	
	
	/* Public ------------------------------------------------------------------ */
	
	/**
	 * Adds a single controller to the controller HashMap.
	 * 
	 * @param controller The Controller object, which should be added to the
	 * 				     HashMap.
	 */
	public void addController(Controller controller)
	{
		// set the controllers attributes
		controller.setServerController(this);
		controller.setModel(this.model);
		controller.setServer(this.server);
		
		// add it to the HashMap, indexing it with the plug-in name
		this.controller.put(controller.getPluginName(), controller);
	}
	
	
	/**
	 * Adds a list of controller to the controller HashMap.
	 * 
	 * @param controller An array of Controller objects, that should be added.
	 */
	public void addController(Controller[] controller)
	{
		// go through each controller and add it to the Controller HashMap
		for (Controller c : controller)
		{
			this.addController(c);
		}
	}
		
	/**
	 * Installs a plug-in. That means it loads the file from the client to the 
	 * server and loads the plug-in controller into the HashMap, if it's a server
	 * side plug-in, so that the server doesn't have to restart.
	 */
	public void installPlugIns()
	{
		//TODO muss noch gemacht werden!
	}
	
	
	/**
	 * Loads all server-side plug-ins and adds the Controller objects to the 
	 * controller HashMap.
	 * 
	 * @throws ClinSysException Is thrown if a plug-in cannot be loaded.
	 */
	public void loadPlugIns() throws ClinSysException
	{
		// create a new PlugInLoader object
		PlugInLoader pl = new PlugInLoader();
		
		// initialize the PlugInLoader object 
		pl.setPart(PlugInLoader.SERVER); // add only the part for the server-side
		pl.setPluginPath("plugins"); // define the path to the plug-ins
		
		// try to go through each AbstractController object, that is returned
		// by the PlugInLoader objects loadPlugins method.
		// Then cast this AbstractController into a server.controller.Controller
		// object and add it to the HashMap
		try  
		{
			for (AbstractController c : pl.loadPlugins())
			{
				this.addController((Controller) c);
			}
		} 
		// if loading of the plug-ins failled throw an error
		catch (ClinSysException e) 
		{
			throw new ClinSysException("(ServerController.loadPlugIns()) : " +
									   "Could not load the plug-ins ");
		}	
	}
	
	
	/**
	 * Deinstall a plug-in, by removing its folder from the plugins folder,
	 * removing its controller from the HashMap and either send a message to the 
	 * clients that the plug-in was deinstalled or just catch error messages 
	 * if someone wants to access the plug-in on the server side	
	 */
	public void deinstallPlugIn()
	{
		//TODO muss noch gemacht werden!
	}
	
	
	/**
	 * Closes the ServerController object. For this it closes and nulls the 
	 * references, as well as closing all controllers stored in the HashMap.
	 * 
	 * @throws ClinSysException Is thrown if the model, the server or one of the 
	 * 							stored controller cannot be closed.
	 */
	public void close() throws ClinSysException
	{
		try 
		{
			// close model and server
			this.model.close();
			this.server.close();
			
			// null the references
			this.model = null;
			this.server = null;
			this.protocol = null;
			
			for (Controller c : this.controller.values())
			{
				c.close();
				c = null;
			}
			
			this.serverController = null;
		} 
		catch (ClinSysException e) //if something couldn't be closed throw an error 
		{
			throw new ClinSysException("(ServerController.close()) : " + 
									   "Could not close either the " + 
									   "DatabaseDictionary, the " + 
									   "ICTMServerSocket or a Controller object.");
		}
		
	}



	@Override
	public void afterCreated() 
	{
		// TODO Auto-generated method stub		
	}
}
