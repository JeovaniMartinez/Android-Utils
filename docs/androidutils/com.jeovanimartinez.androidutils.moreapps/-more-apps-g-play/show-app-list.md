[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.moreapps](../index.md) / [MoreAppsGPlay](index.md) / [showAppList](./show-app-list.md)

# showAppList

`fun showAppList(activity: Activity, firebaseAnalytics: FirebaseAnalytics? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Dirige al usuario a la lista de aplicaciones del desarrollador, en base a su ID (developerId)
Si no es posible, muestra un toast con un mensaje

### Parameters

`activity` - actividad desde donde se llama, se usa para iniciar otras actividades

`firebaseAnalytics` - instancia de FirebaseAnalytics por si se desean registrar los eventos, dejar en null si no se requiere