package poisondog.gnuplot;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import java.awt.Dimension;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import poisondog.gnuplot.GnuplotScript;
import poisondog.io.GetResourceUrl;
import java.io.File;
import java.io.FileInputStream;
import poisondog.vfs.IData;
import poisondog.vfs.FileFactory;

public class Demo extends ApplicationFrame {

	/**
	 * A demonstration application showing an XY series containing a null value.
	 *
	 * @param title  the frame title.
	 */
	public Demo(final String title) {

		super(title);
		final XYSeries series = new XYSeries("Random Data");
		series.add(1.0, 500.2);
		series.add(5.0, 694.1);
		series.add(4.0, 100.0);
		series.add(12.5, 734.4);
		series.add(17.3, 453.2);
		series.add(21.2, 500.2);
		series.add(21.9, null);
		series.add(25.6, 734.4);
		series.add(30.0, 453.2);
		final XYSeriesCollection data = new XYSeriesCollection(series);
		final JFreeChart chart = ChartFactory.createXYLineChart("XY Series Demo", "X", "Y", data,
				PlotOrientation.VERTICAL, true, true, false);

		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(chartPanel);

	}

	// ****************************************************************************
	// * JFREECHART DEVELOPER GUIDE                                               *
	// * The JFreeChart Developer Guide, written by David Gilbert, is available   *
	// * to purchase from Object Refinery Limited:                                *
	// *                                                                          *
	// * http://www.object-refinery.com/jfreechart/guide.html                     *
	// *                                                                          *
	// * Sales are used to provide funding for the JFreeChart project - please    * 
	// * support us so that we can continue developing free software.             *
	// ****************************************************************************

	/**
	 * Starting point for the demonstration application.
	 *
	 * @param args  ignored.
	 */
	public static void main(final String[] args) {

//		final Demo demo = new Demo("XY Series Demo");
//		demo.pack();
//		RefineryUtilities.centerFrameOnScreen(demo);
//		demo.setVisible(true);

		try {
			GetResourceUrl task = new GetResourceUrl();
			String path = task.execute("data.txt");
			System.out.println(path);
			InputStream is = ((IData)FileFactory.getFile(path)).getInputStream();
	//		String data = "Time,IN,out\n2013-07-22T15:59:00.231+08:00,6286,3730\n2013-07-22T15:58:00.987+08:00,10695,14589";
	//		InputStream is = new ByteArrayInputStream(data.getBytes());

			GnuplotScript script = GnuplotScript.time();
			script.setXRange("2013-07-22T15:50:00", "2013-07-22T16:00:00");
			script.setTerminal("jpeg");
			script.setOutput("gnuplot.jpg");
			script.addFilledcurve(2, 3);
			script.execute(is).waitFor();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

}

