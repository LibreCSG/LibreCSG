package ui.utilities;

import icons.ImageLocation;

import java.io.File;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;


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
* @created Feb. 2007
*/
public class ImageUtils {

	
	public static final String ICON_DIR = "icons/";

	private static ImageRegistry imageRegistry = null;
	
	private static void prepImageRegistry() {
		if(imageRegistry == null) {
			imageRegistry = new ImageRegistry(Display.getCurrent());
		}
	}
	private static String processFilename(String fname) {
		if(fname == null)
			return "no-image.png";
		if(fname.equals(""))
			return "no-image.png";
		File f = new File(fname);
		if(fname.indexOf("menuet") >= 0) {
			return "menuet/"+f.getName();
		}
		return f.getName();
	}
	public static Image getIcon(String filename) {
		filename = processFilename(filename);
		prepImageRegistry();
		Image image = imageRegistry.get(filename);
		if(image == null) {
			ImageDescriptor desc = ImageDescriptor.createFromFile (ImageLocation.class, filename);
			imageRegistry.put(filename, desc);
		}
		image = imageRegistry.get(filename);
		if(image == null)
			image = getIcon("no-image.png");
		return image;
	}
	public static Image getIcon(String filename, int width, int height){
		filename = processFilename(filename);
		prepImageRegistry();
//		int ICON_WIDTH  = width;
//		int ICON_HEIGHT = height;
//		String key = filename + "_" + String.valueOf(width)+ "_" + String.valueOf(height);
//		
//		Image image = imageRegistry.get(key);
//		if(image == null) {
//			Image baseImage = getIcon(filename);
//
//			ImageDescriptor id = ImageDescriptor.createFromImageData(baseImage.getImageData().scaledTo(width, height));
//			if(id != null)
//				imageRegistry.put(key, id);
//		}
//		image = imageRegistry.get(key);
//		if(image == null) {
//			image = getIcon("no-image.png",width,height);
//		}
//		return image;
		return getIcon(filename);
	}
	
}
