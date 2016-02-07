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

package ip.tweet.fileTransfer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import ip.tweet.client.ChatScreen;
import ip.tweet.component.FileTransferDetailsPanel;
import ip.tweet.component.JFileChooserSave;
import ip.tweet.constant.ChatConstant;
import ip.tweet.data.ChatMessage;
import ip.tweet.data.FileTransferStatus;
import ip.tweet.service.ChatServiceImpl;

/**
 * @author Vitthal Kavitake
 */
public class FileReceiver extends Thread{
	private int port;
	private String host;
	private ChatScreen userChat;
	
	public FileReceiver(ChatScreen userChat){
		this.userChat = userChat;
	}
	
	public void run(){
		long readLength=0;
		try{
			//try {Thread.sleep(1000);} catch (InterruptedException e) {}
			SocketChannel sChannel = SocketChannel.open();
			sChannel.configureBlocking(true);
			if(sChannel.connect(new InetSocketAddress(host, port))) {
				byte[] bytes = new byte[ChatConstant.FILE_RECEIVE_LENGTH];
				while(sChannel.isOpen()){
					int count = sChannel.socket().getInputStream().read(bytes);
					if(count==-1){
						break;
					}
					readLength+=count;
					bos.write(bytes,0,count);
					bar.setReadLength(readLength);
				}
			}

		}catch(IOException e){

		}
		finally{
			try {
				bos.close();
			} catch (IOException e) {
			}
			bos = null;
			userChat.toFront();
			if(readLength==bar.getMax())
				bar.setStatus(FileTransferStatus.COMPLETE);
			else{
				bar.setReadLength(bar.getMax());
				bar.setStatus(FileTransferStatus.ABORTED);
			}
			ChatServiceImpl.fileTransferComplete();
			//interrupt();
		}
	}
	
	
	BufferedOutputStream bos = null;
	FileTransferDetailsPanel bar = null;
	public String getFile(ChatMessage message){
		String approval = "Accept";
		try {
			if(bos==null){
				bar = userChat.addFileTransferProgressBar(message.getFileLength(), message.getFileName(),"Receiving");
				bar.setReceiver(this);
				int option = JOptionPane.showConfirmDialog(userChat, message.getSender().getDisplayName()
						+ " wants to send you file "+message.getFileName()+ChatConstant.NEW_LINE+"Accept?");
				if(option==JOptionPane.YES_OPTION){
					JFileChooser fileChooser = new JFileChooserSave(new File(System.getenv("USERPROFILE")));
					fileChooser.setSelectedFile(new File(message.getFileName()));
					int selectedOption = fileChooser.showSaveDialog(userChat);

					if(selectedOption==JFileChooser.APPROVE_OPTION){
						File file = fileChooser.getSelectedFile();
						//bar.setFileName(file.getName());
						bar.setSelectedFile(file);
						bos = new BufferedOutputStream(new FileOutputStream(file));
						host = message.getSender().getHostName();
						port = message.getFileTransferPort();
						start();
					}
					else{
						bar.setReadLength(bar.getMax());
						bar.setStatus(FileTransferStatus.ABORTED);
						//UserChat.getInstance().repaint();
						return "Reject";
					}
				}
				else{
					bar.setReadLength(bar.getMax());
					bar.setStatus(FileTransferStatus.ABORTED);
					//UserChat.getInstance().repaint();
					return "Reject";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return approval;
	}
}