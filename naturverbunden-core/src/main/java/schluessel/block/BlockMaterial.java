package schluessel.block;

import java.util.function.Function;
import java.util.function.ToIntFunction;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.Tag;
import schluessel_impl.obj.ImplBlockMaterial;

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
	BlockMaterial.Builder colour(MapColor colour);

	/**
	 * Sets the map colour of the block material.
	 * @param colour the function from block state to colour for display on maps.
	 * @return if a template, a new builder with the applied change. Otherwise, returns itself.
	 */
	BlockMaterial.Builder colour(Function<BlockState, MapColor> colour);

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

	BlockMaterial.Builder sounds(BlockSoundGroup sounds);

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
		return new ImplBlockMaterial.Builder();
	}

	public static BlockMaterial.Builder copy(Block existing) {
		return new ImplBlockMaterial.Builder(existing);
	}
}
