[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.reviews.rateinapp](../index.md) / [RateInApp](index.md) / [showAtEvent](./show-at-event.md)

# showAtEvent

`var showAtEvent: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)

Cuando se llama a checkAndShow() se va a mostrar el flujo si se cumplen las condiciones, setShowAtEvent permite modificar a las cuantas veces
que se llama a checkAndShow() se muestre el flujo. Por ejemplo, se desea mostrar el flujo para calificar en el onResume() de la actividad principal,
pero la tercer vez que se llame a ese método, en ese caso, establecer showAtEvent = 3, que hará que se muestre el flujo hasta la tercera vez que se llame
a onResume(). Si de otro modo, showAtEvent fuera 1, el flujo se mostraría en la primera llamada a onResume(). El valor mínimo de showAtEvent es 1

