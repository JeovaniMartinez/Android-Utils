[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.extensions.view](../index.md) / [android.view.ViewPropertyAnimator](./index.md)

### Extensions for android.view.ViewPropertyAnimator

| Name | Summary |
|---|---|
| [onAnimationEnd](on-animation-end.md) | Callback para cuando finaliza la animación. Es importante llamar siempre a esta función cuando se modifique la animación de una vista, incluso si no se requiere hacer nada cuando finalize la animación, esto con el fin de reemplazar el callback existente y evitar que se ejecute el código de otro callback de una animación ejecutada anteriormente en la vista.`fun ViewPropertyAnimator.onAnimationEnd(continuation: (Animator) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
