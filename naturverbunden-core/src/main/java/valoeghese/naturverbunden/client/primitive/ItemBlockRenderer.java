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

package valoeghese.naturverbunden.client.primitive;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3f;
import valoeghese.naturverbunden.common.primitive.ItemBlockEntity;

public class ItemBlockRenderer implements BlockEntityRenderer<ItemBlockEntity> {
	public ItemBlockRenderer(BlockEntityRendererFactory.Context ctx) {
	}

	@Override
	public void render(ItemBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		DefaultedList<ItemStack> defaultedList = entity.getItems();
		int pos = (int) entity.getPos().asLong();
		int inverseProgress = 4 - entity.getCraftProgress(); // 0 - 4 mapped to 1 - 0
		double spread = inverseProgress * 0.25;

		for(int i = 0; i < defaultedList.size(); ++i) {
			ItemStack stack = (ItemStack)defaultedList.get(i);

			if (!stack.isEmpty()) {
				matrices.push();
				matrices.translate(0.5, 0.05, 0.5D);

				if (i > 0) {
					matrices.translate(0.23 * ((i == 1) ? 1 : -1) * spread, 0.01 * i, 0.23 * ((i == 1) ? 1 : -1) * spread);
				}

				matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F));
				matrices.scale(0.8f, 0.8f, 0.8f);

				MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, pos + i);
				matrices.pop();
			}
		}
	}
}
