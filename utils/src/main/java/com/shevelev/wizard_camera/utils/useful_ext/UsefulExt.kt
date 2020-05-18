package com.shevelev.wizard_camera.utils.useful_ext

inline fun <A, B>ifNotNull(a: A?, b: B?, action: (A, B) -> Unit) {
    if(a!= null && b!=null) {
        action(a, b)
    }
}