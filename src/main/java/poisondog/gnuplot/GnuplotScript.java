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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import poisondog.core.Mission;
import poisondog.io.CopyTask;
import poisondog.io.ReadLine;

/**
 * @author Adam Huang
 * @since 2018-06-01
 */
public class GnuplotScript implements Mission<InputStream> {
	private Map<String, String> mContent1;
	private Map<String, String> mContent2;
	private String mPlot;

	/**
	 * Constructor
	 */
	public GnuplotScript() {
		mContent1 = new HashMap<String, String>();
		mContent2 = new HashMap<String, String>();
	}

	public static GnuplotScript time() {
		GnuplotScript script = new GnuplotScript();
		script.setXData("time");
		script.setStyle("data lines");
		script.setTimeFormat("%Y-%m-%dT%H:%M:%S");
		script.set("format", "x '%H:%M'");
		script.set("key autotitle", "columnheader");
		script.set("autoscale", "y");
		script.setDataFile("separator ','");
		return script;
	}

	@Override
	public Process execute(InputStream input) throws IOException {
		File tempData = File.createTempFile("data", ".txt");
		CopyTask copy = new CopyTask(input, new FileOutputStream(tempData));
		copy.transport();

		ReadLine reader = new ReadLine();
		String line = reader.execute(new FileInputStream(tempData));
		String[] column = line.split(",");
		StringBuilder builder = new StringBuilder();
		for (int i = 1; i < column.length; i++) {
			builder.append(createUsing(tempData.getAbsolutePath(), i + 1));
			if (i != column.length - 1)
				builder.append(", ");
		}
		setPlot(builder.toString());

		File tempScript = File.createTempFile("script", ".txt");
		IOUtils.write(toString(), new FileOutputStream(tempScript), "utf8");
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec("gnuplot " + tempScript.getAbsolutePath());
		return pr;
	}

	public void setPlot(String using) {
		mPlot = using;
	}

	private String createUsing(String filename, int index) {
		StringBuilder builder = new StringBuilder();
		builder.append("'");
		builder.append(filename);
		builder.append("' using ");
		builder.append("1:");
		builder.append(index);
		builder.append(" with lines");
		return builder.toString();
	}

	public void set(String key, String value) {
		mContent1.put(key, value);
	}

	public void setTitle(String title) {
		mContent1.put("title", "'" + title + "'");
	}

	public void setXLabel(String label) {
		mContent1.put("xlabel", "'" + label + "'");
	}

	public void setXData(String data) {
		mContent1.put("xdata", data);
	}

	public void setYLabel(String label) {
		mContent1.put("ylabel", "'" + label + "'");
	}

	public void setTerminal(String terminal) {
		mContent1.put("terminal", terminal);
	}

	public void setStyle(String style) {
		mContent1.put("style", style);
	}

	public void setTimeFormat(String format) {
		mContent1.put("timefmt", "'" + format + "'");
	}

	public void setXRange(String start, String end) {
		StringBuilder builder = new StringBuilder();
		builder.append("['");
		builder.append(start);
		builder.append("':'");
		builder.append(end);
		builder.append("']");
		mContent2.put("xrange", builder.toString());
	}

	public void setDataFile(String data) {
		mContent1.put("datafile", data);
	}

	public void setOutput(String output) {
		mContent1.put("output", "'" + output + "'");
	}

	public String getTitle() {
		return createSetString(mContent1, "title");
	}

	public String getXLabel() {
		return createSetString(mContent1, "xlabel");
	}

	public String getXData() {
		return createSetString(mContent1, "xdata");
	}

	public String getYLabel() {
		return createSetString(mContent1, "ylabel");
	}

	public String getTerminal() {
		return createSetString(mContent1, "terminal");
	}

	public String getStyle() {
		return createSetString(mContent1, "style");
	}

	public String getTimeFormat() {
		return createSetString(mContent1, "timefmt");
	}

	public String getXRange() {
		return createSetString(mContent2, "xrange");
	}

	public String getDataFile() {
		return createSetString(mContent1, "datafile");
	}

	public String getOutput() {
		return createSetString(mContent1, "output");
	}

	private String createSetString(Map<String, String> content, String key) {
		String value = content.get(key);
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
		for (String key : mContent1.keySet()) {
			builder.append(createSetString(mContent1, key));
		}
		for (String key : mContent2.keySet()) {
			builder.append(createSetString(mContent2, key));
		}
		if (mPlot != null) {
			builder.append("plot ");
			builder.append(mPlot);
		}
		return builder.toString();
	}

}
