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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import poisondog.core.Mission;
import poisondog.string.GetPath;
import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.io.IOUtils;
import java.io.FileOutputStream;

/**
 * @author Adam Huang
 * @since 2018-06-01
 */
public class GnuplotScript implements Mission<String> {
	private Map<String, String> mContent;

	/**
	 * Constructor
	 */
	public GnuplotScript() {
		mContent = new HashMap<String, String>();
	}

	public static GnuplotScript time() {
		GnuplotScript script = new GnuplotScript();
		script.setTerminal("jpeg");
		script.setXData("time");
		script.setStyle("data lines");
		script.setTimeFormat("'%Y-%m-%d %H:%M:%S'");
		script.set("format", "x \"%m-%d\n%H:%M\"");
		script.setXLabel("Time");
		script.setYLabel("Y");
		script.set("key autotitle", "columnheader");
		script.set("autoscale", "y");
		script.setXRange("'2013-07-22 15:50'", "'2013-07-22 16:00'");
		script.setOutput("gnuplot.jpg");
		script.setDataFile("separator \",\"");
		return script;
	}

	@Override
	public String execute(String data) throws IOException {
		File temp = File.createTempFile("script", ".tmp");
		IOUtils.write(toString(), new FileOutputStream(temp), "utf8");
		String path = temp.getAbsolutePath();
		Runtime rt = Runtime.getRuntime();
		GetPath task = new GetPath();
		System.out.println("run gnuplot " + path);
		Process pr = rt.exec("gnuplot " + path);
		return null;
	}

	private String createUsing(String filename) {
		StringBuilder builder = new StringBuilder();
		builder.append("'");
		builder.append(filename);
		builder.append("' using ");
		builder.append("1:");
		builder.append(" with lines");
		return builder.toString();
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

	public void setStyle(String style) {
		mContent.put("style", style);
	}

	public void setTimeFormat(String format) {
		mContent.put("timefmt", format);
	}

	public void setXRange(String start, String end) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(start);
		builder.append(":");
		builder.append(end);
		builder.append("]");
		mContent.put("xrange", builder.toString());
	}

	public void setDataFile(String data) {
		mContent.put("datafile", data);
	}

	public void setOutput(String output) {
		mContent.put("output", output);
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

	public String getStyle() {
		return createSetString("style");
	}

	public String getTimeFormat() {
		return createSetString("timefmt");
	}

	public String getXRange() {
		return createSetString("xrange");
	}

	public String getDataFile() {
		return createSetString("datafile");
	}

	public String getOutput() {
		return createSetString("output");
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
