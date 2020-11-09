[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.reviews](../index.md) / [RateApp](index.md) / [rateInGooglePlay](./rate-in-google-play.md)

# rateInGooglePlay

`fun rateInGooglePlay(activity: Activity, firebaseAnalytics: FirebaseAnalytics? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Dirige al usuario a los detalles de la aplicación en Google Play para que pueda calificar la aplicación.
Si es posible, se abre la aplicación directamente en la app de Google Play, en caso de no ser posible, se abre
en el navegador, si tampoco es posible, muestra un toast con un mensaje.
Referencia: https://stackoverflow.com/questions/10816757/rate-this-app-link-in-google-play-store-app-on-the-phone

### Parameters

`activity` - actividad desde donde se llama, se usa para iniciar otras actividades

`firebaseAnalytics` - instancia de FirebaseAnalytics por si se desean registrar los eventos, dejar en null si no se requiere