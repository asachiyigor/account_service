package faang.school.accountservice.model.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import faang.school.accountservice.model.TariffHistory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "savings_account")
public class SavingsAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_number", referencedColumnName = "account_number", nullable = false)
    @JsonIgnore
    private Account account;

    @OneToMany(mappedBy = "savingsAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TariffHistory> tariffHistory;

    @Column(name = "last_date_percent")
    private LocalDateTime lastDatePercent;

    @Version
    @Column(name = "version", insertable = false)
    private Long version;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "last_bonus_update")
    private LocalDateTime lastBonusUpdate;
}