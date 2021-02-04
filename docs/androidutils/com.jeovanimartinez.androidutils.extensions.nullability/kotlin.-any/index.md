[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.extensions.nullability](../index.md) / [kotlin.Any](./index.md)

### Extensions for kotlin.Any

| Name | Summary |
|---|---|
| [isNotNull](is-not-null.md) | Verifica si la variable es null. Si la variable es null devuelve false, si no es null devuelve true. Invocar siempre sin el operador de llamada segura (?). Ejemplos:     val a: String? = null // a.isNotNull() = false;     val b: String? = "demo" // b.isNotNull() = true;`fun `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?.isNotNull(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isNull](is-null.md) | Verifica si la variable es null. Devuelve true si la variable es null, y false si la variable no es null. Invocar siempre sin el operador de llamada segura (?). Ejemplos:     val a: String? = null // a.isNull() = true;     val b: String? = "demo" // b.isNull() = false;`fun `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?.isNull(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
