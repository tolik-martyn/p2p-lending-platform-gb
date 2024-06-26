package com.example.loanservice.controller;

import com.example.loanservice.exception.BorrowerNotFoundException;
import com.example.loanservice.exception.LenderNotFoundException;
import com.example.loanservice.exception.UserNotFoundException;
import com.example.loanservice.model.BorrowerDto;
import com.example.loanservice.model.LenderDto;
import com.example.loanservice.model.Loan;
import com.example.loanservice.model.UserDto;
import com.example.loanservice.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/loans")
@Tag(name = "Loan Controller", description = "Endpoints for managing loans")
public class LoanController {
    private final LoanService loanService;
    private final RestTemplate restTemplate;

    @Value("${url.gateway}")
    private String gatewayUrl;

    @Value("${url.userservice}")
    private String userserviceApiUrl;

    @Value("${url.lenderservice}")
    private String lenderserviceApiUrl;

    @Value("${url.borrowerservice}")
    private String borrowerserviceApiUrl;

    @Autowired
    public LoanController(LoanService loanService, RestTemplate restTemplate) {
        this.loanService = loanService;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    @Operation(summary = "Get all loans", description = "Returns a list of all loans.")
    public String getAllLoans(Model model) {
        List<Loan> loans = loanService.getAllLoans();

        loans.forEach(loan -> {
            LenderDto lenderDto = getDtoById(lenderserviceApiUrl, LenderDto.class, loan.getLenderId());
            loan.setLenderDto(lenderDto);
            UserDto userLenderDto = getDtoById(userserviceApiUrl, UserDto.class, lenderDto.getUserId());
            loan.setUserLenderDto(userLenderDto);
            BorrowerDto borrowerDto = getDtoById(borrowerserviceApiUrl, BorrowerDto.class, loan.getBorrowerId());
            loan.setBorrowerDto(borrowerDto);
            UserDto userBorrowerDto = getDtoById(userserviceApiUrl, UserDto.class, borrowerDto.getUserId());
            loan.setUserBorrowerDto(userBorrowerDto);
        });

        model.addAttribute("loans", loans);
        return "loan-list";
    }

    @GetMapping("/{loanId}")
    @Operation(summary = "Get loan by ID", description = "Returns details of a loan based on the provided ID.")
    public String getLoanById(@PathVariable("loanId") Long id, Model model) {
        Loan loan = loanService.getLoanById(id);

        LenderDto lenderDto = getDtoById(lenderserviceApiUrl, LenderDto.class, loan.getLenderId());
        loan.setLenderDto(lenderDto);
        UserDto userLenderDto = getDtoById(userserviceApiUrl, UserDto.class, lenderDto.getUserId());
        loan.setUserLenderDto(userLenderDto);
        BorrowerDto borrowerDto = getDtoById(borrowerserviceApiUrl, BorrowerDto.class, loan.getBorrowerId());
        loan.setBorrowerDto(borrowerDto);
        UserDto userBorrowerDto = getDtoById(userserviceApiUrl, UserDto.class, borrowerDto.getUserId());
        loan.setUserBorrowerDto(userBorrowerDto);

        model.addAttribute("loan", loan);
        return "loan-details";
    }

    @GetMapping("/add")
    @Operation(summary = "Show form to create loan", description = "Displays a form for creating a new loan.")
    public String showCreateLoanForm(Model model) {
        model.addAttribute("loan", new Loan());
        return "add-loan";
    }

    @PostMapping("/add")
    @Operation(summary = "Create loan", description = "Creates a new loan with the provided details.")
    public String createLoan(@ModelAttribute Loan loan) {
        getDtoById(lenderserviceApiUrl, LenderDto.class, loan.getLenderId());
        getDtoById(borrowerserviceApiUrl, BorrowerDto.class, loan.getBorrowerId());
        loanService.createLoan(loan);
        return "redirect:" + gatewayUrl;
    }

    @GetMapping("/{loanId}/delete")
    @Operation(summary = "Show form to delete loan", description = "Displays a confirmation form for deleting a loan.")
    public String showDeleteLoanForm(@PathVariable("loanId") Long id, Model model) {
        // Проверка на наличие кредита с указанным id
        loanService.getLoanById(id);
        model.addAttribute("loanId", id);
        return "delete-loan";
    }

    @PostMapping("/{loanId}/delete")
    @Operation(summary = "Delete loan", description = "Deletes the loan with the provided ID.")
    public String deleteLoan(@PathVariable("loanId") Long id) {
        loanService.deleteLoan(id);
        return "redirect:" + gatewayUrl;
    }

    @GetMapping("/{loanId}/update")
    @Operation(summary = "Show form to update loan", description = "Displays a form for updating loan details.")
    public String showUpdateLoanForm(@PathVariable("loanId") Long id, Model model) {
        model.addAttribute("loan", loanService.getLoanById(id));
        return "update-loan";
    }

    @PostMapping("/{loanId}/update")
    @Operation(summary = "Update loan", description = "Updates the details of the loan with the provided ID.")
    public String updateLoan(@PathVariable("loanId") Long id, @ModelAttribute Loan loan) {
        getDtoById(lenderserviceApiUrl, LenderDto.class, loan.getLenderId());
        getDtoById(borrowerserviceApiUrl, BorrowerDto.class, loan.getBorrowerId());
        loanService.updateLoan(id, loan);
        return "redirect:" + gatewayUrl;
    }

    /**
     * Получает объект DTO по его идентификатору с использованием указанного URL-адреса API.
     * Если объект не найден, генерирует соответствующее исключение.
     *
     * @param apiUrl   URL-адрес API для запроса объекта DTO.
     * @param dtoClass Класс объекта DTO.
     * @param id       Идентификатор объекта DTO.
     * @param <T>      Тип объекта DTO.
     * @return Объект DTO.
     */
    private <T> T getDtoById(String apiUrl, Class<T> dtoClass, Long id) {
        try {
            return restTemplate.getForObject(apiUrl, dtoClass, id);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw createNotFoundException(dtoClass, id);
            }
            throw ex;
        }
    }

    /**
     * Создает исключение для указанного класса DTO и идентификатора.
     *
     * @param dtoClass Класс объекта DTO.
     * @param id       Идентификатор объекта DTO.
     * @param <T>      ип объекта DTO.
     * @return Исключение.
     */
    private <T> RuntimeException createNotFoundException(Class<T> dtoClass, Long id) {
        if (dtoClass.equals(UserDto.class)) {
            return new UserNotFoundException(id);
        } else if (dtoClass.equals(LenderDto.class)) {
            return new LenderNotFoundException(id);
        } else if (dtoClass.equals(BorrowerDto.class)) {
            return new BorrowerNotFoundException(id);
        } else {
            return new RuntimeException("Not found");
        }
    }
}
