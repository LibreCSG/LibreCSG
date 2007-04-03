package ui.treeviewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import ui.event.ModelListener;
import ui.menuet.Menuet;
import backend.global.AvoGlobal;
import backend.model.Feature2D;
import backend.model.Feature2D3D;
import backend.model.Feature3D3D;
import backend.model.Group;
import backend.model.Part;
import backend.model.Project;
import backend.model.Sketch;
import backend.model.SubPart;


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
		
		tree = new Tree(treeComp, SWT.SINGLE);
		buildTreeFromAssembly();
		
		AvoGlobal.modelEventHandler.addModelListener(new ModelListener(){
			public void activeElementChanged() {
				// TODO highlight the new active element (or none, if null)				
			}
			public void elementAdded() {
				buildTreeFromAssembly();
				
			}
			public void elementRemoved() {
				buildTreeFromAssembly();				
			}			
		});
		
		tree.addMouseListener(new MouseListener(){
			public void mouseDoubleClick(MouseEvent e) {
				// Set active elements in model based on treeItem selected.
				if(tree.getSelection().length > 0){
					TreeItem ti = tree.getSelection()[0];
					int[] indxs = (int[])ti.getData();
					if(indxs.length == 1){
						// group selected
						AvoGlobal.project.setActiveGroup(indxs[0]);
						AvoGlobal.menuet.setCurrentToolMode(Menuet.MENUET_MODE_GROUP);
						AvoGlobal.glView.updateGLView = true;
					}
					if(indxs.length == 2){
						// part selected
						AvoGlobal.project.setActiveGroup(indxs[0]);
						AvoGlobal.project.getActiveGroup().setActivePart(indxs[1]);
						AvoGlobal.menuet.setCurrentToolMode(Menuet.MENUET_MODE_PART);
						AvoGlobal.glView.updateGLView = true;
					}
					if(indxs.length == 3){
						// part selected
						AvoGlobal.project.setActiveGroup(indxs[0]);
						AvoGlobal.project.getActiveGroup().setActivePart(indxs[1]);
						// handle special cases on subpart for properties and root planes
						if(indxs[2] == 0){
							// properties for part
							System.out.println("Tree viewer.. Should open a Part Properties dialog, but not implemented yet.");
							return;
						}
						if(indxs[2] == 1 || indxs[2] == 2 || indxs[2] == 3){
							// root plane selected
							System.out.println("Tree viewer.. you clicked a root plane.. but it hasn't been implemented fully yet.");
							return;
						}else{
							// sub-part selected.
							SubPart subpart = AvoGlobal.project.getActivePart().getAtIndex(indxs[2]-4);
							Sketch sketch =  subpart.getSketch();
							if(sketch != null){
								// subpart was a "sketch" tool
								AvoGlobal.project.getActivePart().setActiveSubPart(indxs[2]-4);
								AvoGlobal.menuet.setCurrentToolMode(Menuet.MENUET_MODE_SKETCH);
								AvoGlobal.glView.updateGLView = true;
							}
							Feature2D3D feat2D3D = subpart.getFeature2D3D();
							if(feat2D3D != null){
								// subpart was a "build" tool
								AvoGlobal.project.getActivePart().setActiveSubPart(indxs[2]-4);
								AvoGlobal.menuet.setCurrentToolMode(Menuet.MENUET_MODE_BUILD);
								AvoGlobal.glView.updateGLView = true;
							}
							Feature3D3D feat3D3D = subpart.getFeature3D3D();
							if(feat3D3D != null){
								// subpart was a "modify" tool.
								AvoGlobal.project.getActivePart().setActiveSubPart(indxs[2]-4);
								AvoGlobal.menuet.setCurrentToolMode(Menuet.MENUET_MODE_MODIFY);
								AvoGlobal.glView.updateGLView = true;
							}							
						}
					}
					
					System.out.print("---> ");
					for(int i : indxs){
						System.out.print(i + ",");
					}
					System.out.print("\n");
				}
			}
			public void mouseDown(MouseEvent e) {			
			}
			public void mouseUp(MouseEvent e) {			
			}			
		});
	}
	
	// triggered via event listener
	void buildTreeFromAssembly(){
		Project project = AvoGlobal.project;
		
		// TODO: HACK! don't build the tree from scratch every time!!
		//tree.removeAll();
		
		if(project == null){
			return;
		}

		for(int i=project.getGroupListSize(); i<tree.getItemCount(); i++){
			// remove groups that no longer exist
			tree.getItem(i).dispose();
		}
		for(int iGroup=0; iGroup < project.getGroupListSize(); iGroup++){
			Group group = project.getAtIndex(iGroup);
			TreeItem tiGroup;
			if(tree.getItemCount() > iGroup){
				tiGroup = tree.getItem(iGroup);
			}else{
				tiGroup = new TreeItem(tree, SWT.NONE, iGroup);
			}
			tiGroup.setText("Group " + group.ID);
			tiGroup.setData(new int[] {iGroup});
			for(int i=group.getPartListSize(); i<tiGroup.getItemCount(); i++){
				// remove parts that no longer exist
				tiGroup.getItem(i).dispose();
			}
			for(int iPart=0; iPart<group.getPartListSize(); iPart++){
				Part part = group.getAtIndex(iPart);
				TreeItem tiPart;
				if(tiGroup.getItemCount() > iPart){
					tiPart = tiGroup.getItem(iPart);
				}else{
					tiPart = new TreeItem(tiGroup, SWT.NONE, iPart);
					tiGroup.setExpanded(true);
				}
				tiPart.setText("Part " + part.ID);
				tiPart.setData(new int[] {iGroup, iPart});
				
				TreeItem tiPartProp;
				if(tiPart.getItemCount() > 0){
					tiPartProp = tiPart.getItem(0);
				}else{
					tiPartProp = new TreeItem(tiPart, SWT.NONE, 0);
					tiPart.setExpanded(true);
				}
				tiPartProp.setData(new int[] {iGroup, iPart, 0});
				tiPartProp.setText("Properties");
				
				TreeItem tiPartXY;
				if(tiPart.getItemCount() > 1){
					tiPartXY = tiPart.getItem(1);
				}else{
					tiPartXY = new TreeItem(tiPart, SWT.NONE, 1);
				}
				tiPartXY.setData(new int[] {iGroup, iPart, 1});
				tiPartXY.setText("XY Plane");	
				
				TreeItem tiPartYZ;
				if(tiPart.getItemCount() > 2){
					tiPartYZ = tiPart.getItem(2);
				}else{
					tiPartYZ = new TreeItem(tiPart, SWT.NONE, 2);
				}
				tiPartYZ.setData(new int[] {iGroup, iPart, 2});
				tiPartYZ.setText("YZ Plane");
				
				TreeItem tiPartZX;
				if(tiPart.getItemCount() > 3){
					tiPartZX = tiPart.getItem(3);
				}else{
					tiPartZX = new TreeItem(tiPart, SWT.NONE, 3);
				}
				tiPartZX.setData(new int[] {iGroup, iPart, 3});
				tiPartZX.setText("ZX Plane");
				
				for(int i=part.getSubPartListSize()+4; i<tiPart.getItemCount(); i++){
					// remove subParts that no longer exist
					tiPart.getItem(i).dispose();
				}
				for(int iSubPart=0; iSubPart < part.getSubPartListSize(); iSubPart++){
					SubPart subPart = part.getAtIndex(iSubPart);
					TreeItem tiSubPart;
					if(tiPart.getItemCount() > iSubPart+4){
						tiSubPart = tiPart.getItem(iSubPart+4);
					}else{
						tiSubPart = new TreeItem(tiPart, SWT.NONE, iSubPart+4);
						tiPart.setExpanded(true);
					}
					tiSubPart.setData(new int[] {iGroup, iPart, iSubPart+4});
					Sketch sketch = subPart.getSketch();					
					if(sketch != null){
						tiSubPart.setText("Sketch " + sketch.getID());
						if(sketch.isConsumed){
							tiSubPart.setText("Sketch(c) " + sketch.getID());
							tiSubPart.setBackground(new Color(Display.getCurrent(), 240, 200, 200));
						}
						
						for(int i=sketch.getFeat2DListSize(); i<tiSubPart.getItemCount(); i++){
							// remove Feature2D that no longer exist
							tiSubPart.getItem(i).dispose();
						}
						for(int iSketch=0; iSketch < sketch.getFeat2DListSize(); iSketch++){
							Feature2D feat2D = sketch.getAtIndex(iSketch);
							TreeItem tiFeat2D;
							if(tiSubPart.getItemCount() > iSketch){
								tiFeat2D = tiSubPart.getItem(iSketch);
							}else{
								tiFeat2D = new TreeItem(tiSubPart, SWT.NONE, iSketch);
							}
							tiFeat2D.setData(new int[] {iGroup, iPart, iSubPart, iSketch});
							tiFeat2D.setText(feat2D.paramSet.label);
						}						
					}
					Feature2D3D feat2D3D = subPart.getFeature2D3D();
					if(feat2D3D != null){
						if(feat2D3D.paramSet != null){
							tiSubPart.setText(feat2D3D.paramSet.label);
						}else{
							tiSubPart.setText("2Dto3D " + feat2D3D.ID);
						}
					}
					Feature3D3D feat3D3D = subPart.getFeature3D3D();
					if(feat3D3D != null){
						tiSubPart.setText("Feature 3D " + feat3D3D.ID);
					}
					
				}
			}
		}

		
	}
	
}
