package cs5500.expensetrackapp.restapi.repository;

import cs5500.expensetrackapp.restapi.entity.BudgetEntity;
import cs5500.expensetrackapp.restapi.entity.ExpenseEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository for Expense resource
 * @author Bushan SC
 * */
public interface BudgetRepository extends JpaRepository<BudgetEntity, Long> {

  /**
   * It will find the single expense from database
   * @param budgetId
   * @return Optional
   * */
  Optional<BudgetEntity> findByBudgetId(String budgetId);

  List<BudgetEntity> findByOwnerId(Long id);

  Optional<BudgetEntity> findByOwnerIdAndBudgetId(Long id, String budgetId);

  @Query(value = "SELECT * FROM tbl_budgets " +
      "WHERE owner_id = :id " +
      "AND YEAR(yearmonth) = :year " +
      "AND MONTH(yearmonth) = :month", nativeQuery = true)
  List<BudgetEntity> findByOwnerIdAndYearAndMonth(@Param("id") Long id, @Param("year") int year, @Param("month") int month);
}
