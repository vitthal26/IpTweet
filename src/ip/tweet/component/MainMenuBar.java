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
import java.util.prefs.Preferences;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import ip.tweet.client.IpTweet;
import ip.tweet.constant.ChatConstant;

/**
 * @author Vitthal Kavitake
 */
public class MainMenuBar extends JMenuBar{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MainMenuBar(){
		super();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		add(fileMenu);
		ActionListener al = new MenuActionListener();
		
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.setMnemonic('A');
		aboutItem.addActionListener(al);
		fileMenu.add(aboutItem);
		
		JMenuItem nickName = new JMenuItem("Set Your Nick-Name");
		nickName.setMnemonic('N');
		nickName.addActionListener(al);
		fileMenu.add(nickName);
		
		JSeparator separator = new JSeparator();
		fileMenu.add(separator);
		
		JMenuItem groupConv = new JMenuItem("Start Group Convresation");
		groupConv.setMnemonic('G');
		groupConv.addActionListener(al);
		fileMenu.add(groupConv);
		
		
		
		separator = new JSeparator();
		fileMenu.add(separator);
		
		JMenuItem exitItem = new JMenuItem("Logoff");
		exitItem.setMnemonic('E');
		exitItem.addActionListener(al);
		fileMenu.add(exitItem); 
		
	}
	class MenuActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Logoff"))
				IpTweet.exit();
			else if(e.getActionCommand().equals("About")){
				JOptionPane.showMessageDialog(IpTweet.getInstance(), ChatConstant.APP_NAME+" "+ChatConstant.APP_VERSION
						+ChatConstant.NEW_LINE+ChatConstant.COPYRIGHT_MSG);
			}
			else if(e.getActionCommand().equals("Start Group Convresation")){
				new GroupConversationMembers();
			}
			else if(e.getActionCommand().equals("Set Your Nick-Name")){
				String nickName = JOptionPane.showInputDialog(IpTweet.getInstance(), "Enter your nick name", "Your Name", JOptionPane.PLAIN_MESSAGE);
				if(nickName!=null && nickName.trim().length()!=0){
					Preferences p = Preferences.userRoot();
					p.put(ChatConstant.NICK_NAME, nickName);
					JOptionPane.showMessageDialog(IpTweet.getInstance(), "You are requested to logoff and login again \nfor changes to take effect.","Please Note",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}
}
