package com.plcoding.testingcourse.part6

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OrderServiceTest {
    private lateinit var orderService: OrderService
    private lateinit var emailClient: EmailClient
    private lateinit var auth: FirebaseAuth

    @BeforeEach
    fun setUp() {
        val firebaseUser = mockk<FirebaseUser> {
            every { isAnonymous } returns false
        }
        auth = mockk(relaxed = true) {
            every { currentUser } returns firebaseUser
        }
        emailClient = mockk(relaxed = true)
        orderService = OrderService(auth, emailClient)
    }

    @Test
    fun `placeOrder, email is sent if non-anonymous user`() {
        val customer = Customer(id = 1, email = "some@gmail.com")
        val productName = "Chocolate"

        orderService.placeOrder(
            customerEmail = customer.email,
            productName = productName
        )

        verify {
            emailClient.send(
                Email(
                    subject = "Order Confirmation",
                    content = "Thank you for your order of $productName.",
                    recipient = customer.email
                )
            )
        }
    }
}