#
# Custom ProGuard Rules for Android Utils Library
#
# - These rules are embedded in the generated AAR and provided to the app that implements the library.
# - For these to take effect, the app using the library must use proguard-android-optimize.txt rules and have isMinifyEnabled = true
#
# Notes: https://developer.android.com/studio/projects/android-library#Considerations
#
# Docs: https://www.guardsquare.com/manual/configuration/usage
#

# Remove all calls to log functions on com.jeovanimartinez.androidutils.logutils.Log
#   -assumenosideeffects = Specifies methods that don't have any side effects, other than possibly returning a value.
#   class = class with full class name (with package)
#   public final void logv(...); # Example
#       public = Access modifier
#       void = Since it doesn't return any value (used as in Java)
#       logv = Function name
#       (...) = ... Matches any number of arguments of any type.

-assumenosideeffects class com.jeovanimartinez.androidutils.logutils.Log {
    public void logv(...);
    public void logd(...);
    public void logi(...);
    public void logw(...);
    public void loge(...);
    public void logwtf(...);
}
