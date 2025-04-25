package cs5500.expensetrackapp.restapi.service.impl;

import cs5500.expensetrackapp.restapi.dto.ExpenseDTO;
import cs5500.expensetrackapp.restapi.entity.BudgetEntity;
import cs5500.expensetrackapp.restapi.entity.ExpenseEntity;
import cs5500.expensetrackapp.restapi.entity.ProfileEntity;
import cs5500.expensetrackapp.restapi.repository.BudgetRepository;
import cs5500.expensetrackapp.restapi.repository.ExpenseRepository;
import cs5500.expensetrackapp.restapi.service.AuthService;
import cs5500.expensetrackapp.restapi.service.ExpenseService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import cs5500.expensetrackapp.restapi.exceptions.ResourceNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * Service implementation for Expense module
 * @author Anne
 * */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {

  private final ExpenseRepository expenseRepository;
  private final BudgetRepository budgetRepository;
  private final ModelMapper modelMapper;

  private final AuthService authService;

  /**
   * It will fetch the expenses from database
   * @return list
   * */
  @Override
  public List<ExpenseDTO> getAllExpenses() {
    //Call the repository method
    Long loggedInProfileId = authService.getLoggedInProfile().getId();
    List<ExpenseEntity> list = expenseRepository.findByOwnerId(loggedInProfileId);
    log.info("Printing the data from repository {}", list);
    //Convert the Entity object to DTO object
    List<ExpenseDTO> listOfExpenses = list.stream().map(expenseEntity -> mapToExpenseDTO(expenseEntity)).collect(Collectors.toList());
    //Return the list
    return listOfExpenses;
  }

  /**
   * It will fetch the single expense details from database
   * @param expenseId
   * @return ExpenseDTO
   * */
  @Override
  public ExpenseDTO getExpenseByExpenseId(String expenseId) {
    ExpenseEntity expenseEntity = getExpenseEntity(expenseId);
    log.info("Printing the expense entity details {}", expenseEntity);
    return mapToExpenseDTO(expenseEntity);
  }

  /**
   * It will delete the expense from database
   * @param expenseId
   * @return void
   * */
  @Override
  public void deleteExpenseByExpenseId(String expenseId) {
    ExpenseEntity expenseEntity = getExpenseEntity(expenseId);
    log.info("Printing the expense entity {}", expenseEntity);
    //add up balance for budget
    expenseEntity.setAmount(expenseEntity.getAmount().negate());
    updateBudgetBalance(expenseEntity);
    expenseRepository.delete(expenseEntity);
  }

  /**

   * It will save the expense details to database
   * @param expenseDTO
   * @return ExpenseDTO
   * */
  @Override
  public ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO) {
    ProfileEntity profileEntity = authService.getLoggedInProfile();
    ExpenseEntity newExpenseEntity = mapToExpenseEntity(expenseDTO);
    newExpenseEntity.setExpenseId(UUID.randomUUID().toString());
    newExpenseEntity.setOwner(profileEntity);
    newExpenseEntity = expenseRepository.save(newExpenseEntity);
    boolean alert = updateBudgetBalance(newExpenseEntity);
    log.info("Printing the new expense entity details {}", newExpenseEntity);
    ExpenseDTO dto = mapToExpenseDTO(newExpenseEntity);
    dto.setAlert(alert);
    return dto;
  }

  @Override
  public ExpenseDTO updateExpenseDetails(ExpenseDTO expenseDTO, String expenseId) {
    ExpenseEntity existingExpense = getExpenseEntity(expenseId);
    ExpenseEntity updatedExpenseEntity = mapToExpenseEntity(expenseDTO);
    updatedExpenseEntity.setId(existingExpense.getId());
    updatedExpenseEntity.setExpenseId(existingExpense.getExpenseId());
    updatedExpenseEntity.setCreatedAt(existingExpense.getCreatedAt());
    updatedExpenseEntity.setUpdatedAt(existingExpense.getUpdatedAt());
    updatedExpenseEntity.setOwner(authService.getLoggedInProfile());
    //for calculating new balance use, revert back after use
    BigDecimal delta = updatedExpenseEntity.getAmount().subtract(existingExpense.getAmount());
    updatedExpenseEntity.setAmount(delta);
    boolean alert = updateBudgetBalance(updatedExpenseEntity);
    updatedExpenseEntity.setAmount(existingExpense.getAmount().add(delta));
    log.info("Printing the updated expense entity details {}", updatedExpenseEntity);
    updatedExpenseEntity = expenseRepository.save(updatedExpenseEntity);
    ExpenseDTO dto = mapToExpenseDTO(updatedExpenseEntity);
    dto.setAlert(alert);
    return dto;
//    updatedExpenseEntity = expenseRepository.save(updatedExpenseEntity);
//    log.info("Printing the updated expense entity details {}", updatedExpenseEntity);
//    return mapToExpenseDTO(updatedExpenseEntity);
  }

  /**
   * Mapper method to map values from Expense dto to Expense entity
   * @param expenseDTO
   * @return ExpenseEntity
   * */
  private ExpenseEntity mapToExpenseEntity(ExpenseDTO expenseDTO) {
    return modelMapper.map(expenseDTO, ExpenseEntity.class);
  }

  /**
   * Mapper method to convert expense entity to expense DTO
   * @param expenseEntity
   * @return ExpenseDTO
   * */
  private ExpenseDTO mapToExpenseDTO(ExpenseEntity expenseEntity) {
    return modelMapper.map(expenseEntity, ExpenseDTO.class);
  }

  /**
   * Fetch the expense by expense id from database
   * @param expenseId
   * @return ExpenseEntity
   * */
  private ExpenseEntity getExpenseEntity(String expenseId) {
    Long id = authService.getLoggedInProfile().getId();
    return expenseRepository.findByOwnerIdAndExpenseId(id, expenseId)
        .orElseThrow(() -> new ResourceNotFoundException("Expense not found for the expense id "+ expenseId));
  }

  private boolean updateBudgetBalance(ExpenseEntity expenseEntity) {
    boolean alert = false;
    Date date = expenseEntity.getDate();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    List<BudgetEntity> budgets = budgetRepository.findByOwnerIdAndYearAndMonth(expenseEntity.getOwner().getId(), year, month);
    for (BudgetEntity budget : budgets) {
      budget.setBalance(budget.getBalance().subtract(expenseEntity.getAmount()));
      budgetRepository.save(budget);
      // check if less than 20% balance is left
      if (budget.getBalance().compareTo(budget.getBudget().multiply(new BigDecimal("0.2"))) < 0) {
        alert = true;
      }
    }
    return alert;
  }
}
