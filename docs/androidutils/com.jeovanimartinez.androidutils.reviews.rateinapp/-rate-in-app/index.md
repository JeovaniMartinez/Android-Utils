[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.reviews.rateinapp](../index.md) / [RateInApp](./index.md)

# RateInApp

`class RateInApp`

Utilidad para iniciar el flujo que invita al usuario a calificar la aplicación.
Para Android 5.0 (API 21) y posteriores, se utiliza Google Play In-App Review API, por lo que se puede calificar dentro de la aplicación.
Para versiones anteriores a Android 5.0, se muestra un diálogo para invitar al usuario a calificar la aplicación, si el usuario acepta, es
dirigido a los detalles de la aplicación en Google Play.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Utilidad para iniciar el flujo que invita al usuario a calificar la aplicación. Para Android 5.0 (API 21) y posteriores, se utiliza Google Play In-App Review API, por lo que se puede calificar dentro de la aplicación. Para versiones anteriores a Android 5.0, se muestra un diálogo para invitar al usuario a calificar la aplicación, si el usuario acepta, es dirigido a los detalles de la aplicación en Google Play.`RateInApp()` |

### Companion Object Properties

| Name | Summary |
|---|---|
| [logEnable](log-enable.md) | Para habilitar o deshabilitar los mensajes del log`var logEnable: <ERROR CLASS>` |
| [minInstallElapsedDays](min-install-elapsed-days.md) | Número mínimo de días requeridos desde que se instalo la app para poder mostrar el flujo, se usa en combinación con minLaunchTimes, y se deben cumplir ambas condiciones para mostrar el flujo`var minInstallElapsedDays: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [minLaunchTimes](min-launch-times.md) | Número mínimo de veces que se ha iniciado la app desde que se instalo para poder mostrar el flujo, se usa en combinación con minInstallElapsedDays, y se deben cumplir ambas condiciones para mostrar el flujo`var minLaunchTimes: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Companion Object Functions

| Name | Summary |
|---|---|
| [build](build.md) | Método para configurar la utilidad, debe llamarse solo una vez al iniciarse la aplicación`fun build(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
