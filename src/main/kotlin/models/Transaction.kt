package org.example.models

import enums.TransactionType
import java.math.BigDecimal

class Transaction(type: TransactionType, amount: BigDecimal) {
    val effectiveAmount: BigDecimal =
        when (type) {
            TransactionType.DEPOSIT -> amount
            TransactionType.WITHDRAWAL -> amount.abs().negate()
        }
}
