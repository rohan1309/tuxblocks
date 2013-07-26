package tuxkids.tuxblocks.core.solve.blocks.n.sprite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import tripleplay.util.Colors;
import tuxkids.tuxblocks.core.layers.LayerWrapper;
import tuxkids.tuxblocks.core.utils.CanvasUtils;

class SimplifyLayer extends LayerWrapper {

	private GroupLayer layer;
	private Simplifiable parent;
	
	public SimplifyLayer(Simplifiable parent) {
		super(graphics().createGroupLayer());
		layer = (GroupLayer) layerAddable();
		this.parent = parent;
	}

	private static Image simplifyImage;
	private List<ImageLayer> simplifyButtons = new ArrayList<ImageLayer>();
	private HashMap<ImageLayer, ModifierBlockSprite> simplifyMap = new HashMap<ImageLayer, ModifierBlockSprite>();
	private HashMap<ImageLayer, ModifierBlockSprite> pairMap = new HashMap<ImageLayer, ModifierBlockSprite>();
	private Listener simplifyListener = new Listener() {
		@Override
		public void onPointerStart(Event event) { onSimplify(event.hit()); }
		@Override
		public void onPointerEnd(Event event) { }
		@Override
		public void onPointerDrag(Event event) { }
		@Override
		public void onPointerCancel(Event event) { }
	};
	
	protected ImageLayer getSimplifyButton(ModifierBlockSprite sprite) {
		return getSimplifyButton(sprite, null, 0);
	}
	
	protected ImageLayer getSimplifyButton(ModifierBlockSprite sprite, ModifierBlockSprite pair) {
		return getSimplifyButton(sprite, pair, 0);
	}
	
	protected ImageLayer getSimplifyButton(ModifierBlockSprite sprite, ModifierBlockSprite pair, int depth) {
		while (simplifyButtons.size() <= simplifyMap.size()) { 
			addSimplifyButton();
		}
		ImageLayer layer = simplifyButtons.get(simplifyMap.size());
		simplifyMap.put(layer, sprite);
		pairMap.put(layer, pair);
		layer.setVisible(true);
		layer.setDepth(depth);
		return layer;
	}
	
	private void addSimplifyButton() {
		if (simplifyImage == null) {
			simplifyImage = CanvasUtils.createCircle(Sprite.modSize() / 3, Colors.GRAY, 1, Colors.BLACK);
		}
		ImageLayer simplifyButton = graphics().createImageLayer(simplifyImage);
		simplifyButton.setAlpha(0.5f);
		simplifyButton.setVisible(false);
		simplifyButton.addListener(simplifyListener );
		centerImageLayer(simplifyButton);
		simplifyButtons.add(simplifyButton);
		layer.add(simplifyButton);
	}
	
	private void onSimplify(Layer hit) {
		ModifierBlockSprite sprite = simplifyMap.get(hit);
		if (sprite != null) {
			ModifierBlockSprite pair = pairMap.get(hit);
			parent.simplify(sprite, pair);
		}
	}
	
	public void update() {
		for (ImageLayer button : simplifyButtons) {
			button.setVisible(false);
		}
		simplifyMap.clear();
		parent.updateSimplify();
	}
	
	protected interface Simplifiable {
		void updateSimplify();
		void simplify(ModifierBlockSprite sprite, ModifierBlockSprite pair);
	}
}
