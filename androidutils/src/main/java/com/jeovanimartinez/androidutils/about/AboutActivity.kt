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
import com.jeovanimartinez.androidutils.analytics.Event
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.shortToast
import com.jeovanimartinez.androidutils.extensions.context.typeAsDrawable
import com.jeovanimartinez.androidutils.extensions.context.typeAsString
import com.jeovanimartinez.androidutils.extensions.dimension.dp2px
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull
import com.jeovanimartinez.androidutils.extensions.view.onAnimationEnd
import com.jeovanimartinez.androidutils.themes.translucent.TranslucentActivity
import com.jeovanimartinez.androidutils.web.SystemWebBrowser
import kotlinx.android.synthetic.main.activity_about.*
import java.util.*


/** About app activity */
class AboutActivity : TranslucentActivity() {

    companion object {
        /*
        * Helper to allow only one instance of this activity. It is used this way and not with android:launchMode="singleInstance" as
        * due to the transparent theme and transition, using that setting does not work properly.
        * */
        var aboutActivityRunning = false
        private const val STATE_TERMS_AND_POLICY_VISIBLE = "state_terms_and_policy_visible"
    }

    private var activityIsRunning = true // To show toast only if the activity is running and not paused
    private var loadingTermsAndPolicyInProgress = false
    private var termsAndPolicyVisible = false
    private lateinit var aboutAppConfig: AboutAppConfig // Config data

    override fun onCreate(savedInstanceState: Bundle?) {
        aboutActivityRunning = true // Indicates that the activity is running
        super.activityOpacity = 0.9f
        super.onCreate(savedInstanceState)
        configureTransitions()
        setContentView(R.layout.activity_about)

        aboutAppConfig = AboutApp.currentConfig!! // The config object is assigned to be able to use it

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

    override fun onDestroy() {
        AboutApp.log("Activity onDestroy(), isFinishing: $isFinishing")
        /*
        * If it is ending, the AboutApp.currentConfig object is set to null to free it from memory, as it is not required and when
        * the activity is shown again, the object is assigned before it is displayed.
        * */
        if (isFinishing) {
            AboutApp.currentConfig = null
            aboutActivityRunning = false // Indicates the end of the activity
        }
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_TERMS_AND_POLICY_VISIBLE, termsAndPolicyVisible) // Save if the terms are visible, for the onRestoreInstanceState
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Terms and policy are shown if they are visible on onSaveInstanceState
        if (savedInstanceState.getBoolean(STATE_TERMS_AND_POLICY_VISIBLE)) {
            loadTermsAndPolicy(false)
        }
    }

    /** Initial setup */
    private fun initSetup() {

        // Configure the task description (if it is necessary)
        aboutAppConfig.taskDescriptionConfig.whenNotNull {
            configureTaskDescription(it)
        }

        about_progressBar.visibility = View.GONE
        about_termsAndPolicyWebView.visibility = View.GONE
        about_topActionCard.visibility = View.GONE

        // Adjusts to hide item
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            about_topActionCard.translationX = dp2px(48).toFloat()
        } else {
            about_topActionCard.translationY = dp2px(48).toFloat()
        }

        about_termsAndPolicy.setOnClickListener {
            loadTermsAndPolicy()
            about_termsAndPolicy.isClickable = false // Click is disabled, to avoid repeated actions
        }

        about_closeTermsBtn.setOnClickListener {
            hideTermsAndPolicy()
        }

        about_openSourceLicenses.setOnClickListener {
            startActivity(Intent(this@AboutActivity, OssLicensesMenuActivity::class.java))
            AboutApp.firebaseAnalytics(Event.ABOUT_APP_OPEN_SOURCE_LICENSES_SHOWN)
        }

        about_closeBtn.setOnClickListener {
            supportFinishAfterTransition()
        }

    }

    /** Configure the activity in base of AboutApp  */
    private fun configureByAboutApp() {

        about_appIcon.setImageDrawable(typeAsDrawable(aboutAppConfig.appIcon))
        about_appName.text = typeAsString(aboutAppConfig.appName)
        about_authorName.text = typeAsString(aboutAppConfig.authorName)
        about_companyLogo.setImageDrawable(typeAsDrawable(aboutAppConfig.companyLogo))
        if (aboutAppConfig.termsAndPrivacyPolicyLink == null) {
            about_termsAndPolicy.visibility = View.GONE
            val openSourceLParams = about_openSourceLicenses.layoutParams as ConstraintLayout.LayoutParams
            openSourceLParams.startToStart = ConstraintLayout.LayoutParams.MATCH_PARENT
            openSourceLParams.endToStart = ConstraintLayout.LayoutParams.UNSET
            about_openSourceLicenses.layoutParams = openSourceLParams
        }

        about_openSourceLicenses.visibility = if (aboutAppConfig.showOpenSourceLicenses) View.VISIBLE else View.GONE

        // In versions prior to Android 4.4 it is always hidden, since the activity does not work correctly
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            about_openSourceLicenses.visibility = View.GONE
        }

        about_topActionCard.setCardBackgroundColor(aboutAppConfig.backgroundColor!!)
        about_contentCard.setCardBackgroundColor(aboutAppConfig.backgroundColor!!)

        // Icon color is assigned, only works from Android 5 onwards
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val closeDrawable = ContextCompat.getDrawable(this@AboutActivity, R.drawable.ic_check_circle_outline)
            val closeTermsDrawable = ContextCompat.getDrawable(this@AboutActivity, R.drawable.ic_back)
            closeDrawable?.setTint(aboutAppConfig.iconsColor!!)
            closeTermsDrawable?.setTint(aboutAppConfig.iconsColor!!)
            about_closeBtn.setImageDrawable(closeDrawable)
            about_closeTermsBtn.setImageDrawable(closeTermsDrawable)
        }

        aboutAppConfig.authorLink.whenNotNull { link ->
            about_authorName.setOnClickListener {
                SystemWebBrowser.openUrl(this@AboutActivity, typeAsString(link), "about_app_author_link")
            }
        }

        aboutAppConfig.companyLink.whenNotNull { link ->
            about_companyLogo.setOnClickListener {
                SystemWebBrowser.openUrl(this@AboutActivity, typeAsString(link), "about_app_company_link")
            }
        }

    }

    /** Set the app version and copyright */
    private fun configureData() {
        about_appVersion.text = getString(R.string.about_app_version, aboutAppConfig.appVersionName)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        about_copyright.text = getString(R.string.about_app_copyright, currentYear.toString(), typeAsString(aboutAppConfig.companyName))
    }

    /** Set the activity transitions */
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
     * Start the process to get the terms and privacy policy in a web view.
     * @param animateShowTermsView Determines if when the terms view is shown it will be done with an animation,
     *        this view is only shown if the data was obtained correctly from the server.
     * */
    private fun loadTermsAndPolicy(animateShowTermsView: Boolean = true) {

        // If are already loading, it is not necessary to do it again
        if (loadingTermsAndPolicyInProgress) {
            AboutApp.log("loadTermsAndPolicy() is already in progress")
            return
        }

        AboutApp.log("loadTermsAndPolicy() Invoked")

        loadingTermsAndPolicyInProgress = true
        about_progressBar.visibility = View.VISIBLE // The progress bar is displayed, as it may take time

        var pageLoadSuccessful = true // Helper to know if the page was loaded successfully, it only changes to false if some error occurs

        // Get te background color and the text color to send the data to the server and obtain the view adapted to the theme (the substring removes the alpha since it is not required)
        val backgroundColor = Integer.toHexString(about_contentCard.cardBackgroundColor.defaultColor).substring(2).toUpperCase(Locale.ROOT)
        val textColor = Integer.toHexString(aboutAppConfig.termsAndPrivacyPolicyTextColor!!).substring(2).toUpperCase(Locale.ROOT)


        // Generate WebViewClient for listening events
        about_termsAndPolicyWebView.webViewClient = object : WebViewClient() {

            // When the page load finished (regardless of whether the result was successful or not)
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                AboutApp.log("WebViewClient onPageFinished() invoked")

                view?.scrollTo(0, 0) // Go to view's top

                // Wait a moment to avoid repeated actions
                Handler(Looper.getMainLooper()).postDelayed({
                    loadingTermsAndPolicyInProgress = false
                }, 500)

                about_progressBar.visibility = View.GONE
                if (pageLoadSuccessful) showTermsAndPolicy(animateShowTermsView) // They are only shown if the page loaded correctly

                about_termsAndPolicy.isClickable = true // It is enabled again
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
        about_termsAndPolicyWebView.settings.javaScriptEnabled = true // Javascript is enabled as it is necessary for the page style to be configured with the URL parameters
        about_termsAndPolicyWebView.settings.domStorageEnabled = true // For best compatibility
        // The URL is loaded, passing the background color and text color parameters
        aboutAppConfig.termsAndPrivacyPolicyLink.whenNotNull {
            about_termsAndPolicyWebView.loadUrl("${typeAsString(it)}?background-color=$backgroundColor&text-color=$textColor")
        }
    }

    /** Show the terms and privacy policy, call only if they were loaded correctly, [animate] determines if they are shown with an animation or not. */
    private fun showTermsAndPolicy(animate: Boolean = true) {

        AboutApp.log("showTermsAndPolicy() Invoked")
        termsAndPolicyVisible = true
        super.activityOpacity = 0.95f

        if (animate) {

            // Set back button visibility and animation
            about_topActionCard.visibility = View.VISIBLE
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                about_topActionCard.animate().translationX(0f).onAnimationEnd { }
            } else {
                about_topActionCard.animate().translationY(0f).onAnimationEnd { }
            }

            about_termsAndPolicyWebView.visibility = View.VISIBLE

            // Sent at the end of the view to show an animation
            about_termsAndPolicyWebView.translationX = about_contentCard.width.toFloat()
            about_termsAndPolicyWebView.visibility = View.VISIBLE

            // The web view is shown and the card container is hidden by means of an animation
            about_contentCardLayout.animate().translationX(-about_contentCard.width.toFloat()).start()
            about_termsAndPolicyWebView.animate().translationX(0f).start()

        } else {

            // Set back button visibility and animation
            about_topActionCard.visibility = View.VISIBLE
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                about_topActionCard.translationX = 0f
            } else {
                about_topActionCard.translationY = 0f
            }

            about_termsAndPolicyWebView.visibility = View.VISIBLE

            // Sent to end of view for animation when exiting view
            about_termsAndPolicyWebView.translationX = about_contentCard.width.toFloat()
            about_termsAndPolicyWebView.visibility = View.VISIBLE

            // The web view is shown and the card container is hidden
            about_contentCardLayout.translationX = -about_contentCard.width.toFloat()
            about_termsAndPolicyWebView.translationX = 0f

        }

        AboutApp.firebaseAnalytics(Event.ABOUT_APP_TERMS_POLICY_SHOWN)

    }

    /** Hide view of terms and privacy policy. */
    private fun hideTermsAndPolicy() {
        AboutApp.log("hideTermsAndPolicy() Invoked")
        termsAndPolicyVisible = false
        super.activityOpacity = 0.9f

        // Set back button visibility and animation
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            about_topActionCard.animate().translationX(dp2px(48).toFloat()).onAnimationEnd { about_topActionCard.visibility = View.GONE }
        } else {
            about_topActionCard.animate().translationY(dp2px(48).toFloat()).onAnimationEnd { about_topActionCard.visibility = View.GONE }
        }

        // The card container is shown and the web view is hidden by means of an animation
        about_contentCardLayout.animate().translationX(0f).start()
        about_termsAndPolicyWebView.animate().translationX(about_contentCard.width.toFloat()).start()
    }

}
