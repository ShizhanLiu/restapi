package cs5500.expensetrackapp.restapi.repository;


import cs5500.expensetrackapp.restapi.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository for Expense resource
 * @author Bushan SC
 * */
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

  /**
   * It will find the single expense from database
   * @param expenseId
   * @return Optional
   * */
//  Optional<ExpenseEntity> findByExpenseId(String expenseId);
//
//  List<ExpenseEntity> findByOwnerId(Long id);

//  Optional<ExpenseEntity> findByOwnerIdAndExpenseId(Long id, String expenseId);
}
