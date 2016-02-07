/*
 * Copyright (c) 2016, Vitthal Kavitake
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * 	 this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *   
 * * Neither the name of IpTweet nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *   
 *   
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *   AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *   IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *   DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 *   FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 *   DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *   SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *   CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *   OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 *   OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ip.tweet.component;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import ip.tweet.data.User;

/**
 * @author Vitthal Kavitake
 */
public class Participants extends JPanel{

	private static final long serialVersionUID = 1L;
	JTextPane textPane;
	private static final int MAX_HEIGHT = 50;
	public Participants(){
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		textPane = new JTextPane();
		//textPane.setContentType("text/html");
		textPane.setEditable(false);
		textPane.setOpaque(true);
		
		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setOpaque(false);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		add(scrollPane);
	}
	public void setUsers(User[] users){
		textPane.removeAll();
		StringBuilder sb = new StringBuilder();
		final Style cwStyle = new StyleContext().addStyle("ConstantWidth", null);
		StyleConstants.setBold(cwStyle, true);
		boolean firstUser = true;
		for(User user: users){
			if(firstUser){
				firstUser = false;
			}
			else{
				sb.append(", ");
			}
			sb.append(user.getDisplayName());
		}
		try {
			textPane.getDocument().insertString(textPane.getDocument().getLength(), "Participants: ", cwStyle);
			StyleConstants.setBold(cwStyle, false);
			textPane.getDocument().insertString(textPane.getDocument().getLength(), sb.toString(), cwStyle);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Dimension getPreferredSize(){
		Dimension d = super.getPreferredSize();
		if(d.height>MAX_HEIGHT){
			requestRepaint();
			return new Dimension(SwingUtilities.getRoot(this).getWidth(), MAX_HEIGHT);
		}
		else{
			requestRepaint();
			return new Dimension(SwingUtilities.getRoot(this).getWidth(),  d.height);
		}
	}
	
	@Override
	public Dimension getMaximumSize(){
		Dimension d = super.getPreferredSize();
		return new Dimension(SwingUtilities.getRoot(this).getWidth(), d.height>MAX_HEIGHT ? MAX_HEIGHT:d.height);
	}
	
	@Override
	public Dimension getMinimumSize(){
		return new Dimension(SwingUtilities.getRoot(this).getWidth(), 30);
	}
	
	private void requestRepaint(){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				//textPane.invalidate();
				//textPane.repaint();
				textPane.getParent().invalidate();
				textPane.getParent().repaint();
			}
		});
	}
}
