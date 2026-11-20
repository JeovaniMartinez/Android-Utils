package com.jeovanimartinez.androidutils.extensions.nullability

/**
 * Extensions to work with null values and with null safety.
 * */

// Reference: https://discuss.kotlinlang.org/t/let-vs-if-not-null/3542/2
/**
 * Execute the code block only if the variable is not null. Inside the function, always use `it` to refer to the value of the variable.
 * ```
 *  For example:
 *  demo.whenNotNull {
 *      // Always use `it` instead of the variable name, since the variable can be null, `it` will never be null if it enters the code block.
 *      Log.d("Check Nullability", it)
 *  }
 *  ```
 * It works the same whether it is called with the safe call operator (?) or without it (demo.whenNotNull is equivalent to demo?.whenNotNull).
 * But it is always recommended to call without the safe call operator (demo.whenNotNull { ... })
 * */
fun <T : Any> T?.whenNotNull(f: (it: T) -> Unit) {
    if (this != null) f(this)
}
