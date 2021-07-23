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

package valoeghese.naturverbunden.mixin;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import valoeghese.naturverbunden.core.NVBToggles;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {
	protected MixinTitleScreen(Text title) {
		super(title);
	}

	@Shadow
	private List<Element> children;
	@Shadow
	private List<Selectable> selectables;
	@Shadow
	private List<Drawable> drawables;

	@Inject(at = @At("RETURN"), method = "init")
	private void onInit(CallbackInfo info) {
		if (NVBToggles.cutePuppies && new Random().nextInt(1000) == 0 ) {
			int j = this.height / 4 + 48;

			final int realmsy = j + 24 * 2;

			ClickableWidget realms = getButtonForY(realmsy);

			if (realms != null) {
				this.drawables.remove(realms);
				this.selectables.remove(realms);
				this.children.remove(realms);
			} else {
				throw new RuntimeException("Bad");
			}

			this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, realmsy, 200, 20, new TranslatableText("menu.online"), (buttonWidget) -> {
				try {
					String cutePuppies = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
					Util.getOperatingSystem().open(cutePuppies);
				} catch (Exception e) {
					throw new RuntimeException("bruh", e);
				}
			})).active = true;
		}
	}

	private ClickableWidget getButtonForY(int y) {
		AtomicReference<ClickableWidget> am = new AtomicReference<>();
		this.drawables.forEach(b -> {
			if (b instanceof ClickableWidget) {
				if (((ClickableWidget) b).y == y) {
					am.set((ClickableWidget)b);
				}
			}
		});

		return am.get();
	}
}
