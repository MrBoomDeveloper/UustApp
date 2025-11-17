package com.mrboomdev.uust.utils

fun <T> MutableIterator<T>.iterateIndexed(
    block: MutableIterator<T>.(Int, T) -> Unit
) {
    var index = 0
    
    while(hasNext()) {
        block(index, next())
        index++
    }
}

fun <T> MutableCollection<T>.iterateIndexed(
    block: MutableIterator<T>.(Int, T) -> Unit
) = iterator().iterateIndexed(block)