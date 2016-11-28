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

/**
 * Interface which returns the GA request that needs to be sent to GA.
 * <p>
 * This interface helps creating the GA request in lazily inside the async thread
 * so the cost of constructing the Request is not part of user related thread or
 * cost is completely avoided if GA is disabled (via {@link GoogleAnalyticsConfig.setEnabled})
 *
 * @author Santhosh Kumar
 */
public interface RequestProvider {

	/**
	 * Constructs and returns the request, that should be sent to GA. If this method throws exception,
	 * nothing will be sent to GA.
	 *
	 * @return the request that must be sent to GA. Can return <code>null</code> and if so,
	 * 		nothing will be sent to GA.
	 */
	@SuppressWarnings("rawtypes")
	GoogleAnalyticsRequest getRequest();
}
