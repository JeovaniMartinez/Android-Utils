//[androidutils](../../index.md)/[com.jeovanimartinez.androidutils.themes.translucent](../index.md)/[TranslucentActivity](index.md)/[configureWindowDim](configure-window-dim.md)



# configureWindowDim  
[androidJvm]  
Content  
fun [configureWindowDim](configure-window-dim.md)(window: [Window](https://developer.android.com/reference/kotlin/android/view/Window.html)?)  
More info  


Configura el dim (atenuación) de una ventana (independiente a esta clase). Invocar a esta función cuando se vaya a mostrar una ventana encima de la actividad, para asegurarse que la atenuación de la ventana a mostrar sea igual o mayor que la opacidad de la actividad, ya que si no se hace se genera un efecto visual indeseable.

Por ejemplo, para un diálogo:  
 val dialog = MaterialAlertDialogBuilder(this@AboutActivity).setTitle("DEMO").show();  
 configureWindowDim(dialog.window);

## Parameters  
  
androidJvm  
  
|  Name|  Summary| 
|---|---|
| <a name="com.jeovanimartinez.androidutils.themes.translucent/TranslucentActivity/configureWindowDim/#android.view.Window?/PointingToDeclaration/"></a>window| <a name="com.jeovanimartinez.androidutils.themes.translucent/TranslucentActivity/configureWindowDim/#android.view.Window?/PointingToDeclaration/"></a><br><br>ventana de la que se va a configurar el dim.<br><br>
  
  



