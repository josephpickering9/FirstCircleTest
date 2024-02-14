package org.example.models

import enums.TransactionType
import java.math.BigDecimal

class Transaction(val type: TransactionType, val amount: BigDecimal)