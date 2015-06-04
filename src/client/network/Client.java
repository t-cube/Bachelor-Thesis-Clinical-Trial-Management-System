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


package client.network;


import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import client.controller.ClientController;
import shared.json.JSONObject;
import shared.system.MessageCoder;
import shared.util.Debugger;
import shared.util.ClinSysException;

/**
 * The Client class is the main network class for the client application. 
 * It establishes the connection to the server, sends messages, as well as 
 * interpreting incoming ones. 
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class Client
{
	
	/* Constants ----------------------------------------------------------------*/
	
	
	
	/* Variables ----------------------------------------------------------------*/
	
	// the IP- or URL-address of the host.
	private String host = "localhost";
	
	// the port used for the connection.
	private int port = 55555;
	
	// Socket object handling the connection to the server.
	private Socket server;
	
	// OutputStream object handling outgoing messages.
	private DataOutputStream out;
	
	// separate thread allowing receiving messages while messages are sent.   
	private ClientThread clientThread;
	
	// reference to the ClientController, as it is the "brain" of the client
	// application.
	private ClientController clientController;
	
	
	/* Constructor --------------------------------------------------------------*/
	
	/**
	 * Standard constructor, only expecting a ClientController object and using the
	 * standard host "{@value host}" and port "{@value port}. Setting up the
	 * connection and throwing an error if one occurred during the connection
	 * process.
	 * 
	 * @param clientController The ClientController object, handling all stuff, the
	 * 							brain of the client so to speak.
	 * @throws ClinSysException Thrown if either the host cannot be found or the 
	 * 							Input/Output stream could not be created.
	 */
	public Client(ClientController clientController) throws ClinSysException
	{
		Debugger.debug("Creating client.network.Client object");
		this.clientController = clientController;
		this.setUp();
	}
		
	
	/**
	 * Extended constructor, expecting a ClientController object and the
	 * host as well as the port. Setting up the connection and throwing an error if
	 * one occurred during the connection process.
	 * 
	 * @param host The address of the host. Either an IP- or an URL-address.
	 * @param port The port number of the host. Integer between 49152 and 65535.
	 * @param clientController The ClientController object, handling all stuff, the
	 * 							brain of the client so to speak.
	 * @throws ClinSysException Thrown if either the host cannot be found or the 
	 * 							Input/Output stream could not be created.
	 */
	public Client(String host, int port, ClientController clientController) 
			throws ClinSysException
	{
		Debugger.debug("Creating ICTMClientSocket object");
		this.host = host;
		this.port = port;
		this.clientController = clientController;
		this.setUp();
	}

	
	/* Getter ------------------------------------------------------------------ */
	
	/**
	 * @return The host address, either an IP- or an URL-address. The standard host
	 * 		   is "{@value host}".
	 */
	public String getHost() 
	{
		return host;
	}
	

	/**
	 * @return The port number of the host. Integer between 49152 and 65535. The 
	 * 		   standard port is {@value port}.
	 */
	public int getPort() 
	{
		return port;
	}

	
	/**
	 * @return The Socket object handling the connection to the server.
	 */
	public Socket getServer() 
	{
		return server;
	}

	
	/**
	 * @return The DataOutputStream used to send messages to the server.
	 */
	public DataOutputStream getOut()
	{
		return out;
	}

	
	/**
	 * @return The ClientThread object used to read incoming messages while the 
	 * 		   client itself sends messages
	 */
	public ClientThread getClient() 
	{
		return clientThread;
	}
	
	
	
	/* Setter ------------------------------------------------------------------ */
	
	/**
	 * @param host The host address. Either an IP-address ("xxx.xxx.xxx.xxx") or an
	 * 			   URL-address ("www.example.com")
	 */
	public void setHost(String host) 
	{
		this.host = host;
	}

	
	/**
	 * @param port The port number. Integer between 49152 and 65535.
	 * @throws ClinSysException Thrown if the given port number is out of range.
	 */
	public void setPort(int port) throws ClinSysException 
	{
		if (port >= 49152 && port <= 65535)
		{
			this.port = port;	
		}
		else
		{
			throw new ClinSysException("Port number out of range: " + port + 
								   ".\nShould be between 49152 and 65535.");
		}
	}

	
	/**
	 * @param server The socket object, used to handle the connection to the 
	 * 				 server.
	 */
	public void setServer(Socket server) 
	{
		this.server = server;
	}

	
	/**
	 * @param out The DataOutputStream object used to send messages to the server.
	 */
	public void setOut(DataOutputStream out) 
	{
		this.out = out;
	}

	
	/**
	 * @param client The ClientThread object used to receive incoming messages.
	 */
	public void setClient(ClientThread client) 
	{
		this.clientThread = client;
	}
	
	
	
	/* Private ----------------------------------------------------------------- */
	
	
	/**
	 * Sets up the Client object. That means establishing the connection to the 
	 * server, as well as opening the incoming and outgoing stream.
	 * 
	 * @throws ClinSysException Throws an error if the host is unknown or the input
	 * 		   /output stream cannot be set.
	 */
	private void setUp() throws ClinSysException
	{
		try 
		{
			Debugger.debug("Establishing network connection");
			// set up the socket, and the input/output streams
			this.server = new Socket(this.host,this.port);
			Debugger.debug("Opening Data Streams");
			this.out = new DataOutputStream(new BufferedOutputStream(
													server.getOutputStream()));
			
			// create a new client thread and a thread for this object
			this.clientThread = new ClientThread(this.server, this);	
		} 
		catch (UnknownHostException e) 
		{
			throw new ClinSysException("Host not known!");
		} 
		catch (IOException e) 
		{
			throw new ClinSysException("Input/Output stream could not be set");
		}	
	}
	
	/* Public ------------------------------------------------------------------ */
	
	
	/**
	 * Closing the Client object, the connection and all its used objects.
	 * 
	 * @throws ClinSysException Throws an exception, if either the server socket or
	 * 							the DataInput/OutputStreams cannot be closed.
	 */
	public void close() throws ClinSysException
	{
		Debugger.debug("Closing the ICTMClientSocket object");
		try
		{
			this.out.close();
			this.server.close();
			this.clientThread.close();
			
			this.out = null;
			this.server = null;
			this.clientThread = null;
		} 
		catch (IOException e) 
		{
			throw new ClinSysException("A problem occured while closing either"
					+ " the server socket or the DataInput/OutputStream");
		}
	}
	
	
	/**
	 * Used by the ClientThread to "read" incoming messages. That means the
	 * ClientThread fetches every incoming message and calls the readMsg function 
	 * with the message as argument. The readMsg function then handles the message.
	 * 
	 * @param msg A JSONObject representing the message data.
	 * @throws ClinSysException Thrown if the message cannot be handled correctly.
	 */
	public void readMsg(JSONObject msg) throws ClinSysException
	{
		this.clientController.getProtocol().interpret(msg, this.clientController);
	}
	
	
	/**
	 * Sends a encoded message to the server.
	 * 
	 * @param msg JSONObject representing the message. Will at least be encoded to
	 * 			  String or ByteArray to send it to the server. Can be encrypted
	 * @throws ClinSysException Throws an error if the message cannot be send.
	 */
	public void sendMsg(JSONObject msg) throws ClinSysException
	{
		try 
		{
			Debugger.debug("Sending message to server: " + msg);
			this.out.writeUTF(MessageCoder.encode(msg));
			this.out.flush();
		} 
		catch (IOException e)
		{
			throw new ClinSysException("It was not possible to communicate with"
					+ " the server");
		}		
	}	
}
