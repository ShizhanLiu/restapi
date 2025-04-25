package cs5500.expensetrackapp.restapi.service.impl;


import cs5500.expensetrackapp.restapi.dto.BudgetDTO;
import cs5500.expensetrackapp.restapi.entity.BudgetEntity;
import cs5500.expensetrackapp.restapi.entity.ExpenseEntity;
import cs5500.expensetrackapp.restapi.entity.ProfileEntity;
import cs5500.expensetrackapp.restapi.exceptions.ResourceNotFoundException;
import cs5500.expensetrackapp.restapi.repository.BudgetRepository;
import cs5500.expensetrackapp.restapi.repository.ExpenseRepository;
import cs5500.expensetrackapp.restapi.service.AuthService;
import cs5500.expensetrackapp.restapi.service.BudgetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for Expense module
 * @author Bushan SC
 * */
@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetServiceImpl implements BudgetService {

  private final BudgetRepository budgetRepository;
  private final ExpenseRepository expenseRepository;
  private final ModelMapper modelMapper;

  private final AuthService authService;

  /**
   * It will fetch the expenses from database
   * @return list
   * */
  @Override
  public List<BudgetDTO> getAllBudgets() {
    //Call the repository method
    Long loggedInProfileId = authService.getLoggedInProfile().getId();
    List<BudgetEntity> list = budgetRepository.findByOwnerId(loggedInProfileId);
    log.info("Printing the data from repository {}", list);
    //Convert the Entity object to DTO object
    List<BudgetDTO> listOfBudgets = list.stream().map(budgetEntity -> mapToBudgetDTO(budgetEntity)).collect(Collectors.toList());
    //Return the list
    return listOfBudgets;
  }

  /**
   * It will fetch the single expense details from database
   * @param budgetId
   * @return ExpenseDTO
   * */
  @Override
  public BudgetDTO getBudgetByBudgetId(String budgetId) {
    BudgetEntity budgetEntity = getBudgetEntity(budgetId);
    log.info("Printing the budget entity details {}", budgetEntity);
    return mapToBudgetDTO(budgetEntity);
  }

  /**
   * It will delete the expense from database
   * @param budgetId
   * @return void
   * */
  @Override
  public void deleteBudgetByBudgetId(String budgetId) {
    BudgetEntity budgetEntity = getBudgetEntity(budgetId);
    log.info("Printing the budget entity {}", budgetEntity);
    budgetRepository.delete(budgetEntity);
  }

  /**
   * It will save the expense details to database
   * @param budgetDTO
   * @return ExpenseDTO
   * */
  @Override
  public BudgetDTO saveBudgetDetails(BudgetDTO budgetDTO) {
    ProfileEntity profileEntity = authService.getLoggedInProfile();
    BudgetEntity newBudgetEntity = mapToBudgetEntity(budgetDTO);
    newBudgetEntity.setBudgetId(UUID.randomUUID().toString());
    newBudgetEntity.setOwner(profileEntity);
    // calculate balance when starting the budget
    newBudgetEntity.setBalance(newBudgetEntity.getBudget());
    newBudgetEntity = getBalanceAtCreation(newBudgetEntity);
    newBudgetEntity = budgetRepository.save(newBudgetEntity);
    log.info("Printing the new budget entity details {}", newBudgetEntity);
    return mapToBudgetDTO(newBudgetEntity);
  }

  @Override
  public BudgetDTO updateBudgetDetails(BudgetDTO budgetDTO, String budgetId) {
    BudgetEntity existingBudget = getBudgetEntity(budgetId);
    BudgetEntity updatedBudgetEntity = mapToBudgetEntity(budgetDTO);
    updatedBudgetEntity.setId(existingBudget.getId());
    updatedBudgetEntity.setBudgetId(existingBudget.getBudgetId());
    updatedBudgetEntity.setCreatedAt(existingBudget.getCreatedAt());
    updatedBudgetEntity.setUpdatedAt(existingBudget.getUpdatedAt());
    updatedBudgetEntity.setOwner(authService.getLoggedInProfile());
    // update balance for new budget
    BigDecimal delta = updatedBudgetEntity.getBudget().subtract(existingBudget.getBudget());
    updatedBudgetEntity.setBalance(existingBudget.getBalance().add(delta));

    updatedBudgetEntity = budgetRepository.save(updatedBudgetEntity);
    log.info("Printing the updated budget entity details {}", updatedBudgetEntity);
    return mapToBudgetDTO(updatedBudgetEntity);
  }

  /**
   * Mapper method to map values from Expense dto to Expense entity
   * @param budgetDTO
   * @return ExpenseEntity
   * */
  private BudgetEntity mapToBudgetEntity(BudgetDTO budgetDTO) {
    return modelMapper.map(budgetDTO, BudgetEntity.class);
  }

  /**
   * Mapper method to convert expense entity to expense DTO
   * @param budgetEntity
   * @return ExpenseDTO
   * */
  private BudgetDTO mapToBudgetDTO(BudgetEntity budgetEntity) {
    return modelMapper.map(budgetEntity, BudgetDTO.class);
  }

  /**
   * Fetch the expense by expense id from database
   * @param budgetId
   * @return ExpenseEntity
   * */
  private BudgetEntity getBudgetEntity(String budgetId) {
    Long id = authService.getLoggedInProfile().getId();
    return budgetRepository.findByOwnerIdAndBudgetId(id, budgetId)
        .orElseThrow(() -> new ResourceNotFoundException("Budget not found for the budget id "+ budgetId));
  }

  private BudgetEntity getBalanceAtCreation(BudgetEntity budgetEntity) {
    Date date= budgetEntity.getYearmonth();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    List<ExpenseEntity> expenses = expenseRepository.findByOwnerIdAndYearAndMonth(budgetEntity.getOwner().getId(), year, month);
    BigDecimal balance = budgetEntity.getBalance();
    for (ExpenseEntity expense : expenses) {
      balance = balance.subtract(expense.getAmount());
    }
    budgetEntity.setBalance(balance);
    return budgetEntity;
  }
}