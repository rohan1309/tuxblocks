//
//  Generated by the J2ObjC translator.  DO NOT EDIT!
//  source: tripleplay/util/SimpleFrames.java
//
//  Created by Thomas on 7/1/13.
//

#import "IPoint.h"
#import "Image.h"
#import "ImageLayer.h"
#import "Layer.h"
#import "Points.h"

@implementation TripleplayUtilSimpleFrames

- (id<PlaynCoreImage>)_source {
  return _source_;
}
- (void)set_source:(id<PlaynCoreImage>)_source {
  JreOperatorRetainedAssign(&_source_, self, _source);
}
@synthesize _source = _source_;
@synthesize _width = _width_;
@synthesize _height = _height_;
@synthesize _count = _count_;

- (id)initWithPlaynCoreImage:(id<PlaynCoreImage>)source
                   withFloat:(float)width {
  return [self initTripleplayUtilSimpleFramesWithPlaynCoreImage:source withFloat:width withFloat:[((id<PlaynCoreImage>) NIL_CHK(source)) height]];
}

- (id)initTripleplayUtilSimpleFramesWithPlaynCoreImage:(id<PlaynCoreImage>)source
                                             withFloat:(float)width
                                             withFloat:(float)height {
  return [self initTripleplayUtilSimpleFramesWithPlaynCoreImage:source withFloat:width withFloat:height withInt:(int) ([((id<PlaynCoreImage>) NIL_CHK(source)) height] / height) * (int) ([((id<PlaynCoreImage>) NIL_CHK(source)) width] / width)];
}

- (id)initWithPlaynCoreImage:(id<PlaynCoreImage>)source
                   withFloat:(float)width
                   withFloat:(float)height {
  return [self initTripleplayUtilSimpleFramesWithPlaynCoreImage:source withFloat:width withFloat:height];
}

- (id)initTripleplayUtilSimpleFramesWithPlaynCoreImage:(id<PlaynCoreImage>)source
                                             withFloat:(float)width
                                             withFloat:(float)height
                                               withInt:(int)count {
  if ((self = [super init])) {
    JreOperatorRetainedAssign(&_source_, self, source);
    _width_ = width;
    _height_ = height;
    _count_ = count;
  }
  return self;
}

- (id)initWithPlaynCoreImage:(id<PlaynCoreImage>)source
                   withFloat:(float)width
                   withFloat:(float)height
                     withInt:(int)count {
  return [self initTripleplayUtilSimpleFramesWithPlaynCoreImage:source withFloat:width withFloat:height withInt:count];
}

- (float)width {
  return _width_;
}

- (float)height {
  return _height_;
}

- (int)count {
  return _count_;
}

- (id<PlaynCoreImage>)frameWithInt:(int)index {
  int cols = [self cols], row = (index % cols), col = (index / cols);
  return [((id<PlaynCoreImage>) NIL_CHK(_source_)) subImageWithFloat:_width_ * row withFloat:_height_ * col withFloat:_width_ withFloat:_height_];
}

- (id<PythagorasFIPoint>)offsetWithInt:(int)index {
  return [PythagorasFPoints ZERO];
}

- (void)applyWithInt:(int)index
withPlaynCoreImageLayer:(id<PlaynCoreImageLayer>)layer {
  (void) [((id<PlaynCoreImageLayer>) NIL_CHK(layer)) setTranslationWithFloat:0 withFloat:0];
  int cols = [self cols], row = (index % cols), col = (index / cols);
  id<PlaynCoreImage> cur = [((id<PlaynCoreImageLayer>) NIL_CHK(layer)) image];
  if ([(id) cur conformsToProtocol: @protocol(PlaynCoreImage_Region)]) {
    id<PlaynCoreImage_Region> curr = (id<PlaynCoreImage_Region>) cur;
    if ([((id<PlaynCoreImage_Region>) NIL_CHK(curr)) parent] == _source_) {
      [((id<PlaynCoreImage_Region>) NIL_CHK(curr)) setBoundsWithFloat:_width_ * row withFloat:_height_ * col withFloat:_width_ withFloat:_height_];
      return;
    }
  }
  (void) [((id<PlaynCoreImageLayer>) NIL_CHK(layer)) setImageWithPlaynCoreImage:[self frameWithInt:index]];
}

- (int)cols {
  return (int) ([((id<PlaynCoreImage>) NIL_CHK(_source_)) width] / _width_);
}

- (void)dealloc {
  JreOperatorRetainedAssign(&_source_, self, nil);
  [super dealloc];
}

- (void)copyAllPropertiesTo:(id)copy {
  [super copyAllPropertiesTo:copy];
  TripleplayUtilSimpleFrames *typedCopy = (TripleplayUtilSimpleFrames *) copy;
  typedCopy._source = _source_;
  typedCopy._width = _width_;
  typedCopy._height = _height_;
  typedCopy._count = _count_;
}

@end