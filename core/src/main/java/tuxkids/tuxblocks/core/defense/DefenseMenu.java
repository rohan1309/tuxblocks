package tuxkids.tuxblocks.core.defense;

import playn.core.Color;
import playn.core.Font.Style;
import playn.core.util.Clock;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.TextFormat;
import tripleplay.util.Colors;
import tuxkids.tuxblocks.core.Constant;
import tuxkids.tuxblocks.core.GameState;
import tuxkids.tuxblocks.core.GameState.Stat;
import tuxkids.tuxblocks.core.MenuSprite;
import tuxkids.tuxblocks.core.layers.LayerWrapper;
import tuxkids.tuxblocks.core.utils.CanvasUtils;

public class DefenseMenu extends MenuSprite {
	
	private static TextFormat barTextFormat = new TextFormat().withFont(
			graphics().createFont(Constant.FONT_NAME, Style.PLAIN, 20));

	private Bar[] bars;
	
	public DefenseMenu(GameState state, float width) {
		super(state, width);
		createBars();
		
	}
	
	private void createBars() {
		bars = new Bar[4];
		int index = 0;
		float barWidth = width / 4;
		float barHeight = height / 6;
		float barIndent = defaultButtonSize() * 1.2f;
		for (Stat stat : Stat.values()) {
			Bar bar = new Bar(stat, barWidth, barHeight);
			int row = index;
			int col = 0;
			bar.setTranslation(barIndent + barWidth * col, height / 5 * (row + 0.5f));
//			int row = index / 2;
//			int col = index % 2;
//			bar.setTranslation(barIndent + barWidth * col, height / 5 * (row + 0.5f) * 2);
			layer.add(bar.layerAddable());
			
			bars[index++] = bar;
		}
	}
	
	@Override
	public void update(int delta) {
		super.update(delta);
		for (Bar bar : bars) {
			bar.update(delta);
		}
	}
	
	@Override
	public void paint(Clock clock) {
		super.paint(clock);
		for (Bar bar : bars) {
			bar.paint(clock);
		}
	}

	private class Bar extends LayerWrapper {

		private final static int TEXT_SPACE = 15;
		
		private GroupLayer layer;
		private float width, height;
		private Stat stat;
		private ImageLayer symbolLayer, levelLayer, barBG, barFill;
		private int level = -1;
		private int color;
		private int strokWidth;
		private float barPerc;
		
		public Bar(Stat stat, float width, float height) {
			super(graphics().createGroupLayer());
			layer = (GroupLayer) layerAddable();
			this.width = width;
			this.height = height;
			this.stat = stat;
			this.color = state.themeColor();
			
			symbolLayer = graphics().createImageLayer(CanvasUtils.createString(
					barTextFormat, stat.symbol(), Colors.BLACK));
			symbolLayer.setTranslation((TEXT_SPACE - symbolLayer.width()) / 2, (height - symbolLayer.height()) / 2);
			layer.add(symbolLayer);
			
			levelLayer = graphics().createImageLayer();
			layer.add(levelLayer);
			
			strokWidth = 3;
			barBG = graphics().createImageLayer(CanvasUtils.createRoundRect(width - TEXT_SPACE * 2, 
					height, height * 0.2f, Color.argb(0, 0, 0, 0), strokWidth, Colors.BLACK));
			barBG.setTranslation(TEXT_SPACE, (height - barBG.height()) / 2);
			layer.add(barBG);
			
			barFill = graphics().createImageLayer(CanvasUtils.createRect(5, height - strokWidth * 2, color));
			barFill.setTranslation(barBG.tx() + strokWidth, barBG.ty() + strokWidth);
			barFill.setDepth(-1);
			layer.add(barFill);
			
			update(0);
		}
		
		float p;
		private void update(int delta) {
			int l = state.getStatLevel(stat);
			if (level != l) {
				level = l;
				levelLayer.setImage(CanvasUtils.createString(
						barTextFormat, "" + level, Colors.BLACK));
				levelLayer.setTranslation(width - (TEXT_SPACE + levelLayer.width()) / 2, 
						(height - levelLayer.height()) / 2);
			}
		}
		
		public void paint(Clock clock) {

			p += clock.dt() / 10000f;
			p %= 1;
			
			barPerc = lerpTime(barPerc, (int)(p * 5) / 5f, 0.995f, clock.dt(), 0.01f);
			barFill.setWidth(barPerc * (barBG.width() - strokWidth * 2));
			barFill.setAlpha(barPerc / 2 + 0.5f);
//			barFill.set
		}
		
	}
}