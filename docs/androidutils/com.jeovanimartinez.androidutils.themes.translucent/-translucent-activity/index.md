[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.themes.translucent](../index.md) / [TranslucentActivity](./index.md)

# TranslucentActivity

`open class TranslucentActivity : AppCompatActivity`

Clase base para actividades con fondo translúcido.
Las actividades que hereden de esta clase deben tener el siguiente tema en el manifest: android:theme="@style/AndroidUtilsTheme.Translucent"
En el diseño de las actividades que hereden de esta clase deben tener un RelativeLayout como elemento raíz en el diseño para que ocupen el espacio completo, o bien un tamaño fijo.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | Clase base para actividades con fondo translúcido. Las actividades que hereden de esta clase deben tener el siguiente tema en el manifest: android:theme="@style/AndroidUtilsTheme.Translucent" En el diseño de las actividades que hereden de esta clase deben tener un RelativeLayout como elemento raíz en el diseño para que ocupen el espacio completo, o bien un tamaño fijo.`TranslucentActivity()` |

### Properties

| Name | Summary |
|---|---|
| [activityOpacity](activity-opacity.md) | Opacidad de la actividad, el valor debe estar en el rango de 0 a 1. Donde 0 es completamente transparente y 1 es completamente opaca. Puede cambiarse en cualquier momento durante la ejecución de la actividad, y la opacidad se vera reflejada inmediatamente. Se recomienda asignar inicialmente antes de llamar al super.onCreate(savedInstanceState) en el onCreate() de la actividad que herede de esta clase.`var activityOpacity: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |

### Functions

| Name | Summary |
|---|---|
| [configureWindowDim](configure-window-dim.md) | Configura el dim (atenuación) de la ventana`fun configureWindowDim(window: Window?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onCreate](on-create.md) | Cuando se crea la actividad, para asignar los valores iniciales`open fun onCreate(savedInstanceState: Bundle?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onPause](on-pause.md) | Al pausar la actividad, se agrega un fondo a la actividad para obtener una mejor vista en las animaciones`open fun onPause(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onResume](on-resume.md) | Al reanudar la actividad, se reasigna el fondo transparente, de modo que se mantenga la opacidad en base al valor de dimAmount`open fun onResume(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [AboutActivity](../../com.jeovanimartinez.androidutils.about/-about-activity/index.md) | Actividad de acerca de`class AboutActivity : `[`TranslucentActivity`](./index.md) |
| [RateAppActivity](../../com.jeovanimartinez.androidutils.reviews.rateinapp/-rate-app-activity/index.md) | Actividad que se muestra en forma de diálogo para invitar al usuario a calificar la aplicación`class RateAppActivity : `[`TranslucentActivity`](./index.md) |
