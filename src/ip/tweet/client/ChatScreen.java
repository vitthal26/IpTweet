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

import static ip.tweet.constant.ChatConstant.NEW_LINE;

import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import ip.tweet.component.ChatMenuBar;
import ip.tweet.component.FileTransferDetailsPanel;
import ip.tweet.component.Participants;
import ip.tweet.component.ScreenShareComponent;
import ip.tweet.constant.ChatConstant;
import ip.tweet.data.ChatMessage;
import ip.tweet.data.MessageOperation;
import ip.tweet.data.User;
import ip.tweet.fileTransfer.FileSender;
import ip.tweet.sharing.ScreenSharingClient;
import ip.tweet.sharing.ScreenSharingServer;
import ip.tweet.util.ChatIdUtil;
import ip.tweet.util.ImageUtil;
import ip.tweet.util.LogWriter;
import ip.tweet.util.SharedData;

/**
 * @author Vitthal Kavitake
 */
public class ChatScreen extends JFrame {
	private static final long serialVersionUID = -2995160526365349052L;
	protected static Map<String, ChatScreen> activeChats = new HashMap<String, ChatScreen>();
	private static SimpleDateFormat SDF = new SimpleDateFormat("h:mm a");
	private StyleContext sc = new StyleContext();
	final private DefaultStyledDocument document = new DefaultStyledDocument(sc);
	final JTextArea tempMessage = new JTextArea();
	private JTextPane chatArea;
	private boolean groupChat = false;
	private List<User> users;
	private String chatId;
	private boolean shiftKeyPressed = false;
	private User lastMsgedUser = null;
	private Participants participants;
	private ScreenShareComponent screenShareComponent;
	private ScreenSharingServer sharingServer;
	private ScreenSharingClient sharingClient;
	private boolean isScreenShareServer = false;
	private boolean isScreenShareClient = false;
	private boolean timeOut = false;

	Timer timer = new Timer();
	private LogWriter logWriter;

	private ChatScreen() {
	}

	public List<User> getChatUsers() {
		return users;
	}

	public String getChatId() {
		return chatId;
	}

	public static Map<String, ChatScreen> getActiveChats() {
		return activeChats;
	}

	public static ChatScreen initialize(final List<User> userList,
			final String chatId, boolean isInitiator) {
		ChatScreen screen;
		if (activeChats.containsKey(chatId)) {
			return activeChats.get(chatId);
		} else {
			screen = new ChatScreen();
			activeChats.put(chatId, screen);
		}

		screen.groupChat = true;
		if (screen.groupChat && isInitiator) {
			for (User user : userList) {
				try {
					if (!user.equals(SharedData.getInstance().getLoggedInUser()))
						user.getChatService().initiateGroupChat(userList,
								chatId);
				} catch (Exception e) {

				}
			}
		}

		screen.chatId = chatId;
		screen.setTitle("Group Conversation");
		screen.setJMenuBar(new ChatMenuBar(ChatMenuBar.GROUP_MENU, isInitiator,
				screen));
		screen.showUI();
		screen.addUsers(userList, isInitiator, false);
		return screen;
	}

	private User singleUser;

	public static ChatScreen initialize(User user, boolean initiate) {
		ChatScreen screen;
		if (activeChats.containsKey(ChatIdUtil.getUserChatId(user))) {
			return activeChats.get(ChatIdUtil.getUserChatId(user));
		} else {
			screen = new ChatScreen();
			activeChats.put(ChatIdUtil.getUserChatId(user), screen);
		}
		screen.groupChat = false;
		screen.singleUser = user;
		screen.chatId = ChatIdUtil.getUserChatId(user);
		screen.setTitle(/* "Chat with "+ */user.getDisplayName());
		screen.setJMenuBar(new ChatMenuBar(ChatMenuBar.USER_MENU, false, screen));
		screen.showUI();
		screen.showUserList();
		return screen;
	}

	private void showUI() {
		logWriter = new LogWriter(chatId);
		logWriter.chatInitiated();
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		chatArea = new JTextPane(document);
		if (!groupChat && ChatConstant.FILE_TRANSFER_ENABLED) {
			DropTarget dropTarget1 = new FileSendTarget();
			DropTarget dropTarget2 = new FileSendTarget();
			tempMessage.setDropTarget(dropTarget1);
			chatArea.setDropTarget(dropTarget2);
		}
		JScrollPane chatScrollPane = new JScrollPane(chatArea);
		chatScrollPane.setPreferredSize(new Dimension(ChatConstant.APP_WIDTH,
				440));

		chatArea.setEditable(false);
		((DefaultCaret) chatArea.getCaret())
				.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		tempMessage.setLineWrap(true);
		tempMessage.addKeyListener(new ChatKeyListener());

		addWindowListener(new ChatWindowListener());
		JScrollPane tempScrollPane = new JScrollPane(tempMessage);
		tempScrollPane.setPreferredSize(new Dimension(ChatConstant.APP_WIDTH,
				60));

		participants = new Participants();

		panel.add(participants);
		panel.setAlignmentX(CENTER_ALIGNMENT);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setAlignmentX(CENTER_ALIGNMENT);

		panel.add(splitPane);
		splitPane.setLeftComponent(null);
		splitPane.setTopComponent(chatScrollPane);
		splitPane.setBottomComponent(tempScrollPane);
		/*
		 * mainPanel.add(panel); add(mainPanel);
		 */
		add(panel);
		setSize(ChatConstant.APP_WIDTH, ChatConstant.APP_HEIGHT);
		setIconImage(ImageUtil.getChatIconImage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setVisible(true);
	}

	public void addUsers(List<User> userList, boolean isChatInitiator,
			boolean isChatInitiated) {
		if (!isChatInitiated) {
			users = userList;
		} else {
			if (isChatInitiator) {
				// send update to all the member about new joining member
				for (User user : users) {
					if (!user.equals(SharedData.getInstance().getLoggedInUser())) {
						try {
							user.getChatService().addMembersToGroupChat(
									userList, chatId);
						} catch (Exception e) {
						}
					}
				}
			}
			users.addAll(userList);
		}

		if (isChatInitiator) {
			// send chat initiation request to newly added member(s)
			for (User user : userList) {
				try {
					if (!user.equals(SharedData.getInstance().getLoggedInUser())) {
						try {
							user.getChatService().initiateGroupChat(users,
									chatId);
						} catch (Exception e) {
						}
					}
				} catch (Exception e) {

				}
			}
		}
		// update the message for each of the member
		for (User user : userList) {
			groupJoinLeftMessage(user, true);
		}
		showUserList();
	}

	public void openChatLog() {
		logWriter.openLogFile();
	}

	private void showUserList() {
		User[] listData = null;
		if (groupChat) {
			listData = new User[users.size()];
			for (int i = 0; i < users.size(); i++)
				listData[i] = users.get(i);
		} else {
			listData = new User[2];
			listData[0] = SharedData.getInstance().getLoggedInUser();
			listData[1] = singleUser;
		}
		participants.setUsers(listData);
	}

	public void leftChatGroup(User user) {
		users.remove(user);
		groupJoinLeftMessage(user, false);
		User[] listData = new User[users.size()];
		// listData[0]=Main.getSelf();
		for (int i = 0; i < users.size(); i++)
			listData[i] = users.get(i);
		participants.setUsers(listData);
	}

	public void leftChat() {
		// cellRenderer.setChatActive(false);
	}

	public final void removeChat() {
		if (groupChat) {
			for (User user : users) {
				if (!user.equals(SharedData.getInstance().getLoggedInUser())) {
					try {
						user.getChatService().leftGroupChat(
								SharedData.getInstance().getLoggedInUser(), chatId);
					} catch (Exception e) {
					}
				}
			}
		} else {
			// singleUser.getChatService().leftChat(chatId);
		}
		stopScreenShare();
		activeChats.remove(chatId);
		lastMsgedUser = null;
		logWriter.chatClosed();
	}

	public FileTransferDetailsPanel addFileTransferProgressBar(long fileLength,
			String fileName, String transferType) {
		logWriter.log("[File Transfer] : " + transferType + " " + fileName
				+ NEW_LINE);
		FileTransferDetailsPanel progressBar = new FileTransferDetailsPanel(
				fileLength, fileName, transferType, this);
		// chatArea.setCaretPosition(chatArea.getText().length()-1);
		chatArea.setCaretPosition(chatArea.getDocument().getLength());
		chatArea.insertComponent(progressBar);
		int length = document.getLength();
		try {
			document.insertString(length, NEW_LINE, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return progressBar;
	}

	public void receivedMessage(ChatMessage message) {
		if (!isActive())
			toFront();
		insertHeader(message.getSender(), MessageOperation.RECEIVING_MESSAGE);
		int length = document.getLength();
		StringBuffer strBfr = new StringBuffer();
		strBfr.append(message.getText());

		final Style cwStyle = sc.addStyle("ConstantWidth", null);
		StyleConstants.setForeground(cwStyle, ChatConstant.RECEIVED_MSG_COLOR);
		try {
			logWriter.log(strBfr.toString());
			document.insertString(length, strBfr.toString(), cwStyle);
			scrollToLast();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void groupJoinLeftMessage(User user, boolean isJoined) {
		int length = document.getLength();
		StringBuffer strBfr = new StringBuffer();
		if (isJoined)
			strBfr.append(user.getDisplayName() + " joined conversation."
					+ NEW_LINE);
		else
			strBfr.append(user.getDisplayName() + " left conversation."
					+ NEW_LINE);
		final Style cwStyle = sc.addStyle("ConstantWidth", null);
		StyleConstants.setFontFamily(cwStyle, "Sylfaen");

		StyleConstants
				.setForeground(cwStyle, ChatConstant.GROUP_LEFT_MSG_COLOR);
		try {
			document.insertString(length, strBfr.toString(), cwStyle);
			scrollToLast();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private void scrollToLast() {
		if (chatArea != null) {
			chatArea.setCaretPosition(document.getLength());
		}
	}

	public String sendMessage(ChatMessage message) {
		insertHeader(SharedData.getInstance().getLoggedInUser(), MessageOperation.SENDING_MESSAGE);

		String approval = "Reject";
		int length = document.getLength();
		StringBuffer strBfr = new StringBuffer();
		strBfr.append(message.getText());
		logWriter.log(strBfr.toString());
		boolean messageSent = false;
		if (groupChat) {
			for (User u : users) {
				if (!u.equals(SharedData.getInstance().getLoggedInUser())) {
					try {
						approval = u.getChatService().message(message);
					} catch (Exception e) {
					}
				}
			}
		} else {
			try {
				approval = singleUser.getChatService().message(message);
				messageSent = true;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this,
						"Message could not be delivered. " + NEW_LINE
								+ "User may have been logged off.");
			}
		}

		final Style cwStyle = sc.addStyle("ConstantWidth", null);
		if (!groupChat && !messageSent)
			StyleConstants.setForeground(cwStyle,
					ChatConstant.SENT_MSG_FAILED_COLOR);
		else
			StyleConstants.setForeground(cwStyle, ChatConstant.SENT_MSG_COLOR);
		try {
			document.insertString(length, strBfr.toString(), cwStyle);
			scrollToLast();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return approval;
	}

	private MessageOperation lastOperation = MessageOperation.NO_OPERATION;

	public void insertHeader(User user, MessageOperation msgOperation) {
		timer.cancel();
		timeOut = false;
		timer = new Timer();
		timer.schedule(new ResetMessage(), 60 * 1000);

		if (!user.equals(lastMsgedUser) || timeOut
				|| !msgOperation.equals(lastOperation)) {
			final Style cwStyle = sc.addStyle("ConstantWidth", null);
			StyleConstants.setBold(cwStyle, true);
			StringBuffer strBfr = new StringBuffer();
			if (MessageOperation.SENDING_MESSAGE.equals(msgOperation)
					&& user.equals(SharedData.getInstance().getLoggedInUser())) {
				strBfr.append("Me: ");
				StyleConstants.setForeground(cwStyle,
						ChatConstant.SENT_MSG_COLOR);
			} else {
				strBfr.append(user.getDisplayName() + ": ");
				StyleConstants.setForeground(cwStyle,
						ChatConstant.RECEIVED_MSG_COLOR);
			}
			try {
				if (lastMsgedUser != null) {
					chatArea.setCaretPosition(document.getLength());
					chatArea.insertComponent(new JSeparator());
					document.insertString(document.getLength(), NEW_LINE,
							cwStyle);
				}
				logWriter.log(strBfr.toString());
				document.insertString(document.getLength(), strBfr.toString(),
						cwStyle);
				chatArea.setCaretPosition(document.getLength());
				chatArea.insertComponent(Box.createHorizontalGlue());
				StyleConstants.setBold(cwStyle, false);
				document.insertString(document.getLength(),
						SDF.format(new Date()) + NEW_LINE, cwStyle);
				scrollToLast();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		lastMsgedUser = user;
		lastOperation = msgOperation;
	}

	class ResetMessage extends TimerTask {
		@Override
		public void run() {
			timeOut = true;
		}
	}

	class ChatKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.VK_SHIFT) {
				shiftKeyPressed = true;
			}
			if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
				if (groupChat) {
					int selected = JOptionPane.showConfirmDialog(
							ChatScreen.this, "Leave group chat?");
					if (selected == JOptionPane.YES_OPTION) {
						removeChat();
						ChatScreen.this.dispose();
					}
				} else {
					removeChat();
					ChatScreen.this.dispose();
				}
			}
		}

		public void keyReleased(KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.VK_SHIFT) {
				shiftKeyPressed = false;
			}
		}

		public void keyTyped(KeyEvent event) {
			if (event.getKeyChar() == '\n') {
				if (shiftKeyPressed) {
					tempMessage.append(NEW_LINE);
				} else {
					if (tempMessage.getText().length() > 1) {
						ChatMessage msg = new ChatMessage();
						msg.setText(tempMessage.getText());
						msg.setSender(SharedData.getInstance().getLoggedInUser());
						msg.setChatId(chatId);
						sendMessage(msg);
						tempMessage.setText("");
					} else {
						tempMessage.setText("");
					}
				}
			}
		}
	}

	class ChatWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent arg0) {
			removeChat();
		}

		public void windowClosed(WindowEvent arg0) {
			// removeChat();
		}

		public void windowOpened(WindowEvent event) {
			tempMessage.requestFocusInWindow();
		}
	}

	class FileSendTarget extends DropTarget {
		private static final long serialVersionUID = 1L;

		public synchronized void drop(DropTargetDropEvent evt) {
			try {
				evt.acceptDrop(DnDConstants.ACTION_COPY);
				@SuppressWarnings("unchecked")
				List<File> droppedFiles = (List<File>) evt.getTransferable()
						.getTransferData(DataFlavor.javaFileListFlavor);
				if (droppedFiles.size() > 0) {
					FileSender sender = new FileSender(ChatScreen.this);
					sender.setSelectedFile(droppedFiles.get(0));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	// /////////// screen share related
	public void initiateScreenShare() {
		if (isAlreadySharing()) {
			JOptionPane.showMessageDialog(this,
					"You already have another screen sharing in progress..");
		} else {
			int option = JOptionPane.showConfirmDialog(ChatScreen.this,
					 "Do you want to share screen with "+singleUser.getDisplayName() + "?");
			if (option == JOptionPane.YES_OPTION) {
				isScreenShareServer = true;
				SharedData.getInstance().setScreenSharing(true);
				sharingServer = new ScreenSharingServer();
				sharingServer.start();
				singleUser.getChatService().screenShareInitiated(
					SharedData.getInstance().getLoggedInUser());
			}
		}
	}

	private boolean isAlreadySharing() {
		return SharedData.getInstance().isScreenSharing() || isScreenShareClient || isScreenShareServer;
	}

	

	public void screenShareInitiated() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				int option = JOptionPane.showConfirmDialog(ChatScreen.this,
						singleUser.getDisplayName() + " wants to share screen."
								+ ChatConstant.NEW_LINE + "Accept?");
				if (option == JOptionPane.YES_OPTION) {
					isScreenShareClient = true;
					SharedData.getInstance().setScreenSharing(true);
					screenShareComponent = new ScreenShareComponent(singleUser);
					sharingClient = new ScreenSharingClient(screenShareComponent,
							singleUser.getHostName());
					sharingClient.start();
					screenShareComponent.addWindowListener(new WindowAdapter() {
						public void windowClosing(WindowEvent arg0) {
							System.out.println("Sharing window closing");
							stopScreenShare();
						}
					});
				}
				else{
					screenShareClosed();
				}
			}
		});
	}

	public void screenShare(byte[] bytes) {
		screenShareComponent.setImageBytes(bytes);
	}

	public void screenShareClosed() {
		System.out.println("Stopping screen share");
		if (isScreenShareClient) {
			System.out.println("Stopping client...");
			screenShareComponent.setVisible(false);
			sharingClient.interrupt();
			isScreenShareClient = false;
			SharedData.getInstance().setScreenSharing(false);
			screenShareComponent.dispose();
		}
		if (isScreenShareServer) {
			System.out.println("Stopping server....");
			isScreenShareServer = false;
			SharedData.getInstance().setScreenSharing(false);
			sharingServer.interrupt();
		}
	}

	private void stopScreenShare() {
		if (isScreenShareClient || isScreenShareServer) {
			screenShareClosed();
			singleUser.getChatService().screenShareClosed(SharedData.getInstance().getLoggedInUser());
		}
	}
}