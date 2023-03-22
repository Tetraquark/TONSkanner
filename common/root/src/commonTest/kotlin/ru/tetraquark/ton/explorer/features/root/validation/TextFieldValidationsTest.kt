package ru.tetraquark.ton.explorer.features.root.validation

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TextFieldValidationsTest {
    @Test
    fun `validateTONWalletAddressField empty field failure`() {
        assertNotNull(validateTONWalletAddressField(""))
    }

    @Test
    fun `validateTONWalletAddressField min length failure`() {
        assertNotNull(validateTONWalletAddressField("123123qweqwe"))
    }

    @Test
    fun `validateTONWalletAddressField regex failure`() {
        assertNotNull(
            validateTONWalletAddressField("#f-ex!@#%^&%IGuFDFVB0ldQzCJxVV6U-YT5B4nrg1VE8Mj1yOEp0")
        )
        assertNotNull(
            validateTONWalletAddressField("Af-exuKIGuFDFVB0ldQzCJxVV6U-YT5B4nrg2eE8Mj1yOEp0")
        )
        assertNotNull(
            validateTONWalletAddressField("E1-exu123uFDFVB0ldQZCJxVV6U-YT5B4nrg2eE8Mj1yOEp0")
        )
        assertNotNull(
            validateTONWalletAddressField("Uw-exu123uFDFVB0ldQZCJxVV6U-YT5B4nrg2eE8Mj1yOEp0")
        )
        assertNotNull(
            validateTONWalletAddressField("0f-exuKIGuFDFVB0ldQzCJxVV6U-Yn5B4nRg1VE8Mj1yOEp1we23")
        )
    }

    @Test
    fun `validateTONWalletAddressField success`() {
        assertNull(
            validateTONWalletAddressField("Ef-exuKIGuFDFVB0ldQzCJxVV6U-YT5B4nrg1VE8Mj1yOEp0")
        )
        assertNull(
            validateTONWalletAddressField("EQ-exuKIGuFDFVB0ldQzCJxVV6U-YT5B4nrg1VE8Mj1yOEp0")
        )
        assertNull(
            validateTONWalletAddressField("UQ-exuKIGuFDFVB0ldQzCJxVV6U-Yn5B4nRg1VE8Mj1yOEp1")
        )
        assertNull(
            validateTONWalletAddressField("Uf-exuKIGuFDFVB0ldQzCJxVV6U-Yn5B4nRg1VE8Mj1yOEp1")
        )
        assertNull(
            validateTONWalletAddressField("kf-exuKIGuFDFVB0ldQzCJxVV6U-Yn5B4nRg1VE8Mj1yOEp1")
        )
        assertNull(
            validateTONWalletAddressField("kQ-exuKIGuFDFVB0ldQzCJxVV6U-Yn5B4nRg1VE8Mj1yOEp1")
        )
        assertNull(
            validateTONWalletAddressField("0Q-exuKIGuFDFVB0ldQzCJxVV6U-Yn5B4nRg1VE8Mj1yOEp1")
        )
        assertNull(
            validateTONWalletAddressField("0f-exuKIGuFDFVB0ldQzCJxVV6U-Yn5B4nRg1VE8Mj1yOEp1")
        )
    }
}
