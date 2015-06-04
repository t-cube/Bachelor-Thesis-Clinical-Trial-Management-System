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

import shared.json.JSONObject;

/**
 * Class to encode messages, which will be send. Still not sure if the messages
 * have to be encoded, and how they should be encoded to save amount of data to be
 * send. If the data is send via a https connection, as planned, maybe this class 
 * is needless and will be removed.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class MessageCoder 
{
	
	/* Constants ----------------------------------------------------------------*/

	
	/* Variables ----------------------------------------------------------------*/
		
	
	/* Constructor --------------------------------------------------------------*/

	
	/* Getter -------------------------------------------------------------------*/
	

	/* Setter -------------------------------------------------------------------*/
		
	
	/* Private ------------------------------------------------------------------*/
	
	
	/* Public -------------------------------------------------------------------*/
	
	/**
	 * Accepts an string containing the encoded message, decodes it and creates 
	 * a JSONObject out of it, which is returned.
	 * 
	 * @param msg A string containing the encoded message.
	 * @return The JSONObject, containing the message data.
	 */
	public static JSONObject decode(String msg)
	{
		return new JSONObject(msg);
	}
	
	/**
	 * Accepts a JSONObject containing the message data, and encodes it and returns
	 * the string containing the encoded message data.
	 * 
	 * @param msg The JSONObject containing the message data.
	 * @return A string containing the encoded representation of the message data.
	 */
	public static String encode(JSONObject msg)
	{
		return msg.toString();
	}
}
