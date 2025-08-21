package com.aditya.lint_rules

import com.aditya.lint_rules.annotation.Buggy
import com.aditya.lint_rules.annotation.NeedsImprovement
import com.aditya.lint_rules.annotation.ReviewLater
import com.aditya.lint_rules.annotation.WrongApproach
import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.*

class CustomAnnotationDetector : Detector(), SourceCodeScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> =
        listOf(UMethod::class.java, UClass::class.java, UField::class.java, UCallExpression::class.java)

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {

            // 🔹 Visit function definitions
            override fun visitMethod(node: UMethod) {
                reportIfAnnotated(context, node, node.annotations.mapNotNull { it.qualifiedName })
            }

            // 🔹 Visit class definitions
            override fun visitClass(node: UClass) {
                reportIfAnnotated(context, node, node.annotations.mapNotNull { it.qualifiedName })
            }

            // 🔹 Visit properties/fields
            override fun visitField(node: UField) {
                reportIfAnnotated(context, node, node.annotations.mapNotNull { it.qualifiedName })
            }

            // 🔹 Visit function calls (call-site usage)
            override fun visitCallExpression(node: UCallExpression) {
                val resolved = node.resolve() as? PsiMethod ?: return
                val annotations = resolved.annotations.mapNotNull { it.qualifiedName }
                reportIfAnnotated(context, node, annotations, isUsage = true)
            }
        }
    }

    private fun reportIfAnnotated(
        context: JavaContext,
        node: UElement,
        annotations: List<String>,
        isUsage: Boolean = false
    ) {
        annotations.forEach { fqName ->
            when (fqName) {
                Buggy::class.qualifiedName -> {
                    context.report(
                        BUGGY_ISSUE,
                        node,
                        context.getLocation(node),
                        if (isUsage) "Calling a @Buggy function – must be fixed before release."
                        else "This code is marked as @Buggy – must be fixed before release."
                    )
                }
                NeedsImprovement::class.qualifiedName -> {
                    context.report(
                        NEEDS_IMPROVEMENT_ISSUE,
                        node,
                        context.getLocation(node),
                        if (isUsage) "Using @NeedsImprovement code – consider refactoring."
                        else "Code marked as @NeedsImprovement – consider refactoring."
                    )
                }
                WrongApproach::class.qualifiedName -> {
                    context.report(
                        WRONG_APPROACH_ISSUE,
                        node,
                        context.getLocation(node),
                        if (isUsage) "Using @WrongApproach code – better alternative exists."
                        else "This code is marked as @WrongApproach – fix recommended."
                    )
                }
                ReviewLater::class.qualifiedName -> {
                    context.report(
                        REVIEW_LATER_ISSUE,
                        node,
                        context.getLocation(node),
                        if (isUsage) "Using @ReviewLater code – review required."
                        else "This code is marked as @ReviewLater – needs further review."
                    )
                }
            }
        }
    }

    companion object {
        val BUGGY_ISSUE: Issue = Issue.create(
            id = "Buggy",
            briefDescription = "Buggy code",
            explanation = "Marked with @Buggy. Code has known issues and must be fixed.",
            category = Category.CORRECTNESS,
            priority = 8,
            severity = Severity.ERROR,
            implementation = Implementation(
                CustomAnnotationDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val NEEDS_IMPROVEMENT_ISSUE: Issue = Issue.create(
            id = "NeedsImprovement",
            briefDescription = "Code needs improvement",
            explanation = "Marked with @NeedsImprovement. Refactor or review this implementation.",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                CustomAnnotationDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val WRONG_APPROACH_ISSUE: Issue = Issue.create(
            id = "WrongApproach",
            briefDescription = "Wrong Approach",
            explanation = "Marked with @WrongApproach. This is not the best way to solve the problem.",
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                CustomAnnotationDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val REVIEW_LATER_ISSUE: Issue = Issue.create(
            id = "ReviewLater",
            briefDescription = "Review Later",
            explanation = "Marked with @ReviewLater. This should be reviewed later.",
            category = Category.CORRECTNESS,
            priority = 3,
            severity = Severity.INFORMATIONAL,
            implementation = Implementation(
                CustomAnnotationDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )

        val ISSUES = listOf(
            BUGGY_ISSUE,
            NEEDS_IMPROVEMENT_ISSUE,
            WRONG_APPROACH_ISSUE,
            REVIEW_LATER_ISSUE
        )
    }
}
