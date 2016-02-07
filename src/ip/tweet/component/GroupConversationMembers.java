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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import ip.tweet.client.ChatScreen;
import ip.tweet.constant.ChatConstant;
import ip.tweet.data.User;
import ip.tweet.util.SharedData;

/**
 * @author Vitthal Kavitake
 */
public class GroupConversationMembers extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean newChat = false;
	
	public GroupConversationMembers(){
		//String chatId = "GrpCnv"+(int)(Math.random()*10000000);
		newChat = true;
		SimpleDateFormat sdf = new SimpleDateFormat("YY_MM_dd_HH_mm_ss");
		showUI(null,ChatConstant.GROUP_CHAT_PREFIX+"_"+sdf.format(new Date()));
	}
	
	public GroupConversationMembers(List<User> alreadyPresent,String chatId){
		newChat = false;
		showUI(alreadyPresent,chatId);
	}
	
	private List<User> selectedUsers = new ArrayList<User>();
	public List<User> getSelectedList(){
		return selectedUsers;
	}
	
	private void showUI(List<User> alreadyPresent,final String chatId){
		final JList<User> userList = new JList<User>();
		JScrollPane pane = new JScrollPane(userList);
		pane.setPreferredSize(new Dimension(ChatConstant.APP_WIDTH,420));
		userList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		List<User> allUsers = SharedData.getInstance().getLoggedInUsers();
		allUsers.remove(SharedData.getInstance().getLoggedInUser());
		if(alreadyPresent!=null)
			allUsers.removeAll(alreadyPresent);
		User[] data = new User[allUsers.size()];
		data = allUsers.toArray(data);
		userList.setListData(data);
		//userList.setCellRenderer(new UserListCellRenderer(Main.getSelf().getUserName()));
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(new JPanel());
		add(pane);
		setTitle("Select Recepients");
		JButton button = new JButton();
		button.setMaximumSize(new Dimension(ChatConstant.APP_WIDTH,20));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedUsers = userList.getSelectedValuesList();
				if(selectedUsers==null || selectedUsers.size()==0){
					JOptionPane.showMessageDialog(GroupConversationMembers.this,"Please select at least one user");
				}
				else{
					if(newChat){
						selectedUsers.add(SharedData.getInstance().getLoggedInUser());
						 ChatScreen.initialize(selectedUsers,chatId,true);
						GroupConversationMembers.this.dispose();
					}
					else{
						// this will always be initiator so second parameter is passed as true
						ChatScreen.getActiveChats()
							.get(chatId).addUsers(selectedUsers,true,true);
						GroupConversationMembers.this.dispose();
					}
				}
			}
		});
		if(newChat)
			button.setText("Start Conversation");
		else
			button.setText("Add Recepient");
		add(button);
		setSize(300, 500);
	}
}
