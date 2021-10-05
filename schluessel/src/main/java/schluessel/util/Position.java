package schluessel.util;

import net.minecraft.util.Mth;

/**
 * Specifies a position in the world.
 */
public record Position(double x, double y, double z) {
	/**
	 * @return the block x position of this position.
	 */
	public int blockX() {
		return Mth.floor(this.x);
	}

	/**
	 * @return the block y position of this position.
	 */
	public int blockY() {
		return Mth.floor(this.y);
	}

	/**
	 * @return the block z position of this position.
	 */
	public int blockZ() {
		return Mth.floor(this.z);
	}

	public Position alignedToGrid() {
		return new Position(blockX(), blockY(), blockZ());
	}

	public Position up(double by) {
		return new Position(this.x, this.y + by, this.z);
	}
	
	public Position down(double by) {
		return new Position(this.x, this.y - by, this.z);
	}
	
	public Position east(double by) {
		return new Position(this.x + by, this.y, this.z);
	}
	
	public Position west(double by) {
		return new Position(this.x - by, this.y, this.z);
	}
	
	public Position south(double by) {
		return new Position(this.x, this.y, this.z + by);
	}

	public Position north(double by) {
		return new Position(this.x, this.y, this.z - by);
	}
}
