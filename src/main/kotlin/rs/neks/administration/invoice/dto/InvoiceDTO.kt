package rs.neks.administration.invoice.dto

import kotlinx.datetime.Instant

data class InvoiceDTO(
    val number: String,
    val cratedAt: Instant,
    var updatedAt: Instant
)
