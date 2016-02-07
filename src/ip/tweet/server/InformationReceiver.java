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

package ip.tweet.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import ip.tweet.client.IpTweet;
import ip.tweet.constant.ChatConstant;
import ip.tweet.data.BroadcastStatus;
import ip.tweet.data.User;
import ip.tweet.service.ChatPublisher;
import ip.tweet.util.EncryptionUtil;
import ip.tweet.util.SharedData;

/**
 * @author Vitthal Kavitake
 */
public class InformationReceiver extends Thread{
	@SuppressWarnings("resource")
	public void run(){
		try {
			byte[] receiveData = new byte[256];
			DatagramSocket clientSocket ;
			DatagramPacket receivePacket =
				new DatagramPacket(receiveData,
						receiveData.length);
			clientSocket = new DatagramSocket(11010);
			while(true){
				try{
					//System.out.println("Entered while loop");
					clientSocket.receive(receivePacket);
				}
				catch(Exception e){
					e.printStackTrace();
					continue;
				}
				//System.out.println(receivePacket.getLength());
				String info = new String(EncryptionUtil.decrypt(receivePacket.getData())).substring(0,receivePacket.getLength());
				System.out.println(info);
				
				String[] splitInfo = info.split(ChatConstant.FIELD_SEPARATOR);
				User user =null ;
				if(splitInfo.length>=5){
					user = new User();
					user.setUserName(splitInfo[1]);
					user.setDisplayName(splitInfo[2]);
					user.setHostName(splitInfo[3]);
					user.setConnectAddress(splitInfo[4]);
				}
				if(BroadcastStatus.LOGIN.toString().equals(splitInfo[0])){
					if(user!=null){
						user.getChatService().sendUserInfo(SharedData.getInstance().getLoggedInUser());
						if(!user.equals(SharedData.getInstance().getLoggedInUser())){
							SharedData.getInstance().addLoggedInUser(user);
							IpTweet.getInstance().refreshUserList();
						}
					}
				}
				else if(BroadcastStatus.LOGOUT.toString().equals(splitInfo[0]) ){
					if(user!=null){
						SharedData.getInstance().removeLoggedOffUser(user);
						IpTweet.getInstance().refreshUserList();
					}
				}
				else if(BroadcastStatus.UDPATE.toString().equals(splitInfo[0]) ){
					if(user!=null){
						if(!user.equals(SharedData.getInstance().getLoggedInUser())){
							user.getChatService().sendUserInfo(SharedData.getInstance().getLoggedInUser());
						}
					}
				}
				
			}
		} catch (SocketException e) {
			User user = new User();
			user.setConnectAddress(ChatPublisher.getPublishUrl());
			user.getChatService().showOnScreen();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}