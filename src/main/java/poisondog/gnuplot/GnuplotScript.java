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
import java.util.List;
import java.util.ArrayList;

/**
 * @author Adam Huang
 * @since 2018-06-01
 */
public class GnuplotScript implements Mission<InputStream> {
	private Map<String, String> mContent1;
	private Map<String, String> mContent2;
	private List<String> mPlots;
	private String mFilename;

	/**
	 * Constructor
	 */
	public GnuplotScript() {
		mContent1 = new HashMap<String, String>();
		mContent2 = new HashMap<String, String>();
		mPlots = new ArrayList<String>();
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

		setFilename(tempData.getAbsolutePath());
		ReadLine reader = new ReadLine();
		String line = reader.execute(new FileInputStream(tempData));
		String[] column = line.split(",");
		for (int i = 1; i < column.length; i++) {
			addLine(i + 1);
		}

		File tempScript = File.createTempFile("script", ".txt");
		IOUtils.write(toString(), new FileOutputStream(tempScript), "utf8");
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec("gnuplot " + tempScript.getAbsolutePath());
		return pr;
	}

	public void addLine(int index) {
		StringBuilder builder = new StringBuilder();
		builder.append("using ");
		builder.append("1:");
		builder.append(index);
		builder.append(" with lines");
		addPlot(builder.toString());
	}

	public void addFilledcurve(int current, int another) {
		addFilledcurve(current, another, "", "");
	}

	public void addFilledcurve(int current, int another, String title) {
		addFilledcurve(current, another, "", title);
	}

	public void addFilledcurve(int current, int another, String condition, String title) {
		StringBuilder builder = new StringBuilder();
		builder.append("using ");
		builder.append("1:");
		builder.append(current);
		builder.append(":");
		builder.append(another);
		builder.append(" with filledcurves ");
		builder.append(condition);
		if (!title.isEmpty()) {
			builder.append(" title '");
			builder.append(title);
			builder.append("'");
		}
		addPlot(builder.toString());
	}

	/**
	 * @param line the line is command starts with "using ... ". ex: "using 1:2:3 with filledcurves above title 'foo'"
	 */
	public void addPlot(String line) {
		mPlots.add(line);
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

	public void setFilename(String filename) {
		mFilename = filename;
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
		if (!mPlots.isEmpty())
			builder.append("plot ");
		for (String line : mPlots) {
			builder.append("'");
			builder.append(mFilename);
			builder.append("' ");
			builder.append(line);
			builder.append(",");
		}
		return builder.toString();
	}

}
