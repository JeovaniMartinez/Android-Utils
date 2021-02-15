//[androidutils](../index.md)/[com.jeovanimartinez.androidutils.extensions.view](index.md)/[onAnimationEnd](on-animation-end.md)



# onAnimationEnd  
[androidJvm]  
Content  
inline fun [ViewPropertyAnimator](https://developer.android.com/reference/kotlin/android/view/ViewPropertyAnimator.html).[onAnimationEnd](on-animation-end.md)(crossinline continuation: ([Animator](https://developer.android.com/reference/kotlin/android/animation/Animator.html)) -> [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))  
More info  


Callback para cuando finaliza la animación. Es importante llamar siempre a esta función cuando se modifique la animación de una vista, incluso si no se requiere hacer nada cuando finalize la animación, esto con el fin de reemplazar el callback existente y evitar que se ejecute el código de otro callback de una animación ejecutada anteriormente en la vista.

  



