[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.about](../index.md) / [AboutApp](./index.md)

# AboutApp

`object AboutApp : `[`Base`](../../com.jeovanimartinez.androidutils/-base/index.md)`<`[`AboutApp`](./index.md)`>`

Utilidad para mostrar una pantalla de acerca de la aplicación

### Properties

| Name | Summary |
|---|---|
| [appIcon](app-icon.md) | Ícono o logo de la aplicación`var appIcon: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [appName](app-name.md) | Nombre de la aplicación`var appName: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [appVersionName](app-version-name.md) | Version de la aplicación`var appVersionName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [authorLink](author-link.md) | URL que se abre al dar clic al nombre del autor, null para no abrir ningún enlace`var authorLink: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [authorName](author-name.md) | Nombre del autor`var authorName: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [backgroundColor](background-color.md) | Color de fondo de la actividad, si no se asigna se usa el color de fondo del tema`var backgroundColor: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [companyLink](company-link.md) | URL que se abre al dar clic al logo de la empresa, null para no abrir ningún enlace`var companyLink: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [companyLogo](company-logo.md) | Logo de la empresa`var companyLogo: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [companyName](company-name.md) | Nombre de la empresa`var companyName: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [iconsColor](icons-color.md) | Color para los iconos`var iconsColor: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [LOG_TAG](-l-o-g_-t-a-g.md) | Etiqueta par el log`val LOG_TAG: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [showOpenSourceLicenses](show-open-source-licenses.md) | Determina si se muestra un botón para ver las licencias de código abierto de la app. En caso de ser afirmativo, el build.gradle del proyecto debe tener la dependencia 'com.google.android.gms:oss-licenses-plugin:VERSION' y el build.gradle del proyecto debe implementar el plugin 'com.google.android.gms.oss-licenses-plugin', tal como se muestra en la documentación oficial: https://developers.google.com/android/guides/opensource`var showOpenSourceLicenses: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [taskDescriptionColor](task-description-color.md) | Color para la Task Description de la actividad. Se usa en conjunto con taskDescriptionTitle y taskDescriptionIcon y ninguna propiedad debe ser null para que se configure el Task Description.`var taskDescriptionColor: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [taskDescriptionIcon](task-description-icon.md) | Icono para la Task Description de la actividad. Se usa en conjunto con taskDescriptionTitle y taskDescriptionColor y ninguna propiedad debe ser null para que se configure el Task Description.`var taskDescriptionIcon: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [taskDescriptionTitle](task-description-title.md) | Título para la Task Description de la actividad. Se usa en conjunto con taskDescriptionIcon y taskDescriptionColor y ninguna propiedad debe ser null para que se configure el Task Description.`var taskDescriptionTitle: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [termsAndPrivacyPolicyLink](terms-and-privacy-policy-link.md) | URL de los términos de uso y política de privacidad, dejar en null si la app no se desea mostrarlos`var termsAndPrivacyPolicyLink: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` |
| [termsAndPrivacyPolicyTextColor](terms-and-privacy-policy-text-color.md) | Color del texto de los términos de uso y política de privacidad, si no se define, se usa el color predeterminado`var termsAndPrivacyPolicyTextColor: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| Name | Summary |
|---|---|
| [show](show.md) | Muestra la actividad de acerca de, requiere la [activity](show.md#com.jeovanimartinez.androidutils.about.AboutApp$show(android.app.Activity)/activity) para poder iniciar la actividad AboutActivity`fun show(activity: Activity): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
