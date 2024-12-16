package faang.school.accountservice.model.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="account")
public class Account {
    @Id
    @Column(name="account_id", length = 20, nullable = false, unique = true)
    @Size(min=12, max=20, message = "Account number must be between 12 and 20 characters")
    private String accountId;

    @Positive(message = "Owner id must be positive")
    @Column(name="owner_id", nullable = false)
    @NotNull(message = "Owner id is required")
    private long ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "owner_type", nullable = false, length = 32)
    private OwnerType ownerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 64)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false, length = 32)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private PaymentStatus status;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @UpdateTimestamp
    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name="version", nullable = false)
    @Version
    private int version;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;

    @Size(max = 4096, message = "Notes must be less than 4096 characters")
    @Column(name = "notes", length = 4096)
    private String notes;
}