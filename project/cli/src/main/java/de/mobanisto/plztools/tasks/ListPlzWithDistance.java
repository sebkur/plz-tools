// Copyright (c) 2021 Sebastian Kuerten
//
// This file is part of plz-tools.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

package de.mobanisto.plztools.tasks;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import de.mobanisto.plztools.data.PostalCodeData;
import de.mobanisto.plztools.data.io.PostalCodeLoader;
import de.topobyte.geomath.WGS84;

public class ListPlzWithDistance
{

	private String distanceCode;
	private String prefix;

	public ListPlzWithDistance(String distanceCode, String prefix)
	{
		this.distanceCode = distanceCode;
		this.prefix = prefix;
	}

	public void execute() throws IOException
	{
		Path repo = Paths.get(System.getProperty("repo"));
		Path file = repo.resolve("data/postal_codes");

		PostalCodeData data = PostalCodeLoader.load(file);

		Map<String, Geometry> codeToGeometry = data.getCodeToGeometry();
		Geometry distanceGeometry = codeToGeometry.get(distanceCode);

		if (distanceGeometry == null) {
			System.out.println(
					String.format("Postal code not found: '%s'", distanceCode));
			System.exit(1);
		}

		Point distanceCentroid = distanceGeometry.getCentroid();

		List<String> selected = new ArrayList<>();
		for (String code : codeToGeometry.keySet()) {
			if (prefix == null || code.startsWith(prefix)) {
				selected.add(code);
			}
		}

		Collections.sort(selected);

		for (String code : selected) {
			Geometry geometry = codeToGeometry.get(code);
			Point centroid = geometry.getCentroid();

			double meters = WGS84.haversineDistance(distanceCentroid.getX(),
					distanceCentroid.getY(), centroid.getX(), centroid.getY());
			double km = meters / 1000;

			System.out.println(String.format("%s: %.3f, %.3f %.2fkm", code,
					centroid.getY(), centroid.getX(), km));
		}
	}

}
