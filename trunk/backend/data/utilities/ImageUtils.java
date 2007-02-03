package backend.data.utilities;

import java.io.File;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;


//
//Copyright (C) 2007 avoCADo (Adam Kumpf creator)
//This code is distributed under the terms of the 
//GNU General Public License (GPL).
//
//This file is part of avoADo.
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

	
	public static final String ICON_DIR = "./icons/";
	
	public static Image getIcon(String filename, int width, int height){
		int ICON_WIDTH  = width;
		int ICON_HEIGHT = height;
		File test = new File(ICON_DIR + filename);
		if(test.isFile() != true){
			filename = "no-image.png";
		}
		Image tempImage = new Image(Display.getCurrent(), ICON_DIR + filename);
		ImageData id = tempImage.getImageData().scaledTo(ICON_WIDTH,ICON_HEIGHT);
		return new Image(Display.getCurrent(),id);
	}
	
}
