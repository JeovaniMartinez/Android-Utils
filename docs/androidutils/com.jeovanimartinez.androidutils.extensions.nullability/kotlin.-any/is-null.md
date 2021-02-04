[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.extensions.nullability](../index.md) / [kotlin.Any](index.md) / [isNull](./is-null.md)

# isNull

`fun `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?.isNull(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Verifica si la variable es null. Devuelve true si la variable es null, y false si la variable no es null.
Invocar siempre sin el operador de llamada segura (?).
Ejemplos:
    val a: String? = null // a.isNull() = true;
    val b: String? = "demo" // b.isNull() = false;

