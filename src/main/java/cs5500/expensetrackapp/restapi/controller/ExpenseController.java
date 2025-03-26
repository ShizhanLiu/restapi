package cs5500.expensetrackapp.restapi.controller;


import cs5500.expensetrackapp.restapi.dto.ExpenseDTO;
import cs5500.expensetrackapp.restapi.io.ExpenseResponse;
import cs5500.expensetrackapp.restapi.service.ExpenseService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
/**
 *
 * This is controller class for Expense module
 * @author:Shizhan Liu
 *
 *
 *
 * */
public class ExpenseController {

  private final ExpenseService expenseService;
  private final ModelMapper modelMapper;
  /**
   * It will fetch the expenses from database
   * @return list
   * */
  @GetMapping("/expenses")
  public List<ExpenseResponse> getExpenses() {
   log.info("API GET /expenses called");
    //Call the service method
    List<ExpenseDTO> list = expenseService.getAllExpenses();
    log.info("Printing the data from service {}", list);
    //Convert the Expense DTO to Expense Response
    List<ExpenseResponse> response = list.stream().map(expenseDTO -> mapToExpenseResponse(expenseDTO)).collect(
        Collectors.toList());
    //Return the list/response
    return response;
  }
  /**
   * Mapper method for converting expense dto object to expense response
   * @param expenseDTO
   * @return ExpenseResponse
   * */
  private ExpenseResponse mapToExpenseResponse(ExpenseDTO expenseDTO) {
    return modelMapper.map(expenseDTO, ExpenseResponse.class);
  }
}
