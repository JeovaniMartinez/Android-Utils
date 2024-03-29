package com.jeovanimartinez.androidutils.lintcheck

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.jeovanimartinez.androidutils.lintcheck.annotations.TypeOrResource

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
}
