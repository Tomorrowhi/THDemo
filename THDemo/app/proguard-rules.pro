# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/zhaotaotao/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
####### 基础混淆部分 #########
    #指定代码的压缩级别
    -optimizationpasses 5
    #包名不混合大小写
    -dontusemixedcaseclassnames
    #不去忽略非公共的库类
    -dontskipnonpubliclibraryclasses
     #优化  不优化输入的类文件
    -dontoptimize
     #预校验
    -dontpreverify
#     混淆时是否记录日志
    -verbose
#      混淆时所采用的算法
    -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#    #不进行混淆类
    -keep public class * extends android.app.Fragment
    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference
    -keep public class com.android.vending.licensing.ILicensingService

    #忽略警告
    -ignorewarning

   #记录生成的日志数据,gradle build时在本项目根目录输出##
    #apk 包内所有 class 的内部结构
    -dump class_files.txt
    #未混淆的类和成员
    -printseeds seeds.txt
    #列出从 apk 中删除的代码
    -printusage unused.txt
    #混淆前后的映射
    -printmapping mapping.txt

    #混淆后，保留源码及混淆后代码对应的信息及具体行数（umeng后台异常可以mapping到具体的行数）
    -renamesourcefileattribute SourceFile
    -keepattributes SourceFile,LineNumberTable

    #保护注解
    -keepattributes *Annotation*

    #support包
    -keep class android.support.**.** { *; }
    -dontwarn android.support.**
    -keep public class * extends android.support.v4.**
    -keep public class * extends android.support.v7.**
     #保持自定义控件类不被混淆
    -keep public class * extends android.view.View {
           public <init>(android.content.Context);
           public <init>(android.content.Context, android.util.AttributeSet);
           public <init>(android.content.Context, android.util.AttributeSet, int);
           public void set*(...);
    }
    #保持 native 方法不被混淆
    -keepclasseswithmembernames class * {
        native <methods>;
    }
    #保持自定义控件类不被混淆
    -keepclasseswithmembernames class * {
        public <init>(android.content.Context, android.util.AttributeSet);
    }
    -keepclasseswithmembernames class * {
        public <init>(android.content.Context, android.util.AttributeSet, int);
    }
    -keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
    }
    #保持 Parcelable 不被混淆
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }
    #保持 Serializable 不被混淆
    -keepnames class * implements java.io.Serializable
    #保持 Serializable 不被混淆并且enum 类也不被混淆
    -keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        !static !transient <fields>;
        !private <fields>;
        !private <methods>;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
    }
    #保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
    -keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }
    -keepclassmembers class * {
        public void *ButtonClicked(android.view.View);
    }
    #不混淆资源类
    -keepclassmembers class **.R$* {
        public static <fields>;
    }

    #adapter不混淆
    -keep public class * extends android.widget.Adapter {*;}



#    不同的app，根据需求定制以下内容
    #项目特殊处理代码
    -keep class com.smawatch.oldpeoplewatch.util.** { *; } #工具类不参与混淆
    -keep class com.smawatch.oldpeoplewatch.bean.** { *; } #实体类不参与混淆

    # 以libaray的形式引用的框架,不想混淆（注意，此处不是jar包形式）
    -keep class com.github.sundeepk.compactcalendarview.** { *; }
    -keep class cn.hugeterry.updatefun.** { *; }

    ### greenDAO 3
    -keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
    }
    -keep class **$Properties
    # If you do not use SQLCipher:
    -dontwarn org.greenrobot.greendao.database.**
    # If you do not use RxJava:
    -dontwarn rx.**

    #butterknife注解
    -keep class butterknife.** { *; }
    -dontwarn butterknife.internal.**
    -keep class **$$ViewBinder { *; }
    -keepclasseswithmembernames class * {
        @butterknife.* <fields>;
    }
    -keepclasseswithmembernames class * {
        @butterknife.* <methods>;
    }

    ###-------- Gson 相关的混淆配置--------
    -keepattributes Signature
    -keepattributes *Annotation*
    -keep class sun.misc.Unsafe { *; }
    -keep class com.google.gson.stream.** { *; }
    -keepattributes EnclosingMethod

    # OkHttp3
    -dontwarn okhttp3.logging.**
    -keep class okhttp3.internal.**{*;}
    # okio
    -dontwarn okio.**
    -keep class okio.**{*;}
    -keep interface okio.**{*;}
    # Retrofit
    -dontwarn retrofit2.**
    -keep class retrofit2.** { *; }
    -keepattributes Signature
    -keepattributes Exceptions
    # RxJava RxAndroid
    -dontwarn sun.misc.**
    -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
        long producerIndex;
        long consumerIndex;
    }
    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
        rx.internal.util.atomic.LinkedQueueNode producerNode;
    }
    -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
        rx.internal.util.atomic.LinkedQueueNode consumerNode;
    }
    # EventBus3.0
    -keepattributes *Annotation*
    -keepclassmembers class ** {
        @org.greenrobot.eventbus.Subscribe <methods>;
    }
    -keep enum org.greenrobot.eventbus.ThreadMode { *; }
    # Only required if you use AsyncExecutor
    -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
        <init>(java.lang.Throwable);
    }
    # picasso
    -dontwarn com.squareup.okhttp.**



    #3D 地图
    -keep   class com.amap.api.maps.**{*;}
    -keep   class com.autonavi.amap.mapcore.*{*;}
    -keep   class com.amap.api.trace.**{*;}
    #定位
    -keep class com.amap.api.location.**{*;}
    -keep class com.amap.api.fence.**{*;}
    -keep class com.autonavi.aps.amapapi.model.**{*;}
    #搜索
    -keep   class com.amap.api.services.**{*;}
    #2D地图
    -keep class com.amap.api.maps2d.**{*;}
    -keep class com.amap.api.mapcore2d.**{*;}
    #导航
    -keep class com.amap.api.navi.**{*;}
    -keep class com.autonavi.**{*;}
    #极光推送
    -dontoptimize
    -dontpreverify
    -dontwarn cn.jpush.**
    -keep class cn.jpush.** { *; }

    #==================gson==========================
    -dontwarn com.google.**
    -keep class com.google.gson.** {*;}

    #==================protobuf======================
    -dontwarn com.google.**
    -keep class com.google.protobuf.** {*;}

    # bugly
    -dontwarn com.tencent.bugly.**
    -keep public class com.tencent.bugly.**{*;}

    #utilsCode
    -keep class com.blankj.utilcode.** { *; }
    -keepclassmembers class com.blankj.utilcode.** { *; }
    -dontwarn com.blankj.utilcode.**
    #retrolambda
    -dontwarn java.lang.invoke.*
