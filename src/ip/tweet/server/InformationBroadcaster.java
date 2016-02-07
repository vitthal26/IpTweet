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
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import ip.tweet.constant.ChatConstant;
import ip.tweet.data.BroadcastStatus;
import ip.tweet.data.User;
import ip.tweet.util.EncryptionUtil;
import ip.tweet.util.SharedData;

/**
 * @author Vitthal Kavitake
 */
public class InformationBroadcaster {

	final int PORT = 11010;

	public void sendSelfInfo(BroadcastStatus status) {
		User user = SharedData.getInstance().getLoggedInUser();
		StringBuilder builder = new StringBuilder();
		// 0
		builder.append(status);
		builder.append(ChatConstant.FIELD_SEPARATOR);

		// 1
		builder.append(user.getUserName());
		builder.append(ChatConstant.FIELD_SEPARATOR);

		// 2
		builder.append(user.getDisplayName());
		builder.append(ChatConstant.FIELD_SEPARATOR);

		// 3
		builder.append(user.getHostName());
		builder.append(ChatConstant.FIELD_SEPARATOR);

		// 4
		builder.append(user.getConnectAddress());
		builder.append(ChatConstant.FIELD_SEPARATOR);
		sendPacket(builder.toString().getBytes());
	}

	private void sendPacket(byte[] bytes) {
		try {
			DatagramSocket ss = new DatagramSocket();
			ss.setBroadcast(true);

			DatagramPacket p = new DatagramPacket(bytes, bytes.length);
			// broadcast to specific subnet
			// 10.224.18.*
			// if needs to broadcast to all then change to
			// 255.255.255.255
			NetworkInterface networkInterface = NetworkInterface
					.getByInetAddress(Inet4Address.getLocalHost());
			InetAddress broadcast = null;
			for (InterfaceAddress address : networkInterface
					.getInterfaceAddresses()) {
				if (address.getAddress().getHostAddress().equals(Inet4Address.getLocalHost().getHostAddress())) {
					broadcast = address.getBroadcast();
					break;
				}
			}
			p.setAddress(broadcast);
			p.setPort(PORT);

			p.setData(EncryptionUtil.encrypt(bytes));
			ss.send(p);
			ss.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
