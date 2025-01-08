package faang.school.accountservice.repository;

import faang.school.accountservice.model.balance.Balance;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {

  @Query(nativeQuery = true, value = """
      INSERT INTO balance (account_id, authorized_value, actual_value, created_at, updated_at)
      VALUES (?1, ?2, ?2, NOW(), NOW()) RETURNING *
      """)
  Balance create(long accountId, BigDecimal value);
}
