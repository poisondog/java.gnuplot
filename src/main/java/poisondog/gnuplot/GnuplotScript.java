/*
 * Copyright (C) 2018 Adam Huang <poisondog@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package poisondog.gnuplot;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Adam Huang
 * @since 2018-06-01
 */
public class GnuplotScript {
	private Map<String, String> mContent;

	/**
	 * Constructor
	 */
	public GnuplotScript() {
		mContent = new HashMap<String, String>();
	}

	public void set(String key, String value) {
		mContent.put(key, value);
	}

	public void setTitle(String title) {
		mContent.put("title", "'" + title + "'");
	}

	public void setXLabel(String label) {
		mContent.put("xlabel", "'" + label + "'");
	}

	public void setXData(String data) {
		mContent.put("xdata", data);
	}

	public void setYLabel(String label) {
		mContent.put("ylabel", "'" + label + "'");
	}

	public void setTerminal(String terminal) {
		mContent.put("terminal", terminal);
	}

	public String getTitle() {
		return createSetString("title");
	}

	public String getXLabel() {
		return createSetString("xlabel");
	}

	public String getXData() {
		return createSetString("xdata");
	}

	public String getYLabel() {
		return createSetString("ylabel");
	}

	public String getTerminal() {
		return createSetString("terminal");
	}

	private String createSetString(String key) {
		String value = mContent.get(key);
		if (value == null || value.isEmpty())
			return "";
		StringBuilder builder = new StringBuilder();
		builder.append("set ");
		builder.append(key);
		builder.append(" ");
		builder.append(value);
		builder.append("\n");
		return builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("reset\n");
		for (String key : mContent.keySet()) {
			builder.append(createSetString(key));
		}
		return builder.toString();
	}

}
