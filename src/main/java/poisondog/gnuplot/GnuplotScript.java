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

/**
 * @author Adam Huang
 * @since 2018-06-01
 */
public class GnuplotScript {
	private String mTitle;
	private String mXLabel;
	private String mYLabel;

	public void setTitle(String title) {
		mTitle = title;
	}

	public void setXLabel(String label) {
		mXLabel = label;
	}

	public void setYLabel(String label) {
		mYLabel = label;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("reset\n");
		if (mTitle != null && !mTitle.isEmpty()) {
			builder.append("set title '");
			builder.append(mTitle);
			builder.append("'");
		}
		if (mXLabel != null && !mXLabel.isEmpty()) {
			builder.append("set xlabel '");
			builder.append(mXLabel);
			builder.append("'");
		}
		if (mYLabel != null && !mYLabel.isEmpty()) {
			builder.append("set ylabel '");
			builder.append(mYLabel);
			builder.append("'");
		}
		return builder.toString();
	}

}
