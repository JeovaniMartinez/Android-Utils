//[androidutils](../../../index.md)/[com.jeovanimartinez.androidutils](../../index.md)/[Base](../index.md)/[Companion](index.md)/[firebaseCrashlyticsInstance](firebase-crashlytics-instance.md)



# firebaseCrashlyticsInstance  
[androidJvm]  
Content  
var [firebaseCrashlyticsInstance](firebase-crashlytics-instance.md): FirebaseCrashlytics? = null  
More info  


Instancia de Firebase Crashlytics, asignar solo si se requiere que la app que implementa la biblioteca registre errores en Firebase Crashlytics, esta propiedad es global y se usa en todas las subclases de esta clase. Asignarla solo una vez en el singleton de la app, teniendo en cuenta que la app debe tener configurado Firebase Crashlytics, o bien dejar en null si no se requiere o si no se usa Firebase Crashlytics en la app. El registro de errores se realiza solo en ciertos casos donde puede ser util, por lo que donde se dese usar en las subclases de Base, invocar firebaseCrashlyticsInstance?.recordException(e) para registrar la excepción, ya que no se hace en las funciones de log directamente porque solo se requiere registrar la excepción en ciertos casos.

  



