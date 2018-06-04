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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import poisondog.io.GetResourceUrl;

/**
 * @author Adam Huang
 * @since 2018-06-01
 */
public class GnuplotScriptTest {
	private GnuplotScript mScript;

	@Before
	public void setUp() throws Exception {
		mScript = new GnuplotScript();
	}

	@Test
	public void testExecute() throws Exception {
		mScript = GnuplotScript.time();
		GetResourceUrl task = new GetResourceUrl();
		String path = task.execute("data.txt");
		mScript.execute(path);
	}

	@Test
	public void testTitle() throws Exception {
		mScript.setTitle("MyPlot");
		Assert.assertEquals("set title 'MyPlot'\n", mScript.getTitle());
		Assert.assertEquals("reset\nset title 'MyPlot'\n", mScript.toString());
	}

	@Test
	public void testXLabel() throws Exception {
		mScript.setXLabel("x axis");
		Assert.assertEquals("set xlabel 'x axis'\n", mScript.getXLabel());
		Assert.assertEquals("reset\nset xlabel 'x axis'\n", mScript.toString());
	}

	@Test
	public void testXData() throws Exception {
		mScript.setXData("time");
		Assert.assertEquals("set xdata time\n", mScript.getXData());
		Assert.assertEquals("reset\nset xdata time\n", mScript.toString());
	}

	@Test
	public void testYLabel() throws Exception {
		mScript.setYLabel("y axis");
		Assert.assertEquals("set ylabel 'y axis'\n", mScript.getYLabel());
		Assert.assertEquals("reset\nset ylabel 'y axis'\n", mScript.toString());
	}

	@Test
	public void testSet() throws Exception {
		mScript.set("output", "'runtime.png'");
		Assert.assertEquals("reset\nset output 'runtime.png'\n", mScript.toString());
	}

	@Test
	public void testTerminal() throws Exception {
		mScript.setTerminal("png");
		Assert.assertEquals("set terminal png\n", mScript.getTerminal());
		Assert.assertEquals("reset\nset terminal png\n", mScript.toString());
	}

	@Test
	public void testStyle() throws Exception {
		mScript.setStyle("data lines");
		Assert.assertEquals("set style data lines\n", mScript.getStyle());
		Assert.assertEquals("reset\nset style data lines\n", mScript.toString());
	}

	@Test
	public void testTimeFormat() throws Exception {
		mScript.setTimeFormat("'%Y-%m-%d %H:%M:%S'");
		Assert.assertEquals("set timefmt '%Y-%m-%d %H:%M:%S'\n", mScript.getTimeFormat());
		Assert.assertEquals("reset\nset timefmt '%Y-%m-%d %H:%M:%S'\n", mScript.toString());
	}

	@Test
	public void testXRange() throws Exception {
		mScript.setXRange("'2013-07-22 15:50'", "'2013-07-22 16:00'");
		Assert.assertEquals("set xrange ['2013-07-22 15:50':'2013-07-22 16:00']\n", mScript.getXRange());
		Assert.assertEquals("reset\nset xrange ['2013-07-22 15:50':'2013-07-22 16:00']\n", mScript.toString());
	}

	@Test
	public void testDataFile() throws Exception {
		mScript.setDataFile("separator \",\"");
		Assert.assertEquals("set datafile separator \",\"\n", mScript.getDataFile());
		Assert.assertEquals("reset\nset datafile separator \",\"\n", mScript.toString());
	}

	@Test
	public void testOutput() throws Exception {
		mScript.setOutput("'datausage.jpg'");
		Assert.assertEquals("set output 'datausage.jpg'\n", mScript.getOutput());
		Assert.assertEquals("reset\nset output 'datausage.jpg'\n", mScript.toString());
	}

}
