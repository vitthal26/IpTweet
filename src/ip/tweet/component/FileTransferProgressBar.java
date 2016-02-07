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
import java.awt.Graphics2D;
import java.text.DecimalFormat;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.LookAndFeel;
import javax.swing.Painter;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import ip.tweet.data.FileTransferStatus;

/**
 * @author Vitthal Kavitake
 */
public class FileTransferProgressBar extends JProgressBar{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long readLength;
	private FileTransferStatus status = FileTransferStatus.IN_PROGRESS;
	//private String fileName;
	private String fileSize;
	private long max;
	private String transferType;

	public void setTransferType(String transferType){
		this.transferType = transferType;
	}
	public long getMax(){
		return max;
	}
	
	public static FileTransferProgressBar newInstance(long max,String fileName){
		LookAndFeel pf = UIManager.getLookAndFeel();
		try{UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");}catch(Exception e){}
		FileTransferProgressBar bar = new FileTransferProgressBar(max, fileName);
		try{UIManager.setLookAndFeel(pf);}catch(Exception e){}
		return bar;
	}
	private FileTransferProgressBar(long max,String fileName){
		this.max = max;
		//	this.fileName = fileName;
		this.fileSize = getFormattedString(max);
		setValue(0);
		if(max>2099999999)
			setMaximum((int)(max/4));
		else
			setMaximum((int)(max));
		setStringPainted(true);
		setString("0 bytes/"+fileSize);
		setPreferredSize(new Dimension(250,20));
		UIDefaults defaults = new UIDefaults();
		defaults.put("ProgressBar[Enabled].foregroundPainter", new MyPainter(new Color(125,125,255)));
		defaults.put("ProgressBar[Enabled+Finished].foregroundPainter", new MyPainter(new Color(150,255,150)));
		defaults.put("ProgressBar[Disabled].foregroundPainter", new MyPainter(Color.RED));
		defaults.put("ProgressBar[Disabled].textForeground", Color.WHITE);
		
		
		putClientProperty("Nimbus.Overrides.InheritDefaults", Boolean.TRUE);
		putClientProperty("Nimbus.Overrides", defaults);
	}

	/*public void setFileName(String fileName){
		this.fileName = fileName;
	}*/

	private String getFormattedString(float value){
		float kbLimit = 1024f;
		float mbLimit = 1024f*1024f;
		float gbLimit = 1024f*1024f*1024f;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		if(value<kbLimit){
			return value + " bytes";
		}
		else if(value<mbLimit){
			return df.format((value/kbLimit))+" Kb";
		}
		else if(value<gbLimit){
			return df.format((value/mbLimit))+" MB";
		}
		else{
			return df.format((value/gbLimit))+" GB";
		}
	}

	public void setReadLength(long readLength){
		if(max>2099999999){
			setValue((int)(readLength/4));
		}
		else{
			setValue((int)readLength);
		}
		this.readLength = readLength;
		doValidation();
	}
	public void setStatus(FileTransferStatus status){
		this.status = status;
		doValidation();
	}

	public FileTransferStatus getStatus(){
		return this.status;
	}

	private void doValidation(){
		if(status.equals(FileTransferStatus.ABORTED)){
			setString("Failed");
			setEnabled(false);
			//setIndeterminate(true);
		}
		else if(status.equals(FileTransferStatus.COMPLETE)){
			setString("Completed: "+getFormattedString(readLength)+"/"+fileSize);
		}
		else{
			setString(transferType +": "+getFormattedString(readLength)+"/"+fileSize);
		}
	}
	class MyPainter implements Painter<JComponent> {

	    private final Color color;

	    public MyPainter(Color c) {
	        color = c;
	    }

	    //@Override
	    public void paint(Graphics2D g, JComponent object, int width, int height) {
	        g.setColor(color);
	        
	        g.fillRect(2, 2, width - 5, height - 5);
	        //g.fill3DRect(2, 2, width - 4, height - 4, false);
	        g.setColor(Color.BLUE);
	        g.drawRect(2, 2, width - 5, height - 5);
	        //g.draw3DRect(2, 2, width - 5, height - 5, false);
	    }
	}
}