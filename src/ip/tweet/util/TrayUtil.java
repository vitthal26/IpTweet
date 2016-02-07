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

package ip.tweet.util;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import ip.tweet.client.IpTweet;
import ip.tweet.constant.ChatConstant;

/**
 * 
 * @author Vitthal Kavitake
 *
 */
public class TrayUtil {
	static TrayIcon trayIcon = null;

	public static void addToTray(final JFrame frame) {
		try {
			if (SystemTray.isSupported()) {
				SystemTray tray = SystemTray.getSystemTray();
				ActionListener listener = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// System.out.println(e.getActionCommand());
						if (e.getActionCommand() == null
								|| e.getActionCommand().equals("Open"))
							frame.setVisible(true);
						else if (e.getActionCommand().equals("Exit")) {
							IpTweet.exit();
						}
					}
				};
				PopupMenu popup = new PopupMenu();
				// create menu item for the default action
				MenuItem item1 = new MenuItem("Open");
				item1.addActionListener(listener);
				MenuItem item2 = new MenuItem("Exit");
				item2.addActionListener(listener);
				popup.add(item1);
				popup.add(item2);

				trayIcon = new TrayIcon(ImageUtil.getChatIconImage(16,16), SharedData.getInstance().getLoggedInUser()
						.getDisplayName() + " on " + ChatConstant.APP_NAME,
						popup);
				trayIcon.addActionListener(listener);

				tray.add(trayIcon);
			}
		} catch (AWTException e) {
			System.err.println(e);
		}
	}

	public static void removeTrayIcon() {
		if (trayIcon != null)
			SystemTray.getSystemTray().remove(trayIcon);
	}
}
