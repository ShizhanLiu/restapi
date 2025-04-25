package cs5500.expensetrackapp.restapi.controller;

import cs5500.expensetrackapp.restapi.dto.BudgetDTO;
import cs5500.expensetrackapp.restapi.io.BudgetRequest;
import cs5500.expensetrackapp.restapi.io.BudgetResponse;
import cs5500.expensetrackapp.restapi.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
/**
 * This is controller class for Expense module
 * @author Anne
 * */
@RestController
@RequiredArgsConstructor
@Slf4j
public class BudgetController {

  private final BudgetService budgetService;
  private final ModelMapper modelMapper;


  /**
   * It will fetch the expenses from database
   * @return list
   * */
  @GetMapping("/budgets")
  public List<BudgetResponse> getBudgets() {
    log.info("API GET /budgets called");
    //Call the service method
    List<BudgetDTO> list = budgetService.getAllBudgets();
    log.info("Printing the data from service {}", list);
    //Convert the Expense DTO to Expense Response
    List<BudgetResponse> response = list.stream().map(budgetDTO -> mapToBudgetResponse(budgetDTO)).collect(Collectors.toList());
    //Return the list/response
    return response;
  }

  /**
   * It will fetch the single expense from database
   * @param budgetId
   * @return BudgetResponse
   * */
  @GetMapping("/budgets/{budgetId}")
  public BudgetResponse getResponseById(@PathVariable String budgetId) {
    log.info("API GET /budgets/{} called", budgetId);
    BudgetDTO budgetDTO = budgetService.getBudgetByBudgetId(budgetId);
    log.info("Printing the budget details {}", budgetDTO);
    return mapToBudgetResponse(budgetDTO);
  }

  /**
   * It will delete the expense from database
   * @param budgetId
   * @return void
   * */
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/budgets/{budgetId}")
  public void deleteBudgetByBudgetId(@PathVariable String budgetId) {
    log.info("API DELETE /budgets/{} called", budgetId);
    budgetService.deleteBudgetByBudgetId(budgetId);
  }

  /**
   * It will save the expense details to database
   * @param budgetRequest
   * @return BudgetResponse
   * */
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/budgets")
  public BudgetResponse saveBudgetDetails(@Valid @RequestBody BudgetRequest budgetRequest) {
    log.info("API POST /budgets called {}", budgetRequest);
    BudgetDTO budgetDTO = mapToBudgetDTO(budgetRequest);
    budgetDTO = budgetService.saveBudgetDetails(budgetDTO);
    log.info("Printing the budgetDTO dto {}", budgetDTO);
    return mapToBudgetResponse(budgetDTO);
  }

  /**
   * It will update the expense details to database
   * @param updateRequest
   * @param budgetId
   * @return BudgetResponse
   * */
  @PutMapping("/budgets/{budgetId}")
  public BudgetResponse updateBudgetDetails(@Valid @RequestBody BudgetRequest updateRequest, @PathVariable String budgetId) {
    log.info("API PUT /budgets/{} request body {}", budgetId, updateRequest);
    BudgetDTO updatedBudgetDTO = mapToBudgetDTO(updateRequest);
    updatedBudgetDTO = budgetService.updateBudgetDetails(updatedBudgetDTO, budgetId);
    log.info("Printing the updated budgetId dto details {}", updatedBudgetDTO);
    return mapToBudgetResponse(updatedBudgetDTO);
  }

  /**
   * Mapper method to map values from Expense request to expense dto
   * @param budgetRequest
   * @return BudgetDTO
   * */
  private BudgetDTO mapToBudgetDTO(BudgetRequest budgetRequest) {
    return modelMapper.map(budgetRequest, BudgetDTO.class);
  }

  /**
   * Mapper method for converting expense dto object to expense response
   * @param budgetDTO
   * @return BudgetResponse
   * */
  private BudgetResponse mapToBudgetResponse(BudgetDTO budgetDTO) {
    return modelMapper.map(budgetDTO, BudgetResponse.class);
  }
}
