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

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import ip.tweet.constant.ChatConstant;

/**
 * @author Vitthal Kavitake
 */
public class JFileChooserSave extends JFileChooser{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JFileChooserSave(File file){
		super(file);
	}
	
	@Override public void approveSelection() {
		if (getDialogType() == SAVE_DIALOG) {
			File selectedFile = getSelectedFile();
			if ((selectedFile != null) && selectedFile.exists()) {
				int response = JOptionPane.showConfirmDialog(this,
						"The file " + selectedFile.getName() + 
						" already exists. "+ChatConstant.NEW_LINE+
						"Do you want to replace the existing file?",
						"Ovewrite file", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);
				if (response != JOptionPane.YES_OPTION)
					return;
			}
		}
		super.approveSelection();
	}
}
