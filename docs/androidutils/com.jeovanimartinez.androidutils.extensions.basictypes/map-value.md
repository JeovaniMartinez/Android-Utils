//[androidutils](../index.md)/[com.jeovanimartinez.androidutils.extensions.basictypes](index.md)/[mapValue](map-value.md)



# mapValue  
[androidJvm]  
Content  
fun [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html).[mapValue](map-value.md)(inMin: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html), inMax: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html), outMin: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html), outMax: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)  
More info  


Mapea el valor de un rango a otro y devuelve el resultado.



## Parameters  
  
androidJvm  
  
|  Name|  Summary| 
|---|---|
| <a name="com.jeovanimartinez.androidutils.extensions.basictypes//mapValue/kotlin.Float#kotlin.Float#kotlin.Float#kotlin.Float#kotlin.Float/PointingToDeclaration/"></a>inMin| <a name="com.jeovanimartinez.androidutils.extensions.basictypes//mapValue/kotlin.Float#kotlin.Float#kotlin.Float#kotlin.Float#kotlin.Float/PointingToDeclaration/"></a><br><br>valor mínimo de la entrada.<br><br>
| <a name="com.jeovanimartinez.androidutils.extensions.basictypes//mapValue/kotlin.Float#kotlin.Float#kotlin.Float#kotlin.Float#kotlin.Float/PointingToDeclaration/"></a>inMax| <a name="com.jeovanimartinez.androidutils.extensions.basictypes//mapValue/kotlin.Float#kotlin.Float#kotlin.Float#kotlin.Float#kotlin.Float/PointingToDeclaration/"></a><br><br>valor maximo de la entrada.<br><br>
| <a name="com.jeovanimartinez.androidutils.extensions.basictypes//mapValue/kotlin.Float#kotlin.Float#kotlin.Float#kotlin.Float#kotlin.Float/PointingToDeclaration/"></a>outMin| <a name="com.jeovanimartinez.androidutils.extensions.basictypes//mapValue/kotlin.Float#kotlin.Float#kotlin.Float#kotlin.Float#kotlin.Float/PointingToDeclaration/"></a><br><br>valor mínimo de la salida.<br><br>
| <a name="com.jeovanimartinez.androidutils.extensions.basictypes//mapValue/kotlin.Float#kotlin.Float#kotlin.Float#kotlin.Float#kotlin.Float/PointingToDeclaration/"></a>outMax| <a name="com.jeovanimartinez.androidutils.extensions.basictypes//mapValue/kotlin.Float#kotlin.Float#kotlin.Float#kotlin.Float#kotlin.Float/PointingToDeclaration/"></a><br><br>valor máximo de la salida.<br><br>Por ejemplo, inMin = 0, inMax = 1, outMin = 0, outMin = 255 - En este caso,  <br>el valor de entrada debe estar entre 0 y 1, y se va a mapear a una salida  <br>entre 0 y 255. Si el valor de entrada fuese 0.5 la salida es 127.5. En este  <br>mismo ejemplo un valor de 2 daria como resultado 510, ya que es doble según  <br>los valores establecidos.
  
  



