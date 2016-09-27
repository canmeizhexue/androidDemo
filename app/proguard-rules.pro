# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Users\zengyaping\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
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

#####glide混淆规则
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule


## deal for core-3.2.1.jar ##
-dontwarn com.google.zxing.**
-keep class com.google.zxing.** { *;}

## deal for dexmaker-1.2.jar ##
-dontwarn com.google.dexmaker.**
-keep class com.google.dexmaker.** { *;}

###webview混淆规则  这个方法是隐藏方法，所以要避免混淆，不然到时候不会调用我们的方法
-keepclassmembers class * extends android.webkit.WebChromeClient {
   public void openFileChooser(...);
}

#### bugly混淆规则
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}


