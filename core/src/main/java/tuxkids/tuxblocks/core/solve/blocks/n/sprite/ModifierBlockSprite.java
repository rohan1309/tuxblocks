package tuxkids.tuxblocks.core.solve.blocks.n.sprite;

import playn.core.Color;
import playn.core.GroupLayer;
import playn.core.util.Clock;
import pythagoras.f.FloatMath;
import tripleplay.util.Colors;
import tuxkids.tuxblocks.core.layers.ImageLayerLike;
import tuxkids.tuxblocks.core.layers.LayerLike;
import tuxkids.tuxblocks.core.utils.CanvasUtils;
import tuxkids.tuxblocks.core.utils.HashCode;

public abstract class ModifierBlockSprite extends BlockSprite {
	
	protected ModifierGroup group;
	protected int value;
	protected ModifierBlockSprite inverse;
	
	protected abstract String operator();
	protected abstract ModifierBlockSprite inverseChild();
	
	public ModifierGroup group() {
		return group;
	}
	
	public final ModifierBlockSprite inverse() {
		return inverse;
	}
	
	public ModifierBlockSprite(int value) {
		this.value = value;
		inverse = inverseChild();
	}
	
	protected ModifierBlockSprite(ModifierBlockSprite inverse) {
		this.value = inverse.value;
		this.inverse = inverse;
		if (inverse.blockListener != null) {
			addBlockListener(inverse.blockListener);
		}
	}
	
	@Override
	protected void initSpriteImpl() {
		super.initSpriteImpl();
		
		layer = generateNinepatch(text());
		if (inverse.layer == null) inverse.initSprite();
	}
	
	@Override
	public void paint(Clock clock) {
		super.paint(clock);
		if (group == null) {
			interpolateDefaultRect(clock);
		}
	}
	
	@Override
	public void addBlockListener(BlockListener listener) {
		super.addBlockListener(listener);
		if (listener != null && inverse.blockListener == null) {
			inverse.addBlockListener(listener);
		}
	}
	
	@Override
	public void showInverse() {
		if (!hasSprite()) return;
		layer.setVisible(false);
		BlockSprite inverse = inverse();
		inverse.layer.setVisible(true);
		inverse.interpolateRect(x(), y(), width(), height(), 0, 1);
		inverse.layer().setTranslation(layer().tx(), layer().ty());
	}
	
	@Override
	public void remove() {
		if (group != null) group.removeChild(this);
		group = null;
	}

	@Override
	public String text() {
		return operator() + value;
	}
	
	public boolean canSimplify() {
		if (group == null) return false;
		return group.children.contains(inverse);
	}

	public boolean canAddInverse() {
		if (group == null) return false;
		return !canSimplify() && group.modifiers == null;
	}
	
	@Override
	public void addFields(HashCode hashCode) {
		hashCode.addField(value);
	}
	
	protected void destroy(boolean destroyInverse) {
		super.destroy();
		if (destroyInverse && inverse != null && !inverse.destroyed()) {
			inverse.destroy();
		}
	}
	
	public void destroy() {
		destroy(true);
	}
	
	public void setValue(int value) {
		this.value = value;
		inverse.destroy(false);
		inverse = inverseChild();
		if (hasSprite()) {
			if (layer instanceof BlockLayer) {
				((BlockLayer) layer).setText(text());
			} else {
				ImageLayerLike oldLayer = layer;
				layer = generateNinepatch(text());
				layer.setDepth(oldLayer.depth());
				layer.setTranslation(oldLayer.tx(), oldLayer.ty());
				layer.setSize(oldLayer.width(), oldLayer.height());
				BlockListener listener = blockListener;
				blockListener = null;
				addBlockListener(listener);
				if (oldLayer.parent() != null) oldLayer.parent().add(layer.layerAddable());
				oldLayer.layerAddable().destroy();
			}
			inverse.initSprite();
		}
	}
}