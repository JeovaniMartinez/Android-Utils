# Custom ProGuard Rules
#
# These rules are embedded in the generated AAR and provided to the app that implements the library.
#
# Notes: https://developer.android.com/studio/projects/android-library#Considerations
#
# Docs: https://www.guardsquare.com/manual/configuration/usage
#

# Remove all calls to log() logw() and loge() function from the Base class
#   -assumenosideeffects Specifies methods that don't have any side effects, other than possibly returning a value.
#   class Class with the methods
#   public log(...); # Example
#       public Access modifier
#       log    Function name
#       (...)  ... Matches any number of arguments of any type.
-assumenosideeffects class com.jeovanimartinez.androidutils.Base {
    public log(...);
    public logw(...);
    public loge(...);
}
