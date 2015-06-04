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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import shared.util.ClinSysException;

/**
 * A thread object used to handle a single connection to a client. 
 * The Server class instantiates the ServerThread objects if a new client
 * connected.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class ServerThread extends Thread 
{
	
	/* Constants ----------------------------------------------------------------*/

	
	/* Variables ----------------------------------------------------------------*/
	
	// The name of the user connected by client to this ServerThread object
	private String userName = null;
	
	// The Socket object to handle the connection
	private Socket client;
	
	// The DataInputStream for reading incoming messages
	private DataInputStream in;
	
	// The DataOutputStream for sending outgoing messages
	private DataOutputStream out;
	
	// The Server object, using this ServerThread object
	private Server server;
	
	// Flag indicating if the thread has stopped
	private boolean stopped = false;
	
	
	/* Constructor --------------------------------------------------------------*/
	
	/**
	 * The constructor of the ServerThread class. It requires a socket, which
	 * already holds the connection to the client and a reference to the
	 * "master-server", which has references to all the other clients.
	 *  
	 * @param client The Socket object, which is already connected with the client.
	 * @param server The master-server, which has references to all the other
	 * 				 clients.
	 * @throws ClinSysException Is thrown if it wasn't possible to open either the 
	 * 						    incoming or outgoing DataStream. 
	 */
	public ServerThread(Socket client, Server server) 
						throws ClinSysException
	{
		// initialize the super class
		super();
		
		// Set up the references
		this.client = client;
		this.server = server;
		
		// try to open incoming and outgoing DataStream
		try 
		{
			this.in = new DataInputStream(new BufferedInputStream(
												this.client.getInputStream()));
			this.out = new DataOutputStream(new BufferedOutputStream(
												this.client.getOutputStream()));
		} 
		// throw an error if it wasn't possible
		catch (IOException e) 
		{
			throw new ClinSysException("(ICTMServerSocketThread(Socket client, " + 
									  "ICTMServerSocket ictmServerSocket)) : " + 
									  "Could not set up the input/output stream.");
		}
		
		// set the stopped flag to false, because the thread will run now
		this.stopped = false;
		// let's get the thread running
		this.start();
	}

	
	/* Getter ------------------------------------------------------------------ */
	
	/**
	 * Returns the name of the user, who is connected by the client.
	 * 
	 * @return The user name
	 */
	public String getUserName() 
	{
		return userName;
	}


	/**
	 * Returns the Socket object handling the connection to the client.
	 * 
	 * @return The socket handling the connections to the connected client.
	 */
	public Socket getClient() 
	{
		return client;
	}


	/**
	 * Returns the DataStream for the incoming messages of the client. Can be used
	 * to read incoming messages from the connected client.
	 * 
	 * @return The DataInputStream object.
	 */
	public DataInputStream getIn() 
	{
		return in;
	}


	/**
	 * Returns the DataStream for the outgoing messages of the client. Can be used
	 * for sending messages to the connected client.
	 * 
	 * @return The DataOutputStream object.
	 */
	public DataOutputStream getOut() 
	{
		return out;
	}


	/**
	 * Returns the reference to the master-server handling all connected clients.
	 * This ServerThread object is a part of/is used by the master-server.
	 * 
	 * @return Reference to the master-server.
	 */
	public Server getServer() 
	{
		return server;
	}
	
	

	/* Setter ------------------------------------------------------------------ */
	
	/**
	 * If the ServerThread should handle another client, the new Socket object
	 * handling connections to the client can be set with this method.
	 * Be Careful: The DataInput/OutputStream has to be set manually as well.
	 * 
	 * @param client A Socket object handling connections to a client.
	 */
	public void setClient(Socket client) 
	{
		this.client = client;
	}


	/**
	 * Sets a new DataInputStream object. Useful if the Socket object was changed
	 * or if, for what ever reason, the incoming messages of a different object
	 * should be read.
	 * 
	 * @param in The DataInputStream object, which should be used to read
	 * 			 incoming messages.
	 */
	public void setIn(DataInputStream in) 
	{
		this.in = in;
	}


	/**
	 * Sets a new DataOutputStream object. Useful if the Socket object was changed
	 * or if, for what ever reason, the outgoing messages should be send to
	 * another object.
	 * 
	 * @param out The DataOutputStream object, which should be used to send 
	 * 			  outgoing messages.
	 */
	public void setOut(DataOutputStream out) 
	{
		this.out = out;
	}


	/**
	 * Used to set a new master-server. For example if more than one master-server
	 * exist, and one master-server has to swap a client to the other one.
	 * 
	 * @param server The reference to the master-server, that should be used.
	 */
	public void setServer(Server server) 
	{
		this.server = server;
	}

	
	/**
	 * Set name of the user connected by the client with this ServerThread object.
	 * The user name is used by the master-server to identify the clients.
	 * 
	 * @param userName The name of the user logged-in.
	 */
	public void setUserName(String userName) 
	{
		this.userName = userName;
	}
	
	
	
	/* Private ----------------------------------------------------------------- */

	
	
	/* Public ------------------------------------------------------------------ */
	
	
	/**
	 * Closes the ServerThread, that means it stops the thread by setting the 
	 * stopped flag to true.
	 */
	public void close() throws ClinSysException
	{
		this.stopped = true;
	}
	
	
	/**
	 * Sends the given message to the client. The message should be encoded
	 * before.
	 * 
	 * @param msg The encoded message that should be sent to the client.
	 * @throws ClinSysException Is thrown if the message cannot be send.
	 */
	public void sendMsg(String msg) throws ClinSysException
	{
		try 
		{
			out.writeUTF(msg); // write the message
			out.flush(); // and flush it (send it)
		} 
		catch (IOException e) 
		{
			throw new ClinSysException("(ICTMServerSocketThread.sendMsg(" + 
									   "String msg)) : Output stream can't " + 
									   "write UTF.");
		}
	}
	
	
	/**
	 * The run method of the thread. Running in repeat until the thread stopped.
	 * Let's it sleep for 100 millisecs and then waits for an incoming message to
	 * read. If the thread was stopped, that means the flag stopped = true, then
	 * close and free the DataStreams and the client reference.
	 */
	public void run()
	{
		// while not stoppe repeat this
		while (!this.stopped)
		{
			// wait 100 millisecs for other things to happen and then
			// try to read a message
			try 
			{
				ServerThread.sleep(100);
				this.server.handleMsg(this.in.readUTF());
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();		
			} 
			catch (ClinSysException e) 
			{
				e.printStackTrace();
			}
		}	

		// if the thread was stopped try to close everything
		try {
			this.in.close();
			this.out.close();
			this.client.close();
		} catch (IOException e) {

		}
		
		// null everything
		this.in = null;
		this.out = null;
		this.client = null;
		this.server = null;
		
		this.interrupt();				
		return;
	}
}
