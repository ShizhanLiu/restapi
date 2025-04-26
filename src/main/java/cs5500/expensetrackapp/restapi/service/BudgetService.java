package cs5500.expensetrackapp.restapi.service;

import cs5500.expensetrackapp.restapi.dto.BudgetDTO;

import java.util.List;

/**
 * Service interface for Expense module
 * */
public interface BudgetService {

  /**
   * It will fetch the expenses from database
   * @return list
   * */
  List<BudgetDTO> getAllBudgets();

  /**
   * It will fetch the single expense details from database
   * @param budgetId
   * @return BudgetDTO
   * */
  BudgetDTO getBudgetByBudgetId(String budgetId);

  /**
   * It will delete the expense from database
   * @param budgetId
   * @return void
   * */
  void deleteBudgetByBudgetId(String budgetId);

  /**
   * It will save the expense details to database
   * @param budgetDTO
   * @return ExpenseDTO
   * */
  BudgetDTO saveBudgetDetails(BudgetDTO budgetDTO);

  /**
   * It will update the expense details to database
   * @param budgetDTO
   * @param budgetId
   * @return BudgetDTO
   * */
  BudgetDTO updateBudgetDetails(BudgetDTO budgetDTO, String budgetId);
}
