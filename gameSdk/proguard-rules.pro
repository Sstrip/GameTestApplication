# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#x5混淆
-dontwarn dalvik.**
-dontwarn com.tencent.smtt.**
-keep class com.tencent.smtt.** {
    *;
}

-keep class com.tencent.tbs.** {
    *;
}
#广告混淆
-keep class com.bun.** {*;}
-dontwarn com.bun.**

-keep, includedescriptorclasses class com.asus.msa.SupplementaryDID.** { *; }
-keepclasseswithmembernames class com.asus.msa.SupplementaryDID.** { *; }
-keep, includedescriptorclasses class com.asus.msa.sdid.** { *; }
-keepclasseswithmembernames class com.asus.msa.sdid.** { *; }
-keep public class com.netease.nis.sdkwrapper.Utils {public <methods>;}
-keep class com.samsung.android.deviceidservice.**{*;}
-keep class a.**{*;}
#end
#统计代码混淆
-dontwarn com.tendcloud.tenddata.**
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keepclassmembers class com.tendcloud.tenddata.**{
public void *(***);
}
-keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
-keep class com.apptalkingdata.** {*;}
#end
#oaid
  -keep class com.bun.** {*;}
  -dontwarn com.bun.**

  -keep, includedescriptorclasses class com.asus.msa.SupplementaryDID.** { *; }
  -keepclasseswithmembernames class com.asus.msa.SupplementaryDID.** { *; }
  -keep, includedescriptorclasses class com.asus.msa.sdid.** { *; }
  -keepclasseswithmembernames class com.asus.msa.sdid.** { *; }
  -keep public class com.netease.nis.sdkwrapper.Utils {public <methods>;}
  -keep class com.samsung.android.deviceidservice.**{*;}
  -keep class a.**{*;}
#end
#sigmob
# android.support.v4
-dontwarn android.support.v4.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep public class * extends android.support.v4.**

# WindAd

-keep class sun.misc.Unsafe { *; }
-dontwarn com.sigmob.**
-keep class com.sigmob.**.**{*;}

# miitmdid

-keep class com.bun.** {*;}
-dontwarn com.bun.**

-keep, includedescriptorclasses class com.asus.msa.SupplementaryDID.** { *; }
-keepclasseswithmembernames class com.asus.msa.SupplementaryDID.** { *; }
-keep, includedescriptorclasses class com.asus.msa.sdid.** { *; }
-keepclasseswithmembernames class com.asus.msa.sdid.** { *; }
-keep public class com.netease.nis.sdkwrapper.Utils {public <methods>;}
-keep class com.samsung.android.deviceidservice.**{*;}
-keep class a.**{*;}
#end
#okhttp3
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-keep interface okhttp3.**{*;}
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep public class * extends retrofit2.Converter {*;}
#end
#常规混淆
-keep class com.ss.gamesdk.bean.** { *; }
-keep class com.ss.gamesdk.base.** { *; }
#end