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

package server.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

import plugins.base.BASE_COMMANDS;


import server.controller.ServerController;
import server.model.SQLCreator;
import server.model.enumerations.CONCAT;
import server.model.enumerations.RIGHTS;
import server.model.enumerations.SQLITEMTYPE;
import server.model.enumerations.SQLWORD;
import server.model.enumerations.STATEMENTTYPE;
import shared.json.JSONObject;
import shared.network.MESSAGE_TYPES;
import shared.network.RECIPIENT;
import shared.system.MessageCoder;
import shared.util.Debugger;
import shared.util.ClinSysException;
import shared.util.RecordSet;

/**
 * The Server class uses sockets to start an accepting server, and adds the 
 * connecting clients to an array list. 
 * It as well sends messages to clients (encoded using the MessageCoder class) 
 * and handles messages (decoded using the MessageCoder class and return the result
 * to the server controller)
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class Server implements Runnable 
{
	
	/* Constants ----------------------------------------------------------------*/
	
	
	/* Variables ----------------------------------------------------------------*/
	
	// the list containing ServerThread objects to each connected client
	private HashMap<String, ServerThread> clientList = null;
	
	// Thread object for this class to make it run in parallel
	private Thread thread = null;
	
	// The actual "Server", it's the socket to accept incoming connections
	private ServerSocket serverSocket = null;
	
	// this is a temporary socket to handle an unidentified client connection
	// until it's checked whether the user name and password combination was
	// correct, the client will then be added to the client list, and the temporary
	// socket is free again
	private Socket tempClientSocket = null;
	
	// A HashMap storing the temporary client sockets. Allowing several clients to
	// connect at the same time and to check their login data successively
	private HashMap<Long, ServerThread> tempClientThreads = null;
	
	// The standard port number the server uses. 
	private int port = 55555;
	
	// Reference to the ServerController, the "control-center" of the server #
	// application
	private ServerController serverController;
	
	// flag indicating if the server has stopped or not
	private boolean stopped = false;
	
	
	/* Constructor --------------------------------------------------------------*/
	
	
	/**
	 * Constructor, which initiates the client list, the server socket and starts a 
	 * thread.
	 * 
	 * @param serverController The "control center" of the Server application.
	 */
	public Server(ServerController serverController)
	{
		Debugger.debug("Creating an Server object");
		
		// create an empty HashMap to store the ServerThread objects handling the 
		// client connections
		this.clientList = new HashMap<String, ServerThread>();
		
		// set the ServerController
		this.serverController = serverController;
		
		// Create a HashMap for the temporary ServerThread objects
		this.tempClientThreads = new HashMap<Long, ServerThread>();
		
		stopped = false;
		
		// put this object in a Thread and start it
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	
	/* Getter ------------------------------------------------------------------ */
	
	/**
	 * Returns the whole client list, to for example iterate through them.
	 * 
	 * @return The complete list of clients
	 */
	public HashMap<String, ServerThread> getClientList() 
	{
		return clientList;
	}
	
	/**
	 * Returns a single client, indexed by the given user name.
	 * 
	 * @param userName The name of the user using the client application, which
	 * 				   should be returned.
	 * @return The ServerThread object, handling the connection to the specified 
	 * 		   client.
	 */
	public ServerThread getClient(String userName)
	{
		return clientList.get(userName);
	}

	


	/* Setter ------------------------------------------------------------------ */
	
	/**
	 * Sets the whole client list. Used to complete exchange the connected clients.
	 * 
	 * @param clientList A HashMap containing the ServerThreads, which handle the
	 * 					 connections to the clients, indexed by the user name of
	 * 					 the user using the client application.
	 */
	public void setClientList(HashMap<String, ServerThread> clientList) 
	{
		this.clientList = clientList;
	}
		
	
	
	/* Private ----------------------------------------------------------------- */
	
	/**
	 * The method reads out the user names of all users, who are in the given 
	 * user group. 
	 * 
	 * @param groupName The name of the group to find the users of.
	 * @return String array containing the names of all users in the given group.
	 * @throws ClinSysException Is thrown if used sql statements cannot be created.
	 */
	private String[] getUserNamesOfGroup(String groupName) throws ClinSysException
	{
		// SQLCreator object for the statement selecting the user data
		SQLCreator sqlUser = null;
		
		// SQLCreator object for the statement selecting the group data
		SQLCreator sqlGroup = null;
		
		// SQLCreator object for the statement selecting the relations of user
		// and groups
		SQLCreator sqlUserUsergroups = null;
		
		// SQLCreator object for the statement joining the sqlGroup with the 
		// sqlUserUsergroup object
		SQLCreator sqlJoinGroup = null;
		
		// SQLCreator object for the statement joining the sqlJoinGroup with the 
		// sqlUser object
		SQLCreator sqlJoin = null;
		
		// RecordSet object containing the result data of the sql statement
		RecordSet result = null;
		
		// try to create the sql statement selecting the group data 
		try 
		{
			sqlGroup = new SQLCreator();
			sqlGroup.setStatementType(STATEMENTTYPE.SELECT);
			sqlGroup.addItem(new String[]{"id","name"}, SQLITEMTYPE.FIELD);
			sqlGroup.addItem(SQLWORD.FROM);
			sqlGroup.addItem("ictm_usergroups", SQLITEMTYPE.TABLE);
			sqlGroup.addItem(SQLWORD.WHERE);
			sqlGroup.addFieldItemPair("name", "=" , groupName, SQLITEMTYPE.TEXT);
		} 
		catch (ClinSysException e) 
		{
			throw new ClinSysException("(ICTMServerSocket.getUserNamesOfGroup(" +
									   "String groupName)) : Could not create " + 
									   "the SQLCreator object 'sqlGroup'");
		}
		
		// try to create the sql statement selecting the user-group relations
		try 
		{
			sqlUserUsergroups = new SQLCreator();
			sqlUserUsergroups.setStatementType(STATEMENTTYPE.SELECT);
			sqlUserUsergroups.addItem(new String[]{"user_id","usergroup_id"}, 
										SQLITEMTYPE.FIELD);
			sqlUserUsergroups.addItem(SQLWORD.FROM);
			sqlUserUsergroups.addItem("ictm_usergroups", SQLITEMTYPE.TABLE);
		} 
		catch (ClinSysException e) 
		{
			throw new ClinSysException("(ICTMServerSocket.getUserNamesOfGroup(" + 
									   "String groupName)) : Could not create " + 
								   	   "the SQLCreator object " +
									   "'sqlUserUsergroupsGroup'");
		}
		
		// try to create the sql statement joining the group with the relation
		// data
		try 
		{
			sqlJoinGroup = new SQLCreator();
			sqlJoinGroup.setLeftPart(sqlGroup);
			sqlJoinGroup.setRightPart(sqlUserUsergroups);
			sqlJoinGroup.setConcat(CONCAT.INNER, "Group", "UserUsergroup");
			sqlJoinGroup.setOnFields(new String[]{"Group.id", 
										"UserUsergroup.usergroup_id"});
		} 
		catch (ClinSysException e) 
		{
			throw new ClinSysException("(ICTMServerSocket.getUserNamesOfGroup(" + 
									   "String groupName)) : Could not create " + 
									   "the SQLCreator object 'sqlJoinGroup'");
		}
		
		// try to create the sql statement selecting the user data
		try 
		{
			sqlUser = new SQLCreator();
			sqlUser.setStatementType(STATEMENTTYPE.SELECT);
			sqlUser.addItem(new String[]{"id","name"}, SQLITEMTYPE.FIELD);
			sqlUser.addItem(SQLWORD.FROM);
			sqlUser.addItem("ictm_user", SQLITEMTYPE.TABLE);
		} 
		catch (ClinSysException e) 
		{
			throw new ClinSysException("(ICTMServerSocket.getUserNamesOfGroup(" + 
									   "String groupName)) : Could not create " +
									   "the SQLCreator object 'sqlUser'");
		}
		
		// try to create the sql statement joining the joined group and relation 
		// data with the user data
		try 
		{
			sqlJoin = new SQLCreator();
			sqlJoin.setLeftPart(sqlJoinGroup);
			sqlJoin.setRightPart(sqlUser);
			sqlJoin.setConcat(CONCAT.INNER, "JoinGroup", "User");
			sqlJoin.setOnFields(new String[]{"JoinGroup.user_id", "User.id"});
		} 
		catch (ClinSysException e) 
		{
			throw new ClinSysException("(ICTMServerSocket.getUserNamesOfGroup(" + 
									   "String groupName)) : Could not create " + 
								   	   "the SQLCreator object 'sqlJoin'");
		}
		
		// try to get the result of the sql statement selecting the joined data of
		// user, groups and their relations
		//TODO wirklich mit Admin machen? oder doch auf nutzer beziehen?!?!?
		try 
		{
			result = this.serverController.getModel().executeSQL(sqlJoin, "admin",
							"getUserNamesOfGroup", "Server", RIGHTS.READ.get());
		} 
		catch (ClinSysException e) 
		{
			throw new ClinSysException("(ICTMServerSocket.getUserNamesOfGroup(" +
									   "String groupName)) : The created " + 
								   	   "SQLCreator object 'sqlJoin' could not " +
									   "be executed");
		}
		
		// if the result is not empty
		if (result!=null)
		{	// return it
			return (String[]) result.getValues("User.name").toArray();
		}
		// else return null
		return null;
	}
	

	/* Public ------------------------------------------------------------------ */
	
	/**
	 * Run-method used in the thread, running in parallel with remaining
	 * application. Starting the server, and then accepting incoming client 
	 * connections, handling them in the temporary client list, while they are 
	 * unidentified and if successfully identified, swap them into the client list,
	 * removing them from the temporary list. 
	 * In the temporary list they are indexed with an ID, in the normal client list
	 * they are indexed with the user name connected on the client. 
	 */
	public void run()
	{
		// temporary id, to identify the clients, who are not yet logged in.
		long tempId = 0;
		
		// try to start the server and let it accept the clients
		try 
		{
			// create a new socket with the standard port
			this.serverSocket = new ServerSocket(this.port);
			
			// print out the server address
			System.out.println("Started a ClinSys Server on " + 
								InetAddress.getLocalHost() + ":" + 
								this.serverSocket.getLocalPort());
			
			// while the server was not stopped
			while (!this.stopped){
				// let the thread sleep for 100 millisecs to do some other stuff in
				// this time
				Thread.sleep(100);
				
				// wait to accept an incoming client
				this.tempClientSocket = this.serverSocket.accept();
				
				// add the accepted client to the list of temporary (unidentified)
				// clients
				this.tempClientThreads.put(tempId, new ServerThread(
												this.tempClientSocket, this));
				
				// set up a message for the log-in plug-in notifing the plug-in,
				// that the client successfully connected to the server
				// and that the server waits for the login information to identify
				// the client
				JSONObject json = new JSONObject();
				json.put("MessageType", MESSAGE_TYPES.NOTIFY);
				json.put("Receiver", "LoginController");
				json.put("Sender", "ServerController");
				json.put("Command", BASE_COMMANDS.CONNECTED);
				json.put("TemporaryId", tempId);
				// send the message to the temporary client
				this.tempClientThreads.get(tempId).sendMsg(
													MessageCoder.encode(json));
				
				// increment the temporary id
				tempId++;
				// if the tempId reached the maximum of the long value
				if (tempId==Long.MAX_VALUE){ // prevent an overflow error					
					tempId = 0;					
					// if the tempId is still occupied find a tempId that is not 
					// occupied
					while (this.tempClientThreads.containsKey(tempId) && 
						   tempId< Long.MAX_VALUE)
					{
						tempId++;
					}
				}
			}
		} 
		// can't create a server, get the server address or accept any clients
		catch (IOException e) 
		{
			
		}
		// Thread can't sleep because it was interrupted
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		// can't create temporary ServerThread or send a message to the client
		catch (ClinSysException e) 
		{
			e.printStackTrace();
		}
		
		// if the server was stopped (stopped == true) then close/null the
		// reference objects		
		this.clientList = null;
		this.serverController = null;
		this.tempClientSocket = null;
		this.tempClientThreads = null;
		this.thread.interrupt();
		this.thread = null;
		
		return;
	}
	
	
	/**
	 * Closes the Server. That means, sends an exit command to every connected
	 * client, even the unidentified and set the stopped flag to true, so that the
	 * run method will stop and clear the references as well as the thread.
	 * 
	 * @throws ClinSysException Is thrown if the exit command cannot be sent to the
	 * 							clients.
	 */
	public void close() throws ClinSysException
	{
		// set up the exit command message
		JSONObject json = new JSONObject();	
		json.put("Command", "Exit");
		
		// try to send the exit message to all identified clients 
		try 
		{
			this.sendTo(RECIPIENT.ALL, "", json);
		}
		catch (ClinSysException e) 
		{
			throw new ClinSysException("(ICTMServerSocket.close()) : " +
									   "Could not send the close message.");
		}
		
		// try to send the exit message to all unidentified clients
		try
		{
			Iterator<Long> id = this.tempClientThreads.keySet().iterator();
			while (id.hasNext())
			{
				this.sendTo(id.next(), json);
			}			
		}
		catch (ClinSysException e)
		{
			throw new ClinSysException("(Server.close()) : " + 
									   "Could not send the close message to an " +
									   "unidentified client.");
		}
		
		// stop the server
		this.stopped = true;
	}
	
	
	/**
	 * Remove a client from the client list, and disconnects it.
	 * 
	 * @param userName The name of the user connected by the client.
	 * @throws ClinSysException Thrown if the ServerThread object, handling the
	 * 							client connection couldn't be closed or the exit 
	 * 							message couldn't be sent.
	 */
	public void removeClient(String userName) throws ClinSysException
	{
		// set up the exit command message
		JSONObject json = new JSONObject();	
		json.put("Command", "Exit");
		
		// try to send an exit message to the client and close the ServerThread
		try 
		{
			this.sendTo(RECIPIENT.CLIENT, userName, json);
			this.clientList.get(userName).close();
		}
		catch (ClinSysException e) 
		{
			throw new ClinSysException("(ICTMServerSocket.removeClient(" +
									   "String userName)) : Can't close client " + 
									   userName);
		}
		// remove the client from the list
		this.clientList.remove(userName);
	}
	
	
	/**
	 * Sends a message to the specified recipient. Either to all identified 
	 * clients, to a single identified client, or to all user of a group. 
	 * 
	 * @param recipient A literal of the RECIPIENT enumeration indicating, who 
	 * 					should receive the message.
	 * @param name	    The user name of a single client or the name of a group, 
	 * 					who should receive the message.
	 * @param msg		The uncoded message.
	 * @throws ClinSysException Is thrown if a value for the recipient is selected,
	 * 						    which is not implemented.
	 */
	public void sendTo(RECIPIENT recipient, String name, JSONObject msg) 
						throws ClinSysException
	{
		// stores all user names of users in the given group
		String[] userNames;
		
		// encode the message
		String encodedMsg = MessageCoder.encode(msg);
		
		// depending on the recipient send the message
		switch (recipient)
		{
		case ALL:
			// go through all identified clients
			for (ServerThread client : this.clientList.values())
			{	// and send them the message
				client.sendMsg(encodedMsg);
			}
			break;
		
		case CLIENT:
			// send the message to a specific client
			this.clientList.get(name).sendMsg(encodedMsg);
			break;
			
		case RIGHTGROUP:
			// get the names of the user in the group
			userNames = this.getUserNamesOfGroup(name);
			
			// send the message to every user of the group
			for (String user : userNames)
			{
				this.clientList.get(user).sendMsg(encodedMsg);
			}			
			break;
			
		// Unimplemented recipient selected -> throw an error
		default:
			throw new ClinSysException("(ICTMServerSocket.sendTo(RECIPIENT " + 
									   "recipient, String name, JSONObject msg)" +
									   ": Undefined RECIPIENT value was given.");		
		}
	}
	
	
	/**
	 * Send the message to a temporary client.
	 * 
	 * @param tempId The id of the temporary client, it was indexed with.
	 * @param msg The message, which should be send to the client.
	 * @throws ClinSysException Is thrown if the message cannot be send.
	 */
	public void sendTo(long tempId, JSONObject msg) throws ClinSysException
	{
		// encode the message
		String encodedMsg = MessageCoder.encode(msg);
		
		// send it
		this.tempClientThreads.get(tempId).sendMsg(encodedMsg);		
	}
	
	
	/**
	 * Handles an incoming message. That means it passes the message to the 
	 * protocol, so the protocol can interpret it. 
	 * 
	 * @param msg The message, which should be interpret.
	 * @throws ClinSysException Is thrown if the message cannot be interpret.
	 */
	public void handleMsg(String msg) throws ClinSysException
	{
		// try to interpret the incoming message
		try 
		{
			this.serverController.getProtocol().interpret(MessageCoder.decode(msg), 
														this.serverController);
		}
		catch (ClinSysException e) 
		{
			Debugger.debug(e.getStackTrace().toString());
			throw new ClinSysException("(ICTMServerSocket.handleMsg(String msg))" +
									   ": Could not interpretate the given " +
									   "message.");
		}
	}
	
	
	/**
	 * Adds a single client to the client list, if it's not already in the list.
	 * 
	 * @param client The ServerThread object, which is handling the client 
	 * 				 connection and which should be added to the client list.
	 * @return True if the client was successfully added, false if the client was
	 * 		   already in the list.
	 */
	public boolean addClient(ServerThread client)
	{
		// if the client is in the list return false
		if (this.clientList.containsKey(client.getUserName()))
		{
			return false;
		}
		
		// else add it to the HashMap and return true
		this.clientList.put(client.getUserName() ,client);
		return true;
	}
	
	
	/**
	 * Adds a single temporary client to the client list, if it's not already in
	 * the list.
	 * 
	 * @param tempId The ID of the temporary client to receive it out of the 
	 * 			     temporary client list.
	 * @param userName The name of the user connected by the client.
	 * @return True if the client was successfully added or false if the client
	 * 		   was already in the list.
	 */
	public boolean addClient(long tempId, String userName)
	{
		// set the user name for the temporary client
		this.tempClientThreads.get(tempId).setUserName(userName);
		
		// add it to the identified client list 
		if (this.addClient(this.tempClientThreads.get(tempId)))
		{ 
			// remove it from the temporary list
			this.tempClientThreads.remove(tempId);
			return true;
		}
		return false; 
	}
	
	
	/**
	 * Disconnect a temporary client from the server and remove it from the
	 * temporary list. 
	 * 
	 * @param tempId The ID of the temporary client.
	 * @throws ClinSysException Is thrown if the temporary client cannot be closed.
	 */
	public void removeTempClient(long tempId) throws ClinSysException
	{
		// try to close the client
		try 
		{
			this.tempClientThreads.get(tempId).close();
		}
		catch (ClinSysException e) 
		{
			throw new ClinSysException("(ICTMServerSocket.removeTempClient(" +
									   "long tempId)) : Can't close temporary " +
									   "client " + tempId);
		}
		
		// remove it
		this.tempClientThreads.remove(tempId);
	}
}
