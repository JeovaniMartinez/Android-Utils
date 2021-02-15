package com.jeovanimartinez.androidutils.about

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Explode
import android.view.View
import android.view.Window
import android.webkit.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat
import com.jeovanimartinez.androidutils.extensions.context.shortToast
import com.jeovanimartinez.androidutils.extensions.dimension.dp2px
import com.jeovanimartinez.androidutils.extensions.nullability.isNotNull
import com.jeovanimartinez.androidutils.extensions.nullability.isNull
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull
import com.jeovanimartinez.androidutils.extensions.view.onAnimationEnd
import com.jeovanimartinez.androidutils.themes.translucent.TranslucentActivity
import com.jeovanimartinez.androidutils.web.SystemWebBrowser
import kotlinx.android.synthetic.main.activity_about.*
import java.util.*


/** Actividad de acerca de */
class AboutActivity : TranslucentActivity() {

    companion object {
        const val STATE_TERMS_AND_POLICY_VISIBLE = "state_terms_and_policy_visible"
    }

    private var activityIsRunning = true // Para mostrar toast solo si la actividad esta en ejecución
    private var loadingTermsAndPolicyInProgress = false
    private var termsAndPolicyVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.activityOpacity = 0.9f
        super.onCreate(savedInstanceState)
        configureTransitions()
        setContentView(R.layout.activity_about)

        AboutApp.log("Started AboutActivity")

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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_TERMS_AND_POLICY_VISIBLE, termsAndPolicyVisible) // Se guarda si están visibles, para el onRestoreInstanceState
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Se muestran los términos y política si estaban visibles
        if (savedInstanceState.getBoolean(STATE_TERMS_AND_POLICY_VISIBLE)) {
            loadTermsAndPolicy(false)
        }
    }

    /** Configuración inicial */
    private fun initSetup() {

        // Se configura el task description si es necesario
        if (AboutApp.taskDescriptionTitle.isNotNull() && AboutApp.taskDescriptionIcon.isNotNull() && AboutApp.taskDescriptionColor.isNotNull()) {
            configureTaskDescription(AboutApp.taskDescriptionTitle!!, AboutApp.taskDescriptionIcon!!, getColorCompat(AboutApp.taskDescriptionColor!!))
            AboutApp.log("AboutActivity task description configured by AboutApp.TaskDescription properties")
        } else {
            AboutApp.log(
                "Is not necessary configure task description " +
                        "[title = ${AboutApp.taskDescriptionTitle}; icon = ${AboutApp.taskDescriptionIcon}; color = ${AboutApp.taskDescriptionColor}]"
            )
        }

        about_progressBar.visibility = View.GONE
        about_termsAndPolicyWebView.visibility = View.GONE
        about_topActionCard.visibility = View.GONE

        // Se ajusta para ocultar el elemento
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            about_topActionCard.translationX = dp2px(48).toFloat()
        } else {
            about_topActionCard.translationY = dp2px(48).toFloat()
        }

        about_termsAndPolicy.setOnClickListener {
            loadTermsAndPolicy()
            about_termsAndPolicy.isClickable = false // Se deshabilita el clic, para evitar acciones repetidas
        }

        about_closeTermsBtn.setOnClickListener {
            hideTermsAndPolicy()
        }

        about_openSourceLicenses.setOnClickListener {
            startActivity(Intent(this@AboutActivity, OssLicensesMenuActivity::class.java))
            AboutApp.firebaseAnalytics("about_app_open_source_licenses_shown")
        }

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
        if (AboutApp.termsAndPrivacyPolicyLink.isNull()) {
            about_termsAndPolicy.visibility = View.GONE
            val openSourceLParams = about_openSourceLicenses.layoutParams as ConstraintLayout.LayoutParams
            openSourceLParams.startToStart = ConstraintLayout.LayoutParams.MATCH_PARENT
            openSourceLParams.endToStart = ConstraintLayout.LayoutParams.UNSET
            about_openSourceLicenses.layoutParams = openSourceLParams
        }

        about_openSourceLicenses.visibility = if (AboutApp.showOpenSourceLicenses) View.VISIBLE else View.GONE

        // En versiones anteriores a Android 4.4 se oculta siempre, ya que la actividad no funciona correctamente
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            about_openSourceLicenses.visibility = View.GONE
        }

        val color = ContextCompat.getColor(this@AboutActivity, AboutApp.backgroundColor)
        about_topActionCard.setCardBackgroundColor(color)
        about_contentCard.setCardBackgroundColor(color)

        // Se asigna el color de los iconos, solo funciona de Android 5 en adelante
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val iconsColor = ContextCompat.getColor(this@AboutActivity, AboutApp.iconsColor)
            val closeDrawable = ContextCompat.getDrawable(this@AboutActivity, R.drawable.ic_check_circle_outline)
            val closeTermsDrawable = ContextCompat.getDrawable(this@AboutActivity, R.drawable.ic_back)
            closeDrawable?.setTint(iconsColor)
            closeTermsDrawable?.setTint(iconsColor)
            about_closeBtn.setImageDrawable(closeDrawable)
            about_closeTermsBtn.setImageDrawable(closeTermsDrawable)
        }

        AboutApp.authorLink.whenNotNull { link ->
            about_authorName.setOnClickListener {
                SystemWebBrowser.openUrl(this@AboutActivity, getString(link), "about_app_author_link")
            }
        }

        AboutApp.companyLink.whenNotNull { link ->
            about_companyLogo.setOnClickListener {
                SystemWebBrowser.openUrl(this@AboutActivity, getString(link), "about_app_company_link")
            }
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
                enterTransition = Explode()
                exitTransition = Explode()
            }
        }
    }

    /**
     * Inicia el proceso para obtener los términos y la política de privacidad en un el web view, ya que se obtienen desde el servidor para mostrar la
     * versión más reciente
     * @param animateShowTermsView determina si cuando se muestre la vista de los términos se va a hacer con una animación, esta vista solo se muestra
     *        si se obtuvieron los datos correctamente del servidor
     * */
    private fun loadTermsAndPolicy(animateShowTermsView: Boolean = true) {

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
        val textColor = resources.getString(AboutApp.termsAndPrivacyPolicyTextColor).replace("#", "").substring(2) // Se obtiene como texto y se ajusta el color


        // Se genera un objeto WebViewClient para los eventos
        about_termsAndPolicyWebView.webViewClient = object : WebViewClient() {

            // Cuando finaliza la carga de la página (independientemente de si el resultado fue exitoso o no)
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                AboutApp.log("WebViewClient onPageFinished() invoked")

                view?.scrollTo(0, 0) // Se posiciona en el top de la vista

                // Se espera un momento para evitar acciones repetidas
                Handler(Looper.getMainLooper()).postDelayed({
                    loadingTermsAndPolicyInProgress = false
                }, 500)

                about_progressBar.visibility = View.GONE // Se oculta al terminar la petición
                if (pageLoadSuccessful) showTermsAndPolicy(animateShowTermsView) // Solo se muestran si la página se cargo correctamente

                about_termsAndPolicy.isClickable = true // Se habilita nuevamente
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
        about_termsAndPolicyWebView.settings.domStorageEnabled = true // Se habilita para mejor compatibilidad
        // Se carga la URL, pasando los parámetros de color de fondo y color de texto
        AboutApp.termsAndPrivacyPolicyLink.whenNotNull {
            about_termsAndPolicyWebView.loadUrl("${getString(it)}?background-color=$backgroundColor&text-color=$textColor")
        }
    }

    /** Muestra los términos y la política de privacidad, llamar solo si se cargaron correctamente, [animate] determina si se muestran con una animación o no */
    private fun showTermsAndPolicy(animate: Boolean = true) {

        AboutApp.log("showTermsAndPolicy() Invoked")
        termsAndPolicyVisible = true // Indica que están visibles
        super.activityOpacity = 0.95f

        if (animate) {

            // Visibilidad y animación del botón de atrás en los términos
            about_topActionCard.visibility = View.VISIBLE
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                about_topActionCard.animate().translationX(0f).onAnimationEnd { }
            } else {
                about_topActionCard.animate().translationY(0f).onAnimationEnd { }
            }

            about_termsAndPolicyWebView.visibility = View.VISIBLE

            // Se manda al final de la vista para mostrar una animación
            about_termsAndPolicyWebView.translationX = about_contentCard.width.toFloat()
            about_termsAndPolicyWebView.visibility = View.VISIBLE

            // Se muestra el web view y se oculta el contenedor de la tarjeta mediante una animación
            about_contentCardLayout.animate().translationX(-about_contentCard.width.toFloat()).start()
            about_termsAndPolicyWebView.animate().translationX(0f).start()

        } else {

            // Visibilidad y animación del botón de atrás en los términos
            about_topActionCard.visibility = View.VISIBLE
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                about_topActionCard.translationX = 0f
            } else {
                about_topActionCard.translationY = 0f
            }

            about_termsAndPolicyWebView.visibility = View.VISIBLE

            // Se manda al final de la vista para la animación al salir de la vista
            about_termsAndPolicyWebView.translationX = about_contentCard.width.toFloat()
            about_termsAndPolicyWebView.visibility = View.VISIBLE

            // Se muestra el web view y se oculta el contenedor de la tarjeta
            about_contentCardLayout.translationX = -about_contentCard.width.toFloat()
            about_termsAndPolicyWebView.translationX = 0f

        }

        AboutApp.firebaseAnalytics("about_app_terms_policy_shown")

    }

    /** Oculta la vista de los términos y la política de privacidad */
    private fun hideTermsAndPolicy() {
        AboutApp.log("hideTermsAndPolicy() Invoked")
        termsAndPolicyVisible = false // Se indica que ya no están visibles
        super.activityOpacity = 0.9f // Se restaura al valor inicial

        // Visibilidad y animación del botón de atrás en los términos
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            about_topActionCard.animate().translationX(dp2px(48).toFloat()).onAnimationEnd { about_topActionCard.visibility = View.GONE }
        } else {
            about_topActionCard.animate().translationY(dp2px(48).toFloat()).onAnimationEnd { about_topActionCard.visibility = View.GONE }
        }

        // Se muestra el contenedor de la tarjeta y se oculta el web view mediante una animación
        about_contentCardLayout.animate().translationX(0f).start()
        about_termsAndPolicyWebView.animate().translationX(about_contentCard.width.toFloat()).start()
    }

}
