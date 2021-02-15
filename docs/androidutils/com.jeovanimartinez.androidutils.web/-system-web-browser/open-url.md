//[androidutils](../../index.md)/[com.jeovanimartinez.androidutils.web](../index.md)/[SystemWebBrowser](index.md)/[openUrl](open-url.md)



# openUrl  
[androidJvm]  
Content  
fun [openUrl](open-url.md)(context: [Context](https://developer.android.com/reference/kotlin/android/content/Context.html), url: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), case: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null)  
More info  


Abre el navegador web del sistema en la url especificada



## Parameters  
  
androidJvm  
  
|  Name|  Summary| 
|---|---|
| <a name="com.jeovanimartinez.androidutils.web/SystemWebBrowser/openUrl/#android.content.Context#kotlin.String#kotlin.String?/PointingToDeclaration/"></a>context| <a name="com.jeovanimartinez.androidutils.web/SystemWebBrowser/openUrl/#android.content.Context#kotlin.String#kotlin.String?/PointingToDeclaration/"></a><br><br>contexto para poder lanzar el intent del navegador<br><br>
| <a name="com.jeovanimartinez.androidutils.web/SystemWebBrowser/openUrl/#android.content.Context#kotlin.String#kotlin.String?/PointingToDeclaration/"></a>url| <a name="com.jeovanimartinez.androidutils.web/SystemWebBrowser/openUrl/#android.content.Context#kotlin.String#kotlin.String?/PointingToDeclaration/"></a><br><br>URL a mostrar, debe ser una URL completa, que incluya http o https, de otro modo no va a pasar la validación<br><br>
| <a name="com.jeovanimartinez.androidutils.web/SystemWebBrowser/openUrl/#android.content.Context#kotlin.String#kotlin.String?/PointingToDeclaration/"></a>case| <a name="com.jeovanimartinez.androidutils.web/SystemWebBrowser/openUrl/#android.content.Context#kotlin.String#kotlin.String?/PointingToDeclaration/"></a><br><br>razón por la que se abre la URL en el navegador. Esto aplica solo si Firebase Analytics esta habilitado, cuando se muestra     una URL en el navegador se registra el evento, este evento contiene un parámetro que ayuda a determinar que sitio web se mostró,     y se registra el valor de case. Debe tener entre 1 y 100 caracteres.<br><br>
  
  



