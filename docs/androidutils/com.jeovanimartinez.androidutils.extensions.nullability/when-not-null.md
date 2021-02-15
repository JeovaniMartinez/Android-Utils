//[androidutils](../index.md)/[com.jeovanimartinez.androidutils.extensions.nullability](index.md)/[whenNotNull](when-not-null.md)



# whenNotNull  
[androidJvm]  
Content  
fun <[T](when-not-null.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)> [T](when-not-null.md)?.[whenNotNull](when-not-null.md)(f: ([T](when-not-null.md)) -> [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))  
More info  


Ejecuta el código del bloque solo si la variable no es null. Dentro de la función, siempre usar it para hacer referencia al valor de la variable. Por ejemplo:

demo.whenNotNull {  
    // Usar siempre it en lugar del nombre de la variable, ya que la variable puede ser null, it nunca lo va a ser si entra al bloque  
    Log.d("Check Nullability", it)  
}

Funciona de igual manera si se llama con el operador de llamada segura (?) o sin el (demo.whenNotNull es equivalente a demo?.whenNotNull). Para efectos prácticos, se recomienda llamar sin el aperador de llamada segura.

  



