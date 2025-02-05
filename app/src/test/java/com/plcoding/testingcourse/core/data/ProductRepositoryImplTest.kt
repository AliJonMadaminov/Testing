package com.plcoding.testingcourse.core.data

import assertk.assertThat
import assertk.assertions.isTrue
import com.plcoding.testingcourse.core.domain.AnalyticsLogger
import com.plcoding.testingcourse.core.domain.LogParam
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException

class ProductRepositoryImplTest {
    private lateinit var repository: ProductRepositoryImpl
    private lateinit var productApi: ProductApi
    private lateinit var analyticsLogger: AnalyticsLogger

    @BeforeEach
    fun setUp() {
        productApi = mockk()
        analyticsLogger = mockk(relaxed = true)
        repository = ProductRepositoryImpl(productApi, analyticsLogger)
    }

    @Test
    fun `Response error, http exception is logged`() = runBlocking {
        coEvery { repository.purchaseProducts(listOf()) } throws mockk<HttpException> {
            every { code() } returns 404
            every { message() } returns "not found - test message"
        }

        val result = repository.purchaseProducts(listOf())

        assertThat(result.isFailure).isTrue()
        verify {
            analyticsLogger.logEvent(
                match { it.contains("http") || it.contains("network") },
                LogParam("code", 404),
                LogParam("message", "not found - test message"),
            )
        }
    }
}