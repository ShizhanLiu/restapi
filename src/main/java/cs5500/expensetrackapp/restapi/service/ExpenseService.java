package cs5500.expensetrackapp.restapi.service;

import cs5500.expensetrackapp.restapi.dto.ExpenseDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service interface for Expense module
 * @author Bushan SC
 * */
@Service
//@RequiredArgsConstructor
public interface ExpenseService {

  /**
   * It will fetch the expenses from database
   * @return list
   * */
  List<ExpenseDTO> getAllExpenses();

//  /**
//   * It will fetch the single expense details from database
//   * @param expenseId
//   * @return ExpenseDTO
//   * */
//  ExpenseDTO getExpenseByExpenseId(String expenseId);
//
//  /**
//   * It will delete the expense from database
//   * @param expenseId
//   * @return void
//   * */
//  void deleteExpenseByExpenseId(String expenseId);
//
//  /**
//   * It will save the expense details to database
//   * @param expenseDTO
//   * @return ExpenseDTO
//   * */
//  ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO);
//
//  /**
//   * It will update the expense details to database
//   * @param expenseDTO
//   * @param expenseId
//   * @return ExpenseDTO
//   * */
//  ExpenseDTO updateExpenseDetails(ExpenseDTO expenseDTO, String expenseId);
}