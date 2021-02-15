//[androidutils](../../index.md)/[com.jeovanimartinez.androidutils.moreapps](../index.md)/[MoreAppsGPlay](index.md)



# MoreAppsGPlay  
 [androidJvm] object [MoreAppsGPlay](index.md) : [Base](../../com.jeovanimartinez.androidutils/-base/index.md)<[MoreAppsGPlay](index.md)> 

Utilidad para dirigir al usuario a Google Play, específicamente a la lista de aplicaciones del desarrollador. El uso principal es para invitar al usuario a que instale otras aplicaciones del desarrollador.

   


## Functions  
  
|  Name|  Summary| 
|---|---|
| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[equals](../../com.jeovanimartinez.androidutils.web/-system-web-browser/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F-1006092240)| <a name="kotlin/Any/equals/#kotlin.Any?/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open operator fun [equals](../../com.jeovanimartinez.androidutils.web/-system-web-browser/index.md#%5Bkotlin%2FAny%2Fequals%2F%23kotlin.Any%3F%2FPointingToDeclaration%2F%5D%2FFunctions%2F-1006092240)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)  <br><br><br>
| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[hashCode](../../com.jeovanimartinez.androidutils.web/-system-web-browser/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F-1006092240)| <a name="kotlin/Any/hashCode/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open fun [hashCode](../../com.jeovanimartinez.androidutils.web/-system-web-browser/index.md#%5Bkotlin%2FAny%2FhashCode%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F-1006092240)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)  <br><br><br>
| <a name="com.jeovanimartinez.androidutils.moreapps/MoreAppsGPlay/showAppList/#android.app.Activity/PointingToDeclaration/"></a>[showAppList](show-app-list.md)| <a name="com.jeovanimartinez.androidutils.moreapps/MoreAppsGPlay/showAppList/#android.app.Activity/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [showAppList](show-app-list.md)(activity: [Activity](https://developer.android.com/reference/kotlin/android/app/Activity.html))  <br>More info  <br>Dirige al usuario a la lista de aplicaciones del desarrollador, en base a su ID (developerId).  <br><br><br>
| <a name="kotlin/Any/toString/#/PointingToDeclaration/"></a>[toString](../../com.jeovanimartinez.androidutils.web/-system-web-browser/index.md#%5Bkotlin%2FAny%2FtoString%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F-1006092240)| <a name="kotlin/Any/toString/#/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>open fun [toString](../../com.jeovanimartinez.androidutils.web/-system-web-browser/index.md#%5Bkotlin%2FAny%2FtoString%2F%23%2FPointingToDeclaration%2F%5D%2FFunctions%2F-1006092240)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  <br><br><br>


## Properties  
  
|  Name|  Summary| 
|---|---|
| <a name="com.jeovanimartinez.androidutils.moreapps/MoreAppsGPlay/developerId/#/PointingToDeclaration/"></a>[developerId](developer-id.md)| <a name="com.jeovanimartinez.androidutils.moreapps/MoreAppsGPlay/developerId/#/PointingToDeclaration/"></a> [androidJvm] var [developerId](developer-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)ID del desarrollador del cual se va a mostrar la lista de aplicaciones al llamar a showAppList   <br>
| <a name="com.jeovanimartinez.androidutils.moreapps/MoreAppsGPlay/firebaseAnalyticsEnabled/#/PointingToDeclaration/"></a>[firebaseAnalyticsEnabled](index.md#%5Bcom.jeovanimartinez.androidutils.moreapps%2FMoreAppsGPlay%2FfirebaseAnalyticsEnabled%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-1006092240)| <a name="com.jeovanimartinez.androidutils.moreapps/MoreAppsGPlay/firebaseAnalyticsEnabled/#/PointingToDeclaration/"></a> [androidJvm] var [firebaseAnalyticsEnabled](index.md#%5Bcom.jeovanimartinez.androidutils.moreapps%2FMoreAppsGPlay%2FfirebaseAnalyticsEnabled%2F%23%2FPointingToDeclaration%2F%5D%2FProperties%2F-1006092240): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)Determina si el registro de eventos en Firebase Analytics esta habilitado para la instancia de la clase, para el registro de eventos, la propiedad estática del companion object firebaseAnalyticsInstance también debe estar asignada, si esa propiedad es null, no se van a registrar eventos, ya que es requerida   <br>

