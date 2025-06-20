package rs.neks.administration.invoice.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import rs.neks.administration.invoice.repository.entity.Invoice

@Repository
interface InvoiceRepository: CrudRepository<Invoice, Long>
