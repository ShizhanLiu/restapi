package cs5500.expensetrackapp.restapi.io;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetRequest {

  @NotBlank(message = "Budget name is required")
  @Size(min = 3, message = "Budget name should be atleast 3 characters")
  private String name;

  private String note;

  @NotNull(message = "Budget month is required")
  private Date yearmonth;

  @NotNull(message = "Budget amount is required")
  private BigDecimal budget;
}
