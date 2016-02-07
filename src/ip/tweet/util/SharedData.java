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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ip.tweet.data.User;

/**
 * @author Vitthal Kavitake
 */
public class SharedData {
	private static SharedData $this;
	private boolean isScreenSharing = false;
	private User loggedInUser;
	private final List<User> loggedInUsers = Collections.synchronizedList(new ArrayList<User>());
	
	
	private SharedData() {

	}

	public void setScreenSharing(boolean isScreenSharing) {
		this.isScreenSharing = isScreenSharing;
	}

	public boolean isScreenSharing() {
		return isScreenSharing;
	}

	public static SharedData getInstance() {
		if ($this == null) {
			$this = new SharedData();
		}
		return $this;
	}

	public void setLoggedInUser(User user) {
		loggedInUser = user;
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}
	
	public void addLoggedInUser(User user){
		if(!loggedInUsers.contains(user)){
			loggedInUsers.add(user);
		}
	}
	
	public void removeAllLoggedInUsers(){
		loggedInUsers.removeAll(loggedInUsers);
	}
	
	public List<User> getLoggedInUsers(){
		return loggedInUsers;
	}
	
	public void removeLoggedOffUser(User user){
		if(user!=null){
			loggedInUsers.remove(user);
			Collections.sort(loggedInUsers);
		}
	}
}
