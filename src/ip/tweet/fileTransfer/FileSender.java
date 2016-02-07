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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import ip.tweet.client.ChatScreen;
import ip.tweet.component.FileTransferDetailsPanel;
import ip.tweet.constant.ChatConstant;
import ip.tweet.data.ChatMessage;
import ip.tweet.data.FileTransferStatus;
import ip.tweet.data.MessageOperation;
import ip.tweet.util.ChatIdUtil;
import ip.tweet.util.SharedData;

/**
 * @author Vitthal Kavitake
 */
public class FileSender extends Thread{
	private File selectedFile;
	private ChatScreen chatScreen;
	
	public FileSender(ChatScreen chatWindow){
		this.chatScreen = chatWindow;
	}
	
	public void setSelectedFile(File file){
		selectedFile = file;
		start();
	}
	public void selectFile(){
		JFileChooser fileChooser = new JFileChooser(new File(System.getenv("USERPROFILE")));
		int selectedOption = fileChooser.showOpenDialog(chatScreen);
		if(selectedOption==JFileChooser.APPROVE_OPTION){
			//userChat.sendMessage("");
			selectedFile = fileChooser.getSelectedFile();
		}
		start();
	}

	public void run(){
		if(selectedFile!=null){
			long sendLength = ChatConstant.FILE_SEND_LENGTH;
			//char[] cbuf=new char[ChatConstant.FILE_SEND_LENGTH];
			byte[] bytes=new byte[ChatConstant.FILE_SEND_LENGTH];
			long readLength = 0;
			long fileLength = selectedFile.length();
			System.out.print(fileLength);
			chatScreen.insertHeader(SharedData.getInstance().getLoggedInUser(), MessageOperation.SENDING_MESSAGE);
			FileTransferDetailsPanel bar = chatScreen.addFileTransferProgressBar(fileLength, selectedFile.getName(),"Sending");
			bar.setSender(this);
			try {
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(selectedFile));

				//BufferedReader br = new BufferedReader(new FileReader(file));
				ServerSocketChannel ssChannel = ServerSocketChannel.open();
				ssChannel.configureBlocking(true);
				int port = -1;
				for(int i=ChatConstant.PORT_FILE_TR_START;i<ChatConstant.PORT_FILE_TR_END;i++){
					try{
						ssChannel.socket().bind(new InetSocketAddress(SharedData.getInstance().getLoggedInUser().getHostName(),i));
						port = i;
						break;
					}
					catch(IOException e){

					}
				}

				if(port==-1){
					bis.close();
					JOptionPane.showMessageDialog(chatScreen, "Error sending file.");
					return;
				}
				
				ChatMessage cMessage = new ChatMessage();
				cMessage.setFileTransfer(true);
				cMessage.setFileLength(fileLength);
				cMessage.setFileName(selectedFile.getName());
				cMessage.setSender(SharedData.getInstance().getLoggedInUser());
				cMessage.setFileTransferPort(port);
				cMessage.setChatId(ChatIdUtil.getUserChatId(SharedData.getInstance().getLoggedInUser()));
				cMessage.setText("");

				String approval = chatScreen.sendMessage(cMessage);
				if(approval.equals("Reject")){
					//userChat.repaint();
					bar.setReadLength(bar.getMax());
					bar.setStatus(FileTransferStatus.ABORTED);
				}
				else{
					SocketChannel sChannel = ssChannel.accept();
					while(true){
						sendLength = fileLength-(readLength+sendLength)>0?sendLength:fileLength-readLength;
						bis.read(bytes, 0, (int)sendLength);
						//System.out.println("Sending : "+sendLength);
						sChannel.socket().getOutputStream().write(bytes, 0, (int)sendLength);
						readLength = readLength + sendLength;

						bar.setReadLength(readLength);
						if(readLength==fileLength){
							sChannel.shutdownInput();
							sChannel.shutdownOutput();
							bis.close();
							bar.setStatus(FileTransferStatus.COMPLETE);
							chatScreen.repaint();
							ssChannel.close();
							break;
						}
					}
				}
			} catch (Exception e) {
				bar.setReadLength(bar.getMax());
				bar.setStatus(FileTransferStatus.ABORTED);
			}
		}
	}
}