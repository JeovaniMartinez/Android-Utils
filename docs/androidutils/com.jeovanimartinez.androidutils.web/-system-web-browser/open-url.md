[androidutils](../../index.md) / [com.jeovanimartinez.androidutils.web](../index.md) / [SystemWebBrowser](index.md) / [openUrl](./open-url.md)

# openUrl

`fun openUrl(context: Context, url: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, case: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Abre el navegador web del sistema en la [url](open-url.md#com.jeovanimartinez.androidutils.web.SystemWebBrowser$openUrl(android.content.Context, kotlin.String, kotlin.String)/url) especificada

### Parameters

`context` - contexto para poder lanzar el intent del navegador

`url` - URL a mostrar, debe ser una URL completa, que incluya http o https, de otro modo no va a pasar la validación

`case` - razón por la que se abre la URL en el navegador. Esto aplica solo si Firebase Analytics esta habilitado, cuando se muestra
    una URL en el navegador se registra el evento, este evento contiene un parámetro que ayuda a determinar que sitio web se mostró,
    y se registra el valor de case. Debe tener entre 1 y 100 caracteres.