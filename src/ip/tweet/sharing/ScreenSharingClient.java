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

package ip.tweet.sharing;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import ip.tweet.component.ScreenShareComponent;
import ip.tweet.constant.ChatConstant;

/**
 * @author Vitthal Kavitake
 */
public class ScreenSharingClient extends Thread {
	private SocketChannel sChannel;
	private BufferedInputStream bis;
	private DataInputStream dis;
	private ScreenShareComponent screenShareComponent;
	private String hostName;
	
	public ScreenSharingClient(ScreenShareComponent screenShareComp,
			String hostName) {
		this.screenShareComponent = screenShareComp;
		this.hostName = hostName;
	}

	@Override
	public void run() {
		try {
			sChannel = SocketChannel.open();
			sChannel.configureBlocking(true);
			System.out.println("Connecting to "+hostName);
			sChannel.connect(new InetSocketAddress(hostName,
					ChatConstant.PORT_SCREEN_SHARE));
			while (!isInterrupted()) {
				while (sChannel.isOpen()) {
					BufferedInputStream bis = new BufferedInputStream(sChannel
							.socket().getInputStream());
					DataInputStream dis = new DataInputStream(bis);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int size = dis.readInt();
					for (int j = 0; j < size; j++) {
						baos.write(bis.read());
					}
					byte[] bytes = baos.toByteArray();
					//byte[] bytes = CommonUtil.unpackRaw(packed);
					screenShareComponent.setImageBytes(bytes);
					
					
					baos.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				sChannel.close();
				bis.close();
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
