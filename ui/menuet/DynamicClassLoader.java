package ui.menuet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;


//
//Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//This code is distributed under the terms of the 
//GNU General Public License (GPL).
//
//This file is part of avoCADo.
//
//AvoCADo is free software; you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation; either version 2 of the License, or
//(at your option) any later version.
//
//AvoCADo is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with AvoCADo; if not, write to the Free Software
//Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
//

/*
* @author  Adam Kumpf
* @created Nov. 2007
*/
public class DynamicClassLoader extends ClassLoader{

	public Class getClassFromFile(String name, String filename){  
    	int compileReturnCode = com.sun.tools.javac.Main.compile(new String[] {filename});
    	Class c = null;
    	if (compileReturnCode == 0) {
    		// run it
    		Object objectParameters[] = {new String[]{}};
    		Class classParameters[] = {objectParameters[0].getClass()};
    		try{
	    		c = Class.forName("ui.menuet.dynamicClasses." + name);
	    		Object instance = c.newInstance();		   
	    		Method theMethod = c.getDeclaredMethod("main", classParameters);
	    		theMethod.invoke(instance, objectParameters);
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}    	
    	return c;
    }
	
	private static byte[] getBytes(String filename) throws IOException{
		File file = new File(filename);
		long len = file.length();
		byte rawBytes[] = new byte[(int)len];
		FileInputStream fins = new FileInputStream(file);
		int r = fins.read(rawBytes);
		if(r != len){
			throw new IOException("can't read all bytes in " + filename + ": " + r + " != " + len);
		}
		fins.close();
		return rawBytes;	
	}
	
}
