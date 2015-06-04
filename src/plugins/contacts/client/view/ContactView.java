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

package plugins.contacts.client.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import plugins.contacts.CONTACTS_COMMANDS;
import plugins.contacts.CONTACT_TYPE;
import plugins.contacts.client.ContactsController;
import plugins.contacts.client.listener.ContactsActionListener;
import shared.util.ListOrderedFocusTraversalPolicy;
import client.view.InnerView;

/**
 * The InnerView object to show detailed, (mostly) editable information about a 
 * selected contact. 
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class ContactView extends InnerView 
{
	
	/* Constants ----------------------------------------------------------------*/
	
	private static final long serialVersionUID = 3649595997876089868L;
	
	
	/* Variables ----------------------------------------------------------------*/	
	
	// Labels
	private JLabel lblContactType = null;
	private JLabel lblFirstname = null;
	private JLabel lblLastname = null;
	private JLabel lblCompany = null;
	private JLabel lblPosition = null;
	private JLabel lblAddress = null;
	private JLabel lblZIP_code = null;
	private JLabel lblPlace = null;
	private JLabel lblState = null;
	private JLabel lblCountry = null;
	private JLabel lblEmail = null;
	private JLabel lblPhone = null;
	private JLabel lblMobile = null;
	private JLabel lblWebsite = null;
	private JLabel lblNotes = null;
	private JLabel lblUser = null;
	
	// Buttons
	private JButton	btnSave = null;
	private JButton btnCancel = null;
	
	// Other GUI Elements
	private JComboBox<String> cbxContactType = null;
	private JTextField txtFirstname = null;
	private JTextField txtLastname = null;
	private JTextField txtCompany = null;
	private JTextField txtPosition = null;
	private JComboBox<String> cbxPosition = null;
	private JTextField txtAddress = null;
	private JTextField txtZIP_code = null;
	private JTextField txtPlace = null;
	private JTextField txtState = null;
	private JComboBox<String> cbxCountry = null;
	private JTextField txtEmail = null;
	private JTextField txtPhone = null;
	private JTextField txtMobile = null;
	private JTextField txtWebsite = null;
	private JTextArea txtNotes = null;
	private JComboBox<String> cbxUser = null;
	
	// Container
	private Container content = null;
	
	// ContactController
	private ContactsController cc = null;
		

	/* Constructor --------------------------------------------------------------*/
	
	/**
	 * The constructor of the contact view. Requiring a ContactsController object
	 * to get access to the functionality, which should be executed for certain
	 * actions.
	 * 
	 * @param cc The client side Controller object of the contacts plug-in.
	 */
	public ContactView(ContactsController cc)
	{
		super("Add/Edit a Contact", "ContactView");
		this.cc = cc;
		setUp();
	}
			
	
	/* Getter -------------------------------------------------------------------*/

	/**
	 * @return The label for the contact type control.
	 */
	public JLabel getLblContactType() 
	{
		return lblContactType;
	}

	/**
	 * @return The label for the first name control.
	 */
	public JLabel getLblFirstname() 
	{	
		return lblFirstname;
	}

	/**
	 * @return The label for the last name control.
	 */
	public JLabel getLblLastname() 
	{
		return lblLastname;
	}

	/**
	 * @return The label for the company control.
	 */
	public JLabel getLblCompany() 
	{
		return lblCompany;
	}

	/**
	 * @return The label for the position control.
	 */
	public JLabel getLblPosition() 
	{
		return lblPosition;
	}

	/**
	 * @return The label for the address control.
	 */
	public JLabel getLblAddress() 
	{
		return lblAddress;
	}

	/**
	 * @return The label for the ZIP code control.
	 */
	public JLabel getLblZIP_code() 
	{
		return lblZIP_code;
	}

	/**
	 * @return The label for the place control.
	 */
	public JLabel getLblPlace() 
	{
		return lblPlace;
	}

	/**
	 * @return The label for the state control
	 */
	public JLabel getLblState() 
	{
		return lblState;
	}

	/**
	 * @return The label for the country control.
	 */
	public JLabel getLblCountry() 
	{
		return lblCountry;
	}

	/**
	 * @return The label for the email address control.
	 */
	public JLabel getLblEmail() 
	{
		return lblEmail;
	}

	/**
	 * @return The label for the phone number control.
	 */
	public JLabel getLblPhone() 
	{
		return lblPhone;
	}

	/**
	 * @return The label for the mobile phone number control.
	 */
	public JLabel getLblMobile() 
	{
		return lblMobile;
	}

	/**
	 * @return The label for the website control.
	 */
	public JLabel getLblWebsite() 
	{
		return lblWebsite;
	}

	/**
	 * @return The label for the notes control.
	 */
	public JLabel getLblNotes() 
	{
		return lblNotes;
	}

	/**
	 * @return The label for the user control.
	 */
	public JLabel getLblUser() 
	{
		return lblUser;
	}

	/**
	 * @return The button to save the contact data.
	 */
	public JButton getBtnSave() 
	{
		return btnSave;
	}

	/**
	 * @return The button to cancel editing/creating the contact.
	 */
	public JButton getBtnCancel() 
	{
		return btnCancel;
	}

	/**
	 * @return The control to select a contact type.
	 */
	public JComboBox<String> getCbxContactType() 
	{
		return cbxContactType;
	}

	/**
	 * @return The control to store the first name of the contact.
	 */
	public JTextField getTxtFirstname() 
	{
		return txtFirstname;
	}

	/**
	 * @return The control to store the last name of the contact.
	 */
	public JTextField getTxtLastname() 
	{
		return txtLastname;
	}

	/**
	 * @return The control to store the name of the company of the contact.
	 */
	public JTextField getTxtCompany() 
	{
		return txtCompany;
	}

	/**
	 * @return The control to store the position of the contact.
	 */
	public JTextField getTxtPosition() 
	{
		return txtPosition;
	}

	/**
	 * @return The control to select a given position (only for contacts that are 
	 * 		   employees).
	 */
	public JComboBox<String> getCbxPosition() 
	{
		return cbxPosition;
	}

	/**
	 * @return The control to store the address of the contact (street and house 
	 * 			number).
	 */
	public JTextField getTxtAddress() 
	{
		return txtAddress;
	}

	/**
	 * @return The control to store the ZIP code of the contact.
	 */
	public JTextField getTxtZIP_code() 
	{
		return txtZIP_code;
	}

	/**
	 * @return The control to store the place of the contact.
	 */
	public JTextField getTxtPlace() 
	{
		return txtPlace;
	}

	/**
	 * @return The control to store the state the contact lives in.
	 */
	public JTextField getTxtState() 
	{
		return txtState;
	}

	/**
	 * @return The control to store the country the contact lives in.
	 */
	public JComboBox<String> getCbxCountry() 
	{
		return cbxCountry;
	}

	/**
	 * @return The control to store the email address of the contact.
	 */
	public JTextField getTxtEmail() 
	{
		return txtEmail;
	}

	/**
	 * @return The control to store the phone number of the contact.
	 */
	public JTextField getTxtPhone() 
	{
		return txtPhone;
	}

	/**
	 * @return The control to store the mobile phone number of the contact.
	 */
	public JTextField getTxtMobile() 
	{
		return txtMobile;
	}

	/**
	 * @return The control to store a website address of the contact.
	 */
	public JTextField getTxtWebsite() 
	{
		return txtWebsite;
	}

	/**
	 * @return The control to store notes regarding the contact.
	 */
	public JTextArea getTxtNotes() 
	{
		return txtNotes;
	}

	/**
	 * @return The control to link a contact to a user.
	 */
	public JComboBox<String> getCbxUser() 
	{
		return cbxUser;
	}

	/**
	 * @return The container of the InnerView containing all the controls of the
	 * 			contact view.
	 */
	public Container getContent() 
	{
		return content;
	}

	/**
	 * @return The client side controller of the contact plug-in, providing the 
	 * 			functionality for the contact view.
	 */
	public ContactsController getCc() 
	{
		return cc;
	}
	

	/* Setter -------------------------------------------------------------------*/
			
	/**
	 * @param lblContactType Label to be set for the contact type control.
	 */
	public void setLblContactType(JLabel lblContactType) 
	{
		this.lblContactType = lblContactType;
	}

	/**
	 * @param lblFirstname Label to be set for the first name control.
	 */
	public void setLblFirstname(JLabel lblFirstname) 
	{
		this.lblFirstname = lblFirstname;
	}

	/**
	 * @param lblLastname Label to be set for the last name control.
	 */
	public void setLblLastname(JLabel lblLastname) 
	{
		this.lblLastname = lblLastname;
	}

	/**
	 * @param lblCompany Label to be set for the company control.
	 */
	public void setLblCompany(JLabel lblCompany) 
	{
		this.lblCompany = lblCompany;
	}

	/**
	 * @param lblPosition Label to be set for the position control.
	 */
	public void setLblPosition(JLabel lblPosition) 
	{
		this.lblPosition = lblPosition;
	}

	/**
	 * @param lblAddress Label to be set for the address control.
	 */
	public void setLblAddress(JLabel lblAddress) 
	{
		this.lblAddress = lblAddress;
	}

	/**
	 * @param lblZIP_code Label to be set for the ZIP code control.
	 */
	public void setLblZIP_code(JLabel lblZIP_code) 
	{
		this.lblZIP_code = lblZIP_code;
	}

	/**
	 * @param lblPlace Label to be set for the place control.
	 */
	public void setLblPlace(JLabel lblPlace) 
	{
		this.lblPlace = lblPlace;
	}

	/**
	 * @param lblState Label to be set for the state control.
	 */
	public void setLblState(JLabel lblState) 
	{
		this.lblState = lblState;
	}

	/**
	 * @param lblCountry Label to be set for the country control.
	 */
	public void setLblCountry(JLabel lblCountry) 
	{
		this.lblCountry = lblCountry;
	}

	/**
	 * @param lblEmail Label to be set for the email address control.
	 */
	public void setLblEmail(JLabel lblEmail) 
	{
		this.lblEmail = lblEmail;
	}

	/**
	 * @param lblPhone Label to be set for the phone number control.
	 */
	public void setLblPhone(JLabel lblPhone) 
	{
		this.lblPhone = lblPhone;
	}

	/**
	 * @param lblMobile Label to be set for the mobile phone number control.
	 */
	public void setLblMobile(JLabel lblMobile) 
	{
		this.lblMobile = lblMobile;
	}

	/**
	 * @param lblWebsite Label to be set for the website url control.
	 */
	public void setLblWebsite(JLabel lblWebsite) 
	{
		this.lblWebsite = lblWebsite;
	}

	/**
	 * @param lblNotes Label to be set for the notes control.
	 */
	public void setLblNotes(JLabel lblNotes) 
	{
		this.lblNotes = lblNotes;
	}

	/**
	 * @param lblUser Label to be set for the user control.
	 */
	public void setLblUser(JLabel lblUser) 
	{
		this.lblUser = lblUser;
	}

	/**
	 * @param btnSave Button to be set for saving the contact.
	 */
	public void setBtnSave(JButton btnSave) 
	{
		this.btnSave = btnSave;
	}

	/**
	 * @param btnCancel Button to be set for canceling editing/creating the 
	 * 					contact.
	 */
	public void setBtnCancel(JButton btnCancel) 
	{
		this.btnCancel = btnCancel;
	}

	/**
	 * @param cbxContactType Control to be set for selecting the contact type.
	 */
	public void setCbxContactType(JComboBox<String> cbxContactType) 
	{
		this.cbxContactType = cbxContactType;
	}

	/**
	 * @param txtFirstname Control to be set for storing the first name.
	 */
	public void setTxtFirstname(JTextField txtFirstname) 
	{
		this.txtFirstname = txtFirstname;
	}

	/**
	 * @param txtLastname Control to be set for storing the last name
	 */
	public void setTxtLastname(JTextField txtLastname) 
	{
		this.txtLastname = txtLastname;
	}

	/**
	 * @param txtCompany Control to be set for storing the company name.
	 */
	public void setTxtCompany(JTextField txtCompany) 
	{
		this.txtCompany = txtCompany;
	}

	/**
	 * @param txtPosition Control to be set for storing the position.
	 */
	public void setTxtPosition(JTextField txtPosition) 
	{
		this.txtPosition = txtPosition;
	}

	/**
	 * @param cbxPosition Control to be set for selecting the position. Only for
	 * 					  contacts that are employees (contact type: employee).
	 */
	public void setCbxPosition(JComboBox<String> cbxPosition) 
	{
		this.cbxPosition = cbxPosition;
	}

	/**
	 * @param txtAddress Control to be set for storing the address.
	 */
	public void setTxtAddress(JTextField txtAddress) 
	{
		this.txtAddress = txtAddress;
	}

	/**
	 * @param txtZIP_code Control to be set for storing the ZIP code.
	 */
	public void setTxtZIP_code(JTextField txtZIP_code) 
	{
		this.txtZIP_code = txtZIP_code;
	}

	/**
	 * @param txtPlace Control to be set for storing the place.
	 */
	public void setTxtPlace(JTextField txtPlace) 
	{
		this.txtPlace = txtPlace;
	}

	/**
	 * @param txtState Control to be set for storing the state. 
	 */
	public void setTxtState(JTextField txtState) 
	{
		this.txtState = txtState;
	}

	/**
	 * @param cbxCountry Control to be set for selecting the county
	 */
	public void setCbxCountry(JComboBox<String> cbxCountry) 
	{
		this.cbxCountry = cbxCountry;
	}

	/**
	 * @param txtEmail Control to be set for storing the email address.
	 */
	public void setTxtEmail(JTextField txtEmail) 
	{
		this.txtEmail = txtEmail;
	}

	/**
	 * @param txtPhone Control to be set for storing the phone number.
	 */
	public void setTxtPhone(JTextField txtPhone) 
	{
		this.txtPhone = txtPhone;
	}

	/**
	 * @param txtMobile Control to be set for storing the mobile phone number.
	 */
	public void setTxtMobile(JTextField txtMobile) 
	{
		this.txtMobile = txtMobile;
	}

	/**
	 * @param txtWebsite Control to be set for storing the website url.
	 */
	public void setTxtWebsite(JTextField txtWebsite) 
	{
		this.txtWebsite = txtWebsite;
	}

	/**
	 * @param txtNotes Control to be set for storing notes regarding a contact.
	 */
	public void setTxtNotes(JTextArea txtNotes) 
	{
		this.txtNotes = txtNotes;
	}

	/**
	 * @param cbxUser Control to be set for linking a contact to a user.
	 */
	public void setCbxUser(JComboBox<String> cbxUser) 
	{
		this.cbxUser = cbxUser;
	}

	/**
	 * @param content Container, to be set, which will include the controls.
	 */
	public void setContent(Container content) 
	{
		this.content = content;
	}

	/**
	 * @param cc The client side controller of the contact plugin, to be set, for
	 * 			 providing the functionality.
	 */
	public void setCc(ContactsController cc) 
	{
		this.cc = cc;
	}

		
	/* Private ------------------------------------------------------------------*/
	
	/**
	 * Creates the labels and controls, sets them into a layout, add the action 
	 * listener and add them all to the container of the InnerView.
	 */
	private void setUp() 
	{
		ContactsActionListener cal = new ContactsActionListener(this.cc);
		
		this.content = this.getContentPane();
		
		// Create the labels
		this.lblContactType = new JLabel("Contact type");
		this.lblFirstname = new JLabel("Firstname");
		this.lblLastname = new JLabel("Lastname");
		this.lblCompany = new JLabel("Company");
		this.lblPosition = new JLabel("Position");
		this.lblAddress = new JLabel("Address");
		this.lblZIP_code = new JLabel("ZIP code");
		this.lblPlace = new JLabel("Place");
		this.lblState = new JLabel("State");
		this.lblCountry = new JLabel("Country");
		this.lblEmail = new JLabel("Email");
		this.lblPhone = new JLabel("Phone");
		this.lblMobile = new JLabel("Mobile");
		this.lblWebsite = new JLabel("Website");
		this.lblNotes = new JLabel("Notes");
		this.lblUser = new JLabel("connect with User");
		
		// Create the Buttons 
		this.btnSave = new JButton("Save");
		this.btnSave.setActionCommand(CONTACTS_COMMANDS.SAVE_CONTACT.get());
		this.btnSave.addActionListener(cal);
		this.btnCancel = new JButton("Cancel");
		this.btnCancel.setActionCommand(CONTACTS_COMMANDS.CANCEL_CONTACT.get());
		this.btnCancel.addActionListener(cal);
		
		// Create the other GUI Elements
		this.cbxContactType = new JComboBox<String>();
		this.txtFirstname = new JTextField();
		this.txtLastname = new JTextField();
		this.txtCompany = new JTextField();
		this.txtPosition = new JTextField();
		this.cbxPosition = new JComboBox<String>();
		this.txtAddress = new JTextField();
		this.txtZIP_code = new JTextField();
		this.txtPlace = new JTextField();
		this.txtState = new JTextField();
		this.cbxCountry = new JComboBox<String>();
		this.txtEmail = new JTextField();		
		this.txtPhone = new JTextField();
		this.txtMobile = new JTextField();		
		this.txtWebsite = new JTextField();
		this.txtNotes = new JTextArea();
		this.cbxUser = new JComboBox<String>();
		
		// Format the GUI Elements
		Dimension d = new Dimension();
		// ComboBox Contact Type
		for (CONTACT_TYPE ct : CONTACT_TYPE.values())
		{
			this.cbxContactType.addItem(ct.get());
		}
		
		// TextField Firstname
		d = new Dimension(300,(int) this.txtFirstname.getPreferredSize()
															.getHeight());
		this.txtFirstname.setPreferredSize(d);
		// TextField Lastname
		d = new Dimension(300,(int) this.txtLastname.getPreferredSize()
															.getHeight());
		this.txtLastname.setPreferredSize(d);
		// TextField Company
		d = new Dimension(300,(int) this.txtCompany.getPreferredSize()
															.getHeight());
		this.txtCompany.setPreferredSize(d);
		// TextField Position
		d = new Dimension(300,(int) this.txtPosition.getPreferredSize()
															.getHeight());
		this.txtPosition.setPreferredSize(d);
		// ComboBox Position
		d = new Dimension(300,(int) this.cbxPosition.getPreferredSize()
															.getHeight());
		this.cbxPosition.setPreferredSize(d);
		this.cbxPosition.setVisible(false);
		// TextField Address
		d = new Dimension(300,(int) this.txtAddress.getPreferredSize()
															.getHeight());
		this.txtAddress.setPreferredSize(d);
		// TextField ZIP_code
		this.txtZIP_code.setColumns(5);
		// TextField Place
		d = new Dimension(300,(int) this.txtPlace.getPreferredSize()
															.getHeight());
		this.txtPlace.setPreferredSize(d);
		// TextField State
		d = new Dimension(300,(int) this.txtState.getPreferredSize().getHeight());
		this.txtState.setPreferredSize(d);
		// ComboBox Country
		d = new Dimension(300,(int) this.cbxCountry.getPreferredSize()
															.getHeight());
		this.cbxCountry.setPreferredSize(d);
		this.insertCountries(this.cbxCountry);
		// TextField Email
		d = new Dimension(300,(int) this.txtEmail.getPreferredSize()
															.getHeight());
		this.txtEmail.setPreferredSize(d);	
		// TextField Phone
		d = new Dimension(200,(int) this.txtPhone.getPreferredSize().getHeight());
		this.txtPhone.setPreferredSize(d);	
		// TextField Mobile
		d = new Dimension(200,(int) this.txtMobile.getPreferredSize().getHeight());
		this.txtMobile.setPreferredSize(d);		
		// TextField Website
		d = new Dimension(300,(int) this.txtWebsite.getPreferredSize()
															.getHeight());
		this.txtWebsite.setPreferredSize(d);		
		// ComboBox User
		d = new Dimension(300,(int) this.cbxUser.getPreferredSize().getHeight());
		this.cbxUser.setPreferredSize(d);	
		// TextArea Notes
		this.txtNotes.setBorder(this.txtFirstname.getBorder());
		
		// create the tab-order
		ArrayList<Component> order = new ArrayList<Component>(19);
		order.add(cbxContactType);
		order.add(txtFirstname);
		order.add(txtLastname);
		order.add(txtCompany);
		order.add(txtPosition);
		order.add(cbxPosition);
		order.add(txtAddress);
		order.add(txtZIP_code);
		order.add(txtPlace);
		order.add(txtState);
		order.add(cbxCountry);
		order.add(txtEmail);
		order.add(txtPhone);
		order.add(txtMobile);
		order.add(txtWebsite);
		order.add(cbxUser);
		order.add(txtNotes);
		order.add(btnSave);
		order.add(btnCancel);
		this.setFocusTraversalPolicy(new ListOrderedFocusTraversalPolicy(order));
		this.setFocusCycleRoot(true);
		// set the layout
		this.content.setLayout(createLayout());
		// add the components
		this.content.add(lblContactType);
		this.content.add(cbxContactType);
		this.content.add(lblFirstname);
		this.content.add(txtFirstname);
		this.content.add(lblLastname);
		this.content.add(txtLastname);
		this.content.add(lblCompany);
		this.content.add(txtCompany);
		this.content.add(lblPosition);
		this.content.add(txtPosition);
		this.content.add(cbxPosition);
		this.content.add(lblAddress);
		this.content.add(txtAddress);
		this.content.add(lblZIP_code);
		this.content.add(txtZIP_code);
		this.content.add(lblPlace);
		this.content.add(txtPlace);
		this.content.add(lblState);
		this.content.add(txtState);
		this.content.add(lblCountry);
		this.content.add(cbxCountry);
		this.content.add(lblEmail);
		this.content.add(txtEmail);
		this.content.add(lblPhone);
		this.content.add(txtPhone);
		this.content.add(lblMobile);
		this.content.add(txtMobile);
		this.content.add(lblWebsite);
		this.content.add(txtWebsite);
		this.content.add(lblNotes);
		this.content.add(txtNotes);
		this.content.add(lblUser);
		this.content.add(cbxUser);
		this.content.add(btnSave);
		this.content.add(btnCancel);
	}
	
	
	/**
	 * Creates a SpringLayout Layout Manager, which defines the layout for the 
	 * controls.
	 * 
	 * @return The created SpringLayout for layouting the controls.
	 */
	private LayoutManager createLayout()
	{
		SpringLayout l = new SpringLayout();
		// create the constraints
		// Create the constraints of the left edge  
		l.putConstraint(SpringLayout.WEST, lblContactType, 10, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, lblFirstname, 10, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, lblLastname, 10, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, lblCompany, 10, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, lblPosition, 10, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, lblAddress, 10, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, lblZIP_code, 10, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, lblPlace, 10, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, lblState, 10, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, lblCountry, 10, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, lblEmail, 10, SpringLayout.WEST,
						content);
		l.putConstraint(SpringLayout.WEST, lblPhone, 10, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, lblMobile, 10, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, lblWebsite, 10, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, lblUser, 10, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, btnSave, 10, SpringLayout.WEST, 
						content);
		// Create the constraint of the second left edge
		l.putConstraint(SpringLayout.WEST, cbxContactType, 310, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, txtFirstname, 310, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, txtLastname, 310, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, txtCompany, 310, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, txtPosition, 310, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, cbxPosition, 310, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, txtAddress, 310, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, txtZIP_code, 310, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, txtPlace, 310, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, txtState, 310, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, cbxCountry, 310, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, txtEmail, 310, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, txtPhone, 310, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, txtMobile, 310, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, txtWebsite, 310, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, cbxUser, 310, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, btnCancel, 10, SpringLayout.EAST, 
						btnSave);
		// Create the constraint of the third left edge
		l.putConstraint(SpringLayout.WEST, lblNotes, 620, SpringLayout.WEST, 
						content);
		l.putConstraint(SpringLayout.WEST, txtNotes, 620, SpringLayout.WEST, 
						content);
		// Create the constraints of the right edge
		l.putConstraint(SpringLayout.EAST, txtNotes, -10, SpringLayout.EAST, 
						content);
		// Create the top constraints of the "first" row
		l.putConstraint(SpringLayout.NORTH, lblContactType, 10, SpringLayout.NORTH,
						content);
		l.putConstraint(SpringLayout.NORTH, cbxContactType, 10, SpringLayout.NORTH,
						content);
		l.putConstraint(SpringLayout.NORTH, lblNotes, 10, SpringLayout.NORTH, 
						content);
		// Create "second" row constraints
		l.putConstraint(SpringLayout.NORTH, lblFirstname, 15, SpringLayout.SOUTH,
						lblContactType);
		l.putConstraint(SpringLayout.NORTH, txtFirstname, 15, SpringLayout.SOUTH,
						lblContactType);
		l.putConstraint(SpringLayout.NORTH, txtNotes, 15, SpringLayout.SOUTH,
						lblContactType);
		// Create "third" row constraints
		l.putConstraint(SpringLayout.NORTH, lblLastname, 15, SpringLayout.SOUTH,
						lblFirstname);
		l.putConstraint(SpringLayout.NORTH, txtLastname, 15, SpringLayout.SOUTH,
						lblFirstname);
		// Create "third" row constraints
		l.putConstraint(SpringLayout.NORTH, lblCompany, 15, SpringLayout.SOUTH, 
						lblLastname);
		l.putConstraint(SpringLayout.NORTH, txtCompany, 15, SpringLayout.SOUTH, 
						lblLastname);
		// Create "third" row constraints
		l.putConstraint(SpringLayout.NORTH, lblPosition, 15, SpringLayout.SOUTH, 
						lblCompany);
		l.putConstraint(SpringLayout.NORTH, txtPosition, 15, SpringLayout.SOUTH, 
						lblCompany);
		l.putConstraint(SpringLayout.NORTH, cbxPosition, 15, SpringLayout.SOUTH, 
						lblCompany);
		// Create "third" row constraints
		l.putConstraint(SpringLayout.NORTH, lblAddress, 15, SpringLayout.SOUTH, 
						lblPosition);
		l.putConstraint(SpringLayout.NORTH, txtAddress, 15, SpringLayout.SOUTH, 
						lblPosition);
		// Create "third" row constraints
		l.putConstraint(SpringLayout.NORTH, lblZIP_code, 15, SpringLayout.SOUTH, 
						lblAddress);
		l.putConstraint(SpringLayout.NORTH, txtZIP_code, 15, SpringLayout.SOUTH, 
						lblAddress);
		// Create "third" row constraints
		l.putConstraint(SpringLayout.NORTH, lblPlace, 15, SpringLayout.SOUTH, 
						lblZIP_code);
		l.putConstraint(SpringLayout.NORTH, txtPlace, 15, SpringLayout.SOUTH, 
						lblZIP_code);
		// Create "third" row constraints
		l.putConstraint(SpringLayout.NORTH, lblState, 15, SpringLayout.SOUTH, 
						lblPlace);
		l.putConstraint(SpringLayout.NORTH, txtState, 15, SpringLayout.SOUTH, 
						lblPlace);
		// Create "third" row constraints
		l.putConstraint(SpringLayout.NORTH, lblCountry, 15, SpringLayout.SOUTH, 
						lblState);
		l.putConstraint(SpringLayout.NORTH, cbxCountry, 15, SpringLayout.SOUTH, 
						lblState);
		// Create "third" row constraints
		l.putConstraint(SpringLayout.NORTH, lblEmail, 15, SpringLayout.SOUTH, 
						lblCountry);
		l.putConstraint(SpringLayout.NORTH, txtEmail, 15, SpringLayout.SOUTH, 
						lblCountry);
		// Create "third" row constraints
		l.putConstraint(SpringLayout.NORTH, lblPhone, 15, SpringLayout.SOUTH, 
						lblEmail);
		l.putConstraint(SpringLayout.NORTH, txtPhone, 15, SpringLayout.SOUTH, 
						lblEmail);
		// Create "third" row constraints
		l.putConstraint(SpringLayout.NORTH, lblMobile, 15, SpringLayout.SOUTH, 
						lblPhone);
		l.putConstraint(SpringLayout.NORTH, txtMobile, 15, SpringLayout.SOUTH, 
						lblPhone);
		// Create "third" row constraints
		l.putConstraint(SpringLayout.NORTH, lblWebsite, 15, SpringLayout.SOUTH, 
						lblMobile);
		l.putConstraint(SpringLayout.NORTH, txtWebsite, 15, SpringLayout.SOUTH, 
						lblMobile);
		// Create "third" row constraints
		l.putConstraint(SpringLayout.NORTH, lblUser, 15, SpringLayout.SOUTH, 
						lblWebsite);
		l.putConstraint(SpringLayout.NORTH, cbxUser, 15, SpringLayout.SOUTH, 
						lblWebsite);
		// Create the bottom edge
		l.putConstraint(SpringLayout.SOUTH, txtNotes, 15, SpringLayout.SOUTH, 
						lblUser);
		// Create the button constraints
		l.putConstraint(SpringLayout.NORTH, btnSave, 20, SpringLayout.SOUTH, 
						lblUser);
		l.putConstraint(SpringLayout.NORTH, btnCancel, 20, SpringLayout.SOUTH, 
						lblUser);
		
		return l;
	}
	
	
	/**
	 * Insert a bunch/most/nearly all of countries as strings to a given JComboBox, 
	 * which accepts Strings.
	 * 
	 * @param cbx The ComboBox to which the country names should be added.
	 */
	private void insertCountries(JComboBox<String> cbx)
	{
		cbx.addItem("AFGHANISTAN");
		cbx.addItem("ALBANIA");
		cbx.addItem("ALGERIA");
		cbx.addItem("ANDORRA");
		cbx.addItem("ANGOLA");
		cbx.addItem("ANTIGUA AND BARBUDA");
		cbx.addItem("ARGENTINA");
		cbx.addItem("ARMENIA");
		cbx.addItem("AUSTRALIA");
		cbx.addItem("AUSTRIA");
		cbx.addItem("AZERBAIJAN");
		cbx.addItem("BAHAMAS");
		cbx.addItem("BAHRAIN");
		cbx.addItem("BANGLADESH");
		cbx.addItem("BARBADOS");
		cbx.addItem("BELARUS");
		cbx.addItem("BELGIUM");
		cbx.addItem("BELIZE");
		cbx.addItem("BENIN");
		cbx.addItem("BHUTAN");
		cbx.addItem("BOLIVIA");
		cbx.addItem("BOSNIA AND HERZEGOVINA");
		cbx.addItem("BOTSWANA");
		cbx.addItem("BRAZIL");
		cbx.addItem("BRUNEI DARUSSALAM");
		cbx.addItem("BULGARIA");
		cbx.addItem("BURKINA FASO");
		cbx.addItem("BURUNDI");
		cbx.addItem("CAMBODIA");
		cbx.addItem("CAMEROON");
		cbx.addItem("CANADA");
		cbx.addItem("CAPE VERDE");
		cbx.addItem("CENTRAL AFRICAN REPUBLIC");
		cbx.addItem("CHAD");
		cbx.addItem("CHILE");
		cbx.addItem("CHINA");
		cbx.addItem("COLOMBIA");
		cbx.addItem("COMOROS");
		cbx.addItem("CONGO");
		cbx.addItem("COSTA RICA");
		cbx.addItem("CÔTE D’IVOIRE");
		cbx.addItem("CROATIA");
		cbx.addItem("CUBA");
		cbx.addItem("CYPRUS");
		cbx.addItem("CZECH REPUBLIC");
		cbx.addItem("DEMOCRATIC PEOPLE’S REPUBLIC OF KOREA");
		cbx.addItem("DEMOCRATIC REPUBLIC OF THE CONGO");
		cbx.addItem("DENMARK");
		cbx.addItem("DJIBOUTI");
		cbx.addItem("DOMINICA");
		cbx.addItem("DOMINICAN REPUBLIC");
		cbx.addItem("ECUADOR");
		cbx.addItem("EGYPT");
		cbx.addItem("EL SALVADOR");
		cbx.addItem("EQUATORAL GUINEA");
		cbx.addItem("ERITREA");
		cbx.addItem("ESTONIA");
		cbx.addItem("ETHIOPIA");
		cbx.addItem("FIJI");
		cbx.addItem("FINLAND");
		cbx.addItem("FRANCE");
		cbx.addItem("GABON");
		cbx.addItem("GAMBIA");
		cbx.addItem("GEORGIA");
		cbx.addItem("GERMANY");
		cbx.addItem("GHANA");
		cbx.addItem("GREECE");
		cbx.addItem("GRENADA");
		cbx.addItem("GUATEMALA");
		cbx.addItem("GUINEA");
		cbx.addItem("GUINEA BISSAU");
		cbx.addItem("GUYANA");
		cbx.addItem("HAITI");
		cbx.addItem("HONDURAS");
		cbx.addItem("HUNGARY");
		cbx.addItem("ICELAND");
		cbx.addItem("INDIA");
		cbx.addItem("INDONESIA");
		cbx.addItem("ISLAMIC REPUBLIC OF IRAN");
		cbx.addItem("IRAQ");
		cbx.addItem("IRELAND");
		cbx.addItem("ISRAEL");
		cbx.addItem("ITALY");
		cbx.addItem("JAMAICA");
		cbx.addItem("JAPAN");
		cbx.addItem("JORDAN");
		cbx.addItem("KAZAKHSTAN");
		cbx.addItem("KENYA");
		cbx.addItem("KIRIBATI");
		cbx.addItem("KUWAIT");
		cbx.addItem("KYRGYZSTAN");
		cbx.addItem("LAO PEOPLE’S DEMOCRATIC REPUBLIC");
		cbx.addItem("LATVIA");
		cbx.addItem("LEBANON");
		cbx.addItem("LESOTHO");
		cbx.addItem("LIBERIA");
		cbx.addItem("LIBYAN ARAB JAMAHIRIYA");
		cbx.addItem("LIECHTENSTEIN");
		cbx.addItem("LITHUANIA");
		cbx.addItem("LUXEMBOURG");
		cbx.addItem("MADAGASCAR");
		cbx.addItem("MALAWI");
		cbx.addItem("MALAYSIA");
		cbx.addItem("MALDIVES");
		cbx.addItem("MALI");
		cbx.addItem("MALTA");
		cbx.addItem("MARSHALL ISLANDS");
		cbx.addItem("MAURITANIA");
		cbx.addItem("MAURITIUS");
		cbx.addItem("MEXICO");
		cbx.addItem("FEDERAL STATES OF MICRONESIA");
		cbx.addItem("MONACO");
		cbx.addItem("MONGOLIA");
		cbx.addItem("MONTENEGRO");
		cbx.addItem("MOROCCO");
		cbx.addItem("MOZAMBIQUE");
		cbx.addItem("MYANMAR");
		cbx.addItem("NAMIBIA");
		cbx.addItem("NAURU");
		cbx.addItem("NEPAL");
		cbx.addItem("NETHERLANDS");
		cbx.addItem("NEW ZEALAND");
		cbx.addItem("NICARAGUA");
		cbx.addItem("NIGER");
		cbx.addItem("NIGERIA");
		cbx.addItem("NORWAY");
		cbx.addItem("OMAN");
		cbx.addItem("PAKISTAN");
		cbx.addItem("PALAU");
		cbx.addItem("PANAMA");
		cbx.addItem("PAPUA NEW GUINEA");
		cbx.addItem("PARAGUAY");
		cbx.addItem("PERU");
		cbx.addItem("PHILIPPINES");
		cbx.addItem("POLAND");
		cbx.addItem("PORTUGAL");
		cbx.addItem("QATAR");
		cbx.addItem("REPUBLIC OF KOREA");
		cbx.addItem("REPUBLIC OF MOLDOVA");
		cbx.addItem("ROMANIA");
		cbx.addItem("RUSSIAN FEDERATION");
		cbx.addItem("RWANDA");
		cbx.addItem("SAINT KITTS AND NEVIS");
		cbx.addItem("SAINT LUCIA");
		cbx.addItem("SAINT VINCENT AND THE GRENADINES");
		cbx.addItem("SAMOA");
		cbx.addItem("SAN MARINO");
		cbx.addItem("SAO TOME AND PRINCIPE");
		cbx.addItem("SAUDI ARABIA");
		cbx.addItem("SENEGAL");
		cbx.addItem("SERBIA");
		cbx.addItem("SEYCHELLES");
		cbx.addItem("SIERRA LEONE");
		cbx.addItem("SINGAPORE");
		cbx.addItem("SLOVAKIA");
		cbx.addItem("SLOVENIA");
		cbx.addItem("SOLOMON ISLANDS");
		cbx.addItem("SOMALIA");
		cbx.addItem("SOUTH AFRICA");
		cbx.addItem("SPAIN");
		cbx.addItem("SRI LANKA");
		cbx.addItem("SUDAN");
		cbx.addItem("SURINAME");
		cbx.addItem("SWAZILAND");
		cbx.addItem("SWEDEN");
		cbx.addItem("SWITZERLAND");
		cbx.addItem("SYRIAN ARAB REPUBLIC");
		cbx.addItem("TAJIKISTAN");
		cbx.addItem("THAILAND");
		cbx.addItem("THE FORMER YUGOSLAV REPUBLIC OF MACEDONIA");
		cbx.addItem("TIMOR-LESTE");
		cbx.addItem("TOGO");
		cbx.addItem("TONGA");
		cbx.addItem("TRINIDAD AND TOBAGO");
		cbx.addItem("TUNISIA");
		cbx.addItem("TURKEY");
		cbx.addItem("TURKMENISTAN");
		cbx.addItem("TUVALU");
		cbx.addItem("UGANDA");
		cbx.addItem("UKRAINE");
		cbx.addItem("UNITED ARAB EMIRATES");
		cbx.addItem("GREAT BRITAIN");
		cbx.addItem("UNITED REPUBLIC OF TANZANIA");
		cbx.addItem("UNITED STATES OF AMERICA");
		cbx.addItem("URUGUAY");
		cbx.addItem("UZBEKISTAN");
		cbx.addItem("VANUATU");
		cbx.addItem("VENEZUELA");
		cbx.addItem("VIETNAM");
		cbx.addItem("YEMEN");
		cbx.addItem("ZAMBIA");
		cbx.addItem("ZIMBABWE");
	}
	
	
	/* Public -------------------------------------------------------------------*/
	
	
}
