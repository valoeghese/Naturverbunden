package schluessel.impl.registry;

import net.devtech.arrp.json.loot.JCondition;
import net.devtech.arrp.json.loot.JLootTable;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import schluessel.block.Layer;
import schluessel.core.Mod;
import schluessel.impl.ClientConversions;
import schluessel.impl.RuntimeResources;
import schluessel.impl.mixin.FireBlockAccessor;
import schluessel.impl.obj.ImplBlockMaterial;
import schluessel.impl.obj.ImplBlockModel;
import schluessel.impl.obj.ImplItemSettings;
import schluessel.impl.obj.ImplSchluesselBlock;
import schluessel.registry.RegistryEvent;

public class ImplRegistryEvent {
	static class Block implements RegistryEvent<schluessel.block.Block> {
		Block(Mod mod) {
			this.context = mod;
		}

		private final Mod context;

		@Override
		public void register(String id, schluessel.block.Block object) {
			ImplSchluesselBlock block = (ImplSchluesselBlock) object;
			Registry.register(Registry.BLOCK, context.locationOf(id), block);
			ImplBlockModel model = ((ImplBlockModel) block.getModel());
			ImplBlockMaterial material = ((ImplBlockMaterial) block.getMaterial());

			model.createModelFor(this.context, block, id);

			// might be burny
			if (material.getBurnChance() > -1) {
				((FireBlockAccessor) Blocks.FIRE).callSetFlammable(block, material.getBurnChance(), material.getSpreadChance());
			}

			ResourceLocation identifier = context.locationOf(id);

			// Block Item
			Item.Properties blockItemSettings = ((ImplItemSettings) block.getItemSettings()).build();
			Registry.register(Registry.ITEM, identifier, new BlockItem(block, blockItemSettings));

			if (block.defaultLootTable()) {
				RuntimeResources.RESOURCE_PACK.addLootTable(new ResourceLocation(identifier.getNamespace(), "blocks/" + identifier.getPath()),
						JLootTable.loot("minecraft:block")
						.pool(JLootTable.pool()
								.rolls(1)
								.entry(JLootTable.entry()
										.type("minecraft:item")
										.name(identifier.toString()))
								.condition(new JCondition("minecraft:survives_explosion"))));
			}

			// Add the item model
			RuntimeResources.RESOURCE_PACK.addModel(block.computeItemModel(identifier), this.context.locationOf("item/" + id));

			// Client Stuff
			if (model.getRenderLayer() != Layer.DEFAULT) {
				ClientConversions.INSTANCE.setRenderLayer(block, model.getRenderLayer());
			}
		}
	}
}
