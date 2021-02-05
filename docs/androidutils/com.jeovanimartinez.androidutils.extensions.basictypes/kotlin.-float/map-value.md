[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.extensions.basictypes](../index.md) / [kotlin.Float](index.md) / [mapValue](./map-value.md)

# mapValue

`fun `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`.mapValue(inMin: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, inMax: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, outMin: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`, outMax: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)`): `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)

Mapea el valor de un rango a otro y devuelve el resultado

### Parameters

`inMin` - valor mínimo de la entrada

`inMax` - valor maximo de la entrada

`outMin` - valor mínimo de la salida

`outMin` - valor máximo de la salida
Por ejemplo, inMin = 0, inMax = 1, outMin = 0, outMin = 255 - En este caso,
el valor de entrada debe estar entre 0 y 1, y se va a mapear a una salida
entre 0 y 255. Si el valor de entrada fuese 0.5 la salida es 127.5. En este
mismo ejemplo un valor de 2 daria como resultado 510, ya que es doble según
los valores establecidos.