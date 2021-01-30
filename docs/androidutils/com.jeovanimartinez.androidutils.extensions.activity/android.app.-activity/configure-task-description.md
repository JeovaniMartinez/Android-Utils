[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.extensions.activity](../index.md) / [android.app.Activity](index.md) / [configureTaskDescription](./configure-task-description.md)

# configureTaskDescription

`fun Activity.configureTaskDescription(@StringRes title: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, @DrawableRes icon: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, @ColorRes color: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Configura la TaskDescription de la actividad, funciona desde Android 5 en adelante y ajusta la configuración
automáticamente para las diferentes versiones de Android

### Parameters

`title` - título para la descripción (id del recurso). Debe ser una imagen PNG, si el id del recurso es un SVG, no se va a aplicar
    y se va a mostrar el ícono de la app.

`icon` - ícono a mostrar (id del recurso)

`color` - color de fondo de la barra (id del recurso)