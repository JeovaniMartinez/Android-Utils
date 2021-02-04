[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.extensions.nullability](../index.md) / [kotlin.Any](index.md) / [isNotNull](./is-not-null.md)

# isNotNull

`fun `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?.isNotNull(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Verifica si la variable es null. Si la variable es null devuelve false, si no es null devuelve true.
Invocar siempre sin el operador de llamada segura (?).
Ejemplos:
    val a: String? = null // a.isNotNull() = false;
    val b: String? = "demo" // b.isNotNull() = true;

