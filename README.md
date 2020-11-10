# Android Utils

Conjunto de utilidades para el desarrollo de aplicaciones Android

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
MoreAppsGPlay.developerId = "Jedemm+Technologies" // Asignar el ID del desarrollador
MoreAppsGPlay.showAppList(this@MainActivity, null)
```
**[Documentación ](docs/androidutils/com.jeovanimartinez.androidutils.moreapps/-more-apps-g-play/index.md)**

#
