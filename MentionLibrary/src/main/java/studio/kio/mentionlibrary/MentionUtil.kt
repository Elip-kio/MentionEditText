package studio.kio.mentionlibrary

import kotlin.reflect.KClass

/**
 * created by KIO on 2020/10/10
 */
object MentionUtil {

    fun <T : Any> withType(type: KClass<T>): MentionHandlerBuilder<T> {
        return MentionHandlerBuilder()
    }

}