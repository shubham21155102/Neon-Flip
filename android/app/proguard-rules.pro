# Keep data classes used with Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.neonflip.domain.model.** { *; }
-keep class com.neonflip.data.remote.dto.** { *; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
