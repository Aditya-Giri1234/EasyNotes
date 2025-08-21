package com.aditya.lint_rules.annotation

// 🚨 Code that must be fixed immediately
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class Buggy(val message: String = "Critical bug detected!")

// ⚠️ Needs refactor or cleanup
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class NeedsImprovement(val message: String = "This code can be improved.")

// 💡 Not recommended, but not critical
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class WrongApproach(val message: String = "Alternative approach is better.")

// 📝 For reminders / TODOs
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class ReviewLater(val message: String = "Needs further review.")
