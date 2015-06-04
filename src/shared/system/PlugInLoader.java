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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import shared.util.ClinSysException;
import shared.util.Debugger;



/**
 * 
 * 
 * @author Torsten Dietl
 * @version 1.0.0a
 */
public class PlugInLoader 
{	
	
	/* Constants ----------------------------------------------------------------*/
	
	// used to specify, which part of the plug-in should be loaded.
	public static final String CLIENT = "client";
	public static final String SERVER = "server";
	
	
	/* Variables ----------------------------------------------------------------*/
	
	// path to the plug-ins folder
	private String pluginPath;
	
	// variable storing the part of the plug-in that should be loaded:
	// either the constant CLIENT or constant SERVER
	private String part;	
	
	/* Constructor --------------------------------------------------------------*/
	
	
	/* Getter -------------------------------------------------------------------*/
	
	
	/**
	 * Returns the path to the plugins folder that is used.
	 * 
	 * @return Path to the plugins folder, which contains the plug-ins
	 */
	public String getPluginPath() 
	{
		return pluginPath;
	}

	
	/**
	 * Returns the part of a plug-in that should be loaded. 
	 * 
	 * @return Part of the plug-in that should be loaded.
	 */
	public String getPart() 
	{
		return part;
	}


	/* Setter -------------------------------------------------------------------*/
	
	/**
	 * Sets the part of a plug-in that should be loaded either CLIENT or SERVER.
	 * 
	 * @param part Either the constant CLIENT or SERVER. Indicating the part of the
	 * 			   plug-in that should be loaded.
	 */
	public void setPart(String part) 
	{
		this.part = part;
	}

	/**
	 * Sets the path to the plugins folder from which the plug-ins will be loaded.
	 * 
	 * @param pluginPath the pluginPath to set
	 */
	public void setPluginPath(String pluginPath) 
	{
		this.pluginPath = pluginPath;
	}
	
	
	/* Private ------------------------------------------------------------------*/
	
	/**
	 * loads all classes of the part folder (CLIENT or SERVER) and its sub-folder 
	 * and returns a list of all found controllers of which there should be 
	 * only one... ?!?
	 * 
	 * @param plugin The folder of the plug-in part (either CLIENT or SERVER)
	 * @param packages The "package path", of the given plug-in part
	 * @return A list of Controller objects, should be only one in general
	 * @throws ClinSysException Is thrown if a class cannot be loaded
	 */
	private ArrayList<AbstractController> loadSpecificClasses(File plugin, 
									String packages) throws ClinSysException
	{
		// all files and folders of the part folder of a plug-in
		File[] classes;
		
		// an url object covering the path to the file/folder 
		URL url = null;
		
		// a classloader
		URLClassLoader cl = null;
		
		// a class object used to check if it's a Controller object
		Class<?> c;
		
		// array storing class name parts, [0] = path + name, [1] = file format
		String[] classNameParts;
		
		// path and name of the class
		String className;
		
		// list containing the found and loaded Controller objects
		ArrayList<AbstractController> controllerList = 
											new ArrayList<AbstractController>();
		
		// load specific classes, only the classes of a part, either SERVER or 
		// CLIENT
		
		// get the files/folders of the part folder
		classes = plugin.listFiles();
		
		// go through all files/folders of the part folder
		for (File classFile : classes)
		{
			// if it's a file, then it should be a class file
			if (classFile.isFile())
			{
				// split the name to get the file format
				classNameParts = classFile.getPath().split("\\.");
				
				// to check if it's a class file
				if (classNameParts[1].equals("class"))
				{
					// retrieve the path+name of the class
					className = classNameParts[0];
					
					try 
					{
						// try to get the url to the file,
						// create a classloader
						// and load the given class
						url = classFile.toURI().toURL();
						cl = new URLClassLoader(new URL[]{url});
						c = cl.loadClass(className.replace("\\", "."));
						
						// if the class is derived from the AbstractController
						// class, which means it is a Controller object
						if (AbstractController.class.isAssignableFrom(c))
						{ 
							// try to add a new instance of it to the 
							// controller list
							try 
							{
								controllerList.add(
											(AbstractController) c.newInstance());
							} 
							catch (InstantiationException e) 
							{
								Debugger.debug(e.getMessage());
								throw new ClinSysException(
										"(PlugInLoader.loadSpecificClasses(File " +
									    "plugin, String packages)) : Could not " +
										"instantiate the class " + c.getName());
							} 
							catch (IllegalAccessException e) 
							{
								Debugger.debug(e.getMessage());
								throw new ClinSysException("(PlugInLoader." + 
								"loadSpecificClasses(File plugin, " + 
								"String packages)) : Could not instantiate the " +
								"class " + c.getName());
							}						
						}					
					} 
					catch (MalformedURLException e) 
					{
						throw new ClinSysException("(PlugInLoader." +
								"loadSpecificClasses(File plugin, " +
								"String packages)) : The given file " + 
								"url is not correct.");
					} 
					catch (ClassNotFoundException e) 
					{
						throw new ClinSysException("(PlugInLoader." +
								"loadSpecificClasses(File plugin, " + 
								"String packages)) : Class not found. " +
								"Can't load the class.");
					}
					finally
					{
						// finally try to close the classloader
						try
						{
							cl.close();
						} 
						catch (IOException e) 
						{
							throw new ClinSysException("(PlugInLoader." +
									"loadSpecificClasses(File plugin, " + 
									"String packages)) : Could not close the " +
									"URLClassLoader object");
						}
					}
				}
			}
			// if it's a folder then, go into it and load the classes
			// and add the Controller objects to the list.
			else if(classFile.isDirectory())
			{
				className = classFile.getName().split("\\.")[0];
				controllerList.addAll(this.loadSpecificClasses(classFile, 
									packages + "." + className.toLowerCase()));
			}
		}
		
		// return all found controller
		return controllerList;	
	}
	
	
	/**
	 * Loads all general class files. That means all files used by both the CLIENT
	 * and the SERVER part of the plug-in.
	 * 
	 * @param plugin The folder of the plug-in, which is loaded
	 * @return A list containing all Controller objects of a plug-in, should be 
	 * 		   only one in general. 
	 * @throws ClinSysException Is thrown if a class cannot be loaded.
	 */
	private ArrayList<AbstractController> loadPlugin(File plugin) 
							throws ClinSysException
	{
		// array containing all files and folders in the plug-in folder that is 
		// loaded
		File[] classes;
		
		// the path to the file/folder
		URL url = null;
		
		// the classloader to load the selected class during runtime
		URLClassLoader cl = null;
		
		// the name parts of a class, [0] = path + name, [1] = file format
		String[] classNameParts;
		
		// the path + name of a class
		String className;
		
		// get all files/folders of the plug-in folder
		classes = plugin.listFiles();
		
		// go through all files/folders
		for (File generalClass : classes)
		{
			// if it's a file, than it should be a general class, like the COMMAND
			// enumeration or something else. For sure it should be a class used
			// in both server and client application
			if (generalClass.isFile())
			{
				// split the path+name of the file at the position of '.' 
				// giving path + name in [0] and file format in [1]
				classNameParts = generalClass.getPath().split("\\.");
				
				// if the file format equals class, that means it is a class file
				if (classNameParts[1].equals("class"))
				{
					// store the path + filename in the className variable
					className = classNameParts[0];
					
					// try to create the url to the class file,
					// to create a classloader and
					// to load the class
					try 
					{
						url = generalClass.toURI().toURL();
						cl = new URLClassLoader(new URL[]{url});
						cl.loadClass(className.replace("\\", "."));					
					} 
					catch (MalformedURLException e) 
					{
						throw new ClinSysException("(PlugInLoader." +
										"loadPlugin(File plugin)) : The given " + 
										"file url is not correct.");
					} 
					catch (ClassNotFoundException e) 
					{
						throw new ClinSysException("(PlugInLoader.loadPlugin(" +
										"File plugin)) : Class '" + 
										(this.pluginPath + "." + className) + 
										"' not found. Can't load the class.");
					} 
					finally
					{
						// at least try to close the classloader again if something
						// failed
						try 
						{
							cl.close();
						} 
						catch (IOException e) 
						{
							throw new ClinSysException("(PlugInLoader." +
										"loadSpecificClasses(File plugin, " + 
										"String packages)) : Could not close " +
										"the URLClassLoader object");
						}
					}
				}
			}
			
		}
		
		// return the specific classes, which are the Controller classes of the 
		// specified part
		return this.loadSpecificClasses(new File(plugin.getPath() + "\\" + 
								this.part), plugin.getName().split("\\.")[0] + 
								"." + this.part);	
	}
	

	/* Public -------------------------------------------------------------------*/
	
	/**
	 * goes through all folder in the plugins folder and loads the plug-in classes
	 * as well as the Controller object. 
	 * 
	 * @return A list of all instantiated Controller objects of the plug-ins
	 * @throws ClinSysException is thrown if a class cannot be loaded.
	 */
	public AbstractController[] loadPlugins() throws ClinSysException
	{
		// open the plug-ins directory
		File dir = new File(this.pluginPath);
		
		// get all files and folders of the plug-ins directory
		String[] dirList = dir.list();

		// stores the folder of the current plug-in 
		File plugin;
		
		// the result list containing the plug-ins
		ArrayList<AbstractController> plugins;
		
		// create the new plug-in
		plugins = new ArrayList<AbstractController>();
		
		
		// go through the complete directory, folder by folder, 
		// each folder is a plug-in, should only contain folders
		for (String pluginDir : dirList)
		{
			// get the plug-in folder
			plugin = new File(this.pluginPath + "\\" + pluginDir);
			
			// only do something if it is really a folder
			if (plugin.isDirectory())
			{
				// add all plug-in classes 
				plugins.addAll(this.loadPlugin(plugin));
			}
		}
		
		// create a new array of AbstractController objects, with the count of 
		// found plug-ins as size
		AbstractController[] pluginArray = new AbstractController[plugins.size()];
		
		// put the found controller objects from the plugins list into the 
		// AbstractController array: pluginArray
		plugins.toArray(pluginArray);
		
		// return the AbstractController array
		return  pluginArray;
	}
}
