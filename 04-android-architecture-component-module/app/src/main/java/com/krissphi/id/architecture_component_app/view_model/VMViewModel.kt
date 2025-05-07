package com.krissphi.id.architecture_component_app.view_model

import androidx.lifecycle.ViewModel

class VMViewModel : ViewModel() {
    var result = 0

    fun calculate(width: String, height: String, length: String) {
        result = width.toInt() * height.toInt() * length.toInt()
    }
}