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


package shared.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.Window;
import java.util.List;

/**
 * Class, which allows to define a order for tabbing through the components.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class ListOrderedFocusTraversalPolicy extends FocusTraversalPolicy 
{
	
	/* Constants ----------------------------------------------------------------*/
	
	
	/* Variables ----------------------------------------------------------------*/	
	
	// The list containing the components in order.
	private List<Component> list = null;
	
	// Array containing the ordered components.
	private Component[] order = null;
	
	/* Constructor --------------------------------------------------------------*/
	
	/**
	 * Constructor, requiring a ordered list of the components, which is then
	 * transformed into an array.
	 * 
	 * @param list THe ordered components.
	 */
	public ListOrderedFocusTraversalPolicy(List<Component> list)
	{
		// set the list
		this.list = list;
		
		// create the array
		this.order = new Component[this.list.size()];
		
		// and insert the components in order.
		// don't know why i did this but it works.
		for (int i = 0; i<this.list.size(); i++)
		{
			this.order[i] = this.list.get(i);
		}
	}
	
	
	/* Getter -------------------------------------------------------------------*/
	

	/* Setter -------------------------------------------------------------------*/
		
	
	/* Private ------------------------------------------------------------------*/
	
	
	/* Public -------------------------------------------------------------------*/
	
	/* (non-Javadoc)
	 * @see java.awt.FocusTraversalPolicy#getInitialComponent(java.awt.Window)
	 */
	public Component getInitialComponent(Window window)
	{		
		int i;
		for (i=0; i<order.length;i++)
		{
			if (isReachable(order[i]))
			{
				return order[i];
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.FocusTraversalPolicy#getComponentAfter(java.awt.Container, 
 * 		    java.awt.Component)
	 */
	@Override
	public Component getComponentAfter(Container aContainer,
										Component aComponent) 
	{
		int index = list.indexOf(aComponent);
		int i;
		// to get the first element if the last of the list was given
		for (i=(index + 1) % order.length; i!=index; i=(i + 1) % order.length)
		{
			if (isReachable(order[i]))
			{
				return order[i];
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see java.awt.FocusTraversalPolicy#getComponentBefore(java.awt.Container, 
	 *      java.awt.Component)
	 */
	@Override
	public Component getComponentBefore(Container aContainer,
										Component aComponent) 
	{
		int index = list.indexOf(aComponent);
		int i;
		// to get the last element if the first of the list was given
		for (i=(index-1 + order.length) % order.length; i!=index; 
					i=(i-1 + order.length) % order.length)
		{
			if (isReachable(order[i]))
			{
				return order[i];
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see java.awt.FocusTraversalPolicy#getDefaultComponent(java.awt.Container)
	 */
	@Override
	public Component getDefaultComponent(Container aContainer) 
	{
		int i;
		for (i=0; i<order.length;i++)
		{
			if (isReachable(order[i]))
			{
				return order[i];
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see java.awt.FocusTraversalPolicy#getFirstComponent(java.awt.Container)
	 */
	@Override
	public Component getFirstComponent(Container aContainer) 
	{
		int i;
		for (i=0; i<order.length;i++)
		{
			if (isReachable(order[i]))
			{
				return order[i];
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see java.awt.FocusTraversalPolicy#getLastComponent(java.awt.Container)
	 */
	@Override
	public Component getLastComponent(Container aContainer) 
	{
		int i;
		for (i=order.length-1; i>=0; i--)
		{
			if (isReachable(order[i]))
			{
				return order[i];
			}
		}
		return null;
	}
	
	private boolean isReachable(Component c)
	{
		if (c.isVisible() && c.isEnabled())
		{
			return true;
		}
		return false;
	}

}
