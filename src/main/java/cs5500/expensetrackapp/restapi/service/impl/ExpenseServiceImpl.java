package cs5500.expensetrackapp.restapi.service.impl;

import cs5500.expensetrackapp.restapi.dto.ExpenseDTO;
import cs5500.expensetrackapp.restapi.entity.ExpenseEntity;
import cs5500.expensetrackapp.restapi.repository.ExpenseRepository;
import cs5500.expensetrackapp.restapi.service.ExpenseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;



/**
 * Service implementation for Expense module
 * @author Shizhan Liu
 * */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseServiceImpl implements ExpenseService {
  private final ExpenseRepository expenseRepository;
  private final ModelMapper modelMapper;

//  private final AuthService authService;

  /**
   * It will fetch the expenses from database
   * @return list
   * */
  @Override
  public List<ExpenseDTO> getAllExpenses() {
    List<ExpenseEntity> list = expenseRepository.findAll();
    List<ExpenseDTO> listOfExpenses = list.stream().map(expenseEntity -> mapToExpenseDTO(expenseEntity)).collect(Collectors.toList());


    //Call the repository method
//    Long loggedInProfileId = authService.getLoggedInProfile().getId();
//    List<ExpenseEntity> list = expenseRepository.findByOwnerId(loggedInProfileId);
      log.info("Printing the data from repository {}", list);
//    //Convert the Entity object to DTO object
//    List<ExpenseDTO> listOfExpenses = list.stream().map(expenseEntity -> mapToExpenseDTO(expenseEntity)).collect(Collectors.toList());
//    //Return the list
    return listOfExpenses;
  }

//  @Override
//  public ExpenseDTO getExpenseByExpenseId(String expenseId) {
//    return null;
//  }
//
//  @Override
//  public void deleteExpenseByExpenseId(String expenseId) {
//
//  }
//
//  @Override
//  public ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO) {
//    return null;
//  }
//
//  @Override
//  public ExpenseDTO updateExpenseDetails(ExpenseDTO expenseDTO, String expenseId) {
//    return null;
//  }
//  /**
//   * Mapper method to map values from Expense dto to Expense entity
////   * @param expenseDTO
//   * @return ExpenseEntity
//   * */
////  private ExpenseEntity mapToExpenseEntity(ExpenseDTO expenseDTO) {
////    return modelMapper.map(expenseDTO, ExpenseEntity.class);
////  }

  /**
   * Mapper method to convert expense entity to expense DTO
   * @param expenseEntity
   * @return ExpenseDTO
   * */
  private ExpenseDTO mapToExpenseDTO(ExpenseEntity expenseEntity) {
    return modelMapper.map(expenseEntity, ExpenseDTO.class);
  }

}
