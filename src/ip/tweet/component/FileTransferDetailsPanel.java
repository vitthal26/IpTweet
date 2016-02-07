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

package ip.tweet.component;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import ip.tweet.client.ChatScreen;
import ip.tweet.data.FileTransferStatus;
import ip.tweet.fileTransfer.FileReceiver;
import ip.tweet.fileTransfer.FileSender;

/**
 * @author Vitthal Kavitake
 */
public class FileTransferDetailsPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FileTransferProgressBar bar;
	private LinkButton labelFolder;
	private LinkButton labelFile;
	private File selectedFile;
	private JPanel panel2;
	private FileSender sender;
	private FileReceiver receiver;
	private String transferType;
	private ChatScreen userChat;
	public void setSender(FileSender sender){
		this.sender = sender;
	}
	public void setReceiver(FileReceiver receiver){
		this.receiver = receiver;
	}
	
	
	public void setSelectedFile(File file){
		this.selectedFile = file;
		Border border = BorderFactory.createTitledBorder(
				BorderFactory.createRaisedBevelBorder(), file.getName());
		setBorder(border);
	}

	/*public void setFileName(String fileName){
		bar.setFileName(fileName);
	}*/
	public void setReadLength(long readLength){
		bar.setReadLength(readLength);
		/*if(readLength==bar.getMaximum()){
			
		}*/
	}
	public long getMax(){
		return bar.getMax();
	}
	
	public void setStatus(FileTransferStatus status){
		bar.setStatus(status);
		if(panel2!=null){
			if(transferType.equals("Sending")){
				remove(panel2);
				setLayout(new GridLayout(1,1,5,5));
			}
			else{
				remove(panel2);
				setLayout(new GridLayout(1,1,5,5));
				if(bar.getStatus().equals(FileTransferStatus.COMPLETE)){
					setLayout(new GridLayout(2,1,5,5));
					panel2 = new JPanel();
					panel2.setLayout(new GridLayout(1,2,5,5));
					labelFolder = new LinkButton("View Folder");
					labelFile = new LinkButton("Open File");
					panel2.add(labelFolder);
					panel2.add(labelFile);
					add(panel2);
				}
			}
		}
	}
	public FileTransferDetailsPanel(long max, String fileName,String transferType, ChatScreen userChat){
		this.transferType = transferType;
		this.userChat = userChat;
		Border border = BorderFactory.createTitledBorder(
				BorderFactory.createRaisedBevelBorder(), fileName);
		setBorder(border);
		setLayout(new GridLayout(2,1,5,5));
		bar = FileTransferProgressBar.newInstance(max, fileName);
		bar.setTransferType(transferType);
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(1,1,5,5));
		panel1.add(bar);
		add(panel1);

		panel2 = new JPanel();
		panel2.setLayout(new GridLayout(1,2,5,5));
		LinkButton labelCancel = new LinkButton("Cancel");
		panel2.add(labelCancel);
		add(panel2);
	}

	class LinkButton extends JButton{
		private static final long serialVersionUID = 1L;

		public LinkButton(String text){
			setText("<HTML><FONT color=\"#000099\"><U>"+text+"</U></FONT>"
					+ "</HTML>");
			setBorderPainted(false);
			setOpaque(false);
			setPreferredSize(new Dimension(125,20));
			addActionListener(new ActionListener() {
				Desktop desktop = Desktop.getDesktop();
				public void actionPerformed(ActionEvent e) {
					try {
						if(e.getSource()==labelFolder){
							desktop.open(selectedFile.getParentFile());
						}
						else if(e.getSource()==labelFile){
							Runtime.getRuntime().exec("RUNDLL32.EXE SHELL32.DLL,ShellExec_RunDLL "+selectedFile.getAbsolutePath());
						}
						else if (e.getActionCommand().equals("<HTML><FONT color=\"#000099\"><U>Accept</U></FONT></HTML>")){
							
						}
						else if(e.getActionCommand().equals("<HTML><FONT color=\"#000099\"><U>Cancel</U></FONT></HTML>")){
							if(sender!=null){
								sender.interrupt();
								// probably no need to send notification as when
								// server socket is closed, reciever will automatically go to 
								// exception
							}
							else if(receiver!=null){
								receiver.interrupt();
								// send notification to sender that 
								// I have cancelled the file receiving.
								// -----> no need as server gets into exception when the thread 
								// here is interrupted
							}
							 
						}
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(userChat, "The file cannot be opened. Please open from the folder.");
					}
				}
			});
		}
	}
}