package com.plcoding.testingcourse.part7.presentation

import androidx.lifecycle.SavedStateHandle
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import com.plcoding.testingcourse.part7.data.UserRepositoryFake
import com.plcoding.testingcourse.part7.util.MainCoroutineExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineExtension::class)
class ProfileViewModelTest {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var repository: UserRepositoryFake

    @BeforeEach
    fun setUp() {
        repository = UserRepositoryFake()
        viewModel = ProfileViewModel(
            repository = repository,
            savedStateHandle = SavedStateHandle(
                mapOf("userId" to repository.profileToReturn.user.id)
            )
        )
    }

    @Test
    fun `getProfile, if success stops loading and returns data`() = runTest {
        viewModel.loadProfile()
        advanceUntilIdle()

        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(viewModel.state.value.profile).isEqualTo(repository.profileToReturn)
    }

    @Test
    fun `getProfile, if error stops loading and returns exception`() = runTest {
        val exception = Exception("Something went wrong")
        repository.errorToReturn = exception

        viewModel.loadProfile()
        advanceUntilIdle()

        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(viewModel.state.value.errorMessage).isEqualTo(exception.message)
    }
}