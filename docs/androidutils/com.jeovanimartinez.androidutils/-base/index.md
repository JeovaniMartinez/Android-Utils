[androidutils](../../index.md) / [com.jeovanimartinez.androidutils](../index.md) / [Base](./index.md)

# Base

`abstract class Base<T : `[`Base`](./index.md)`<T>>`

Clase base con propiedades y funciones comunes

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Clase base con propiedades y funciones comunes`Base()` |

### Properties

| Name | Summary |
|---|---|
| [firebaseAnalyticsEnabled](firebase-analytics-enabled.md) | Determina si el registro de eventos en Firebase Analytics esta habilitado para la instancia de la clase, para el registro de eventos, la propiedad estática del companion object firebaseAnalyticsInstance también debe estar asignada, si esa propiedad es null, no se van a registrar eventos, ya que es requerida`var firebaseAnalyticsEnabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [LOG_TAG](-l-o-g_-t-a-g.md) | Etiqueta par el log`abstract val LOG_TAG: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [firebaseAnalyticsInstance](firebase-analytics-instance.md) | Instancia de Firebase Analytics, asignar solo si se requiere que la app que implementa la biblioteca registre los eventos de análisis, esta propiedad es global y se usa en todas las subclases de esta clase. Si se desea desactivar el registro de eventos para una clase en específico, hacerlo mediante la propiedad firebaseAnalyticsEnabled de la instancia de la clase`var firebaseAnalyticsInstance: FirebaseAnalytics?` |
| [logEnable](log-enable.md) | Para habilitar o deshabilitar los mensajes del log de depuración, de manera predeterminada es según BuildConfig.DEBUG, esta configuración aplica para todas las clases que hereden de Base, es decir se activa o desactiva de manera global`var logEnable: <ERROR CLASS>` |

### Inheritors

| Name | Summary |
|---|---|
| [AboutApp](../../com.jeovanimartinez.androidutils.about/-about-app/index.md) | Utilidad para mostrar una pantalla de acerca de la aplicación`object AboutApp : `[`Base`](./index.md)`<`[`AboutApp`](../../com.jeovanimartinez.androidutils.about/-about-app/index.md)`>` |
| [MoreAppsGPlay](../../com.jeovanimartinez.androidutils.moreapps/-more-apps-g-play/index.md) | Utilidad para dirigir al usuario a Google Play, específicamente a la lista de aplicaciones del desarrollador. El uso principal es para invitar al usuario a que instale otras aplicaciones del desarrollador.`object MoreAppsGPlay : `[`Base`](./index.md)`<`[`MoreAppsGPlay`](../../com.jeovanimartinez.androidutils.moreapps/-more-apps-g-play/index.md)`>` |
| [RateApp](../../com.jeovanimartinez.androidutils.reviews/-rate-app/index.md) | Utilidad para dirigir al usuario a Google Play, específicamente a los detalles de la aplicación. El uso principal es para invitar al usuario a que califique la aplicación, se abren los detalles generales de la app, ya que no hay manera de mandarlo directamente a calificar la app con este método.`object RateApp : `[`Base`](./index.md)`<`[`RateApp`](../../com.jeovanimartinez.androidutils.reviews/-rate-app/index.md)`>` |
| [RateInApp](../../com.jeovanimartinez.androidutils.reviews.rateinapp/-rate-in-app/index.md) | Utilidad para iniciar el flujo que invita al usuario a calificar la aplicación, en base a ciertas condiciones sobre el uso de la aplicación. Para Android 5.0 (API 21) y posteriores, se utiliza Google Play In-App Review API, por lo que se puede calificar dentro de la aplicación. Para versiones anteriores a Android 5.0, se muestra un diálogo para invitar al usuario a calificar la aplicación, si el usuario acepta, es dirigido a los detalles de la aplicación en Google Play.`object RateInApp : `[`Base`](./index.md)`<`[`RateInApp`](../../com.jeovanimartinez.androidutils.reviews.rateinapp/-rate-in-app/index.md)`>` |
| [SystemWebBrowser](../../com.jeovanimartinez.androidutils.web/-system-web-browser/index.md) | Utilidad para interactuar con el navegador web del sistema`object SystemWebBrowser : `[`Base`](./index.md)`<`[`SystemWebBrowser`](../../com.jeovanimartinez.androidutils.web/-system-web-browser/index.md)`>` |
