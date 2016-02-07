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

import javax.jws.WebMethod;
import javax.jws.WebService;

import ip.tweet.data.ChatMessage;
import ip.tweet.data.User;
/**
 * 
 * @author Vitthal Kavitake
 *
 */
@WebService
public interface ChatService {
	@WebMethod
	public String message(ChatMessage msg);
	@WebMethod
	public boolean isAlive();
	@WebMethod
	public void showOnScreen();
	@WebMethod
	public void initiateGroupChat(List<User> userList, String chatId);
	@WebMethod
	public void addMembersToGroupChat(List<User> userList, String chatId);
	@WebMethod
	public void leftGroupChat(User user, String chatId);
	@WebMethod
	public void leftChat(String chatId);
	@WebMethod
	public void sendUserInfo(User user);
	@WebMethod
	
	public void screenShareInitiated(User user);
	@WebMethod
	public void screenShare(User user, byte[] bytes);
	/*@WebMethod
	public void screenShareIs(User user, InputStream is);*/
	@WebMethod
	public void screenShareClosed(User user); 
}
