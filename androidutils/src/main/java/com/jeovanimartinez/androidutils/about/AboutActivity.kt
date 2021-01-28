package com.jeovanimartinez.androidutils.about

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Fade
import android.view.View
import android.view.Window
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.extensions.context.shortToast
import kotlinx.android.synthetic.main.activity_about.*
import java.util.*


/** Actividad de acerca de */
class AboutActivity : AppCompatActivity() {

    var activityIsRunning = true // Para mostrar toast solo si la actividad esta en ejecución
    var loadingTermsAndPolicyInProgress = false
    var termsAndPolicyVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureTransitions()
        setContentView(R.layout.activity_about)

        AboutApp.log("Started About Activity")

        initSetup()
        configureByAboutApp()
        configureData()

    }

    override fun onBackPressed() {
        if (termsAndPolicyVisible) return hideTermsAndPolicy()
        super.onBackPressed()
    }

    override fun onResume() {
        activityIsRunning = true
        super.onResume()
    }

    override fun onStop() {
        activityIsRunning = false
        super.onStop()
    }

    /** Configuración inicial */
    private fun initSetup() {

        about_progressBar.visibility = View.GONE
        about_termsAndPolicyWebView.visibility = View.GONE

        about_closeBtn.setOnClickListener {
            supportFinishAfterTransition()
        }

    }

    /** Configura la actividad en base al objeto AboutApp  */
    private fun configureByAboutApp() {

        about_appIcon.setImageResource(AboutApp.appIcon)
        about_appName.text = getString(AboutApp.appName)
        about_authorName.text = getString(AboutApp.authorName)
        about_companyLogo.setImageResource(AboutApp.companyLogo)

        about_openSourceLicenses.visibility = if (AboutApp.showOpenSourceLicenses) View.VISIBLE else View.GONE

        about_openSourceLicenses.setOnClickListener {
            startActivity(Intent(this@AboutActivity, OssLicensesMenuActivity::class.java))
        }

        about_termsAndPolicy.setOnClickListener {
            loadTermsAndPolicy()
        }

    }

    /** Configura la versión de la app y el copyright */
    private fun configureData() {

        about_appVersion.text = getString(R.string.about_app_version, AboutApp.appVersionName)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        about_copyright.text = getString(R.string.about_app_copyright, currentYear.toString(), getString(AboutApp.companyName))

    }


    /** Configura la transición de entrada y salida */
    private fun configureTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            with(window) {
                requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
                enterTransition = Fade().setDuration(200)
                exitTransition = Fade().setDuration(200)
            }
        }
    }


    /**
     * Inicia el proceso para obtener los términos y la política de privacidad en un el web view, ya que se obtienen desde el servidor para mostrar la
     * versión más reciente
     * */
    private fun loadTermsAndPolicy() {

        // Si ya están en proceso de carga, no es necesario hacerlo nuevamente
        if (loadingTermsAndPolicyInProgress) {
            AboutApp.log("loadTermsAndPolicy() is already in progress")
            return
        }

        AboutApp.log("loadTermsAndPolicy() Invoked")

        loadingTermsAndPolicyInProgress = true // Se indica que se están cargando
        about_progressBar.visibility = View.VISIBLE // Se muestra la barra de progreso, ya que puede demorar un tiempo

        var pageLoadSuccessful = true // Auxiliar para saber si la página se cargo con éxito, solo se cambia a false si se produce algún error

        // Se obtiene el color de fondo y el color del texto para enviar los datos al servidor y obtener la vista adaptada al tema (el substring elimina el alpha ya que no se requiere)
        val backgroundColor = Integer.toHexString(about_contentCard.cardBackgroundColor.defaultColor).substring(2)
        val textColor = Integer.toHexString(about_appName.currentTextColor).substring(2)

        // Se genera un objeto WebViewClient para los eventos
        about_termsAndPolicyWebView.webViewClient = object : WebViewClient() {

            // Cuando finaliza la carga de la página (independientemente de si el resultado fue exitoso o no)
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                AboutApp.log("WebViewClient onPageFinished() invoked")

                // Se espera un momento para evitar acciones repetidas
                Handler(Looper.getMainLooper()).postDelayed({
                    loadingTermsAndPolicyInProgress = false
                }, 500)

                about_progressBar.visibility = View.GONE // Se oculta al terminar la petición
                if (pageLoadSuccessful) showTermsAndPolicy() // Solo se muestran si la página se cargo correctamente
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                pageLoadSuccessful = false
                if (activityIsRunning) shortToast(R.string.about_app_terms_and_policy_network_error)
                AboutApp.log("WebViewClient onReceivedError() invoked")
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                pageLoadSuccessful = false
                if (activityIsRunning) shortToast(R.string.about_app_terms_and_policy_not_available)
                AboutApp.log("WebViewClient onReceivedHttpError() invoked")
                super.onReceivedHttpError(view, request, errorResponse)
            }
        }

        @SuppressLint("SetJavaScriptEnabled")
        about_termsAndPolicyWebView.settings.javaScriptEnabled = true // Se habilita javascript ya que es necesario para que se configure el estilo de la página con los parámetros de la URL
        // Se carga la URL, pasando los parámetros de color de fondo y color de texto
        about_termsAndPolicyWebView.loadUrl("https://s3.amazonaws.com/jedemm.com/RCC_Calculator/license.html?background-color=$backgroundColor&text-color=$textColor")
    }

    /** Muestra los términos y la política de privacidad, llamar solo si se cargaron correctamente */
    private fun showTermsAndPolicy() {

        AboutApp.log("showTermsAndPolicy() Invoked")
        termsAndPolicyVisible = true // Indica que están visibles

        about_termsAndPolicyWebView.visibility = View.VISIBLE

        // Se manda al final de la vista para mostrar una animación
        about_termsAndPolicyWebView.translationX = about_contentCard.width.toFloat()
        about_termsAndPolicyWebView.visibility = View.VISIBLE

        // Se muestra el web view y se oculta el contenedor de la tarjeta mediante una animación
        about_contentCardLayout.animate().translationX(-about_contentCard.width.toFloat()).start()
        about_termsAndPolicyWebView.animate().translationX(0f).start()

    }

    /** Oculta la vista de los términos y la política de privacidad solo */
    private fun hideTermsAndPolicy() {
        AboutApp.log("hideTermsAndPolicy() Invoked")
        termsAndPolicyVisible = false // Se indica que ya no están visibles

        // Se muestra el contenedor de la tarjeta y se oculta el web view mediante una animación
        about_contentCardLayout.animate().translationX(0f).start()
        about_termsAndPolicyWebView.animate().translationX(about_contentCard.width.toFloat()).start()
    }


}
