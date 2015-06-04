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

import javax.swing.JInternalFrame;

import shared.util.Debugger;

/**
 * The template for every plug-in view, as they are Internal GUI windows which are 
 * are shown in the main window/frame. 
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class InnerView extends JInternalFrame
{
	
	/* Constants ----------------------------------------------------------------*/
	
	// just a system serial, needed for the serialization
	private static final long serialVersionUID = 1L;
	
	
	/* Variables ----------------------------------------------------------------*/
	
	
	
	/* Constructor --------------------------------------------------------------*/
	
	/**
	 * The constructor for the InnerView class. Requires a label for the window
	 * and a name for the object. 
	 * 
	 * @param label A string shown as the "title" for the window.
	 * @param name The name for the object, to index it in lists.
	 */
	public InnerView(String label, String name)
	{
		super(label);
		Debugger.debug("Creating ICTMView object: " + name);
		this.setName(name);
		this.setVisible(true);
	}
	
	
	/* Getter -------------------------------------------------------------------*/
	

	/* Setter -------------------------------------------------------------------*/
		
	
	/* Private ------------------------------------------------------------------*/
	
	
	/* Public -------------------------------------------------------------------*/
	
	
}
