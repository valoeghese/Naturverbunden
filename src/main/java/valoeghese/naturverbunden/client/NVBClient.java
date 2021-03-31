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

package valoeghese.naturverbunden.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import valoeghese.naturverbunden.Naturverbunden;
import valoeghese.naturverbunden.client.primitive.ItemBlockRenderer;
import valoeghese.naturverbunden.common.primitive.PrimitiveContent;

public class NVBClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		Naturverbunden.LOGGER.info("Initializing Client");
		BlockEntityRendererRegistry.INSTANCE.register(PrimitiveContent.ITEM_BLOCK_ENTITY, ItemBlockRenderer::new);
	}

}
