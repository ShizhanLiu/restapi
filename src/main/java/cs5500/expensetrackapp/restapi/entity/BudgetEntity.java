package cs5500.expensetrackapp.restapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "tbl_budgets")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String budgetId;

  private String name;

  private String note;

  private Date yearmonth;

  private BigDecimal budget;

  private BigDecimal balance;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private Timestamp createdAt;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private Timestamp updatedAt;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "owner_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ProfileEntity owner;
}
