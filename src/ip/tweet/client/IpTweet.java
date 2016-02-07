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


package ip.tweet.client;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import ip.tweet.component.MainMenuBar;
import ip.tweet.component.SplashScreen;
import ip.tweet.component.RefreshPopup;
import ip.tweet.constant.ChatConstant;
import ip.tweet.data.BroadcastStatus;
import ip.tweet.data.User;
import ip.tweet.renderer.UserListCellRenderer;
import ip.tweet.server.InformationBroadcaster;
import ip.tweet.server.InformationReceiver;
import ip.tweet.service.ChatPublisher;
import ip.tweet.util.ChatIdUtil;
import ip.tweet.util.CommonUtil;
import ip.tweet.util.ImageUtil;
import ip.tweet.util.SharedData;
import ip.tweet.util.TrayUtil;

/**
 * 
 * @author Vitthal Kavitake
 *
 */
public class IpTweet extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7979445332604816492L;
	final static JList<User> userList = new JList<User>();
	static IpTweet thisFrame;
	
	public void refreshUserList(){
		List<User> loggedInUsers = SharedData.getInstance().getLoggedInUsers();
		User[] data = new User[loggedInUsers.size()];
		loggedInUsers.toArray(data);
		userList.setListData(data);
	}

	public static IpTweet getInstance(){
		return thisFrame;
	}

	public static void showOnScreen(){
		thisFrame.setVisible(true);
		thisFrame.setState(JFrame.NORMAL);
		thisFrame.toFront();
	}

	private IpTweet(){
		thisFrame = this;
		
		CommonUtil.initialize();
		SharedData.getInstance().setLoggedInUser(login());

		new InformationReceiver().start();
		try{Thread.sleep(500);}catch(Exception e){}
		
		InformationBroadcaster ib = new InformationBroadcaster();
		ib.sendSelfInfo(BroadcastStatus.LOGIN);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(LEFT_ALIGNMENT);
		setContentPane(panel);

		JScrollPane pane = new JScrollPane(userList);
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				if(event.getClickCount()==2 && event.getButton()==MouseEvent.BUTTON1 && userList.getSelectedValue()!=null){
					/*if(userList.getSelectedValue().equals(self))
						return;
					else*/
					 ChatScreen.initialize(userList.getSelectedValue(), true);
				}
				if(event.getButton()==MouseEvent.BUTTON3){
					JPopupMenu popup = new RefreshPopup();
					popup.show(IpTweet.userList,event.getX(), event.getY());
				}
			}
		});
		refreshUserList();
		userList.setCellRenderer(new UserListCellRenderer(SharedData.getInstance().getLoggedInUser().getUserName()));
		JLabel lblWelcome = new JLabel();
		lblWelcome.setFont(ChatConstant.WELCOME_LBL_FONT);
		lblWelcome.setText("Welcome "+SharedData.getInstance().getLoggedInUser().getDisplayName());
		lblWelcome.setForeground(ChatConstant.WELCOME_LBL_COLOR);
		JPanel welcomePanel = new JPanel();
		welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.X_AXIS));
		welcomePanel.add(Box.createHorizontalGlue());
		welcomePanel.add(lblWelcome);
		welcomePanel.add(Box.createHorizontalGlue());
		panel.add(welcomePanel);
		panel.add(Box.createRigidArea(new Dimension(1,15)));
		
		JLabel lblUserList = new JLabel("Online Users:");
		JPanel userListLblPanel = new JPanel();
		userListLblPanel.setLayout(new BoxLayout(userListLblPanel, BoxLayout.X_AXIS));
		userListLblPanel.add(lblUserList);
		userListLblPanel.add(Box.createHorizontalGlue());
		panel.add(userListLblPanel);
		
		panel.add(pane);

		setTitle(ChatConstant.APP_NAME + " - "+SharedData.getInstance().getLoggedInUser().getDisplayName());
		setSize(ChatConstant.APP_WIDTH, ChatConstant.APP_HEIGHT);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		setJMenuBar(new MainMenuBar());
		
		setVisible(true);
		
		setIconImage(ImageUtil.getChatLogoImage());
		TrayUtil.addToTray(this);
	}
	

	public User login(){
		User user = new User();
		user.setUserName(ChatIdUtil.getLoginName());
		
		user.setDisplayName(ChatIdUtil.getDisplayName());
		user.setHostName(ChatIdUtil.getHostName());
		
		ChatPublisher.publish();
		user.setConnectAddress(ChatPublisher.getPublishUrl());
		return user;
	}

	public static void main(String args[]){
		SplashScreen splash = new SplashScreen();
		splash.showSplash();
		createAndShowGUI();
		splash.hideSplash();
	}
	
	public static void createAndShowGUI(){
		final SplashScreen splash = new SplashScreen();
		splash.showSplash();
		try{Thread.sleep(2000);}catch(Exception e){}//banner display timeout
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new IpTweet();
				splash.hideSplash();
			}
		});
	}

	public static void exit(){
		// log off the user
		InformationBroadcaster ib = new InformationBroadcaster();
		ib.sendSelfInfo(BroadcastStatus.LOGOUT);
		
		//remove from tray icon
		TrayUtil.removeTrayIcon();
		//send group conversation left message
		if(ChatScreen.getActiveChats().size()>0){
			Iterator<String> itrChatId = ChatScreen.getActiveChats().keySet().iterator();
			while(itrChatId.hasNext()){
				String chatId = itrChatId.next();
				ChatScreen.getActiveChats().get(chatId).removeChat();
			}
		}
		System.exit(0);
	}
}
