## Checklist for v1.0.0

* Solo este archivo esta en español.

* Lista de verificación de partes de la biblioteca que ya se terminaron de desarrollar.

* Hasta este punto, lo que aparece en este archivo ya esta listo, solo van a quedar pendientes las pruebas en diversas versiones de Android,
  ya que por el momento se desarrolla y prueba en un celular físico con Android 8.1.0.

* En el caso de las dependencias de Gradle, para la versión 1.0.0 de la biblioteca ya no actualizar ninguna (a menos que se especifique lo contrario) 
  ya que las que se tienen ya se van a probar en el desarrollo, y para evitar poribles conflictos ya dejarlas como están, ya que se actualizaron
  el 04/06/2023.

* ✔✔ Indica que el archivo/utilidad ya esta lista.

* ✔⚠⚠ Indica que el archivo/utilidad ya casi esta lista, solo requiere pequeños ajustes que se van a realizar después ya que dependen de otra cosa,
  y abajo se agrega una anotación indicando lo que está pendiende de ajustar.

***************************************************************************************************************************************************

- Verificar que las nuevas versiones de analytics y crashlytics funciones correctamente
- Verificar que la documentación se genere correctamente, ajustar si es requerido

***************************************************************************************************************************************************

Gradle Scripts (Todo terminado, solo los 2 pequeños ajustes de billingclient pendientes)
  > build.gradle (Project: Android-Utils) ✔✔
  > build.gradle (Module :androidutils) ✔⚠⚠
    - Actualizar la dependencia 'com.android.billingclient:billing-ktx' cuando se actualice la utilidad
  > build.gradle (Module :app) ✔⚠⚠
    - Actualizar la dependencia 'com.android.billingclient:billing-ktx' cuando se actualice la utilidad
  > build.gradle (Module :lintcheck) ✔✔
  > consumer-rules.pro ✔✔
  > proguard-rules.pro ✔✔
  > proguard-rules.pro ✔✔
  > gradle.properties ✔✔
  > gradle-wrapper.properties ✔✔
  > local.properties ✔✔
  > settings.gradle ✔✔

***************************************************************************************************************************************************

lintcheck [Módulo] ✔✔ (Todo terminado, módulo listo)
  > main ✔✔
    > java ✔✔
      > com.jeovanimartinez.androidutils.lintcheck.annotations ✔✔
        > annotations ✔✔
          > TypeOrResource ✔✔
        > IssueRegistry ✔✔
    > resources ✔✔
      > META-INF ✔✔
        > services ✔✔
          > com.android.tools.lint.client.api.IssueRegistry ✔✔
    
***************************************************************************************************************************************************

app [Módulo]
  > java
    > annotations
      > CodeInspectionAnnotations.kt ✔✔

***************************************************************************************************************************************************
