/*
 * Naturverbunden
 * Copyright (C) 2021, 2022 Valoeghese
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package valoeghese.naturverbunden.terrain.tests;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import valoeghese.naturverbunden.terrain.core.TerrainGenerator;

public class JFrameViewer extends JPanel {
	private static final long serialVersionUID = 1L;

	public JFrameViewer() {
		final long seed = 1;
		this.generator = new TerrainGenerator(seed);

		// pregen
		long timeMillis = System.currentTimeMillis();
		this.generator.pregen();
		System.out.println("Pregenerated basic continent features in " + (System.currentTimeMillis() - timeMillis) + "ms.");

		this.image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);

		timeMillis = System.currentTimeMillis();
		for (int x = -250; x < 250; ++x) {
			int imgx = x + 250;

			for (int z = -250; z < 250; ++z) {
				int height = this.generator.height(x * ZOOM_OUT, z * ZOOM_OUT);
				this.image.setRGB(imgx, z + 250, (255 << 6) | (height << 2) | (height << 4) | height);
			}
		}
		// TODO separate
		System.out.println("Sampled terrain and created image in " + (System.currentTimeMillis() - timeMillis) + "ms.");
	}

	private final TerrainGenerator generator;
	private final BufferedImage image;

	@Override
	public void paint(Graphics g) {
		g.drawImage(this.image, 0, 0, this);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(500, 500);
		frame.add(new JFrameViewer());
		frame.setTitle("Naturverbunden Terrain Viewer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private static int ZOOM_OUT = 1;
}
