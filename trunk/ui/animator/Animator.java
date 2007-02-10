package ui.animator;

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
public abstract class Animator {

	int mSec = 25;	// 40Hz animation framerate (ideally)
	long timeBegin = 0L;
	long duration  = 0L;
	float lastPercentComplete = 0.0f;
	boolean goingFwd = true;
	
	public void animateForwards(long mSecDuration){
		goingFwd = true;
		timeBegin = System.currentTimeMillis();
		duration  = mSecDuration; 
		timeBegin -= (long)(lastPercentComplete*(float)duration);

		if(duration <= 0){
			animatorTransition(1.0f);
			lastPercentComplete = 1.0f;
			return;
		}
		new Runnable() {
			public void run() {		
				if(goingFwd){
					float percentComplete = (float)(System.currentTimeMillis()-timeBegin) / (float)duration;
					if(percentComplete <= 1.0){
						animatorTransition(percentComplete);
						lastPercentComplete = percentComplete;
						Display.getCurrent().timerExec(mSec,this);
					}else{
						animatorTransition(1.0f);
						lastPercentComplete = 1.0f;
					}
				}
			}
		}.run();
	}
	
	public void animateBackwards(long mSecDuration){
		goingFwd = false;
		timeBegin = System.currentTimeMillis();
		duration  = mSecDuration; 	
		timeBegin -= (long)((1.0f-lastPercentComplete)*(float)duration);
		
		if(duration <= 0){
			animatorTransition(0.0f);
			lastPercentComplete = 0.0f;
			return;
		}
		new Runnable() {
			public void run() {
				if(!goingFwd){
					float percentComplete = (float)(System.currentTimeMillis()-timeBegin) / (float)duration;
					if(percentComplete <= 1.0){
						animatorTransition(1.0f - percentComplete);
						lastPercentComplete = (1.0f - percentComplete);
						Display.getCurrent().timerExec(mSec,this);
					}else{
						animatorTransition(0.0f);
						lastPercentComplete = 0.0f;
					}
				}
			}
		}.run();
	}	
	
	public void setToLastValue(){
		animatorTransition(lastPercentComplete);
	}
	
	public abstract void animatorTransition(float percentComplete);
	
}
