package faang.school.accountservice.controller.balance;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.accountservice.dto.balance.BalanceCreateDto;
import faang.school.accountservice.dto.balance.BalanceDto;
import faang.school.accountservice.service.balance.BalanceService;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@ContextConfiguration(classes = {BalanceController.class})
class BalanceControllerTest {

  private static final String TEST_DATE_TIME = "2024-11-22 00:21:39";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd HH:mm:ss");
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @MockBean
  private BalanceService balanceService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("Should return created Balance")
  void addBalance() throws Exception {
    BalanceCreateDto balanceCreateDto = BalanceCreateDto.builder()
        .accountId(2L)
        .authorizedValue(BigDecimal.TEN)
        .build();
    long userId = 1L;
    BalanceDto balanceDto = BalanceDto.builder()
        .id(1L)
        .accountId(2L)
        .authorizedValue(BigDecimal.TEN)
        .actualValue(BigDecimal.TEN)
        .createdAt(TEST_DATE_TIME)
        .updatedAt(TEST_DATE_TIME)
        .version(0L)
        .build();

    when(balanceService.create(userId, balanceCreateDto)).thenReturn(balanceDto);

    mockMvc.perform(post("/api/v1/balance/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(OBJECT_MAPPER.writeValueAsString(balanceCreateDto))
        .header("x-user-id", userId))
        .andExpect(content().json(OBJECT_MAPPER.writeValueAsString(balanceDto)))
        .andExpect(status().isOk());

    verify(balanceService, times(1)).create(userId, balanceCreateDto);
  }

  @Test
  @DisplayName("Authorization: Should return updated balance with authorization value decreased and version incremented")
  void updateBalance() {
  }

  @Test
  @DisplayName("Clearing: Should return updated balance with actual value decreased and version incremented")
  void getBalanceById() {
  }
}