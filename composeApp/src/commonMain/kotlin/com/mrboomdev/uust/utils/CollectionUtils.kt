package com.mrboomdev.uust.utils

inline fun <T> Collection<T>.firstAndIndexOrNull(
    predicate: (T) -> Boolean
): Pair<T, Int>? {
    forEachIndexed { index, element ->
        if(predicate(element)) {
            return element to index
        }
    }
    
    return null
}

fun <T> Collection<T>.firstAndIndex(
    predicate: (T) -> Boolean
) = firstAndIndexOrNull(predicate) 
    ?: throw NoSuchElementException("Collection contains no element matching the predicate.")