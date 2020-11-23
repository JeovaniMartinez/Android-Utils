# Android Utils

Conjunto de utilidades para el desarrollo de aplicaciones Android

#

### Consideraciones
- Toda la definición de paquetes, clases, propiedades, métodos, etc. está escrita en idioma inglés, y toda la documentación está escrita en español.
- La biblioteca muestra algunas vistas, considerando lo siguiente:
	- Soporte para tema claro y oscuro, se utiliza los componentes de Material Design para Android, y se sigue su estilo de diseño.
	- Las vistas se pueden mostrar en los siguientes idiomas: Inglés, Español

#### Configuración
Agregar la siguiente configuración en Gradle (a nivel de la app)
```Gradle
dependencies {
    implementation 'com.github.JeovaniMartinez:Android-Utils:VERSION'
}
```

La biblioteca muestra algunas vistas, las cuales siguen el estilo del tema de la aplicación y tienen soporte para el tema claro y oscuro, para que dichas vistas se muestren con el estilo de la app, es necesario declarar el siguiente estilo en la app, el cual debe heredar del tema principal de la aplicación.
```XML
<!-- Tema de la biblioteca Android Utils, se usa para conservar el estilo de la aplicación  -->
<style name="AndroidUtilsTheme" parent="AppTheme" />
```

#### General

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

### RateInApp 
Utilidad para iniciar un flujo que invita al usuario a calificar la aplicación, en base a ciertas condiciones sobre el uso de la aplicación (Cantos días tiene instalada, cuantas veces se ha iniciado, etc.).
Para Android 5.0 (API 21) y posteriores, se utiliza Google Play In-App Review API, por lo que se puede calificar dentro de la aplicación.
Para versiones anteriores a Android 5.0, se muestra un diálogo para invitar al usuario a calificar la aplicación, si el usuario acepta, es dirigido a los detalles de la aplicación en Google Play.

Ejemplo de uso:

En el onCreate() del singleton o de la actividad principal, hay que establecer los valores de configuración e inicializar la utilidad pasando un contexto. Es muy importante hacerlo solo una vez en la app, ya que en ese momento se contabilizan las veces que ha iniciado el usuario la aplicación. (Consultar la documentación para ver la función de cada parámetro de la configuración).
```Kotlin
RateInApp
    .setMinInstallElapsedDays(10)
    .setMinInstallLaunchTimes(10)
    .setMinRemindElapsedDays(2)
    .setMinRemindLaunchTimes(4)
    .setShowAtEvent(2)
    .setShowNeverAskAgainButton(true)
    .init(this@MainActivity)
```

Ya que se ha configurado la utilidad, llamar al siguiente método en el momento en el que se quiera mostrar el flujo para calificar, el flujo se mostrará solo si se cumplen las condiciones especificadas en la configuración.
```Kotlin
RateInApp.checkAndShow(this@MainActivity)
```
**[Documentación ](docs/androidutils/com.jeovanimartinez.androidutils.reviews.rateinapp/-rate-in-app/index.md)**

#

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
