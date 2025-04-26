package cs5500.expensetrackapp.restapi.repository;

import cs5500.expensetrackapp.restapi.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * JPA repository for Expense resource
 * */
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

  /**
   * It will find the single expense from database
   * @param expenseId
   * @return Optional
   * */
  Optional<ExpenseEntity> findByExpenseId(String expenseId);

  List<ExpenseEntity> findByOwnerId(Long id);

  Optional<ExpenseEntity> findByOwnerIdAndExpenseId(Long id, String expenseId);

  @Query(value = "SELECT * FROM tbl_expenses " +
      "WHERE owner_id = :id " +
      "AND YEAR(date) = :year " +
      "AND MONTH(date) = :month", nativeQuery = true)
  List<ExpenseEntity> findByOwnerIdAndYearAndMonth(@Param("id") Long id, @Param("year") int year, @Param("month") int month);
}