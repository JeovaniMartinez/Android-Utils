[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.moreapps](../index.md) / [MoreAppsGPlay](./index.md)

# MoreAppsGPlay

`object MoreAppsGPlay`

Utilidad para dirigir al usuario a Google Play, específicamente a la lista de aplicaciones del desarrollador
El uso principal es para invitar al usuario a que instale otras aplicaciones del desarrollador

### Properties

| Name | Summary |
|---|---|
| [developerId](developer-id.md) | ID del desarrollador`var developerId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [logEnable](log-enable.md) | Para habilitar o deshabilitar los mensajes del log`var logEnable: <ERROR CLASS>` |

### Functions

| Name | Summary |
|---|---|
| [showAppList](show-app-list.md) | Dirige al usuario a la lista de aplicaciones del desarrollador, en base a su ID (developerId). Si no es posible, muestra un toast con un mensaje.`fun showAppList(activity: Activity, firebaseAnalytics: FirebaseAnalytics? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
