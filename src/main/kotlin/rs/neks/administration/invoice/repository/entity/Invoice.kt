package rs.neks.administration.invoice.repository.entity

import jakarta.persistence.*
import kotlinx.datetime.Instant

@Entity
@Table(name = "invoice")
data class Invoice(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @Column(name = "number", length = 20, nullable = false)
    var number: String,

    @Column(name = "created_at")
    var createdAt: Instant,

    @Column(name = "updated_at")
    var updatedAt: Instant,

    @OneToMany(mappedBy = "invoice", cascade = [(CascadeType.ALL)])
    var lines: ArrayList<InvoiceLine>
)