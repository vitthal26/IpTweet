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

import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import ip.tweet.constant.ChatConstant;
import ip.tweet.data.User;
import ip.tweet.util.ImageUtil;

/**
 * @author Vitthal Kavitake
 */
public class ScreenShareComponent extends JFrame{
	private static final long serialVersionUID = 1L;
	private ImagePanel imagePanel;
	public ScreenShareComponent(User user){
		imagePanel = new ImagePanel();
		JScrollPane scrollPane = new JScrollPane(imagePanel);
		setContentPane(scrollPane);
		setVisible(true);
		setTitle("Screen Share with "+user.getDisplayName());
		setSize(ChatConstant.SHARE_WIDTH, ChatConstant.APP_HEIGHT);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		Image icon = ImageUtil.getScreenShareImage();//new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE);
		setIconImage(icon);
	}
	
	public void setImageBytes(byte[] bytes){
		imagePanel.setImageBytes(bytes);
	}
	
}
