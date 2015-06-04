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

import server.model.DatabaseDictionary;
import server.network.Server;
import shared.system.AbstractController;
import shared.system.Protocol;
import shared.util.ClinSysException;


/**
 * Abstract class to derive controller classes for the server side of a plug-in.
 * 
 * @author Torsten Dietl
 * @version  1.0.0a
 */
public abstract class Controller extends AbstractController 
{	
	
	/* Constants ----------------------------------------------------------------*/
	

	/* Variables ----------------------------------------------------------------*/
	
	// the database dictionary containing the databases 
	// (connection, properties, names, etc.) 
	protected DatabaseDictionary model;
	
	// reference to the server controller, that means the "brain" of the server
	protected ServerController serverController;
	
	// the Server object, establishing, and handling the connections to the clients
	protected Server server;
	
	
	/* Constructor --------------------------------------------------------------*/
	
	/**
	 * Standard Constructor, which just instantiates the super class with the name
	 * of the plug-in.
	 */
	public Controller(String pluginName)
	{
		super(pluginName);
	}
	
	/**
	 * Constructor accepting and setting the model, the protocol, the network 
	 * socket and the server controller.
	 * 
	 * @param DatabaseDictionary The DatabaseDictionary, which is used by the 
	 * 							 server to request or transform the data.
	 * @param Protocol protocol  The protocol which is used to handle the messages.
	 * @param Server server      The Server object, which establishes and handles
	 * 							 the connection to the clients.  
	 * @param SererController serverController The "control center" of the server
	 * 										   application.
	 */
	public Controller(String pluginName, DatabaseDictionary model, 
					  Protocol protocol, Server ictmServerSocket, 
					  ServerController serverController)
	{
		super(pluginName, protocol);
		this.model = model;
		this.serverController = serverController;
		this.server = ictmServerSocket;
	}
	
	
	
	
	/* Getter ------------------------------------------------------------------ */
	
	/**
	 * Returns the database model that is used by the controller.
	 * @return DatabaseDictionary
	 */
	public DatabaseDictionary getModel() 
	{
		return this.model;
	}
	
	
	/**
	 * Returns the server controller, that has this plug-in installed.
	 * @return ServerController
	 */
	public ServerController getServerController() 
	{
		return this.serverController;
	}
			
	
	/**
	 * Returns the Server object, used to establish and handle connections to 
	 * the clients.
	 * 
	 * @return server Server object (Socket) handling the connections.
	 */
	public Server getServer() 
	{
		return server;
	}
	
	
	/* Setter ------------------------------------------------------------------ */


	/**
	 * Sets the model.
	 * @param DatabaseDictionary model
	 */
	public void setModel(DatabaseDictionary model) 
	{
		this.model = model;
	}

	/**
	 * Sets the ServerController, the "control center", of the application.  
	 * @param serverController The ServerController object, which should be checked
	 */
	public void setServerController(ServerController serverController) 
	{
		this.serverController = serverController;
	}
	
	/**
	 * Sets the network server socket that is used to communicate with the clients.
	 * 
	 * @param Server server
	 */
	public void setServer(Server server)
	{
		this.server = server;
	}
	
	
	/* Private ----------------------------------------------------------------- */
	
	
	
	/* Public ------------------------------------------------------------------ */
	
	/**
	 * Closes the Controller. For this it closes the reference to the server and 
	 * the model as well as it nulls the server, model, protocol and the 
	 * serverController references.
	 * 
	 * @throws ClinSysException Is thrown, if the server or the model cannot be
	 * 							closed. 
	 */
	public void close() throws ClinSysException
	{
			try 
			{
				// close server and model
				this.server.close();
				this.model.close();
				
				// null the references
				this.server = null;
				this.model = null;
				this.protocol = null;
				this.serverController = null;
			} 
			catch (ClinSysException e) // either server or model cannot be closed 
			{   // throw an exception
				throw new ClinSysException("((Serverside)Controller.close()) : " + 
										   "Could not close the ICTMServerSocket" +
										   "or the DatabaseDictionary object.");
			}
	}
}
