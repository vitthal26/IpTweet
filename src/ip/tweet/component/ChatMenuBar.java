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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import ip.tweet.client.ChatScreen;
import ip.tweet.constant.ChatConstant;
import ip.tweet.fileTransfer.FileSender;

/**
 * @author Vitthal Kavitake
 */
public class ChatMenuBar extends JMenuBar{
	/**
	 * 
	 */
	public static final int USER_MENU = 1;
	public static final int GROUP_MENU = 2;
	private ChatScreen userChat;
	private static final long serialVersionUID = 1L;
	public ChatMenuBar(int menuType,boolean isInitiator,ChatScreen userChat){
		super();
		this.userChat = userChat;
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		add(fileMenu);
		ActionListener al = new MenuActionListener();

		JMenuItem logFile = new JMenuItem("Open Chat Log");
		logFile.setMnemonic('O');
		logFile.addActionListener(al);
		fileMenu.add(logFile);
		
		JMenuItem shareScreen = new JMenuItem("Share Screen");
		shareScreen.setMnemonic('O');
		shareScreen.addActionListener(al);
		fileMenu.add(shareScreen);
		
		if(menuType==USER_MENU && ChatConstant.FILE_TRANSFER_ENABLED){
			JMenuItem aboutItem = new JMenuItem("Send File");
			aboutItem.setMnemonic('S');
			aboutItem.addActionListener(al);
			fileMenu.add(aboutItem);
		}

		if(menuType==GROUP_MENU && isInitiator){
			JMenuItem aboutItem = new JMenuItem("Add Recepient");
			aboutItem.setMnemonic('A');
			aboutItem.addActionListener(al);
			fileMenu.add(aboutItem);
		}

		JSeparator separator = new JSeparator();
		fileMenu.add(separator);

		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('E');
		exitItem.addActionListener(al);
		fileMenu.add(exitItem);
	}
	class MenuActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Send File")){
				FileSender fileTransfer = new FileSender(userChat);
				fileTransfer.selectFile();
			}
			else if(e.getActionCommand().equals("Add Recepient")){
				new GroupConversationMembers(ChatMenuBar.this.userChat.getChatUsers(),ChatMenuBar.this.userChat.getChatId());
			}
			else if(e.getActionCommand().equals("Exit")){
				ChatMenuBar.this.userChat.removeChat();
				ChatMenuBar.this.userChat.dispose();
			}
			else if(e.getActionCommand().equals("Open Chat Log")){
				ChatMenuBar.this.userChat.openChatLog();
			}
			else if(e.getActionCommand().equals("Share Screen")){
				ChatMenuBar.this.userChat.initiateScreenShare();
			}
		}
	}
}
