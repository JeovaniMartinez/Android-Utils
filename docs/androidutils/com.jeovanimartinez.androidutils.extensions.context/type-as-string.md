//[androidutils](../index.md)/[com.jeovanimartinez.androidutils.extensions.context](index.md)/[typeAsString](type-as-string.md)



# typeAsString  
[androidJvm]  
Content  
fun [Context](https://developer.android.com/reference/kotlin/android/content/Context.html).[typeAsString](type-as-string.md)(@[StringOrStringRes](../com.jeovanimartinez.androidutils.annotations/-string-or-string-res/index.md)()stringOrStringRes: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  
More info  


Analiza el tipo de dato recibido stringOrStringRes y devuelve siempre un string.

<ul><li>Si el tipo de dato es string lo devuelve tal cual, sin procesar.</li><li>Si el tipo de dato es char, lo devuelve como string.</li><li>Si el tipo de dato es entero, se asume que es un ID de recurso, por lo que se obtiene y devuelve el texto, en caso de no existir el recurso, lanza una excepción.</li><li>Para cualquier otro tipo de dato lanza una excepción, ya que el dato no se puede tratar directamente como string.</li></ul>  



