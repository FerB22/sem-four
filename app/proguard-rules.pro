# Reglas ProGuard para SemFour

# Mantener clases de Room
-keep class * extends androidx.room.RoomDatabase
-keepclassmembers class * extends androidx.room.RoomDatabase { *; }

# Mantener entidades Room
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }

# Mantener data classes serializables (Kotlinx Serialization)
-keepattributes *Annotation*
-keepclassmembers class kotlinx.serialization.json.** { *** *; }
-keep @kotlinx.serialization.Serializable class * { *; }

# Retrofit
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class retrofit2.** { *; }

# Hilt
-keepnames @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel

# Glance Widgets
-keep class androidx.glance.** { *; }
