package studio.kio.demo

import java.io.Serializable

/**
 * created by KIO on 2020/10/10
 */


data class User(
    val Id: Int,
    val name: String
) : Serializable