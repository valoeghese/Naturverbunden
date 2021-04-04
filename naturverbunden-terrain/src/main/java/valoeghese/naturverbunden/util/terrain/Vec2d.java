/*
 * Naturverbunden
 * Copyright (C) 2021 Valoeghese
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

package valoeghese.naturverbunden.util.terrain;

public final class Vec2d {
	public Vec2d(Vec2d other) {
		this(other.x, other.y);
	}

	public Vec2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	private final double x;
	private final double y;

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public Vec2d add(double x, double y) {
		return new Vec2d(this.x + x, this.y + y);
	}

	public Vec2d add(Vec2d other) {
		return this.add(other.x, other.y);
	}

	public double squaredDist(Vec2d other) {
		return this.squaredDist(other.x, other.y);
	}

	public double squaredDist(double x, double y) {
		double dx = Math.abs(x - this.x);
		double dy = Math.abs(y - this.y);
		return dx * dx + dy * dy;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o == null) {
			return false;
		} else if (o instanceof Vec2d) {
			Vec2d Vec2d = (Vec2d) o;
			return Vec2d.x == this.x && Vec2d.y == this.y;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int result = 7;
		result = 29 * result + Double.hashCode(this.x);
		result = 29 * result + Double.hashCode(this.y);
		return result;
	}

	@Override
	public String toString() {
		return "Vec2d(" + this.x
				+ ", " + this.y
				+ ')';
	}
}