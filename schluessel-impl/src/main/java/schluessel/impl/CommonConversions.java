package schluessel.impl;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import schluessel.util.ActionResult;
import schluessel.util.Position;

public class CommonConversions {
	public static Position blockposToPosition(BlockPos pos) {
		return new Position(pos.getX(), pos.getY(), pos.getZ());
	}

	public static InteractionResult convertAction(ActionResult action) {
		return switch (action) {
			case SKIP, PASS -> InteractionResult.PASS;
			case CONSUME -> InteractionResult.CONSUME;
			case SUCCESS -> InteractionResult.SUCCESS;
			case FAIL -> InteractionResult.FAIL;
		};
	}
}
