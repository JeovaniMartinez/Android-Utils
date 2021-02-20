### Biblioteca en desarrollo, se están realizando constantes cambios, mejoras y agregando nuevas funcionalidades. Por el momento no está lista para producción.

#

[![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/JeovaniMartinez/Android-Utils?color=orange&include_prereleases&style=flat-square)](#) [![JitPack](https://img.shields.io/jitpack/v/github/JeovaniMartinez/Android-Utils?color=blue&style=flat-square)](https://jitpack.io/#JeovaniMartinez/Android-Utils) [![API](https://img.shields.io/badge/API-17%2B-lightgrey?style=flat-square)](#) [![GitHub](https://img.shields.io/github/license/JeovaniMartinez/Android-Utils?style=flat-square)](/LICENSE)


# Android Utils

Conjunto de utilidades para el desarrollo de aplicaciones Android.

#

#### Consideraciones
- Toda la definición de paquetes, clases, propiedades, métodos, etc. está escrita en idioma inglés, y toda la documentación está escrita en español.
- La biblioteca muestra algunas vistas, considerando lo siguiente:
	- Soporte para tema claro y oscuro, se utiliza los componentes de Material Design para Android, y se sigue su estilo de diseño.
	- Las vistas se pueden mostrar en los siguientes idiomas: Inglés, Español.
- En algunos ejemplos de uso se muestra un panel colapsable llamado "Modo compacto" donde se muestra el código con la configuración mínima requerida para la utilidad, teniendo en cuanta que en algunos casos, se usan valores predeterminados definidos en la biblioteca.

#### Configuración
Agregar la siguiente configuración en Gradle (a nivel del proyecto)
```Gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

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

#### Configuración General

El log de depuración se puede habilitar y deshabilitar de manera global, y afecta a todas las utilidades de la biblioteca.
Se recomienda usar la siguiente configuración para habilitarlo en desarrollo y deshabilitarlo en producción, solo es necesario ajustarlo una vez dentro de la app, ya sea en el singleton de la app o en el `onCreate()` de la actividad principal.
```Kotlin
Base.logEnable = BuildConfig.DEBUG
```
Nota: Base es la superclase de la mayoría de las utilidades. **[Documentación ](docs/androidutils/com.jeovanimartinez.androidutils/-base/index.md)**

#### Firebase Analytics
El registro de eventos personalizados es muy útil para comprender como interactúan los usuarios con la aplicación, las utilidades de la biblioteca registran eventos útiles en Firebase Analytics. Este registro esta deshabilitado de manera predeterminada, para habilitarlo, hay que definir la instancia de Firebase Analytics, esto es necesario hacerlo en el `onCreate()` del singleton de la app o de la actividad principal, al asignar la instancia, el registro de eventos se va a habilitar para todas las utilidades de la biblioteca.

Para habilitar el registro de eventos en Firebase Analytics:
```Kotlin
Base.firebaseAnalyticsInstance = FirebaseAnalytics.getInstance(context)
```
También se puede desactivar el registro de eventos para una utilidad en específico:
```Kotlin
MoreAppsGPlay.firebaseAnalyticsEnabled = false
```

#### Firebase Crashlytics
La biblioteca puede registrar algunos flujos de error recuperables en Firebase Crashlytics, para esto es necesario asignar la instancia de crashlytics a la biblioteca, esto es necesario hacerlo en el `onCreate()` del singleton de la app o de la actividad principal, teniendo en cuenta que la app que implementa la biblioteca debe tener configurado Firebase Crashlytics.
```Kotlin
Base.firebaseCrashlyticsInstance = FirebaseCrashlytics.getInstance()
```

#
#

## Anotaciones

### Anotaciones para inspección de código

Estas anotaciones están diseñadas para orientar al desarrollador sobre el tipo de dato o recurso que se puede asignar a una propiedad o variable, la biblioteca cuenta con el módulo `lintcheck` que se encarga de verificar el uso de las anotaciones y mostrar advertencias en caso de que se detecte un valor incorrecto.

Por lo general, en estas anotaciones, el tipo de dato esperado es Any, para dar la flexibilidad de aceptar diferentes tipos de datos. Por ejemplo, en algunos casos se espera un tipo de dato String, que se procesa tal cual, pero también se puede esperar un tipo de dato Int, para obtener el String de los recursos mediante su ID.
La biblioteca permite esta flexibilidad y se encarga de identificar el tipo de dato y tratarlo de manera adecuada, y en caso de ser un tipo de dato incorrecto, se genera una excepción.


#### `@DrawableOrDrawableRes`

Indica que el valor esperado debe ser un Drawable o el ID de un recuso de drawable. Por ejemplo: `drawableObject`, `R.drawable.demo`.

#### `@StringOrStringRes` 

Indica que el valor esperado debe ser un dato tipo String, Char (ya que se puede representar como String) o el ID de un recuso de string. Por ejemplo: `'a'`, `"Hola"`, `R.string.demo`.

#
#

## Lista de Utilidades

### Translucent Theme 
Estilo y clase base que permite que las actividades tengan un fondo completamente transparente, o bien un fondo con cierta opacidad entre 0 (completamente transparente) y 1 (completamente opaco). 

**[Demostración](/resources/images/translucent-theme/translucent-theme-demo.png?raw=true)**

Ejemplo de uso:

Nota: En el archivo de diseño, las actividades que hereden de `TranslucentActivity` deben tener un `RelativeLayout` como elemento raíz en para que ocupen el espacio completo de la pantalla, o bien un tamaño fijo.
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
3. Asignar el valor a la propiedad `activityOpacity` en el lugar donde se desea ajustar la opacidad de la actividad, para que la actividad tenga el fondo al iniciarse se debe asignar entes de `super.onCreate(savedInstanceState)` en el `onCreate()`. La propiedad puede reasignarse en cualquier parte y el cambio se ve reflejado de manera instantánea. El valor debe estar entre 0 y 1, que corresponde a una opacidad del 0% y del 100% respectivamente.
```Kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        super.activityOpacity = 0.9f
        super.onCreate(savedInstanceState)
}
```
Para generar el mejor efecto de opacidad en la actividad, se usa la propiedad `dimAmount` de la ventana para controlar la opacidad. En algunos casos (como cuando se muestra un diálogo) este cuenta con su propio valor de dimAmount, y cuando ese valor es menor que el de activityOpacity se genera un efecto indeseado en la vista. Para corregir esto, la actividad TranslucentActivity cuenta con una función especial llamada `configureWindowDim` que hay que invocar cuando se va a mostrar una vista encima de la actividad, esto corrige ese inconveniente y mantiene la opacidad adecuada. Por ejemplo, para realizar el ajuste en un diálogo:
```Kotlin
val dialog = MaterialAlertDialogBuilder(this@AboutActivity).setTitle("DEMO").show()
configureWindowDim(dialog.window)
```
**[Documentación](docs/androidutils/com.jeovanimartinez.androidutils.themes.translucent/-translucent-activity/index.md)**<br>
**[Ejemplo de implementación (diseño)](androidutils/src/main/res/layout/activity_about.xml)**<br>
**[Ejemplo de implementación (código)](androidutils/src/main/java/com/jeovanimartinez/androidutils/about/AboutActivity.kt)**

#

### RateInApp 
Utilidad para iniciar un flujo que invita al usuario a calificar la aplicación, en base a ciertas condiciones sobre el uso de la aplicación (Cantos días tiene instalada, cuantas veces se ha iniciado, etc.).
Para Android 5.0 (API 21) y posteriores, se utiliza Google Play In-App Review API, por lo que se puede calificar dentro de la aplicación.
Para versiones anteriores a Android 5.0, se muestra un diálogo para invitar al usuario a calificar la aplicación, si el usuario acepta, es dirigido a los detalles de la aplicación en Google Play.

![Demo](/resources/images/rate-in-app/rate-in-app-demo.png?raw=true)

Ejemplo de uso:

En el `onCreate()` del singleton de la app o de la actividad principal, hay que establecer los valores de configuración e inicializar la utilidad pasando un contexto. Es muy importante hacerlo solo una vez en la app, ya que en ese momento se contabilizan las veces que ha iniciado el usuario la aplicación. (Consultar la documentación para ver la función de cada parámetro de la configuración).
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

### About 
Utilidad para mostrar una actividad de acerca de, donde se incluye la información de la app, del autor y de la empresa, el copyright, así como las licencias de código abierto, los términos de licencia y la política de privacidad.

![Demo](/resources/images/about/about-demo.png?raw=true)

Ejemplo de uso:
```Kotlin
AboutApp(
    activity = this@MainActivity,
    backgroundColor = getColorCompat(R.color.colorBackground),
    iconsColor = getColorCompat(R.color.colorIcon),
    appIcon = R.drawable.library_logo,
    appName = R.string.about_app_app_name,
    appVersionName = BuildConfig.VERSION_NAME,
    authorName = R.string.about_app_author_name,
    authorLink = R.string.about_app_author_link,
    companyLogo = R.drawable.logo_jedemm_com,
    companyName = R.string.about_app_company_name,
    companyLink = R.string.about_app_company_link,
    termsAndPrivacyPolicyLink = R.string.about_app_terms_and_policy_link,
    termsAndPrivacyPolicyTextColor = getColorCompat(R.color.colorTermsAndPrivacyPolicyText),
    showOpenSourceLicenses = true,
    taskDescriptionTitle = R.string.app_name,
    taskDescriptionIcon = R.mipmap.ic_launcher,
    taskDescriptionColor = getColorCompat(R.color.colorBackground)
).show()
```
**[Documentación ](docs/androidutils/com.jeovanimartinez.androidutils.about/-about-app/index.md)**

#### Términos de Licencia y Política de Privacidad
La actividad muestra los términos y la política en un `WebView`, que se cargan desde una URL (`termsAndPrivacyPolicyLink`) para poder mostrar siempre la versión más actualizada de los mismos. Con el fin de mostrar la página web con el estilo del tema que usa la app, se envían dos parámetros a la URL ` background-color` y ` text-color` para personalizar el color de fondo de la página y el color del texto respectivamente.
<br><br>
La siguiente plantilla para la página es la que se usa en el ejemplo, y ya se encarga de procesar los parámetros y ajustar la vista: 

**[Plantilla](resources/terms-and-privacy-policy)**<br>
**[Demostración](https://jedemm.com/android-utils/terms-and-policy/license.html?background-color=212121&text-color=CFCFCF)**

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

### TempFiles  
Utilidad para crear y eliminar fácilmente archivos temporales de la app. Los archivos temporales se crean en una carpeta privada de la aplicación llamada `androidutils_tempfiles`

#### - clearTempFilesFolder
Elimina todos los archivos creados con esta utilidad (los archivos de la carpeta temporal), se recomienda llamar al inicio de la app o donde se necesite limpiar el contenido de la carpeta. La función se ejecuta de manera asíncrona para no afectar el flujo de la app y puede llamarse desde cualquier lugar.

Ejemplo de uso:
```Kotlin
TempFiles.clearTempFilesFolder(this@MainActivity)
```

#### - [Lista de utilidades](docs/androidutils/com.jeovanimartinez.androidutils.filesystem.tempfiles/-temp-files/index.md)

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

#### Activity **[[ Documentación ]](docs/androidutils/com.jeovanimartinez.androidutils.extensions.activity/index.md)**

- TaskDescription.kt: Contiene extensiones para configurar fácilmente el TaskDescription de las actividades.

#### Basic Types **[[ Documentación ]](docs/androidutils/com.jeovanimartinez.androidutils.extensions.basictypes/index.md)**

- Float.kt: Extensiones para el tipo de dato Float.

#### Context **[[ Documentación ]](docs/androidutils/com.jeovanimartinez.androidutils.extensions.context/index.md)**

- Resources.kt: Conjunto de extensiones para trabajar con los recursos dentro del context.
- Toast.kt: Extensiones para mostrar rápidamente toast de corta y larga duración.
- TypeOrResource.kt: Conjunto de extensiones para tratar un valor como un tipo de dato, o bien obtener el valor de los recursos.

#### Dimension **[[ Documentación ]](docs/androidutils/com.jeovanimartinez.androidutils.extensions.dimension/index.md)**

- Dimensions.kt: Extensiones disponibles para cualquier vista y cualquier contexto, permiten convertir fácilmente dp o sp a px y viceversa.

#### Nullability **[[ Documentación ]](docs/androidutils/com.jeovanimartinez.androidutils.extensions.nullability/index.md)**

- NullSafety.kt: Extensiones para trabajar más fácilmente con valores nulos y con la seguridad contra nulos.

#### View **[[ Documentación ]](docs/androidutils/com.jeovanimartinez.androidutils.extensions.view/index.md)**

- Animations.kt: Extensiones para facilitar el uso de animaciones en las vistas.
