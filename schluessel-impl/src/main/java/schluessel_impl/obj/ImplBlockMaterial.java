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

package schluessel_impl.obj;

import java.util.function.Function;
import java.util.function.ToIntFunction;

import net.fabricmc.fabric.mixin.object.builder.AbstractBlockAccessor;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.Tag;
import schluessel.block.BlockMaterial;

public class ImplBlockMaterial implements BlockMaterial {
	private ImplBlockMaterial(ImplBlockMaterial builder) {
		this.material = builder.material;
		this.materialColour = builder.materialColour;
		this.hardness = builder.hardness;
		this.luminosity = builder.luminosity;
		this.resistance = builder.resistance;

		this.sounds = builder.sounds;
		this.ticksRandomly = builder.ticksRandomly;
		this.dropsNothing = builder.dropsNothing;
		this.slipperiness = builder.slipperiness;
		this.toolRequired = builder.toolRequired;
		this.collidable = builder.collidable;
		this.opaque = builder.opaque;

		this.toolType = builder.toolType;
		this.miningLevel = builder.miningLevel;

		this.burnChance = builder.burnChance;
		this.spreadChance = builder.spreadChance;
	}

	private ImplBlockMaterial() {
	}

	// Based Properties
	Material material;
	Function<BlockState, MapColor> materialColour;
	float hardness = 0.0f;
	float resistance = 0.0f;
	ToIntFunction<BlockState> luminosity = ignored -> 0;

	// Cringe Properties
	BlockSoundGroup sounds = BlockSoundGroup.STONE;
	boolean ticksRandomly = false;
	boolean dropsNothing = false;
	float slipperiness = 0.6f;
	boolean toolRequired = false;
	boolean collidable = true;
	Boolean opaque = null;

	// Blessed Fabric Stuff
	Tag<Item> toolType;
	int miningLevel;

	// Nice Lint Properties
	int burnChance = -1;
	int spreadChance;

	// BlockMaterial.Builder methods

	@Override
	public BlockMaterial.Builder material(Material material) {
		return new Builder(this).material(material);
	}

	@Override
	public BlockMaterial.Builder colour(MapColor colour) {
		return new Builder(this).colour(colour);
	}

	@Override
	public BlockMaterial.Builder colour(Function<BlockState, MapColor> colour) {
		return new Builder(this).colour(colour);
	}

	@Override
	public BlockMaterial.Builder hardness(float hardness) {
		return new Builder(this).hardness(hardness);
	}

	@Override
	public BlockMaterial.Builder resistance(float resistance) {
		return new Builder(this).resistance(resistance);
	}

	/**
	 * Sets both hardness and resistance to the same value.
	 */
	@Override
	public BlockMaterial.Builder strength(float strength) {
		return new Builder(this).strength(strength);
	}

	@Override
	public BlockMaterial.Builder luminosity(int luminosity) {
		return new Builder(this).luminosity(luminosity);
	}

	@Override
	public BlockMaterial.Builder luminosity(ToIntFunction<BlockState> luminosity) {
		return new Builder(this).luminosity(luminosity);
	}

	@Override
	public BlockMaterial.Builder sounds(BlockSoundGroup sounds) {
		return new Builder(this).sounds(sounds);
	}

	/**
	 * Sets hardness and resistance to 0.
	 */
	@Override
	public BlockMaterial.Builder breaksInstantly() {
		return new Builder(this).breaksInstantly();
	}

	@Override
	public BlockMaterial.Builder ticksRandomly() {
		return new Builder(this).ticksRandomly();
	}

	@Override
	public BlockMaterial.Builder dropsNothing() {
		return new Builder(this).dropsNothing();
	}

	@Override
	public BlockMaterial.Builder slipperiness(float slipperiness) {
		return new Builder(this).slipperiness(slipperiness);
	}

	@Override
	public BlockMaterial.Builder collidable(boolean collidable) {
		return new Builder(this).collidable(this.collidable);
	}

	@Override
	public BlockMaterial.Builder miningLevel(Tag<Item> toolType, int miningLevel) {
		return new Builder(this).miningLevel(toolType, miningLevel);
	}

	@Override
	public BlockMaterial.Builder flammability(int burnChance, int spreadChance) {
		return new Builder(this).flammability(burnChance, spreadChance);
	}

	public int getBurnChance() {
		return this.burnChance;
	}

	public int getSpreadChance() {
		return this.spreadChance;
	}

	public static class Builder extends ImplBlockMaterial implements BlockMaterial.Builder {
		private Builder(ImplBlockMaterial builder) {
			super(builder);
		}

		public Builder(Block existing) {
			AbstractBlockSettingsAccessor settings = (AbstractBlockSettingsAccessor) ((AbstractBlockAccessor) existing).getSettings();

			this.material = settings.getMaterial();
			this.materialColour = settings.getMapColorProvider();
			this.hardness = settings.getHardness();
			this.luminosity = settings.getLuminance();
			this.resistance = settings.getResistance();

			this.sounds = settings.getSoundGroup();
			this.ticksRandomly = settings.getRandomTicks();
			this.dropsNothing = false; // TODO how
			this.slipperiness = settings.getSlipperiness();
			this.toolRequired = settings.isToolRequired();
			this.collidable = settings.getCollidable();
			this.opaque = settings.getOpaque();
		}

		public Builder() {
		}

		@Override
		public BlockMaterial.Builder material(Material material) {
			this.material = material;
			return this;
		}

		@Override
		public BlockMaterial.Builder colour(MapColor colour) {
			this.materialColour = ignored -> colour;
			return this;
		}

		@Override
		public BlockMaterial.Builder colour(Function<BlockState, MapColor> colour) {
			this.materialColour = colour;
			return this;
		}

		@Override
		public BlockMaterial.Builder hardness(float hardness) {
			this.hardness = hardness;
			return this;
		}

		@Override
		public BlockMaterial.Builder strength(float strength) {
			this.hardness = strength;
			this.resistance = strength;
			return this;
		}

		@Override
		public BlockMaterial.Builder resistance(float resistance) {
			this.resistance = resistance;
			return this;
		}

		@Override
		public BlockMaterial.Builder luminosity(int luminosity) {
			this.luminosity = ignored -> luminosity;
			return this;
		}

		@Override
		public BlockMaterial.Builder luminosity(ToIntFunction<BlockState> luminosity) {
			this.luminosity = luminosity;
			return this;
		}

		@Override
		public BlockMaterial.Builder sounds(BlockSoundGroup sounds) {
			this.sounds = sounds;
			return this;
		}

		@Override
		public BlockMaterial.Builder breaksInstantly() {
			return this.strength(0.0F);
		}

		@Override
		public BlockMaterial.Builder ticksRandomly() {
			this.ticksRandomly = true;
			return this;
		}

		@Override
		public BlockMaterial.Builder dropsNothing() {
			this.dropsNothing = true;
			return this;
		}

		@Override
		public BlockMaterial.Builder slipperiness(float slipperiness) {
			this.slipperiness = slipperiness;
			return this;
		}

		@Override
		public BlockMaterial.Builder collidable(boolean collidable) {
			this.collidable = collidable;
			return this;
		}

		/**
		 * Set the mining level and preferred tool. If mining level is 0 or positive, it indicates a required tool. If it is negative, it indicates the tool is not required, rather only preferred.
		 */
		@Override
		public BlockMaterial.Builder miningLevel(Tag<Item> toolType, int miningLevel) {
			this.toolType = toolType;

			if (miningLevel < 0) {
				this.miningLevel = 0;
				this.toolRequired = false;
			} else {
				this.miningLevel = miningLevel;
				this.toolRequired = true;
			}

			return this;
		}

		@Override
		public BlockMaterial.Builder flammability(int burnChance, int spreadChance) {
			this.burnChance = burnChance;
			this.spreadChance = spreadChance;
			return this;
		}

		public BlockMaterial template() {
			return new ImplBlockMaterial(this);
		}
	}
}
