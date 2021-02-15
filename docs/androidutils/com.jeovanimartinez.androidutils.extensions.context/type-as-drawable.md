//[androidutils](../index.md)/[com.jeovanimartinez.androidutils.extensions.context](index.md)/[typeAsDrawable](type-as-drawable.md)



# typeAsDrawable  
[androidJvm]  
Content  
fun [Context](https://developer.android.com/reference/kotlin/android/content/Context.html).[typeAsDrawable](type-as-drawable.md)(@[DrawableOrDrawableRes](../com.jeovanimartinez.androidutils.annotations/-drawable-or-drawable-res/index.md)()drawableOrDrawableRes: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Drawable](https://developer.android.com/reference/kotlin/android/graphics/drawable/Drawable.html)?  
More info  


Analiza el tipo de dato recibido drawableOrDrawableRes y devuelve siempre un Drawable.

<ul><li>Si el tipo de dato es Drawable lo devuelve tal cual, sin procesar.</li><li>Si el tipo de dato es entero, se asume que es un ID de recurso, por lo que se obtiene y devuelve el Drawable, en caso de no existir el recurso, lanza una excepción.</li><li>Para cualquier otro tipo de dato lanza una excepción, ya que el dato no se puede tratar directamente como Drawable.</li></ul>

El valor de retorno puede ser null de acuerdo a la definición de ContextCompat.getDrawable()

  



