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
| [firebaseAnalytics](firebase-analytics.md) | Instancia de Firebase Analytics para registro de eventos, asignar solo si se requiere`var firebaseAnalytics: FirebaseAnalytics?` |
| [LOG_TAG](-l-o-g_-t-a-g.md) | Etiqueta par el log`abstract val LOG_TAG: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [isLogEnable](is-log-enable.md) | Devuelve el estado actual del log, true si esta habilitado, false de lo contrario`fun isLogEnable(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setFirebaseAnalyticsInstance](set-firebase-analytics-instance.md) | Asigna la instancia de [firebaseAnalytics](set-firebase-analytics-instance.md#com.jeovanimartinez.androidutils.Base$setFirebaseAnalyticsInstance(com.google.firebase.analytics.FirebaseAnalytics)/firebaseAnalytics) para registro de eventos`fun setFirebaseAnalyticsInstance(firebaseAnalytics: FirebaseAnalytics): T` |
| [setLogEnable](set-log-enable.md) | Habilita o deshabilita los mensajes del log de depuración en base a [logEnable](set-log-enable.md#com.jeovanimartinez.androidutils.Base$setLogEnable(kotlin.Boolean)/logEnable)`fun setLogEnable(logEnable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): T` |

### Inheritors

| Name | Summary |
|---|---|
| [AboutApp](../../com.jeovanimartinez.androidutils.about/-about-app/index.md) | Utilidad para mostrar una pantalla de acerca de la aplicación`object AboutApp : `[`Base`](./index.md)`<`[`AboutApp`](../../com.jeovanimartinez.androidutils.about/-about-app/index.md)`>` |
| [MoreAppsGPlay](../../com.jeovanimartinez.androidutils.moreapps/-more-apps-g-play/index.md) | Utilidad para dirigir al usuario a Google Play, específicamente a la lista de aplicaciones del desarrollador. El uso principal es para invitar al usuario a que instale otras aplicaciones del desarrollador.`object MoreAppsGPlay : `[`Base`](./index.md)`<`[`MoreAppsGPlay`](../../com.jeovanimartinez.androidutils.moreapps/-more-apps-g-play/index.md)`>` |
| [RateApp](../../com.jeovanimartinez.androidutils.reviews/-rate-app/index.md) | Utilidad para dirigir al usuario a Google Play, específicamente a los detalles de la aplicación. El uso principal es para invitar al usuario a que califique la aplicación, se abren los detalles generales de la app, ya que no hay manera de mandarlo directamente a calificar la app con este método.`object RateApp : `[`Base`](./index.md)`<`[`RateApp`](../../com.jeovanimartinez.androidutils.reviews/-rate-app/index.md)`>` |
| [RateInApp](../../com.jeovanimartinez.androidutils.reviews.rateinapp/-rate-in-app/index.md) | Utilidad para iniciar el flujo que invita al usuario a calificar la aplicación, en base a ciertas condiciones sobre el uso de la aplicación. Para Android 5.0 (API 21) y posteriores, se utiliza Google Play In-App Review API, por lo que se puede calificar dentro de la aplicación. Para versiones anteriores a Android 5.0, se muestra un diálogo para invitar al usuario a calificar la aplicación, si el usuario acepta, es dirigido a los detalles de la aplicación en Google Play.`object RateInApp : `[`Base`](./index.md)`<`[`RateInApp`](../../com.jeovanimartinez.androidutils.reviews.rateinapp/-rate-in-app/index.md)`>` |
