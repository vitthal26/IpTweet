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

package ip.tweet.constant;

import java.awt.Color;
import java.awt.Font;

/**
 * @author Vitthal Kavitake
 */
public class ChatConstant {
	public static final String APP_NAME = " Ip Tweet";
	public static final String COPYRIGHT_MSG = " Developed by  Vitthal Kavitake";
	public static final String APP_VERSION = "v1.0";
	
	public static final boolean FILE_TRANSFER_ENABLED = true;
	public static final int FILE_SEND_LENGTH=1000000;//10 lakh
	public static final int FILE_RECEIVE_LENGTH=200000;// 2 lakh
	
	public static final String NEW_LINE = System.getProperty("line.separator");
	
	//ports to be used
	public static final int PORT_BROADCAST = 11010;
	public static final int PORT_CHAT = 5555;
	public static final int PORT_FILE_TR_START = 12345;
	public static final int PORT_FILE_TR_END = 12400;
	public static final int PORT_SCREEN_SHARE = 12401;
	
	public static final String GROUP_CHAT_PREFIX = "GroupChat";
	public static final String FIELD_SEPARATOR = "&qc;";
	
	public static final int APP_WIDTH = 290;
	public static final int APP_HEIGHT = 600;
	public static final int SHARE_WIDTH = 700;
	
	public static final Font USER_LIST_FONT =  new Font("Arial", Font.PLAIN, 14);
	public static final Font WELCOME_LBL_FONT =  new Font("Arial", Font.BOLD, 14);
	
	public static final Color WELCOME_LBL_COLOR = new Color(10,174,245);
	public static final Color SENT_MSG_COLOR = new Color(50,50,50);
	public static final Color SENT_MSG_FAILED_COLOR = new Color(255,50,50);
	public static final Color RECEIVED_MSG_COLOR = new Color(70,70,255);
	public static final Color GROUP_LEFT_MSG_COLOR = Color.GRAY;
	
	public static final String NICK_NAME = "NICK_NAME";
}
