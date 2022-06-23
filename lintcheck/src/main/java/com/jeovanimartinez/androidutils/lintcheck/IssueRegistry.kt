package com.jeovanimartinez.androidutils.lintcheck

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.jeovanimartinez.androidutils.lintcheck.annotations.TypeOrResource

// Custom Lint Rules Example: https://github.com/googlesamples/android-custom-lint-rules

/**
 * Register the class for the lint check.
 *
 * This class must be referenced on \resources\META-INF\services
 * */
@Suppress("UnstableApiUsage")
class IssueRegistry : IssueRegistry() {
    override val issues = listOf(TypeOrResource.ISSUE)

    override val api: Int
        get() = CURRENT_API

    override val vendor: Vendor = Vendor(
        vendorName = "Android Utils Library",
        feedbackUrl = "https://github.com/JeovaniMartinez/Android-Utils/issues",
        contact = "https://github.com/JeovaniMartinez/Android-Utils"
    )
}
