[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.moreapps](../index.md) / [MoreAppsGPlay](./index.md)

# MoreAppsGPlay

`object MoreAppsGPlay : `[`Base`](../../com.jeovanimartinez.androidutils/-base/index.md)`<`[`MoreAppsGPlay`](./index.md)`>`

Utilidad para dirigir al usuario a Google Play, específicamente a la lista de aplicaciones del desarrollador.
El uso principal es para invitar al usuario a que instale otras aplicaciones del desarrollador.

### Properties

| Name | Summary |
|---|---|
| [LOG_TAG](-l-o-g_-t-a-g.md) | Etiqueta par el log`val LOG_TAG: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [setDeveloperId](set-developer-id.md) | Asigna el ID de desarrollador [developerId](set-developer-id.md#com.jeovanimartinez.androidutils.moreapps.MoreAppsGPlay$setDeveloperId(kotlin.String)/developerId) del cual se va a mostrar la lista de aplicaciones`fun setDeveloperId(developerId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`MoreAppsGPlay`](./index.md) |
| [showAppList](show-app-list.md) | Dirige al usuario a la lista de aplicaciones del desarrollador, en base a su ID (developerId). Si no es posible, muestra un toast con un mensaje.`fun showAppList(activity: Activity): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
