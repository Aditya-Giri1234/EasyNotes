package com.aditya.lint_rules

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class MyIssueRegistry : IssueRegistry() {
    override val issues = CustomAnnotationDetector.ISSUES
    override val api: Int = CURRENT_API
}
