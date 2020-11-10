[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.reviews](../index.md) / [RateApp](./index.md)

# RateApp

`object RateApp : `[`Base`](../../com.jeovanimartinez.androidutils/-base/index.md)`<`[`RateApp`](./index.md)`>`

Utilidad para dirigir al usuario a Google Play, específicamente a los detalles de la aplicación.
El uso principal es para invitar al usuario a que califique la aplicación, se abren los detalles generales de la app,
ya que no hay manera de mandarlo directamente a calificar la app con este método.

### Properties

| Name | Summary |
|---|---|
| [LOG_TAG](-l-o-g_-t-a-g.md) | Etiqueta par el log`val LOG_TAG: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [goToRateInGooglePlay](go-to-rate-in-google-play.md) | Dirige al usuario a los detalles de la aplicación en Google Play para que pueda calificar la aplicación. Si es posible, se abre la aplicación directamente en la app de Google Play, en caso de no ser posible, se abre en el navegador, si tampoco es posible, muestra un toast con un mensaje. Referencia: https://stackoverflow.com/questions/10816757/rate-this-app-link-in-google-play-store-app-on-the-phone`fun goToRateInGooglePlay(activity: Activity): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
