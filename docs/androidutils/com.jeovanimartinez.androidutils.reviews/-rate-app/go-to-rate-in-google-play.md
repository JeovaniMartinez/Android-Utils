[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.reviews](../index.md) / [RateApp](index.md) / [goToRateInGooglePlay](./go-to-rate-in-google-play.md)

# goToRateInGooglePlay

`fun goToRateInGooglePlay(activity: Activity): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Dirige al usuario a los detalles de la aplicación en Google Play para que pueda calificar la aplicación.
Si es posible, se abre la aplicación directamente en la app de Google Play, en caso de no ser posible, se abre
en el navegador, si tampoco es posible, muestra un toast con un mensaje.

### Parameters

`activity` - actividad desde donde se llama, se usa para iniciar otras actividades