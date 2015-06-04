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



package shared.system;

import shared.util.ClinSysException;

/**
 * This is the abstact class for the Controller classes. Defining the minimal
 * attributes and methods a Controller must have.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public abstract class AbstractController 
{
	
	/* Constants ----------------------------------------------------------------*/
	
	
	/* Variables ----------------------------------------------------------------*/
	
	// The Protocol object used to interpret incoming messages.
	protected Protocol protocol;
	
	// the name of the plug-in the Controller was built for.
	protected String pluginName;
	
	// the path to the plug-ins (in general only used in the Server- and
	// ClientController to load the plug-ins
	private String plugInPath = "plugins/";
		
	
	/* Constructor --------------------------------------------------------------*/
	
	/**
	 * Standard Constructor, just requiring the plug-in name.
	 * @param pluginName The name of the plug-in the controller is build for. 
	 */
	public AbstractController(String pluginName)
	{
		this.pluginName = pluginName;
	}
	
	/**
	 * Constructor accepting and setting the protocol and the plug-in name.
	 * 
	 * @param pluginName The name of the plug-in the controller is build for. 
	 * @param protocol The Protocol object, used to interpret the incoming 
	 * 				   messages.
	 */
	public AbstractController(String pluginName, Protocol protocol)
	{
		this.pluginName = pluginName;
		this.protocol = protocol;
	}
	
	
	/* Getter ------------------------------------------------------------------ */
	
	/**
	 * Returns the used protocol.
	 * 
	 * @return The Protocol object, which is used to interpret incoming messages.
	 */
	public Protocol getProtocol() 
	{
		return this.protocol;
	}

	
	/**
	 * Returns the plug-in name.
	 * 
	 * @return The name of the plug-in the Controller was built for. 
	 */
	public String getPluginName() 
	{
		return pluginName;
	}
	

	/**
	 * Returns the name of the current running function. Was an idea to use it for
	 * the rights management to individually grant access to functions. 
	 * 
	 * @return Name of the current running mehtod.
	 */
	protected String getFunctionName()
	{
		StackTraceElement[] s = Thread.currentThread().getStackTrace();
		return s[0].getMethodName();
	}
	
	
	/**
	 * Returns the path to the plug-ins folder, in which the plug-ins are stored.
	 * In general only used by the Server- and ClientController.
	 * 
	 * @return The path to the plug-ins folder.
	 */
	public String getPlugInPath()
	{
		return this.plugInPath;
	}
	
	
	/* Setter ------------------------------------------------------------------ */
	
	/**
	 * Set the path to the plug-ins folder. Should only be used by Server- and 
	 * ClientController in general. 
	 * 
	 * @param pluginPath Path to the plug-ins folder.
	 */
	public void setPluginPath(String pluginPath)
	{
		this.plugInPath = pluginPath;
	}

	
	/**
	 * Sets the name of the plug-in the Controller was built for.
	 * 
	 * @param pluginName Name of the plug-in, which the Controller is a part of.
	 */
	public void setPluginName(String pluginName) 
	{
		this.pluginName = pluginName;
	}

	
	/**
	 * Sets the protocol, which is used to interpret incoming messages.
	 * 
	 * @param protocol The Protocol object used to interpret incoming messages.
	 */
	public void setProtocol(Protocol protocol) 
	{
		this.protocol = protocol;
	}
	
	
	/* Private ----------------------------------------------------------------- */
	
	
	/* Public ------------------------------------------------------------------ */
	
	/**
	 * Abstract method, which can contain statements, that should be executed 
	 * directly after a Controller was created. Must be called in the constructor.
	 */
	public abstract void afterCreated();	
	
	
	/**
	 * "Closes the Controller object by nulling the references.
	 * 
	 * @throws ClinSysException Is thrown if something can't be closed.
	 */
	public void close() throws ClinSysException
	{
		this.protocol = null;
	}
}
