package com.jeovanimartinez.androidutils.about

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Explode
import android.view.View
import android.view.Window
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.about.config.AboutAppConfig
import com.jeovanimartinez.androidutils.analytics.Event
import com.jeovanimartinez.androidutils.databinding.ActivityAboutBinding
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat
import com.jeovanimartinez.androidutils.extensions.context.getDrawableCompat
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
internal class AboutActivity : TranslucentActivity() {

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
    private var loadingTermsAndPolicy = false
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

        backButtonSetup()
        generalSetup()
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

    /** Set the activity transitions */
    private fun configureTransitions() {
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Explode()
            exitTransition = Explode()
        }
    }

    /** Back button setup */
    private fun backButtonSetup() {
        // Reference: https://medium.com/tech-takeaways/how-to-migrate-the-deprecated-onbackpressed-function-e66bb29fa2fd
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (termsAndPolicyVisible) return hideTermsAndPolicy()
                supportFinishAfterTransition()
            }
        })
    }

    /** Initial and general setup */
    private fun generalSetup() {

        // Configure the task description (if it is necessary)
        aboutAppConfig.taskDescriptionConfig.whenNotNull {
            configureTaskDescription(it)
        }

        // Adjust elements visibility
        binding.pbLoadingTermsAndPolicy.visibility = View.GONE
        binding.webViewTermsAndPolicy.visibility = View.GONE
        binding.cardTopAction.visibility = View.GONE

        // Adjusts to hide the element and show it properly with animations
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.cardTopAction.translationX = dp2px(48).toFloat()
        } else {
            binding.cardTopAction.translationY = dp2px(48).toFloat()
        }

        // IMPORTANT: Due to the use of Material3 which uses dynamic colors, in some occasions the applied colors may vary slightly from those specified in the colors files.

        // Configure the colors for the activity (background color, text color, icons color, etc.)
        binding.cardTopAction.setCardBackgroundColor(getColorCompat(R.color.about_app_background_color))
        binding.cardContent.setCardBackgroundColor(getColorCompat(R.color.about_app_background_color))
        binding.layoutRoot.changeAllTextViewsTextColor(getColorCompat(R.color.about_app_text_color))
        val closeDrawable = getDrawableCompat(R.drawable.about_app_ic_check)
        val closeTermsDrawable = getDrawableCompat(R.drawable.about_app_ic_back)
        closeDrawable?.setTint(getColorCompat(R.color.about_app_icons_color))
        closeTermsDrawable?.setTint(getColorCompat(R.color.about_app_icons_color))
        binding.btnClose.setImageDrawable(closeDrawable)
        binding.btnCloseTerms.setImageDrawable(closeTermsDrawable)
        binding.pbLoadingTermsAndPolicy.indeterminateTintList = ColorStateList.valueOf(getColorCompat(R.color.about_app_primary_color))

        // App data
        binding.ivAppLogo.setImageDrawable(typeAsDrawable(aboutAppConfig.appLogo))
        binding.tvAppName.text = typeAsString(aboutAppConfig.appName)
        binding.tvAppVersion.text = getString(R.string.about_app_version, aboutAppConfig.appVersion)

        // Credits
        binding.tvCredits.text = typeAsString(aboutAppConfig.creditsText)
        aboutAppConfig.creditsUrl.whenNotNull { url ->
            binding.tvCredits.setOnClickListener {
                SystemWebBrowser.openUrl(this@AboutActivity, typeAsString(url), "about_app_credits_url")
            }
        }

        // Company data and copyright
        binding.ivAuthorLogo.setImageDrawable(typeAsDrawable(aboutAppConfig.authorLogo))
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        binding.tvCopyright.text = getString(R.string.about_app_copyright, currentYear.toString(), typeAsString(aboutAppConfig.copyrightHolderName))
        aboutAppConfig.authorUrl.whenNotNull { url ->
            binding.ivAuthorLogo.setOnClickListener {
                SystemWebBrowser.openUrl(this@AboutActivity, typeAsString(url), "about_app_author_url")
            }
        }

        // Terms and privacy policy
        if (aboutAppConfig.termsAndPrivacyPolicyUrl == null) {
            binding.btnTermsAndPolicy.visibility = View.GONE
            // Adjust btnOpenSourceLicenses to be displayed correctly
            val btnOpenSourceLicensesParams = binding.btnOpenSourceLicenses.layoutParams as ConstraintLayout.LayoutParams
            btnOpenSourceLicensesParams.startToStart = ConstraintLayout.LayoutParams.MATCH_PARENT
            btnOpenSourceLicensesParams.endToStart = ConstraintLayout.LayoutParams.UNSET
            binding.btnOpenSourceLicenses.layoutParams = btnOpenSourceLicensesParams
        } else {
            binding.btnTermsAndPolicy.setOnClickListener {
                loadTermsAndPolicy()
                binding.btnTermsAndPolicy.isClickable = false // Click is disabled, to avoid repeated actions
            }
            binding.btnCloseTerms.setOnClickListener {
                hideTermsAndPolicy()
            }
        }

        // Open source licenses
        binding.btnOpenSourceLicenses.visibility = if (aboutAppConfig.showOpenSourceLicenses) View.VISIBLE else View.GONE
        binding.btnOpenSourceLicenses.setOnClickListener {
            startActivity(Intent(this@AboutActivity, OssLicensesMenuActivity::class.java))
            AboutApp.firebaseAnalytics(Event.ABOUT_APP_OSL_SHOWN)
        }

        // Close button
        binding.btnClose.setOnClickListener {
            supportFinishAfterTransition()
        }
    }

    /**
     * Start the process to get the terms and privacy policy in a web view.
     * @param animateShowTermsView Determines if when the terms view is shown it will be done with animation,
     *        this view is only shown if the data was obtained correctly from the server.
     * */
    private fun loadTermsAndPolicy(animateShowTermsView: Boolean = true) {

        // If are already loading, it is not necessary to do it again
        if (loadingTermsAndPolicy) {
            AboutApp.log("loadTermsAndPolicy() is already in progress")
            return
        }

        AboutApp.log("loadTermsAndPolicy() Invoked")

        loadingTermsAndPolicy = true
        binding.pbLoadingTermsAndPolicy.visibility = View.VISIBLE // The progress bar is displayed, as it may take time

        var pageLoadSuccessful = true // Helper to know if the page was loaded successfully, it only changes to false if some error occurs

        // Get the background color and the text color to send the data to the server and obtain the view adapted to the theme (the substring removes the alpha since it is not required)
        val backgroundColor = Integer.toHexString(getColorCompat(R.color.about_app_background_color)).substring(2).uppercase(Locale.ROOT)
        val textColor = Integer.toHexString(getColorCompat(R.color.about_app_text_color)).substring(2).uppercase(Locale.ROOT)

        // Configure the web view
        @SuppressLint("SetJavaScriptEnabled")
        binding.webViewTermsAndPolicy.settings.javaScriptEnabled = true // Javascript is enabled as it is necessary for the page style to be configured with the URL parameters
        binding.webViewTermsAndPolicy.settings.domStorageEnabled = true // For best compatibility

        // The URL is loaded, passing the background color and text color parameters
        aboutAppConfig.termsAndPrivacyPolicyUrl.whenNotNull {
            val url = "${typeAsString(it)}?background-color=$backgroundColor&text-color=$textColor&lang=${Locale.getDefault().language}"
            AboutApp.log("Terms and Privacy Policy URL = $url")
            binding.webViewTermsAndPolicy.loadUrl(url)
        }

        // Generate WebViewClient for listening events
        binding.webViewTermsAndPolicy.webViewClient = object : WebViewClient() {

            // When the page load finished (regardless of whether the result was successful or not)
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                AboutApp.log("WebViewClient onPageFinished() invoked")

                view?.scrollTo(0, 0) // Go to view's top

                // Wait a moment to avoid repeated actions
                Handler(Looper.getMainLooper()).postDelayed({
                    loadingTermsAndPolicy = false
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
                binding.cardTopAction.animate().translationX(0f)
            } else {
                binding.cardTopAction.animate().translationY(0f)
            }

            binding.webViewTermsAndPolicy.visibility = View.VISIBLE

            // Sent at the end of the view to show an animation
            binding.webViewTermsAndPolicy.translationX = binding.cardContent.width.toFloat()
            binding.webViewTermsAndPolicy.visibility = View.VISIBLE

            // The web view is shown and the card container is hidden by means of an animation
            binding.layoutContent.animate().translationX(-binding.cardContent.width.toFloat()).start()
            binding.webViewTermsAndPolicy.animate().translationX(0f).start()

        } else {

            // Set back button visibility
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
