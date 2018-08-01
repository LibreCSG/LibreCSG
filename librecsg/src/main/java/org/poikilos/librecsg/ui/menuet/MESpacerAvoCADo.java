package org.poikilos.librecsg.ui.menuet;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;

import org.poikilos.librecsg.backend.data.utilities.ImageUtils;

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
* @created Mar. 2007
*/
public class MESpacerAvoCADo {

	public MESpacer spacer = null;

	private int conversationIndex = 0;
	private String[] currentConversation = {""};

	/**
	 * The avoCADo with a personality!
	 * @param menuet
	 * @param mode
	 */
	public MESpacerAvoCADo(Menuet menuet, int mode) {
		Image image = ImageUtils.getIcon("./avoCADo.png", 32, 32);
		spacer = new MESpacer(menuet, Menuet.MENUET_MODE_PROJECT, image);
		spacer.setToolTipText("Hello.  How are you today?\nCare to talk to an avocado?");
		spacer.addMouseTrackListener(new MouseTrackListener(){
			public void mouseEnter(MouseEvent e) {
			}
			public void mouseExit(MouseEvent e) {
				spacer.setToolTipText(getRandomQuote());
			}
			public void mouseHover(MouseEvent e) {
			}
		});
	}

	private String getRandomQuote(){
		conversationIndex++;
		if(conversationIndex < currentConversation.length){
			String convWithHistory = "";
			for(int i=0; i< conversationIndex; i++){
				convWithHistory += "> " + currentConversation[i] + "\n";
			}
			return convWithHistory + currentConversation[conversationIndex];
		}
		//
		// These are purely to entertain people.
		// Ideally it gives the program a bit of a comical personality.
		// try to keep things light-hearted and funny. :)
		//
		String[][] allQuotes = {
				{"Salsa tastes better."},
				{"potty break... brb."},
				{"Knock Knock", "Who's there?", "Guess!", "Guess Who?", "That's what I was trying to do!"},
				{"Guess a number between 1 and 10.", "  I picked the number " + Integer.toString(1+(int)Math.floor(Math.random()*10)),
					"Why don't you try again. :)", "  I picked the number " + Integer.toString(1+(int)Math.floor(Math.random()*10))},
				{"I'm green with envy!"},
				{"Don't mind me.  I'll be here all day."},
				{"You look fantabulous!", "just kidding :P"},
				{"I'm a bit hungry...", "Out for a byte to eat", "mmm.. java and chips again!"},
				{"thinking..."}
		};
		int numQuotes = allQuotes.length;
		int index = (int)(Math.floor(Math.random()*numQuotes));
		conversationIndex = 0;
		currentConversation = allQuotes[index];
		return currentConversation[conversationIndex];
	}

}
