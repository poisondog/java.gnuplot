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

}
