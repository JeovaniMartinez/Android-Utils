# Android Utils

Conjunto de utilidades para el desarrollo de aplicaciones Android

La mayoría de las clases tienen los siguientes métodos en común
```Kotlin
setLogEnable(true) // Habilita o deshabilita el log de depuración

/*
* Para asignar la instancia de FirebaseAnalytics, que permite que algunas clases registren eventos.
* Es necesario que la aplicación tenga configurado FirebaseAnalytics
* */
setFirebaseAnalyticsInstance(FirebaseAnalytics.getInstance(this@MainActivity))
```

## Lista de utilidades

### RateApp 
Utilidad para dirigir al usuario a los detalles de la aplicación en Google Play, usualmente usada para invitar al usuario a calificar la aplicación.

Ejemplo de uso:
```Kotlin
RateApp.goToRateInGooglePlay(this@MainActivity)
```
**[Documentación ](docs/androidutils/com.jeovanimartinez.androidutils.reviews/-rate-app/index.md)**

#

### MoreAppsGPlay 
Utilidad para dirigir al usuario a la página del desarrollador en Google Play, usualmente usada para invitar al usuario a que instale las aplicaciones del desarrollador.

Ejemplo de uso:
```Kotlin
MoreAppsGPlay.setDeveloperId("Jedemm+Technologies").showAppList(this@MainActivity)
```
**[Documentación ](docs/androidutils/com.jeovanimartinez.androidutils.moreapps/-more-apps-g-play/index.md)**

#
