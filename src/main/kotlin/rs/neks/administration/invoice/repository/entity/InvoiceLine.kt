package rs.neks.administration.invoice.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "invoice_lines")
data class InvoiceLine(
    val no: Int,

    val invoiceId: String,

    @ManyToOne
    val invoice: Invoice
)
