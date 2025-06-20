package faang.school.accountservice.model.account;

import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.model.TariffHistory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "savings_account_rate")
public class SavingsAccountRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "tariff_id", nullable = false)
    private Tariff tariff;

    @Column(name = "rate")
    private BigDecimal rate;

    @ManyToOne
    @JoinColumn(name = "tariff_history_id", nullable = false)
    private TariffHistory tariffHistory;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "rate_bonus_added")
    private BigDecimal rateBonusAdded;
}