package com.plcoding.testingcourse.part7.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserRepositoryImplTest {
    private lateinit var userApi: UserApi
    private lateinit var userRepository: UserRepositoryImpl

    @BeforeEach
    fun setUp() {
        userApi = UserApiFake()
        userRepository = UserRepositoryImpl(userApi)
    }

    @Test
    fun `Get profile, correct profile returned`() = runBlocking {
        val profileResult = userRepository.getProfile("1")
        val profile = profileResult.getOrThrow()
        val expectedPosts = userApi.getPosts("1")

        assertThat(profileResult.isSuccess).isTrue()
        assertThat(profile.user.id).isEqualTo("1")
        assertThat(profile.posts).isEqualTo(expectedPosts)
    }
}