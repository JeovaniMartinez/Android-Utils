## Lista de eventos para Firebase Analytics

### AboutApp
- `about_app_shown` Se muestra la actividad de acerca de. 
- `about_app_open_source_licenses_shown` Se muestran las licencias de código abierto en la actividad de acerca de.
- `about_app_terms_policy_shown` Se muestran los términos de licencia y política de privacidad en la actividad de acerca de.

### MoreAppsGPlay
- `more_apps_sent_to_google_play` Se dirige al usuario a la página del desarrollador, ya sea en el navegador web o en Google Play.
- `more_apps_unable_to_show_dev_page` No es posible dirigir al usuario a la pagina del desarrollador, ya que no se pudo abrir la URL en el navegador web ni en Google Play.

### RateApp
- `rate_app_sent_to_google_play_app` Se dirige al usuario a la app de Google Play para que califique la aplicación.
- `rate_app_sent_to_google_play_web` Se dirige al usuario a la página web de Google Play para que califique la aplicación, ya que no fue posible dirigirlo a la app de Google Play.
- `rate_app_unable_to_show_on_google_play` No se puede dirigir al usuario a la app de Google Play ni a la página web para que califique la aplicación.

### RateInApp

- `rate_app_review_flow_successful` Se pudo obtener correctamente el flujo para invitar al usuario a calificar la app. 
- `rate_app_request_review_flow_error` Se produjo un error al obtener el flujo para invitar al usuario a calificar la app. 
- `rate_app_review_flow_showed` Se mostró el flujo para invitar al usuario a calificar la app (la API de revisiones no proporciona el resultado, pero se puede inferir, aunque no es 100% exacto).
- `rate_app_review_flow_failure` El flujo se pudo obtener (rate_app_review_flow_successful) pero no se pudo mostrar.
- `rate_app_dialog_shown` Se mostró el diálogo para calificar la app, solo aplica en versiones anteriores a Android 5 donde la API de revisiones no está disponible.

La cantidad de veces que se solicito el flujo se puede calcular mediante la suma de rate_app_review_flow_successful, rate_app_request_review_flow_error y rate_app_review_flow_failure.

SystemWebBrowser
- `open_url_in_system_web_browser` Se dirige al usuario a una página web en el navegador del sistema.
  - `open_url_case` Parámetro que indica la razón por la que se dirigió al usuario el navegador, y registra también cuando se produce un error.
