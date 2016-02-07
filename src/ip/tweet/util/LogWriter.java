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

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ip.tweet.constant.ChatConstant;

/**
 * @author Vitthal Kavitake
 */
public class LogWriter {
	private String BASE_PATH = System.getProperty("user.home");
	private String LOG_DIR = "IpTweetLogs";
	private String LOG_EXT = ".log";
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	private File logFile;
	
	public void log(String message){
		try {
			FileWriter fw = new FileWriter(logFile, true);
			fw.write(message);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void chatInitiated(){
		log("------------------ Chat started at : "+sdf.format(new Date())+" --------------------"+ChatConstant.NEW_LINE);
	}
	
	public void chatClosed(){
		log("--------------- Chat terminated at : "+sdf.format(new Date())+" --------------------"+ChatConstant.NEW_LINE);
	}
	
	public LogWriter(String chatId){
		File file = new File(BASE_PATH+File.separator+LOG_DIR);
		if(!file.exists()){
			file.mkdir();
		}
		this.logFile = new File(BASE_PATH+File.separator+LOG_DIR+File.separator+chatId+LOG_EXT);
	}
	
	public void openLogFile(){
		try {
			Desktop.getDesktop().open(logFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
