[androidutils](../index.md) / [com.jeovanimartinez.androidutils.extensions.nullability](./index.md)

## Package com.jeovanimartinez.androidutils.extensions.nullability

### Extensions for External Classes

| Name | Summary |
|---|---|
| [kotlin.Any](kotlin.-any/index.md) |  |

### Functions

| Name | Summary |
|---|---|
| [whenNotNull](when-not-null.md) | Ejecuta el código del bloque solo si la variable no es null. Dentro de la función, siempre usar it para hacer referencia al valor de la variable. Por ejemplo: demo.whenNotNull {     // Usar siempre it en lugar del nombre de la variable, ya que la variable puede ser null, it nunca lo va a ser si entra al bloque     Log.d("Check Nullability", it) } Funciona de igual manera si se llama con el operador de llamada segura (?) o sin el (demo.whenNotNull es equivalente a demo?.whenNotNull). Para efectos prácticos, se recomienda llamar sin el aperador de llamada segura.`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> T?.whenNotNull(f: (it: T) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
