package ru.tetraquark.ton.explorer.features.root.validation

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull

class TextFieldValidationsTest : FunSpec({
    test("validateTONWalletAddressField empty field failure") {
        validateTONWalletAddressField("").shouldNotBeNull()
    }

    test("validateTONWalletAddressField min length failure") {
        validateTONWalletAddressField("123123qweqwe").shouldNotBeNull()
    }

    test("validateTONWalletAddressField regex failure") {
        validateTONWalletAddressField("#f-ex!@#%^&%IGuFDFVB0ldQzCJxVV6U-YT5B4nrg1VE8Mj1yOEp0")
            .shouldNotBeNull()
        validateTONWalletAddressField("Af-exuKIGuFDFVB0ldQzCJxVV6U-YT5B4nrg2eE8Mj1yOEp0")
            .shouldNotBeNull()
        validateTONWalletAddressField("E1-exu123uFDFVB0ldQZCJxVV6U-YT5B4nrg2eE8Mj1yOEp0")
            .shouldNotBeNull()
        validateTONWalletAddressField("Uw-exu123uFDFVB0ldQZCJxVV6U-YT5B4nrg2eE8Mj1yOEp0")
            .shouldNotBeNull()
        validateTONWalletAddressField("0f-exuKIGuFDFVB0ldQzCJxVV6U-Yn5B4nRg1VE8Mj1yOEp1we23")
            .shouldNotBeNull()
    }

    test("validateTONWalletAddressField success") {
        validateTONWalletAddressField("Ef-exuKIGuFDFVB0ldQzCJxVV6U-YT5B4nrg1VE8Mj1yOEp0")
            .shouldBeNull()
        validateTONWalletAddressField("EQ-exuKIGuFDFVB0ldQzCJxVV6U-YT5B4nrg1VE8Mj1yOEp0")
            .shouldBeNull()
        validateTONWalletAddressField("UQ-exuKIGuFDFVB0ldQzCJxVV6U-Yn5B4nRg1VE8Mj1yOEp1")
            .shouldBeNull()
        validateTONWalletAddressField("Uf-exuKIGuFDFVB0ldQzCJxVV6U-Yn5B4nRg1VE8Mj1yOEp1")
            .shouldBeNull()
        validateTONWalletAddressField("kf-exuKIGuFDFVB0ldQzCJxVV6U-Yn5B4nRg1VE8Mj1yOEp1")
            .shouldBeNull()
        validateTONWalletAddressField("kQ-exuKIGuFDFVB0ldQzCJxVV6U-Yn5B4nRg1VE8Mj1yOEp1")
            .shouldBeNull()
        validateTONWalletAddressField("0Q-exuKIGuFDFVB0ldQzCJxVV6U-Yn5B4nRg1VE8Mj1yOEp1")
            .shouldBeNull()
        validateTONWalletAddressField("0f-exuKIGuFDFVB0ldQzCJxVV6U-Yn5B4nRg1VE8Mj1yOEp1")
            .shouldBeNull()
    }
})
