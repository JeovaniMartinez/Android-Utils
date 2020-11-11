[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.reviews.rateinapp](../index.md) / [RateInApp](./index.md)

# RateInApp

`object RateInApp : `[`Base`](../../com.jeovanimartinez.androidutils/-base/index.md)`<`[`RateInApp`](./index.md)`>`

Utilidad para iniciar el flujo que invita al usuario a calificar la aplicación, en base a ciertas condiciones sobre el uso de la aplicación.
Para Android 5.0 (API 21) y posteriores, se utiliza Google Play In-App Review API, por lo que se puede calificar dentro de la aplicación.
Para versiones anteriores a Android 5.0, se muestra un diálogo para invitar al usuario a calificar la aplicación, si el usuario acepta, es
dirigido a los detalles de la aplicación en Google Play.

### Properties

| Name | Summary |
|---|---|
| [LOG_TAG](-l-o-g_-t-a-g.md) | Etiqueta par el log`val LOG_TAG: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Functions

| Name | Summary |
|---|---|
| [checkAndShow](check-and-show.md) | Verifica si se cumplen las condiciones para mostrar el flujo para calificar, y muestra el flujo solo si se cumplen las condiciones`fun checkAndShow(activity: Activity): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [init](init.md) | Inicializa y configura la utilidad, debe llamarse siempre solo una vez en la aplicación ya sea en el onCreate() de la app o de la actividad principal`fun init(context: Context): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setMinInstallElapsedDays](set-min-install-elapsed-days.md) | Establece el número mínimo de días requeridos desde que se instalo la app para poder mostrar el flujo, se usa en combinación con minInstallLaunchTimes, y se deben cumplir ambas condiciones para mostrar el flujo, el valor mínimo es 0 (se muestra a partir del dia en que se instalo)`fun setMinInstallElapsedDays(minInstallElapsedDays: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`RateInApp`](./index.md) |
| [setMinInstallLaunchTimes](set-min-install-launch-times.md) | Establece el número mínimo de veces que se debe haber iniciado la app desde que se instalo para poder mostrar el flujo, se usa en combinación con minInstallElapsedDays, y se deben cumplir ambas condiciones para mostrar el flujo, el valor mínimo es 1, se muestra a partir del primer inicio`fun setMinInstallLaunchTimes(minInstallLaunchTimes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`RateInApp`](./index.md) |
| [setMinRemindElapsedDays](set-min-remind-elapsed-days.md) | Número mínimo de días requeridos desde que se mostró el flujo para mostrarlo nuevamente, se usa en combinación con minRemindLaunchTimes, y se deben cumplir ambas condiciones para mostrar el flujo, el valor mínimo es 0 (se muestra a partir de ese mismo dia)`fun setMinRemindElapsedDays(minRemindElapsedDays: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`RateInApp`](./index.md) |
| [setMinRemindLaunchTimes](set-min-remind-launch-times.md) | Número mínimo de veces que se debe haber iniciado la app desde que mostró el flujo para mostrarlo nuevamente, se usa en combinación con minRemindElapsedDays, y se deben cumplir ambas condiciones para mostrar el flujo, el valor mínimo es 1 (se muestra a partir del primer inicio)`fun setMinRemindLaunchTimes(minRemindLaunchTimes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`RateInApp`](./index.md) |
| [setShowAtEvent](set-show-at-event.md) | Cuando se llama a checkAndShow() se va a mostrar el flujo si se cumplen las condiciones, setShowAtEvent permite modificar a las cuantas veces que se llama a checkAndShow() se muestre el flujo. Por ejemplo, se desea mostrar el flujo para calificar en el onResume() de la actividad principal, pero la tercer vez que se llame a ese método, en ese caso, establecer showAtEvent = 3, que hará que se muestre el flujo hasta la tercera vez que se llame a onResume(). Si de otro modo, showAtEvent fuera 1, el flujo se mostraría en la primera llamada a onResume(). El valor mínimo de showAtEvent es 1`fun setShowAtEvent(showAtEvent: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`RateInApp`](./index.md) |
