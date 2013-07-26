//
//  Generated by the J2ObjC translator.  DO NOT EDIT!
//  source: C:\Users\Thomas\Documents\Eclipse\Tux\tuxblocks\core\src\main\java\tuxkids\tuxblocks\core\utils\MultiList.java
//
//  Created by Thomas on 7/1/13.
//

@class IOSObjectArray;
@protocol JavaUtilList;

#import "JreEmulation.h"

@interface TBMultiList : NSObject {
 @public
  id<JavaUtilList> lists_;
}

@property (nonatomic, retain) id<JavaUtilList> lists;

- (id)initWithJavaUtilListArray:(IOSObjectArray *)lists;
- (int)size;
- (BOOL)isEmpty;
- (BOOL)containsWithId:(id)o;
- (BOOL)addWithJavaUtilList:(id<JavaUtilList>)e;
- (BOOL)removeWithJavaUtilList:(id<JavaUtilList>)o;
- (BOOL)removeWithId:(id)o;
- (void)clear;
- (id)getWithInt:(int)index;
@end

typedef TBMultiList TuxkidsTuxblocksCoreUtilsMultiList;