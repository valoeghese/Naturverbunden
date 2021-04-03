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