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

package ip.tweet.service;

import java.util.List;

import javax.jws.WebService;

import ip.tweet.client.ChatScreen;
import ip.tweet.client.IpTweet;
import ip.tweet.constant.ChatConstant;
import ip.tweet.data.ChatMessage;
import ip.tweet.data.User;
import ip.tweet.fileTransfer.FileReceiver;
import ip.tweet.util.ChatIdUtil;
import ip.tweet.util.SharedData;
/**
 * 
 * @author Vitthal Kavitake
 *
 */
@WebService(endpointInterface = "ip.tweet.service.ChatService")
public class ChatServiceImpl implements ChatService{
	private static int inProgressFileTransferCount = 0;
	public static void fileTransferComplete(){
		inProgressFileTransferCount--;
	}
	public static boolean isFileTransferInprogress(){
		return inProgressFileTransferCount==0?true:false;
	}

	public String message(ChatMessage msg){
		String approval = "Reject";
		if(msg.getChatId().startsWith(ChatConstant.GROUP_CHAT_PREFIX)){
			ChatScreen groupChat = ChatScreen.getActiveChats().get(msg.getChatId());
			if(groupChat!=null){
				groupChat.receivedMessage(msg);
			}
		}
		else{
			ChatScreen userChat = ChatScreen.getActiveChats().get(msg.getSender().getUserName());
			if(userChat==null){
				userChat = ChatScreen.initialize(msg.getSender(), false);
			}
			if(msg.isFileTransfer()){
				// this loop is called if the file transfer is required
				inProgressFileTransferCount++;
				// this loop is called the very first time the file transfer
				// request is received
				userChat.receivedMessage(msg);
				FileReceiver ft = new FileReceiver(userChat);
				approval = ft.getFile(msg);
			}
			else{
				// this is for normal texting
				userChat.receivedMessage(msg);
			}
		}
		// returns the approval of the file transfer, not used in case of
		// normal text messaging
		return approval;
	}

	public boolean isAlive(){
		return true;
	}

	public void showOnScreen(){
		IpTweet.showOnScreen();
	}
	public void initiateGroupChat(List<User> userList, String chatId) {
		 ChatScreen.initialize(userList, chatId,false);
	}
	public void addMembersToGroupChat(List<User> userList, String chatId) {
		ChatScreen.getActiveChats().get(chatId).addUsers(userList, false, true);
	}
	public void leftGroupChat(User user, String chatId) {
		ChatScreen.getActiveChats().get(chatId).leftChatGroup(user);
	}
	public void leftChat(String chatId) {
		ChatScreen.getActiveChats().get(chatId).leftChat();
	}
	public void sendUserInfo(User user) {
		SharedData.getInstance().addLoggedInUser(user);
		IpTweet.getInstance().refreshUserList();
	}
	@Override
	public void screenShareInitiated(User user) {
		getChatScreen(user).screenShareInitiated();
	}
	@Override
	public void screenShare(User user, byte[] bytes) {
		ChatScreen screen = getChatScreen(user);
		if(screen!=null){
			screen.screenShare(bytes);
		}
	}
	
	@Override
	public void screenShareClosed(User user) {
		getChatScreen(user).screenShareClosed();
	}
	
	private ChatScreen getChatScreen(User user){
		return ChatScreen.getActiveChats().get(ChatIdUtil.getUserChatId(user));
	}
}