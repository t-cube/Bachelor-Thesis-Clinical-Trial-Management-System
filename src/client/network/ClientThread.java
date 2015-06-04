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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import shared.system.MessageCoder;
import shared.util.Debugger;
import shared.util.ClinSysException;
import shared.util.Dialog;

/**
 * The ClientThread class is used to allow reading incoming messages while sending
 * messages to the server as well. For this the second thread is needed.
 * The first thread is the Client application itself.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class ClientThread extends Thread
{
	
	/* Constants ----------------------------------------------------------------*/
	
	
	
	/* Variables ----------------------------------------------------------------*/
	
	// Socket object, for the connection to the server
	private Socket server;
	
	// Reference to the Client object using this ClientThread object
	private Client client;
	
	// The DataInputStream to receive incoming messages
	private DataInputStream in;
	
	// flag if the thread was stopped
	private boolean stopped = false;
	
	

	/* Constructor --------------------------------------------------------------*/
	
	/**
	 * The constructor of the ClientThread class. Requires the server Socket
	 * object, to which the Client is connected as well as the Client object
	 * itself. Then opens the DataInputStream of the server Socket object to
	 * receive incoming messages.
	 * 
	 * @param server The Socket object, which establishes the connection to the
	 * 				 server.
	 * @param client The Client object, handling the server connection and sending
	 * 				 messages to the server.
	 * @throws ClinSysException Exception is thrown, if the DataInputStream could
	 * 							not be opened.
	 */
	public ClientThread(Socket server, Client client) throws ClinSysException
	{
		super(); // initialize the super class
		
		Debugger.debug("Creating an ICTMClientSocketThread object");
		
		// try to set the server and client attribute to the given arguments
		// as well as open the DataInputStream
		try 
		{
			this.server = server;
			this.client = client;
			// open the DataInputStream
			this.in = new DataInputStream(new BufferedInputStream(
												this.server.getInputStream()));
			// indicate that the thread is running
			this.stopped = false;
			// start the thread
			this.start();
		}
		// the DataInputStream could not be opened 
		catch (IOException e)
		{	// throw an exception 
			throw new ClinSysException("Could not create the input stream");
		}
	}
	
	
	/**
	 * Closes the object. For this it sets the stopped flag to true, which 
	 * terminates the run method and with this closing the DataInputStream, nulling
	 * the references and interrupting the thread
	 */
	public void close() throws ClinSysException
	{
		Debugger.debug("Closing the ClientThread object");
		this.stopped = true;
	}
	
	
	/**
	 * The run method of the thread. Once started it will be repeated until the 
	 * thread is stopped by setting the stopped flag to true. If being stopped, 
	 * close the DataInputStream, null the references and interrupt the thread.
	 */
	public void run()
	{
		// run while the thread is not beeing stopped
		while(!this.stopped)
		{
			try  
			{	
				// try to let the thread sleep for 100 milliseconds
				ClientThread.sleep(100);
				// try to receive incoming messages, decode them and let the client
				// read the decoded messages
				this.client.readMsg(MessageCoder.decode(this.in.readUTF()));
			} 
			// I/O error while receiving the message
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			// the thread can't sleep, because it was interrupted
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			} 
			// the decoded message can't be read
			catch (ClinSysException e) 
			{
				e.printStackTrace();
			}
		}
		
		// try to close the DataInputStream
		try {
			this.in.close();
		} catch (IOException e) {
			// show an error message, if it can't be closed
			Dialog.msgBox("The DataInputStream of the ClientThread object could " + 
						  "not be closed", "Error: Could not close the " + 
						  "DataInputStream", Dialog.ERROR_MESSAGE);
		}
		
		// null the references, interrupt the thread and return
		this.client = null;
		this.server = null;
		this.interrupt();
		return;
	}
}
