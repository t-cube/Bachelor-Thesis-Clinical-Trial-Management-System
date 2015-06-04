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

import java.util.ArrayList;

/**
 * Provides to set, unset or toggle flags to change the behavior of the
 * application.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class Flag 
{
		
	/* Constants ----------------------------------------------------------------*/
	
	
	/* Variables ----------------------------------------------------------------*/	
	
	// List storing the set flags.
	private static ArrayList<FLAGS> flags = new ArrayList<FLAGS>();

	
	/* Constructor --------------------------------------------------------------*/
	
	
	/* Getter -------------------------------------------------------------------*/
	

	/* Setter -------------------------------------------------------------------*/
		
	
	/* Private ------------------------------------------------------------------*/
	
	
	/* Public -------------------------------------------------------------------*/
	
	/**
	 * Sets a flag, by adding it to the list
	 * 
	 * @param flag The flag that should be set.
	 */
	public static void set(FLAGS flag)
	{
		// if the flag wasn't set before, add it to the list
		if (!flags.contains(flag))
		{
			flags.add(flag);	
		} 
	}
	
	
	/**
	 * Checks if a flag is set.
	 * 
	 * @param flag The flag which should be checked.
	 * @return True if the flag was set, False if not.
	 */
	public static boolean isSet(FLAGS flag)
	{
		return flags.contains(flag);
	}
	
	
	/**
	 * Toggles the state of a flag. From set to unset and vice versa.
	 * 
	 * @param flag Flag that should be set.
	 * @return State after toggle. True if set, False if unset.
	 */
	public static boolean toogle(FLAGS flag)
	{
		// get the index of the flag in the list
		int index = flags.indexOf(flag);
		
		// if it does not exist
		if (index == -1)
		{ 
			// add it and return true as new state
			flags.add(flag);
			return true;
		}
		// if it exists
		else
		{ 
			// remove it and return false as new state
			flags.remove(index);
			return false;
		}		
	}
	
	
	/**
	 * Unsets a flag, by removing it from the list.
	 * 
	 * @param flag The flag that should be unset.
	 */
	public static void unset(FLAGS flag)
	{
		// get the index of the flag in the list 
		int index = flags.indexOf(flag);
		
		// if it exists in the list
		if (index!=-1)
		{
			// remove it
			flags.remove(index);
		}
	}
}
