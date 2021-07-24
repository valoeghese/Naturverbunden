package schluessel_impl.registry;

import net.devtech.arrp.json.loot.JCondition;
import net.devtech.arrp.json.loot.JLootTable;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import schluessel.block.Layer;
import schluessel.core.Mod;
import schluessel.registry.RegistryEvent;
import schluessel_impl.ImplSchluesselARRP;
import schluessel_impl.ImplSchluesselClient;
import schluessel_impl.mixin.FireBlockAccessor;
import schluessel_impl.obj.ImplBlockMaterial;
import schluessel_impl.obj.ImplBlockModel;
import schluessel_impl.obj.ImplItemSettings;
import schluessel_impl.obj.ImplSchluesselBlock;

public class ImplRegistryEvent {
	static class Block implements RegistryEvent<schluessel.block.Block> {
		Block(Mod mod) {
			this.context = mod;
		}

		private final Mod context;

		@Override
		public void register(String id, schluessel.block.Block object) {
			ImplSchluesselBlock block = (ImplSchluesselBlock) object;
			Registry.register(Registry.BLOCK, context.identifierOf(id), block);
			ImplBlockModel model = ((ImplBlockModel) block.getModel());
			ImplBlockMaterial material = ((ImplBlockMaterial) block.getMaterial());

			model.createModelFor(this.context, block, id);

			// might be burny
			if (material.getBurnChance() > -1) {
				((FireBlockAccessor) Blocks.FIRE).callRegisterFlammableBlock(block, material.getBurnChance(), material.getSpreadChance());
			}

			Identifier identifier = context.identifierOf(id);

			// Block Item
			Item.Settings blockItemSettings = ((ImplItemSettings) block.getItemSettings()).build();
			Registry.register(Registry.ITEM, identifier, new BlockItem(block, blockItemSettings));

			if (block.defaultLootTable()) {
				ImplSchluesselARRP.RESOURCE_PACK.addLootTable(new Identifier(identifier.getNamespace(), "blocks/" + identifier.getPath()),
						JLootTable.loot("minecraft:block")
						.pool(JLootTable.pool()
								.rolls(1)
								.entry(JLootTable.entry()
										.type("minecraft:item")
										.name(identifier.toString()))
								.condition(new JCondition("minecraft:survives_explosion"))));
			}

			// Add the item model
			ImplSchluesselARRP.RESOURCE_PACK.addModel(block.computeItemModel(identifier), this.context.identifierOf("item/" + id));

			// Client Stuff
			if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT && model.getRenderLayer() != Layer.DEFAULT) {
				ImplSchluesselClient.setRenderLayer(block, model.getRenderLayer());
			}
		}
	}
}
