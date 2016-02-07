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

package ip.tweet.data;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import ip.tweet.service.ChatService;
/**
 * 
 * @author Vitthal Kavitake
 *
 */
public class User implements Comparable<User>,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 921177585872789163L;
	private String userName;
	private String hostName;
	private String displayName;
	private String connectAddress;
	private String nickName;
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public void setConnectAddress(String connectAddress){
		this.connectAddress = connectAddress;
	}
	public String getConnectAddress(){
		return this.connectAddress;
	}
	public ChatService getChatService() {
		URL url=null;
		try {
			url = new URL(this.connectAddress+"?wsdl");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		QName qname = new QName("http://service.tweet.ip/", "ChatServiceImplService");
		Service service = Service.create(url, qname);
		ChatService chatService = service.getPort(ChatService.class);
		return chatService;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
		//this.displayName = userName;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
/*	public String getConnectAddress() {
		return connectAddress;
	}
	public void setConnectAddress(String connectAddress) {
		this.connectAddress = connectAddress;
	}*/
	
	@Override
	public int hashCode(){
		return (2*this.userName.hashCode())+this.hostName.hashCode();
	}
	
	@Override
	public boolean equals(Object user){
		if(user==null)
			return false;
		else if(!(user instanceof User))
			return false;
		else if(this==user)
			return true;
		else if(user.hashCode()!=this.hashCode())
			return false;
		else{
			if(((User)user).getUserName().equals(this.userName)
					&& ((User)user).getHostName().equals(this.hostName)){
				return true;
			}
			else{ 
				return false;
			}
		}
	}
	public String toString(){
		return displayName+" ("+hostName+")";
	}
	public int compareTo(User toComapre) {
		return displayName.compareTo(toComapre.getDisplayName());
	}
}
