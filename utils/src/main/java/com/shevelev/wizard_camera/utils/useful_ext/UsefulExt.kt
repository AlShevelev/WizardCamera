package com.shevelev.wizard_camera.utils.useful_ext

inline fun <A, B>ifNotNull(a: A?, b: B?, action: (A, B) -> Unit) {
    if(a!= null && b!=null) {
        action(a, b)
    }
}

inline fun <A, B, C>ifNotNull(a: A?, b: B?, c: C?, action: (A, B, C) -> Unit) {
    if(a!= null && b!=null && c!=null) {
        action(a, b, c)
    }
}


fun <T>T.at(vararg args: T): Boolean = args.contains(this)
