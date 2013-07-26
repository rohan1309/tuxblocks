//
//  Generated by the J2ObjC translator.  DO NOT EDIT!
//  source: C:\Users\Thomas\Documents\Eclipse\Tux\tuxblocks\core\src\main\java\tuxkids\tuxblocks\core\defense\walker\ShrinkWalker.java
//
//  Created by Thomas on 7/1/13.
//

#import "java/lang/Math.h"
#import "pythagoras/f/FloatMath.h"
#import "pythagoras/i/Point.h"
#import "tuxkids/tuxblocks/core/ImageLayerTintable.h"
#import "tuxkids/tuxblocks/core/defense/Grid.h"
#import "tuxkids/tuxblocks/core/defense/walker/ShrinkWalker.h"
#import "tuxkids/tuxblocks/core/defense/walker/Walker.h"

@implementation TBShrinkWalker

- (id)initWithInt:(int)maxHp
          withInt:(int)walkCellTime {
  return [super initWithInt:maxHp withInt:walkCellTime];
}

- (void)updateMovementWithFloat:(float)perc {
  PythagorasIPoint *coords;
  if (perc < 0.5f) {
    coords = lastCoordinates_;
  }
  else {
    coords = coordinates__;
  }
  [((TBImageLayerTintable *) NIL_CHK(layer__)) setOriginWithFloat:[((TBImageLayerTintable *) NIL_CHK(layer__)) width] / 2 withFloat:[((TBImageLayerTintable *) NIL_CHK(layer__)) height] / 2];
  [((TBImageLayerTintable *) NIL_CHK(layer__)) setScaleWithFloat:[JavaLangMath absWithFloat:[PythagorasFFloatMath cosWithFloat:PythagorasFFloatMath_PI * perc]]];
  [((TBImageLayerTintable *) NIL_CHK(layer__)) setTranslationWithFloat:((PythagorasIPoint *) NIL_CHK(coords)).y_ * [((TBDGrid *) NIL_CHK(grid_)) cellSize] + [((TBImageLayerTintable *) NIL_CHK(layer__)) originX] withFloat:((PythagorasIPoint *) NIL_CHK(coords)).x_ * [((TBDGrid *) NIL_CHK(grid_)) cellSize] + [((TBImageLayerTintable *) NIL_CHK(layer__)) originY]];
}

- (TBWalker *)copy__ OBJC_METHOD_FAMILY_NONE {
  return [[[TBShrinkWalker alloc] initWithInt:maxHp_ withInt:walkCellTime__] autorelease];
}

- (void)dealloc {
  [super dealloc];
}

@end