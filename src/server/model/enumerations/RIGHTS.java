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


package server.model.enumerations;

import java.util.ArrayList;

public enum RIGHTS {
	READ (1),
	CREATE (2),
	EDIT (4),
	DELETE (8);
	
	
	private int right;
	
	RIGHTS(int r){
		this.right = r;
	}
	
	public int get(){
		return this.right;
	}
	
	public static boolean hasRight(int grantedLevel, RIGHTS neededLevel){
		if (grantedLevel>=RIGHTS.DELETE.get()){
			if (neededLevel == RIGHTS.DELETE){
				return true;
			}
			grantedLevel -= RIGHTS.DELETE.get();
		}else{
			return false;
		}
		if (grantedLevel >= RIGHTS.EDIT.get()){
			if (neededLevel == RIGHTS.EDIT){
				return true;
			}
			grantedLevel -= RIGHTS.EDIT.get();
		}else{
			return false;
		}
		if (grantedLevel >= RIGHTS.CREATE.get()){
			if (neededLevel == RIGHTS.CREATE){
				return true;
			}
			grantedLevel -= RIGHTS.CREATE.get();
		}else{
			return false;
		}
		if (grantedLevel == RIGHTS.READ.get()){
			return true;
		}
		return false;
	}
	
	public static boolean hasRight(int grantedLevel, int neededLevel){
		ArrayList<RIGHTS> rights = new ArrayList<RIGHTS>();
		
		if (hasRight(neededLevel, RIGHTS.READ)){
			rights.add(RIGHTS.READ);
		}
		if (hasRight(neededLevel, RIGHTS.CREATE)){
			rights.add(RIGHTS.READ);
		}
		if (hasRight(neededLevel, RIGHTS.EDIT)){
			rights.add(RIGHTS.READ);
		}
		if (hasRight(neededLevel, RIGHTS.DELETE)){
			rights.add(RIGHTS.READ);
		}
		
		for (RIGHTS right: rights){
			if (!hasRight(grantedLevel, right)){
				return false;
			}
		}
		
		return true;
	}
	
	public static int extractRights(int level, int extract){
		int rightLevel = 0;
		
		if (hasRight(level, RIGHTS.READ) && !hasRight(extract, RIGHTS.READ)){
			rightLevel += RIGHTS.READ.get();
		}
		if (hasRight(level, RIGHTS.CREATE) && !hasRight(extract, RIGHTS.CREATE)){
			rightLevel += RIGHTS.CREATE.get();
		}
		if (hasRight(level, RIGHTS.EDIT) && !hasRight(extract, RIGHTS.EDIT)){
			rightLevel += RIGHTS.EDIT.get();
		}
		if (hasRight(level, RIGHTS.DELETE) && !hasRight(extract, RIGHTS.DELETE)){
			rightLevel += RIGHTS.DELETE.get();
		}
		
		return rightLevel;
	}

	public static int mergeRights(int level1, int level2) {
		int rightLevel = 0;
		
		if (hasRight(level1, RIGHTS.READ)||hasRight(level2, RIGHTS.READ)){
			rightLevel += RIGHTS.READ.get();
		}
		if (hasRight(level1, RIGHTS.CREATE)||hasRight(level2, RIGHTS.CREATE)){
			rightLevel += RIGHTS.CREATE.get();
		}
		if (hasRight(level1, RIGHTS.EDIT)||hasRight(level2, RIGHTS.EDIT)){
			rightLevel += RIGHTS.EDIT.get();
		}
		if (hasRight(level1, RIGHTS.DELETE)||hasRight(level2, RIGHTS.DELETE)){
			rightLevel += RIGHTS.DELETE.get();
		}
		
		return rightLevel;
	}
}
