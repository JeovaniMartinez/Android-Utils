[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.themes.translucent](../index.md) / [TranslucentActivity](index.md) / [configureWindowDim](./configure-window-dim.md)

# configureWindowDim

`fun configureWindowDim(window: Window?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Configura el dim (atenuación) de una ventana (independiente a esta clase)

### Parameters

`window` - ventana de la que se va a configurar el dim
Invocar a esta función cuando se vaya a mostrar una ventana encima de la actividad, para asegurarse que la atenuación
de la ventana a mostrar sea igual o mayor que la opacidad de la actividad, ya que si no se hace se genera
un efecto visual indeseable.
Por ejemplo, para un diálogo:
val dialog = MaterialAlertDialogBuilder(this@AboutActivity).setTitle("DEMO").show();
configureWindowDim(dialog.window);