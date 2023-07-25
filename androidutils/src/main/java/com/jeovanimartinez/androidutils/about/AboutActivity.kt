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
import com.jeovanimartinez.androidutils.databinding.ActivityAboutBinding
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.shortToast
import com.jeovanimartinez.androidutils.extensions.context.typeAsDrawable
import com.jeovanimartinez.androidutils.extensions.context.typeAsString
import com.jeovanimartinez.androidutils.extensions.dimension.dp2px
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull
import com.jeovanimartinez.androidutils.extensions.view.changeAllTextViewsTextColor
import com.jeovanimartinez.androidutils.extensions.view.onAnimationEnd
import com.jeovanimartinez.androidutils.themes.translucent.TranslucentActivity
import com.jeovanimartinez.androidutils.web.SystemWebBrowser
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

    private lateinit var binding: ActivityAboutBinding

    private var activityIsRunning = true // To show toast only if the activity is running and not paused
    private var loadingTermsAndPolicyInProgress = false
    private var termsAndPolicyVisible = false
    private lateinit var aboutAppConfig: AboutAppConfig // Config data

    override fun onCreate(savedInstanceState: Bundle?) {
        aboutActivityRunning = true // Indicates that the activity is running
        super.activityOpacity = 0.9f
        super.onCreate(savedInstanceState)
        configureTransitions()
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.pbLoadingTermsAndPolicy.visibility = View.GONE
        binding.webViewTermsAndPolicy.visibility = View.GONE
        binding.cardTopAction.visibility = View.GONE

        // Adjusts to hide item
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.cardTopAction.translationX = dp2px(48).toFloat()
        } else {
            binding.cardTopAction.translationY = dp2px(48).toFloat()
        }

        binding.btnTermsAndPolicy.setOnClickListener {
            loadTermsAndPolicy()
            binding.btnTermsAndPolicy.isClickable = false // Click is disabled, to avoid repeated actions
        }

        binding.btnCloseTerms.setOnClickListener {
            hideTermsAndPolicy()
        }

        binding.btnOpenSourceLicenses.setOnClickListener {
            startActivity(Intent(this@AboutActivity, OssLicensesMenuActivity::class.java))
            AboutApp.firebaseAnalytics(Event.ABOUT_APP_OSL_SHOWN)
        }

        binding.btnClose.setOnClickListener {
            supportFinishAfterTransition()
        }

    }

    /** Configure the activity based on AboutApp */
    private fun configureByAboutApp() {

        // Configure the colors for the activity (background color, text color and icons color)
        binding.cardTopAction.setCardBackgroundColor(aboutAppConfig.backgroundColor)
        binding.cardContent.setCardBackgroundColor(aboutAppConfig.backgroundColor)
        binding.layoutRoot.changeAllTextViewsTextColor(aboutAppConfig.textColor)
        val closeDrawable = ContextCompat.getDrawable(this@AboutActivity, R.drawable.about_app_ic_check)
        val closeTermsDrawable = ContextCompat.getDrawable(this@AboutActivity, R.drawable.about_app_ic_back)
        closeDrawable?.setTint(aboutAppConfig.iconsColor)
        closeTermsDrawable?.setTint(aboutAppConfig.iconsColor)
        binding.btnClose.setImageDrawable(closeDrawable)
        binding.btnCloseTerms.setImageDrawable(closeTermsDrawable)

        binding.ivAppIcon.setImageDrawable(typeAsDrawable(aboutAppConfig.appIcon))
        binding.tvAppName.text = typeAsString(aboutAppConfig.appName)
        binding.tvAuthor.text = typeAsString(aboutAppConfig.authorName)
        binding.ivCompanyLogo.setImageDrawable(typeAsDrawable(aboutAppConfig.companyLogo))
        if (aboutAppConfig.termsAndPrivacyPolicyLink == null) {
            binding.btnTermsAndPolicy.visibility = View.GONE
            val openSourceLParams = binding.btnOpenSourceLicenses.layoutParams as ConstraintLayout.LayoutParams
            openSourceLParams.startToStart = ConstraintLayout.LayoutParams.MATCH_PARENT
            openSourceLParams.endToStart = ConstraintLayout.LayoutParams.UNSET
            binding.btnOpenSourceLicenses.layoutParams = openSourceLParams
        }

        binding.btnOpenSourceLicenses.visibility = if (aboutAppConfig.showOpenSourceLicenses) View.VISIBLE else View.GONE

        // In versions prior to Android 4.4 it is always hidden, since the activity does not work correctly
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            binding.btnOpenSourceLicenses.visibility = View.GONE
        }

        aboutAppConfig.authorLink.whenNotNull { link ->
            binding.tvAuthor.setOnClickListener {
                SystemWebBrowser.openUrl(this@AboutActivity, typeAsString(link), "about_app_author_link")
            }
        }

        aboutAppConfig.companyLink.whenNotNull { link ->
            binding.ivCompanyLogo.setOnClickListener {
                SystemWebBrowser.openUrl(this@AboutActivity, typeAsString(link), "about_app_company_link")
            }
        }

    }

    /** Set the app version and copyright */
    private fun configureData() {
        binding.tvAppVersion.text = getString(R.string.about_app_version, aboutAppConfig.appVersionName)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        binding.tvCopyright.text = getString(R.string.about_app_copyright, currentYear.toString(), typeAsString(aboutAppConfig.companyName))
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
     * @param animateShowTermsView Determines if when the terms view is shown it will be done with animation,
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
        binding.pbLoadingTermsAndPolicy.visibility = View.VISIBLE // The progress bar is displayed, as it may take time

        var pageLoadSuccessful = true // Helper to know if the page was loaded successfully, it only changes to false if some error occurs

        // Get the background color and the text color to send the data to the server and obtain the view adapted to the theme (the substring removes the alpha since it is not required)
        val backgroundColor = Integer.toHexString(binding.cardContent.cardBackgroundColor.defaultColor).substring(2).toUpperCase(Locale.ROOT)
        val textColor = Integer.toHexString(aboutAppConfig.termsAndPrivacyPolicyTextColor!!).substring(2).toUpperCase(Locale.ROOT)


        // Generate WebViewClient for listening events
        binding.webViewTermsAndPolicy.webViewClient = object : WebViewClient() {

            // When the page load finished (regardless of whether the result was successful or not)
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                AboutApp.log("WebViewClient onPageFinished() invoked")

                view?.scrollTo(0, 0) // Go to view's top

                // Wait a moment to avoid repeated actions
                Handler(Looper.getMainLooper()).postDelayed({
                    loadingTermsAndPolicyInProgress = false
                }, 500)

                binding.pbLoadingTermsAndPolicy.visibility = View.GONE
                if (pageLoadSuccessful) showTermsAndPolicy(animateShowTermsView) // They are only shown if the page loaded correctly

                binding.btnTermsAndPolicy.isClickable = true // It is enabled again
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
        binding.webViewTermsAndPolicy.settings.javaScriptEnabled = true // Javascript is enabled as it is necessary for the page style to be configured with the URL parameters
        binding.webViewTermsAndPolicy.settings.domStorageEnabled = true // For best compatibility
        // The URL is loaded, passing the background color and text color parameters
        aboutAppConfig.termsAndPrivacyPolicyLink.whenNotNull {
            val url = "${typeAsString(it)}?background-color=$backgroundColor&text-color=$textColor&lang=${Locale.getDefault().language}"
            AboutApp.log("Terms and Privacy Policy URL = $url")
            binding.webViewTermsAndPolicy.loadUrl(url)
        }
    }

    /** Show the terms and privacy policy, call only if they were loaded correctly, [animate] determines if they are shown with animation or not. */
    private fun showTermsAndPolicy(animate: Boolean = true) {

        AboutApp.log("showTermsAndPolicy() Invoked")
        termsAndPolicyVisible = true
        super.activityOpacity = 0.95f

        if (animate) {

            // Set back button visibility and animation
            binding.cardTopAction.visibility = View.VISIBLE
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                binding.cardTopAction.animate().translationX(0f).onAnimationEnd { }
            } else {
                binding.cardTopAction.animate().translationY(0f).onAnimationEnd { }
            }

            binding.webViewTermsAndPolicy.visibility = View.VISIBLE

            // Sent at the end of the view to show an animation
            binding.webViewTermsAndPolicy.translationX = binding.cardContent.width.toFloat()
            binding.webViewTermsAndPolicy.visibility = View.VISIBLE

            // The web view is shown and the card container is hidden by means of an animation
            binding.layoutContent.animate().translationX(-binding.cardContent.width.toFloat()).start()
            binding.webViewTermsAndPolicy.animate().translationX(0f).start()

        } else {

            // Set back button visibility and animation
            binding.cardTopAction.visibility = View.VISIBLE
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                binding.cardTopAction.translationX = 0f
            } else {
                binding.cardTopAction.translationY = 0f
            }

            binding.webViewTermsAndPolicy.visibility = View.VISIBLE

            // Sent to end of view for animation when exiting view
            binding.webViewTermsAndPolicy.translationX = binding.cardContent.width.toFloat()
            binding.webViewTermsAndPolicy.visibility = View.VISIBLE

            // The web view is shown and the card container is hidden
            binding.layoutContent.translationX = -binding.cardContent.width.toFloat()
            binding.webViewTermsAndPolicy.translationX = 0f

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
            binding.cardTopAction.animate().translationX(dp2px(48).toFloat()).onAnimationEnd { binding.cardTopAction.visibility = View.GONE }
        } else {
            binding.cardTopAction.animate().translationY(dp2px(48).toFloat()).onAnimationEnd { binding.cardTopAction.visibility = View.GONE }
        }

        // The card container is shown and the web view is hidden by means of an animation
        binding.layoutContent.animate().translationX(0f).start()
        binding.webViewTermsAndPolicy.animate().translationX(binding.cardContent.width.toFloat()).start()
    }

}
