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
import backend.global.AvoGlobal;
import backend.model.Feature2D;
import backend.model.Feature2D3D;
import backend.model.Feature3D;
import backend.model.Feature3D3D;
import backend.model.Group;
import backend.model.Part;
import backend.model.Project;
import backend.model.Sketch;


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
		
		AvoGlobal.modelEventHAndler.addModelListener(new ModelListener(){
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
				// TODO Set active elements in model based on treeItem selected!
				if(tree.getSelection().length > 0){
					TreeItem ti = tree.getSelection()[0];
					int[] indxs = (int[])ti.getData();
					System.out.println("TreeViewer clicked.. should change active element in model (not implemented)");
					System.out.print(" ---> ");
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
	
	// TODO: this should NOT be public.. use a listener for model changes!!
	void buildTreeFromAssembly(){
		Project project = AvoGlobal.project;
		
		// TODO: HACK! don't build the tree from scratch every time!!
		tree.removeAll();
		
		if(project == null){
			return;
		}

		for(int iGroup=0; iGroup < project.getGroupListSize(); iGroup++){
			Group group = project.getAtIndex(iGroup);
			TreeItem tiGroup = new TreeItem(tree, SWT.NONE, iGroup);
			tiGroup.setText("Group");
			tiGroup.setData(new int[] {iGroup});
			for(int iPart=0; iPart<group.getPartListSize(); iPart++){
				Part part = group.getAtIndex(iPart);
				TreeItem tiPart = new TreeItem(tiGroup, SWT.NONE, iPart);
				tiPart.setText("Part");
				tiPart.setData(new int[] {iGroup, iPart});
				for(int iFeat3D=0; iFeat3D < part.getFeat3DListSize(); iFeat3D++){
					Feature3D feat3D = part.getAtIndex(iFeat3D);
					TreeItem tiFeat3D = new TreeItem(tiPart, SWT.NONE, iFeat3D);
					tiFeat3D.setText("Feat3D");
					tiFeat3D.setData(new int[] {iGroup, iPart, iFeat3D});
					if(feat3D instanceof Feature2D3D){
						Feature2D3D feat2D3D = (Feature2D3D)feat3D;
						for(int iSketch=0; iSketch < feat2D3D.getSketchListSize(); iSketch++){
							Sketch sketch = feat2D3D.getAtIndex(iSketch);
							TreeItem tiSketch = new TreeItem(tiFeat3D, SWT.NONE, iSketch);
							tiSketch.setText("Sketch");
							tiSketch.setData(new int[] {iGroup, iPart, iFeat3D, iSketch});
							for(int iFeat2D=0; iFeat2D < sketch.getFeat2DListSize(); iFeat2D++){
								Feature2D feat2D = sketch.getAtIndex(iFeat2D);
								TreeItem tiFeat2D = new TreeItem(tiSketch, SWT.NONE, iFeat2D);
								tiFeat2D.setText(feat2D.getParamSet().label);
								tiFeat2D.setData(new int[] {iGroup, iPart, iFeat3D, iSketch, iFeat2D});
							}
						}
					}
					if(feat3D instanceof Feature3D3D){
						Feature3D3D feat3D3D = (Feature3D3D)feat3D;
						for(int iFeat3D3D=0; iFeat3D3D < feat3D3D.getFeat3DListSize(); iFeat3D3D++){
							Feature3D subFeat3D = feat3D3D.getAtIndex(iFeat3D3D);
							TreeItem tiSubFeat3D = new TreeItem(tiFeat3D, SWT.NONE, iFeat3D3D);
							tiSubFeat3D.setText("SubFeat3D");
							tiSubFeat3D.setData(new int[] {iGroup, iPart, iFeat3D, iFeat3D3D});
						}
					}
				}
			}
		}

		
	}
	
}
