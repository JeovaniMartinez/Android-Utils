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
| [log](log.md) | Muestra el [message](log.md#com.jeovanimartinez.androidutils.Base$log(kotlin.Any, kotlin.Throwable)/message) y el [throwable](log.md#com.jeovanimartinez.androidutils.Base$log(kotlin.Any, kotlin.Throwable)/throwable) en el log de DEPURACIÓN, usado para detallar el flujo de ejecución, se puede habilitar y deshabilitar con setLogEnable`fun log(message: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, throwable: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [loge](loge.md) | Muestra el [message](loge.md#com.jeovanimartinez.androidutils.Base$loge(kotlin.Any, kotlin.Throwable)/message) y el [throwable](loge.md#com.jeovanimartinez.androidutils.Base$loge(kotlin.Any, kotlin.Throwable)/throwable) en el log de ERROR`fun loge(message: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, throwable: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [logw](logw.md) | Muestra el [message](logw.md#com.jeovanimartinez.androidutils.Base$logw(kotlin.Any, kotlin.Throwable)/message) y el [throwable](logw.md#com.jeovanimartinez.androidutils.Base$logw(kotlin.Any, kotlin.Throwable)/throwable) en el log de ADVERTENCIA`fun logw(message: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, throwable: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setFirebaseAnalyticsInstance](set-firebase-analytics-instance.md) | Asigna la instancia de [firebaseAnalytics](set-firebase-analytics-instance.md#com.jeovanimartinez.androidutils.Base$setFirebaseAnalyticsInstance(com.google.firebase.analytics.FirebaseAnalytics)/firebaseAnalytics) para registro de eventos`fun setFirebaseAnalyticsInstance(firebaseAnalytics: FirebaseAnalytics): T` |
| [setLogEnable](set-log-enable.md) | Habilita o deshabilita los mensajes del log de depuración en base a [logEnable](set-log-enable.md#com.jeovanimartinez.androidutils.Base$setLogEnable(kotlin.Boolean)/logEnable)`fun setLogEnable(logEnable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): T` |

### Inheritors

| Name | Summary |
|---|---|
| [MoreAppsGPlay](../../com.jeovanimartinez.androidutils.moreapps/-more-apps-g-play/index.md) | Utilidad para dirigir al usuario a Google Play, específicamente a la lista de aplicaciones del desarrollador. El uso principal es para invitar al usuario a que instale otras aplicaciones del desarrollador.`object MoreAppsGPlay : `[`Base`](./index.md)`<`[`MoreAppsGPlay`](../../com.jeovanimartinez.androidutils.moreapps/-more-apps-g-play/index.md)`>` |
