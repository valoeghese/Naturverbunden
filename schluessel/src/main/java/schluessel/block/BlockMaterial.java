package schluessel.block;

import java.util.function.Function;
import java.util.function.ToIntFunction;

import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import schluessel.impl.Implementation;
import schluessel.util.MapColour;

/**
 * Represents the properties of a block material.
 */
public interface BlockMaterial {
	/**
	 * Sets the base material of the block material.
	 * @param material the base material
	 * @return if a template, a new builder with the applied change. Otherwise, returns itself.
	 */
	BlockMaterial.Builder material(Material material);

	/**
	 * Sets the map colour of the block material.
	 * @param colour the colour for display on maps.
	 * @return if a template, a new builder with the applied change. Otherwise, returns itself.
	 */
	BlockMaterial.Builder colour(MapColour colour);

	/**
	 * Sets the map colour of the block material.
	 * @param colour the function from block state to colour for display on maps.
	 * @return if a template, a new builder with the applied change. Otherwise, returns itself.
	 */
	BlockMaterial.Builder colour(Function<BlockState, MapColour> colour);

	/**
	 * Sets the hardness of the block material.
	 * @param hardness the hardness.
	 * @return if a template, a new builder with the applied change. Otherwise, returns itself.
	 */
	BlockMaterial.Builder hardness(float hardness);

	/**
	 * Sets the resistance of the block material.
	 * @param resistance the resistance.
	 * @return if a template, a new builder with the applied change. Otherwise, returns itself.
	 */
	BlockMaterial.Builder resistance(float resistance);

	/**
	 * Sets both hardness and resistance to the same value.
	 * @return if a template, a new builder with the applied change. Otherwise, returns itself.
	 */
	BlockMaterial.Builder strength(float strength);

	BlockMaterial.Builder luminosity(int luminosity);

	BlockMaterial.Builder luminosity(ToIntFunction<BlockState> luminosity);

	BlockMaterial.Builder sounds(SoundType sounds);

	/**
	 * Sets hardness and resistance to 0.
	 * @return if a template, a new builder with the applied change. Otherwise, returns itself.
	 */
	BlockMaterial.Builder breaksInstantly();

	BlockMaterial.Builder ticksRandomly();

	BlockMaterial.Builder dropsNothing();

	BlockMaterial.Builder slipperiness(float slipperiness);

	BlockMaterial.Builder collidable(boolean collidable);

	BlockMaterial.Builder miningLevel(Tag<Item> toolType, int miningLevel);

	BlockMaterial.Builder flammability(int burnChance, int spreadChance);

	/**
	 * A mutable version of BlockMaterial
	 */
	interface Builder extends BlockMaterial {
		BlockMaterial template();
	}

	public static BlockMaterial.Builder builder() {
		return Implementation.INSTANCE.newBlockMaterialBuilder();
	}

	public static BlockMaterial.Builder copy(Block existing) {
		return Implementation.INSTANCE.newBlockMaterialBuilder(existing);
	}
}
