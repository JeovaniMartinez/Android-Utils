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
