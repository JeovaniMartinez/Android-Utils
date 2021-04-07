// Code

private fun tmp() {

    // OK
    showImage(R.drawable.library_logo)

    // ERROR: Invalid resource, expected drawable resource or drawable object
    showImage(R.color.colorAccent)

    /*
    * In some cases, the data type cannot be detected at compile time, so an
    * exception will be thrown at runtime to indicate the error if the data
    * type is wrong.
    */
    showImage(this@MainActivity)


    // OK
    showMessage("Hello")

    // OK: The char literal can be interpreted as string
    showMessage('h')

    // OK
    showMessage(R.string.app_name)

    // ERROR: Invalid resource, expected string resource or string object
    showMessage(R.color.colorAccent)

    // ERROR: Invalid type, expected string object or string resource
    showMessage(55)

    /*
    * In some cases, the data type cannot be detected at compile time, so an
    * exception will be thrown at runtime to indicate the error if the data
    * type is wrong.
    */
    showMessage(this@MainActivity)

}

private fun showImage(@DrawableOrDrawableRes image: Any) {

}

private fun showMessage(@StringOrStringRes message: Any) {

}

private fun showImage(@DrawableOrDrawableRes image: Any) {
    val finalImage = typeAsDrawable(image)
}

private fun showMessage(@StringOrStringRes message: Any) {
    val finalMessage = typeAsString(message)
}
