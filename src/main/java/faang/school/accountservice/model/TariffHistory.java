package faang.school.accountservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import faang.school.accountservice.model.account.SavingsAccount;
import faang.school.accountservice.model.account.SavingsAccountRate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tariff_history")
public class TariffHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "savings_account_id", nullable = false)
    @JsonIgnore
    private SavingsAccount savingsAccount;

    @OneToMany(mappedBy = "tariffHistory", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavingsAccountRate> savingsAccountRates;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "savings_account_tariff_id", nullable = false)
    @JsonIgnore
    private Tariff tariff;
}