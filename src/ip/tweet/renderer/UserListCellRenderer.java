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

package ip.tweet.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;

import ip.tweet.constant.ChatConstant;
import ip.tweet.data.User;

/**
 * 
 * @author Vitthal Kavitake
 *
 */
public class UserListCellRenderer extends JLabel implements
		ListCellRenderer<User> {
	private static final long serialVersionUID = 2452433802186870598L;
	private String chatInitiatorUserName;

	public UserListCellRenderer(String userName) {
		super();
		setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		chatInitiatorUserName = userName;
		setFont(ChatConstant.USER_LIST_FONT);
	}

	public Component getListCellRendererComponent(JList<? extends User> list,
			User user, int index, boolean isSelected, boolean cellHasFocus) {
		if(index%2==0){
			setOpaque(true);
			setBackground(new Color(240,240,255));
		}
		else{
			setOpaque(false);
		}
		if (user != null) {
			setText(user.getDisplayName() + " (" + user.getHostName() + ")");
			if (chatInitiatorUserName != null) {
				if (isSelected) {
					setOpaque(true);
					setBackground(new Color(210,210,255));
					//setForeground(ChatConstant.WELCOME_LBL_COLOR);
					setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				} else {
					setBorder(BorderFactory.createEmptyBorder());
					setForeground(ChatConstant.SENT_MSG_COLOR);
				}
			}
		}
		return this;
	}
}
