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

import javax.swing.JOptionPane;

/**
 * The Dialog class, which shows either a simple message box, an input box or a
 * confirmation box dialog.
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class Dialog 
{
	
	/* Constants ----------------------------------------------------------------*/
	
	// the message types
	/**
	 * Message type:
	 * Shows the dialog with an "X" on red background. Used for error messages. 
	 */
	public static final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
	
	/**
	 * Message type:
	 * Shows a "i" on a bluish background. Used for information messages.
	 */
	public static final int INFORMATION_MESSAGE = JOptionPane.INFORMATION_MESSAGE;
	
	/**
	 * Message type:
	 * An exclamation mark in a yellow triangle. Used for warnings.
	 */
	public static final int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;
	
	/**
	 * Message type:
	 * A question mark on a green background. Used for questions.
	 */
	public static final int QUESTION_MESSAGE = JOptionPane.QUESTION_MESSAGE;
	
	/**
	 * Message type:
	 * No extra icon for the dialog.
	 */
	public static final int PLAIN_MESSAGE = JOptionPane.PLAIN_MESSAGE;

	
	// the possible buttons for the message
	/**
	 * Option type:
	 * Shows the default options for this message type.
	 */
	public static final int DEFAULT_OPTION = JOptionPane.DEFAULT_OPTION;
	
	/**
	 * Option type:
	 * Shows Yes and No buttons.
	 */
	public static final int YES_NO_OPTION = JOptionPane.YES_NO_OPTION;
	
	/**
	 * Option type:
	 * Shows Yes, No and Cancel buttons.
	 */
	public static final int YES_NO_CANCEL_OPTION = JOptionPane.YES_NO_CANCEL_OPTION;
	
	/**
	 * Option type:
	 * Shows Ok and Cancel buttons.
	 */
	public static final int OK_CANCEL_OPTION = JOptionPane.OK_CANCEL_OPTION;
	
	
	// selected answer
	/**
	 * By user selected answer option:
	 * Yes was clicked.
	 */
	public static final int YES_OPTION = JOptionPane.YES_OPTION;
	
	/**
	 * By user selected answer option:
	 * No was clicked.
	 */
	public static final int NO_OPTION = JOptionPane.NO_OPTION;
	
	/**
	 * By user selected answer option:
	 * Cancel was clicked.
	 */
	public static final int CANCEL_OPTION = JOptionPane.CANCEL_OPTION;
	
	/**
	 * By user selected answer option:
	 * Ok was clicked.
	 */
	public static final int OK_OPTION = JOptionPane.OK_OPTION;
	
	/**
	 * By user selected answer option:
	 * It was closed without clicking on a button.
	 */
	public static final int CLOSED_OPTION = JOptionPane.CLOSED_OPTION;
	
	/* Variables ----------------------------------------------------------------*/	
	
	
	/* Constructor --------------------------------------------------------------*/
	
	
	/* Getter -------------------------------------------------------------------*/
	

	/* Setter -------------------------------------------------------------------*/
		
	
	/* Private ------------------------------------------------------------------*/
	
	
	/* Public -------------------------------------------------------------------*/
	
	/**
	 * Shows a simple message box dialog.
	 * 
	 * @param msg The message, which should be displayed in the dialog.
	 * @param titel The title for the message box dialog.
	 * @param messageType The type of the message box. See message type constants. 
	 */
	public static void msgBox(String msg, String titel, int messageType)
	{
		JOptionPane.showMessageDialog(null, msg, titel, messageType);
	}
	
	
	/**
	 * Shows a simple input box to receive a user input.
	 * 
	 * @param msg The message, which should be displayed in the dialog.
	 * @param titel The title for the dialog.
	 * @param messageType The type of the message box. See message type constants.
	 * @return Returns the user input.
	 */
	public static String inputBox(String msg, String titel, int messageType)
	{
		return JOptionPane.showInputDialog(null, msg, titel, messageType);
	}
	
	
	/**
	 * Shows a simple confirmation box, with Buttons as YES/NO, etc.
	 * 
	 * @param msg The message, which should be displayed in the dialog.
	 * @param titel The title for the dialog.
	 * @param optionType The possible options like YES, NO, CANCEL.
	 * @param messageType The type of the message box. See message type constants.
	 * @return The selected option constant.
	 */
	public static int confirmationBox(String msg, String titel, int optionType, 
										int messageType)
	{
		return JOptionPane.showConfirmDialog(null, msg, titel, optionType, messageType);
	}
}
