package cs5500.expensetrackapp.restapi.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetResponse {

  private String budgetId;

  private String name;

  private String note;

  private Date yearmonth;

  private BigDecimal budget;

  private BigDecimal balance;

  private Timestamp createdAt;

  private Timestamp updatedAt;
}
