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

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

import javax.imageio.ImageIO;

import ip.tweet.constant.ChatConstant;
import ip.tweet.util.SharedData;

/**
 * @author Vitthal Kavitake
 */
public class ScreenSharingServer extends Thread {
	private Robot robot;
	private BufferedOutputStream bos;
	private DataOutputStream dos;
	private Socket socket;
	private ServerSocketChannel ssChannel;
	public ScreenSharingServer() {
		try {
			robot = new Robot();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			ssChannel = ServerSocketChannel.open();
			ssChannel.configureBlocking(true);
			ssChannel.bind(new InetSocketAddress(SharedData.getInstance().getLoggedInUser().getHostName(), ChatConstant.PORT_SCREEN_SHARE));
			socket = ssChannel.accept().socket();
			bos = new BufferedOutputStream(socket.getOutputStream());
			dos = new DataOutputStream(bos);
			while (!isInterrupted()) {
				Rectangle screenRect = new Rectangle(Toolkit
						.getDefaultToolkit().getScreenSize());
				BufferedImage capture = robot.createScreenCapture(screenRect);
				//capture = Scalr.resize(capture, Scalr.Method.QUALITY, 1000, Scalr.OP_ANTIALIAS);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(capture, "png", baos);
				baos.flush();
				//byte[] imageInByte = CommonUtil.packRaw(baos.toByteArray());
				baos.close();
				sendOverSocket(baos.toByteArray());
				//Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				ssChannel.close();
				socket.close();
				bos.close();
				dos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void sendOverSocket(byte[] bytes) throws IOException {
	    dos.writeInt(bytes.length);
	    bos.write(bytes);
	}
}
