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

package plugins.home.client.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import client.view.InnerView;

/**
* @author torstend
*
*/
public class HomeView extends InnerView {
	private static final long serialVersionUID = 1443950876456631847L;
	private JPanel widgetPanel;
    private JPanel shortcutPanel;
    private JPanel infoPanel;
    private LayoutManager widgetLayout;
    private LayoutManager shortcutLayout;
    private LayoutManager infoLayout;
    private Container content;
    
    public HomeView(){
       super("Home", "HomeView");
       this.setUp();
    }
    
    private void setUp(){
       JPanel tempPanel = new JPanel();
       this.content = this.getContentPane();
       
       this.widgetLayout = new GridBagLayout();
       this.shortcutLayout = new GridLayout(2,5,10,10);
       this.infoLayout = new GridBagLayout();
       
       this.widgetPanel = new JPanel();
       this.shortcutPanel = new JPanel();
       this.infoPanel = new JPanel();
                                       
       this.widgetPanel.setLayout(this.widgetLayout);
       this.shortcutPanel.setLayout(this.shortcutLayout);
       this.infoPanel.setLayout(this.infoLayout);
       
       this.widgetPanel.setBackground(new Color(255,0,0));
       this.shortcutPanel.setBackground(new Color(0,255,0));
       this.infoPanel.setBackground(new Color(0,0,255));

       this.widgetPanel.setSize(300,300);
       this.shortcutPanel.setSize(300,300);
       this.infoPanel.setSize(300,600);
       
       tempPanel.setLayout(new GridLayout(2,1));
       tempPanel.add(this.widgetPanel);
       tempPanel.add(this.shortcutPanel);
       
       this.content.setLayout(new GridLayout(1,2));
       this.content.add(tempPanel);
       this.content.add(this.infoPanel);
    }
}
