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

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * @author Vitthal Kavitake
 */
public class ImageUtil {
	public static Image getChatIconImage(){
		Image image = new ImageIcon(Thread.currentThread().getContextClassLoader()
				.getResource("images/logo-24.png")).getImage();
		return image;
	}
	public static Image getChatIconImage(int width, int height){
		return getChatIconImage().getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
	}
	
	public static Image getChatLogoImage(){
		Image image = new ImageIcon(Thread.currentThread().getContextClassLoader()
				.getResource("images/logo-24.png")).getImage();
		return image;
	}
	
	public static Image getChatLogoImage(int width, int height){
		return getChatLogoImage().getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
	}
	
	public static Image getChatLogoImageBig(){
		Image image = new ImageIcon(Thread.currentThread().getContextClassLoader()
				.getResource("images/logo-150.png")).getImage();
		return image;
	}
	
	public static Image getChatLogoImageBig(int width, int height){
		return getChatLogoImage().getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
	}
	
	public static Image getScreenShareImage(){
		Image image = new ImageIcon(Thread.currentThread().getContextClassLoader()
				.getResource("images/share.png")).getImage().getScaledInstance(16, 16, Image.SCALE_AREA_AVERAGING);
		return image;
	}
	public static Image getScreenShareImage(int width, int height){
		return getScreenShareImage().getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
	}
}
