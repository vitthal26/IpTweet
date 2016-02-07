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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import ip.tweet.constant.ChatConstant;
import ip.tweet.util.ImageUtil;

/**
 * @author Vitthal Kavitake
 */
public class SplashScreen extends JWindow {

	private static final long serialVersionUID = 1L;

	public SplashScreen() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		mainPanel.setBackground(new Color(140, 160, 180));
		mainPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		panel.setBorder(BorderFactory.createRaisedBevelBorder());
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		setContentPane(mainPanel);
		Image image = ImageUtil.getChatLogoImageBig();
		JLabel img = new JLabel(new ImageIcon(image, ChatConstant.APP_NAME));
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		textPanel.setOpaque(false);
		panel.add(Box.createHorizontalStrut(10));
		panel.add(img);
		JLabel text = new JLabel(ChatConstant.APP_NAME);
		text.setForeground(Color.CYAN);
		text.setFont(new Font("Times New Roman", Font.BOLD, 60));
		
		JLabel author = new JLabel(ChatConstant.COPYRIGHT_MSG);
		
		author.setForeground(Color.WHITE);
		author.setFont(new Font("Arial", Font.HANGING_BASELINE, 20));
		
		textPanel.add(text);
		textPanel.add(author);
		panel.add(textPanel);
		mainPanel.add(panel);
	}

	public void showSplash() {
		setVisible(true);
		//setAlwaysOnTop(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		pack();
		this.setLocation((screenSize.width - getWidth()) / 2,
				(screenSize.height - getHeight()) / 2);
	}

	public void hideSplash() {
		setVisible(false);
		dispose();
	}
}
