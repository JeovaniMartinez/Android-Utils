@file:Suppress("UnstableApiUsage")

package com.jeovanimartinez.androidutils.lintcheck.annotations

import com.android.tools.lint.detector.api.*
import com.intellij.psi.tree.IElementType
import org.jetbrains.kotlin.KtNodeTypes.DOT_QUALIFIED_EXPRESSION
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.uast.*

// Custom Lint Rules Example: https://github.com/googlesamples/android-custom-lint-rules

// Debugging instructions, bug fixes and general notes about this class: see documentation at the end of this file.

/**
 * Lint check that is responsible for verifying the TypeOrResource annotations and verifying that the property or variable that
 * the annotation uses has a suitable value and warning the user otherwise.
 */
class TypeOrResource : Detector(), Detector.UastScanner {

    companion object {
        // Configuration and problem description
        val ISSUE = Issue.create(
            id = "TypeOrResource",
            briefDescription = "Validate that the correct type or correct resource is received",
            explanation = """
                When using this annotation, different types of data are generally accepted, so this verification is in charge of validating 
                that the correct data type is received according to the annotation. For example, the `@StringOrStringRes` annotation is
                expected to receive a string value, such as "Hello" or an ID of a string resource, such as R.string.demo. If a String 
                is received, the data is treated as is, if a string ID is received, the value is obtained and it is treated as a String, 
                but if an incorrect data type is received like a Float or an incorrect resource like R.color.demo app will throw an 
                exception at runtime. For this reason, this check helps to assign a correct value according to the annotation.
            """, // No need to .trimIndent(), lint does that automatically
            category = Category.CORRECTNESS,
            priority = 8,
            severity = Severity.ERROR,
            implementation = Implementation(TypeOrResource::class.java, Scope.JAVA_FILE_SCOPE)
        )

    }

    /** Auxiliary enum to determine what type of verification needs to be performed. */
    enum class CheckType {
        DATA_TYPE, RESOURCE_TYPE
    }

    /** The annotations that this class is responsible for checking are assigned. */
    override fun applicableAnnotations(): List<String> {
        // Each element must contain the complete package and the name of the annotation
        return listOf(
            "com.jeovanimartinez.androidutils.annotations.DrawableOrDrawableRes",
            "com.jeovanimartinez.androidutils.annotations.StringOrStringRes"
        )
    }

    /** When annotation usage is detected */
    override fun visitAnnotationUsage(context: JavaContext, element: UElement, annotationInfo: AnnotationInfo, usageInfo: AnnotationUsageInfo) {

        // Get the item type with its debug name, for example REGULAR_STRING_PART for a String
        val elementType = element.sourcePsi?.node?.firstChildNode?.elementType
        // Get the value in text
        val text = element.sourcePsi?.node?.firstChildNode?.text

        val elementPrev = element.sourcePsi?.node?.firstChildNode?.treePrev // Next item
        val elementNext = element.sourcePsi?.node?.firstChildNode?.treeNext // Previous item

        /*
        * It is validated that there is content to verify, otherwise no problem is reported.
        * It is also validated that the value of text is not "null", since this indicates that the value
        * of the element is null, for the purposes of this validation if null values can be accepted.
        * */
        if (elementType != null && text != null && text != "null") {

            /*
            * For the data type, it is only validated if elementPrev && elementNext are null, this indicates that there
            * is nothing before the value or after, for example if the value where it is invoked is:
            *   5 = if it enters to validate, since there is nothing before or after.
            *   5f = if it enters to validate, since f is considered part of the value.
            *   5.toString() = does not enter to validate, since after the value there is a point.
            * It is also validated that the data type is not an identifier, to avoid showing the error when using the
            * variable or property that the annotation has, and only showing the error in the assignment.
            * */
            if (elementPrev == null && elementNext == null && elementType != KtTokens.IDENTIFIER) {
                // Verification is executed according to the annotation found
                when (annotationInfo.qualifiedName) {
                    "com.jeovanimartinez.androidutils.annotations.DrawableOrDrawableRes" -> checkDrawableOrDrawableRes(context, element, elementType, text, CheckType.DATA_TYPE)
                    "com.jeovanimartinez.androidutils.annotations.StringOrStringRes" -> checkStringOrStringRes(context, element, elementType, text, CheckType.DATA_TYPE)
                }
            }

            // Now it is verified if the value corresponds to a value of the resources.
            if (elementType == DOT_QUALIFIED_EXPRESSION && text.contains("R.") && (text.startsWith("R.") || text.startsWith("android.R."))) {
                // Verification is executed according to the annotation found
                when (annotationInfo.qualifiedName) {
                    "com.jeovanimartinez.androidutils.annotations.DrawableOrDrawableRes" -> checkDrawableOrDrawableRes(context, element, elementType, text, CheckType.RESOURCE_TYPE)
                    "com.jeovanimartinez.androidutils.annotations.StringOrStringRes" -> checkStringOrStringRes(context, element, elementType, text, CheckType.RESOURCE_TYPE)
                }
            }
        }

        super.visitAnnotationUsage(context, element, annotationInfo, usageInfo)
    }

    /**
     * Check the use of the DrawableOrDrawableRes annotation and report the issue if applicable.
     * @param context Context.
     * @param element The AST node scope the error applies to (reference within the code where the annotation is used and where the code is being parsed).
     * @param elementType Element type.
     * @param text Value assigned to the variable or property of the annotation in String.
     * @param checkType Type of verification.
     * */
    @Suppress("UNUSED_PARAMETER")
    private fun checkDrawableOrDrawableRes(context: JavaContext, element: UElement, elementType: IElementType, text: String, checkType: CheckType) {
        when (checkType) {
            CheckType.DATA_TYPE -> {
                // Drawable is not a primitive data type, so identifiers or references are received, and in this case nothing problem is reported
                // Invalid type, expected drawable object or drawable resource
            }
            CheckType.RESOURCE_TYPE -> {
                // The problem is reported if the resource is not a drawable resource
                if (text != "R.drawable" && text != "android.R.drawable") {
                    return context.report(
                        issue = ISSUE,
                        scope = element,
                        location = context.getLocation(element),
                        message = "**Invalid resource.** Expected drawable resource or drawable object."
                    )
                }
            }
        }
    }

    /**
     * Check the use of the StringOrStringRes annotation and report the issue if applicable.
     * @param context Context.
     * @param element The AST node scope the error applies to (reference within the code where the annotation is used and where the code is being parsed).
     * @param elementType Element type.
     * @param text Value assigned to the variable or property of the annotation in String.
     * @param checkType Type of verification.
     * */
    private fun checkStringOrStringRes(context: JavaContext, element: UElement, elementType: IElementType, text: String, checkType: CheckType) {
        when (checkType) {
            CheckType.DATA_TYPE -> {
                // The problem is reported if the type of data assigned is not a String or a Char
                if (elementType != KtTokens.CHARACTER_LITERAL && elementType != KtTokens.REGULAR_STRING_PART && elementType != KtTokens.OPEN_QUOTE) {
                    context.report(
                        issue = ISSUE,
                        scope = element,
                        location = context.getLocation(element),
                        message = "**Invalid type.** Expected string object or string resource."
                    )
                }
            }
            CheckType.RESOURCE_TYPE -> {
                // The problem is reported if the resource is not a string resource
                if (text != "R.string" && text != "android.R.string") {
                    return context.report(
                        issue = ISSUE,
                        scope = element,
                        location = context.getLocation(element),
                        message = "**Invalid resource.** Expected string resource or string object."
                    )
                }
            }
        }
    }

}

/*

Debugging instructions, bug fixes and general notes.

General:
- The library must be include "lintPublish project(':lintcheck')" in the gradle file, and the test app should be include "implementation project(":androidutils")" in the gradle file.
- To debug and test this file, is need to reference one of the annotations being checked somewhere in the test project, for example in the MainActivity add the following code:
    private fun showMessage(@StringOrStringRes message: Any) {

    }
- Then invoke the function somewhere in the code:
    showMessage(R.color.colorAccent)

Debugging:
- Preferably only have this file open, avoid having the file open where the annotations are used (MainActivity).
- Place the desired breakpoints.
- In the IDE settings in Experimental > "Do not build Gradle task list during Gradle sync" make sure the option is unchecked, otherwise the gradle task list for the project is not generated.
- Sync gradle again to generate the task list for the project.
- In the gradle panel inside the IDE, expand Android-Utils > app > verification (in the app since this is where the annotations are used and this is where the verification will be executed).
    - To run the verification, right-click on lint option and select Run.
    - To debug the file, right click on lint option and select Debug.
- Then, in the file where the annotations were used, the errors (if applicable) should appear.

Bug fixes:
- To make sure that the lint check module is compiled with the latest changes and the test app is checked with those changes, go to Build > Clean Project before compiling or debugging.
    - If the IDE throws an error saying it can't delete the files, close the IDE and manually delete the folder ~\Android-Utils\androidutils\build\
        - If the folder cannot be deleted because it indicates that it is still in use, open the task manager and end the "OpenJDK Platform Binary" tasks.
        - After this the folder should be able to be deleted.
    - Open the IDE and go to Build > Clean Project, later compile/debug again.

**/
