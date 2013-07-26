//
//  Generated by the J2ObjC translator.  DO NOT EDIT!
//  source: pythagoras/d/IQuaternion.java
//
//  Created by Thomas on 7/9/13.
//

@class IOSDoubleArray;
@class PythagorasDQuaternion;
@class PythagorasDVector3;
@protocol PythagorasDIVector3;

#import "JreEmulation.h"

@protocol PythagorasDIQuaternion < NSObject >
- (double)x;
- (double)y;
- (double)z;
- (double)w;
- (void)getWithJavaLangDoubleArray:(IOSDoubleArray *)values;
- (BOOL)hasNaN;
- (PythagorasDVector3 *)toAnglesWithPythagorasDVector3:(PythagorasDVector3 *)result;
- (PythagorasDVector3 *)toAngles;
- (PythagorasDQuaternion *)normalize;
- (PythagorasDQuaternion *)normalizeWithPythagorasDQuaternion:(PythagorasDQuaternion *)result;
- (PythagorasDQuaternion *)invert;
- (PythagorasDQuaternion *)invertWithPythagorasDQuaternion:(PythagorasDQuaternion *)result;
- (PythagorasDQuaternion *)multWithPythagorasDIQuaternion:(id<PythagorasDIQuaternion>)other;
- (PythagorasDQuaternion *)multWithPythagorasDIQuaternion:(id<PythagorasDIQuaternion>)other
                                withPythagorasDQuaternion:(PythagorasDQuaternion *)result;
- (PythagorasDQuaternion *)slerpWithPythagorasDIQuaternion:(id<PythagorasDIQuaternion>)other
                                                withDouble:(double)t;
- (PythagorasDQuaternion *)slerpWithPythagorasDIQuaternion:(id<PythagorasDIQuaternion>)other
                                                withDouble:(double)t
                                 withPythagorasDQuaternion:(PythagorasDQuaternion *)result;
- (PythagorasDVector3 *)transformWithPythagorasDIVector3:(id<PythagorasDIVector3>)vector;
- (PythagorasDVector3 *)transformWithPythagorasDIVector3:(id<PythagorasDIVector3>)vector
                                  withPythagorasDVector3:(PythagorasDVector3 *)result;
- (PythagorasDVector3 *)transformUnitXWithPythagorasDVector3:(PythagorasDVector3 *)result;
- (PythagorasDVector3 *)transformUnitYWithPythagorasDVector3:(PythagorasDVector3 *)result;
- (PythagorasDVector3 *)transformUnitZWithPythagorasDVector3:(PythagorasDVector3 *)result;
- (PythagorasDVector3 *)transformAndAddWithPythagorasDIVector3:(id<PythagorasDIVector3>)vector
                                       withPythagorasDIVector3:(id<PythagorasDIVector3>)add
                                        withPythagorasDVector3:(PythagorasDVector3 *)result;
- (PythagorasDVector3 *)transformScaleAndAddWithPythagorasDIVector3:(id<PythagorasDIVector3>)vector
                                                         withDouble:(double)scale_
                                            withPythagorasDIVector3:(id<PythagorasDIVector3>)add
                                             withPythagorasDVector3:(PythagorasDVector3 *)result;
- (double)transformZWithPythagorasDIVector3:(id<PythagorasDIVector3>)vector;
- (double)getRotationZ;
- (PythagorasDQuaternion *)integrateWithPythagorasDIVector3:(id<PythagorasDIVector3>)velocity
                                                 withDouble:(double)t;
- (PythagorasDQuaternion *)integrateWithPythagorasDIVector3:(id<PythagorasDIVector3>)velocity
                                                 withDouble:(double)t
                                  withPythagorasDQuaternion:(PythagorasDQuaternion *)result;
@end