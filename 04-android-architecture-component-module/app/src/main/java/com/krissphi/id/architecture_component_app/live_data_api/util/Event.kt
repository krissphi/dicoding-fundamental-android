package com.krissphi.id.architecture_component_app.live_data_api.util

open class Event<out T>(private val content: T) {


    //menambahkan live
    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeenHandled = false
        private set
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
    fun peekContent(): T = content
}