package com.plcoding.testingcourse.part7.util

import com.plcoding.testingcourse.part7.domain.Post
import com.plcoding.testingcourse.part7.domain.Profile
import com.plcoding.testingcourse.part7.domain.User
import java.util.UUID

fun profile(): Profile {
    val user = user()
    return Profile(
        user = user,
        posts = (1..10).map {
            post(user.id)
        }
    )
}

fun user() = User(
    id = UUID.randomUUID().toString(),
    username = "Test"
)

fun post(userID: String) = Post(
    id = UUID.randomUUID().toString(),
    userId = userID,
    title = "Test title",
    body = "Test body"
)