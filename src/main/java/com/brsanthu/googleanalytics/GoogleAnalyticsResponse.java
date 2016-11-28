/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.brsanthu.googleanalytics;

import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Response for GA tracking request.
 *
 * @author Santhosh Kumar
 */
public class GoogleAnalyticsResponse {
	private int statusCode = 200;
	private List<NameValuePair> postedParms = null;

	public Map<String, String> getPostedParmsAsMap() {
		if (postedParms == null) {
			return null;
		}
		
		Map<String, String> paramsMap = new HashMap<String, String>();
		for (NameValuePair pair : postedParms) {
			paramsMap.put(pair.getName(), pair.getValue());
		}
		
		return paramsMap;
	}

	public List<NameValuePair> getPostedParms() {
		return postedParms;
	}

	public void setPostedParms(List<NameValuePair> postedParms) {
		this.postedParms = postedParms;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Response [statusCode=");
		builder.append(statusCode);
		builder.append("]");
		return builder.toString();
	}
}
