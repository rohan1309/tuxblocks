//
//  Generated by the J2ObjC translator.  DO NOT EDIT!
//  source: tripleplay/flump/Texture.java
//
//  Created by Thomas on 7/1/13.
//

@class TripleplayFlumpTexture_Symbol;
@protocol PlaynCoreImage;
@protocol PlaynCoreImageLayer;
@protocol PlaynCoreImage_Region;
@protocol PlaynCoreJson_Object;
@protocol PlaynCoreUtilClock;
@protocol PythagorasFIPoint;

#import "JreEmulation.h"
#import "Instance.h"
#import "Symbol.h"

@interface TripleplayFlumpTexture : NSObject < TripleplayFlumpInstance > {
 @public
  id<PlaynCoreImageLayer> _layer_;
}

@property (nonatomic, retain) id<PlaynCoreImageLayer> _layer;

- (id)initWithTripleplayFlumpTexture_Symbol:(TripleplayFlumpTexture_Symbol *)symbol;
- (id<PlaynCoreImageLayer>)layer;
- (void)paintWithPlaynCoreUtilClock:(id<PlaynCoreUtilClock>)clock;
- (void)paintWithFloat:(float)dt;
@end

@interface TripleplayFlumpTexture_Symbol : NSObject < TripleplayFlumpSymbol > {
 @public
  id<PlaynCoreImage_Region> region_;
  id<PythagorasFIPoint> origin_;
  NSString *_name_;
}

@property (nonatomic, retain) id<PlaynCoreImage_Region> region;
@property (nonatomic, retain) id<PythagorasFIPoint> origin;
@property (nonatomic, copy) NSString *_name;

- (id)initWithPlaynCoreJson_Object:(id<PlaynCoreJson_Object>)json
                withPlaynCoreImage:(id<PlaynCoreImage>)atlas;
- (NSString *)name;
- (TripleplayFlumpTexture *)createInstance;
@end