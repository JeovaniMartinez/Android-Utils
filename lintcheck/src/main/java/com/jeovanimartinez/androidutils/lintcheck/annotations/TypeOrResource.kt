@file:Suppress("UnstableApiUsage")

package com.jeovanimartinez.androidutils.lintcheck.annotations

import com.android.tools.lint.checks.AnnotationDetector
import com.android.tools.lint.detector.api.*
import com.intellij.psi.*
import com.intellij.psi.tree.IElementType
import org.jetbrains.kotlin.KtNodeTypes.DOT_QUALIFIED_EXPRESSION
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.uast.*

/*
* To debug this class:
* - Make sure the test app has a call to the required annotation.
* - Set the breakpoint.
* - In the Gradle panel of the IDE, expand the app option, since to execute this class, it is done through the app's lint verification.
* - Go to Task > Verification and right click on lint and select debug.
* */

/**
 * Lint check that is responsible for verifying the TypeOrResource annotations and verifying that the property or variable that
 * the annotation uses has a suitable value and warning the user otherwise.
 */
class TypeOrResource : AnnotationDetector() {

    companion object {
        // Configuration and problem description
        val ISSUE = Issue.create(
            id = "TypeOrResource",
            briefDescription = "Validate that the correct type or correct resource is received.",
            explanation = """
                When using this annotation, different types of data are generally accepted, so this verification is in charge of validating 
                that the correct data type is received according to the annotation. For example, the @StringOrStringRes annotation is
                expected to receive a string value, such as "Hello" or an ID of a string resource, such as R.string.demo. If a String 
                is received, the data is treated as is, if a string ID is received, the value is obtained and it is treated as a String, 
                but if an incorrect data type is received like a Float or an incorrect resource like R.color.demo app will throw an 
                exception at runtime. For this reason, this check helps to assign a correct value according to the annotation.
            """.trimIndent(),
            category = Category.CORRECTNESS,
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

    /** Cuando se detecta el uso de la anotación */
    override fun visitAnnotationUsage(
        context: JavaContext, usage: UElement, type: AnnotationUsageType, annotation: UAnnotation, qualifiedName: String,
        method: PsiMethod?, annotations: List<UAnnotation>, allMemberAnnotations: List<UAnnotation>, allClassAnnotations: List<UAnnotation>, allPackageAnnotations: List<UAnnotation>
    ) {

        // Se obtiene el tipo de elemento con su nombre de debug, por ejemplo REGULAR_STRING_PART para un String
        val elementType = usage.sourcePsi?.node?.firstChildNode?.elementType
        // Se obtiene el valor en texto
        val text = usage.sourcePsi?.node?.firstChildNode?.text

        val elementPrev = usage.sourcePsi?.node?.firstChildNode?.treePrev // Siguiente elemento
        val elementNext = usage.sourcePsi?.node?.firstChildNode?.treeNext // Elemento anterior

        /*
        * Se valida que haya contenido para verificar, de otro modo no se reporta ningún problema.
        * También se valida que el valor de text no sea "null", ya que esto indica que el valor del elemento
        * es null, para efectos de esta validación si se pueden aceptar valores null.
        * */
        if (elementType != null && text != null && text != "null") {

            /*
            * Para el tipo de dato, solo se valida si elementPrev && elementNext son null, esto indica que no hay
            * nada antes del valor ni después, por ejemplo si el valor donde se invoca es:
            *   5 = si entra, ya que no hay nada antes ni después
            *   5f = si entra, ya que f se considera parte del valor
            *   5.toString() = no entra, ya que después del valor hay un punto
            * También se valida que el tipo de dato no sea un identificador, para evitar mostrar el error al usar la variable
            * o propiedad que tiene la anotación, y solo mostrar el error en la asignación
            * */
            if (elementPrev == null && elementNext == null && elementType != KtTokens.IDENTIFIER) {
                // Se ejecuta la verificación según la anotación encontrada
                when (qualifiedName) {
                    "com.jeovanimartinez.androidutils.annotations.DrawableOrDrawableRes" -> checkDrawableOrDrawableRes(context, usage, elementType, text, CheckType.DATA_TYPE)
                    "com.jeovanimartinez.androidutils.annotations.StringOrStringRes" -> checkStringOrStringRes(context, usage, elementType, text, CheckType.DATA_TYPE)
                }
            }

            // Ahora se verifica si el valor corresponde a un valor de los recursos.
            if (elementType == DOT_QUALIFIED_EXPRESSION && text.contains("R.") && (text.startsWith("R.") || text.startsWith("android.R."))) {
                // Se ejecuta la verificación según la anotación encontrada
                when (qualifiedName) {
                    "com.jeovanimartinez.androidutils.annotations.DrawableOrDrawableRes" -> checkDrawableOrDrawableRes(context, usage, elementType, text, CheckType.RESOURCE_TYPE)
                    "com.jeovanimartinez.androidutils.annotations.StringOrStringRes" -> checkStringOrStringRes(context, usage, elementType, text, CheckType.RESOURCE_TYPE)
                }
            }
        }

    }

    /**
     * Verifica el uso de la anotación DrawableOrDrawableRes y reporta el problema (issue) si aplica
     * @param context contexto
     * @param usage referencia dentro del código donde se usa la anotación y donde se esta analizando el código
     * @param elementType tipo de elemento
     * @param text valor que se le asigna a la variable o propiedad de la anotación en string
     * @param checkType tipo de verificación
     * */
    @Suppress("UNUSED_PARAMETER")
    private fun checkDrawableOrDrawableRes(context: JavaContext, usage: UElement, elementType: IElementType, text: String, checkType: CheckType) {
        when (checkType) {
            CheckType.DATA_TYPE -> {
                // Drawable no es un tipo de dato primitivo, por lo que se reciben identificadores o referencias, y en este caso no se reporta ningún problema
                // Invalid type, expected drawable object or drawable resource
            }
            CheckType.RESOURCE_TYPE -> {
                // Se reporta el reporta el problema si el recurso no es un drawable resource
                if (text != "R.drawable" && text != "android.R.drawable") {
                    return context.report(
                        issue = ISSUE,
                        scope = usage,
                        location = context.getLocation(usage),
                        message = "Invalid resource, expected drawable resource or drawable object"
                    )
                }
            }
        }
    }

    /**
     * Verifica el uso de la anotación StringOrStringRes y reporta el problema (issue) si aplica
     * @param context contexto
     * @param usage referencia dentro del código donde se usa la anotación y donde se esta analizando el código
     * @param elementType tipo de elemento
     * @param text valor que se le asigna a la variable o propiedad de la anotación en string
     * @param checkType tipo de verificación
     * */
    private fun checkStringOrStringRes(context: JavaContext, usage: UElement, elementType: IElementType, text: String, checkType: CheckType) {
        when (checkType) {
            CheckType.DATA_TYPE -> {
                // Se reporta el problema si el tipo de dato que se asigna no es un string o un carácter
                if (elementType != KtTokens.CHARACTER_LITERAL && elementType != KtTokens.REGULAR_STRING_PART && elementType != KtTokens.OPEN_QUOTE) {
                    context.report(
                        issue = ISSUE,
                        scope = usage,
                        location = context.getLocation(usage),
                        message = "Invalid type, expected string object or string resource"
                    )
                }
            }
            CheckType.RESOURCE_TYPE -> {
                // Se reporta el reporta el problema si el recurso no es un string resource
                if (text != "R.string" && text != "android.R.string") {
                    return context.report(
                        issue = ISSUE,
                        scope = usage,
                        location = context.getLocation(usage),
                        message = "Invalid resource, expected string resource or string object"
                    )
                }
            }
        }
    }

}
