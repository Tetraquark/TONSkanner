package ru.tetraquark.ton.explorer.core.ton.exception

import org.ton.cell.Cell

class TonIncorrectCellException(val cell: Cell) : TonLiteClientException(
    message = "Incorrect cell structure"
)
