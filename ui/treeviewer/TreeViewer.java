package ui.treeviewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import backend.global.AvoGlobal;
import backend.model.Assembly;
import backend.model.Feature2D;
import backend.model.Feature3D;
import backend.model.Part;


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
public class TreeViewer {

	private static Composite treeComp;
	private static Tree tree;
	
	public TreeViewer(Composite comp, int style){
		treeComp = new Composite(comp, style);
		treeComp.setBackground(new Color(Display.getCurrent(), 200, 200, 240));
		treeComp.setLayout(new FillLayout());
		
		tree = new Tree(treeComp, SWT.NONE);
		buildTreeFromAssembly();
	}
	
	public void buildTreeFromAssembly(){
		Assembly asm = AvoGlobal.assembly;
		tree.removeAll();
		
		if(asm == null){
			return;
		}
		
		if(asm.partList.size() > 0){
			int iPart = 0;
			for(Part p : asm.partList){
				TreeItem tiPart = new TreeItem(tree, SWT.NONE, iPart++);
				tiPart.setText("Part");
				if(p.feat3DList.size() > 0){
					int iF3D = 0;
					for(Feature3D f3D : p.feat3DList){
						TreeItem tiFeat3D = new TreeItem(tiPart, SWT.NONE, iF3D++);
						tiFeat3D.setText("Feature3D");
						if(f3D.feat2DList.size() > 0){
							int iF2D = 0;
							for(Feature2D f2D : f3D.feat2DList){
								TreeItem tiFeat2D = new TreeItem(tiFeat3D, SWT.NONE, iF2D++);
								tiFeat2D.setText(f2D.label);
							}
						}
					}
				}				
			}
		}
		
		
	}
	
}
