package com.jeovanimartinez.androidutils.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jeovanimartinez.androidutils.app.R
import com.jeovanimartinez.androidutils.app.databinding.ActivityBaseBinding
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat

/** Base activity for a new utility */
class BaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Edge-to-edge configuration
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBars = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            val navBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            view.setPadding(0, statusBars.top, 0, navBars.bottom)
            insets
        }

        configureTaskDescription(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.md_theme_background))

        binding.appBar.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.appBar.tvTitle.text = getString(R.string.app_name)

    }

}
