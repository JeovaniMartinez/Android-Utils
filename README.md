[![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/JeovaniMartinez/Android-Utils?color=orange&include_prereleases&style=flat-square)](#) [![JitPack](https://img.shields.io/jitpack/v/github/JeovaniMartinez/Android-Utils?color=blue&style=flat-square)](https://jitpack.io/#JeovaniMartinez/Android-Utils) [![API](https://img.shields.io/badge/API-17%2B-lightgrey?style=flat-square)](#) [![GitHub](https://img.shields.io/github/license/JeovaniMartinez/Android-Utils?style=flat-square)](/LICENSE)


# Android Utils

Conjunto de utilidades para el desarrollo de aplicaciones Android

#

### Consideraciones
- Toda la definición de paquetes, clases, propiedades, métodos, etc. está escrita en idioma inglés, y toda la documentación está escrita en español.
- La biblioteca muestra algunas vistas, considerando lo siguiente:
	- Soporte para tema claro y oscuro, se utiliza los componentes de Material Design para Android, y se sigue su estilo de diseño.
	- Las vistas se pueden mostrar en los siguientes idiomas: Inglés, Español.
- En algunos ejemplos de uso se muestra un panel colapsable llamado "Modo compacto" donde se muestra el código con la configuración mínima requerida para la utilidad, teniendo en cuanta que en algunos casos, se usan valores predeterminados definidos en la biblioteca, por lo que se recomienda usar ese código solo si se tiene una rama de la biblioteca y se han ajustado los valores predeterminados por los deseados.

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

El log de depuración se puede habilitar y deshabilitar de manera global, y afecta a todas las utilidades de la biblioteca.
Se recomienda usar la siguiente configuración para habilitarlo en desarrollo y deshabilitarlo en producción, solo es necesario ajustarlo una vez dentro de la app, ya sea en el singleton de la app o en el onCreate() de la actividad principal.
```Kotlin
Base.logEnable = BuildConfig.DEBUG
```
Nota: Base es la superclase de la mayoría de las utilidades. **[Documentación ](docs/androidutils/com.jeovanimartinez.androidutils/-base/index.md)**

#### Firebase Analytics
El registro de eventos personalizados es muy útil para comprender como interactúan los usuarios con la aplicación, las utilidades de la biblioteca registran eventos útiles en Firebase Analytics. Este registro esta deshabilitado de manera predeterminada, para habilitarlo, hay que definir la instancia de Firebase Analytics, esto es necesario hacerlo en el onCreate() del singleton de la app o de la actividad principal, al asignar la instancia, el registro de eventos se va a habilitar para todas las utilidades de la biblioteca.

Para habilitar el registro de eventos en Firebase Analytics:
```Kotlin
Base.firebaseAnalyticsInstance = FirebaseAnalytics.getInstance(this)
```
También se puede desactivar el registro de eventos para una utilidad en específico:
```Kotlin
MoreAppsGPlay.firebaseAnalyticsEnabled = false
```

#
#

## Lista de Utilidades

### Translucent Theme 
Estilo y clase base que permite que las actividades tengan un fondo completamente transparente, o bien un fondo con cierta opacidad entre 0 (completamente transparente) y 1 (completamente opaco). 

**[Demostración](/resources/images/translucent-theme/translucent-theme-demo.png?raw=true)**

Ejemplo de uso:

1. En el archivo `AndroidManifest` asignar el tema `AndroidUtilsTheme.Translucent` a la actividad deseada.
```xml
 <activity
            android:name=".about.AboutActivity"
            android:theme="@style/AndroidUtilsTheme.Translucent" />
```
2. En la actividad deseada, heredar de `TranslucentActivity` que a su vez hereda de `AppCompatActivity`.
```Kotlin
class AboutActivity : TranslucentActivity() { ... }
```

### RateInApp 
Utilidad para iniciar un flujo que invita al usuario a calificar la aplicación, en base a ciertas condiciones sobre el uso de la aplicación (Cantos días tiene instalada, cuantas veces se ha iniciado, etc.).
Para Android 5.0 (API 21) y posteriores, se utiliza Google Play In-App Review API, por lo que se puede calificar dentro de la aplicación.
Para versiones anteriores a Android 5.0, se muestra un diálogo para invitar al usuario a calificar la aplicación, si el usuario acepta, es dirigido a los detalles de la aplicación en Google Play.

![Demo](/resources/images/rate-in-app/rate-in-app-demo.png?raw=true)

Ejemplo de uso:

En el onCreate() del singleton de la app o de la actividad principal, hay que establecer los valores de configuración e inicializar la utilidad pasando un contexto. Es muy importante hacerlo solo una vez en la app, ya que en ese momento se contabilizan las veces que ha iniciado el usuario la aplicación. (Consultar la documentación para ver la función de cada parámetro de la configuración).
```Kotlin
RateInApp.apply {
    minInstallElapsedDays = 10
    minInstallLaunchTimes = 10
    minRemindElapsedDays = 2
    minRemindLaunchTimes = 4
    showAtEvent = 2
    showNeverAskAgainButton = true
}.init(this@MainActivity)
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
MoreAppsGPlay.apply { developerId = "GitHub" }.showAppList(this@MainActivity)
```
<details>
<summary>Modo compacto</summary>
<p>
	
```Kotlin
MoreAppsGPlay.showAppList(this@MainActivity)
```
</p>
</details>

**[Documentación ](docs/androidutils/com.jeovanimartinez.androidutils.moreapps/-more-apps-g-play/index.md)**

#

### SystemWebBrowser 
Utilidad para interactuar con el navegador web del sistema.

#### - openUrl
Abre el navegador web del sistema en la URL especificada.

Ejemplo de uso:
```Kotlin
SystemWebBrowser.openUrl(this@MainActivity, "https://jedemm.com", "jedemm_website")
```
<details>
<summary>Modo compacto</summary>
<p>
	
```Kotlin
SystemWebBrowser.openUrl(this@MainActivity, "https://jedemm.com")
```
</p>
</details>

**[Documentación ](docs/androidutils/com.jeovanimartinez.androidutils.web/-system-web-browser/open-url.md)**

#

#

## Funciones de Extensión

#### Activity **[[ Documentación ]](docs/androidutils/com.jeovanimartinez.androidutils.extensions.activity/android.app.-activity/index.md)**

- TaskDescription.kt: Contiene extensiones para configurar fácilmente el TaskDescription de las actividades.

#### Context **[[ Documentación ]](docs/androidutils/com.jeovanimartinez.androidutils.extensions.context/android.content.-context/index.md)**

- Toast.kt: Extensiones para mostrar rápidamente toast de corta y larga duración.

#### Dimension **[[ Documentación ]](docs/androidutils/com.jeovanimartinez.androidutils.extensions.dimension/android.content.-context/index.md)**

- Dimensions.kt: Extensiones disponibles para cualquier vista y cualquier contexto, permiten convertir fácilmente dp o sp a px y viceversa.

#### Nullability **[[ Documentación ]](docs/androidutils/com.jeovanimartinez.androidutils.extensions.nullability/index.md)**

- NullSafety.kt: Extensiones para trabajar más fácilmente con valores nulos y con la seguridad contra nulos.

#### View **[[ Documentación ]](docs/androidutils/com.jeovanimartinez.androidutils.extensions.view/android.view.-view-property-animator/index.md)**

- Animations.kt: Extensiones para facilitar el uso de animaciones en las vistas.
