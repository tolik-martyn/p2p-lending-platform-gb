package com.example.borrowerservice.controller;

import com.example.borrowerservice.exception.UserNotFoundException;
import com.example.borrowerservice.model.Borrower;
import com.example.borrowerservice.model.UserDto;
import com.example.borrowerservice.service.BorrowerService;
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
@RequestMapping("/borrowers")
@Tag(name = "Borrower Controller", description = "Endpoints for managing borrowers")
public class BorrowerController {
    private final BorrowerService borrowerService;
    private final RestTemplate restTemplate;

    @Value("${url.gateway}")
    private String gatewayUrl;

    @Value("${url.userservice}")
    private String userServiceApiUrl;

    @Autowired
    public BorrowerController(BorrowerService borrowerService, RestTemplate restTemplate) {
        this.borrowerService = borrowerService;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    @Operation(summary = "Get all borrowers", description = "Returns a list of all borrowers.")
    public String getAllBorrowers(Model model) {
        List<Borrower> borrowers = borrowerService.getAllBorrowers();

        borrowers.forEach(borrower -> {
            UserDto userDto = getUserDtoById(borrower.getUserId());
            borrower.setUserDto(userDto);
        });

        model.addAttribute("borrowers", borrowers);
        return "borrower-list";
    }

    @GetMapping("/{borrowerId}")
    @Operation(summary = "Get borrower by ID", description = "Returns details of a borrower based on the provided ID.")
    public String getBorrowerById(@PathVariable("borrowerId") Long id, Model model) {
        Borrower borrower = borrowerService.getBorrowerById(id);
        UserDto userDto = getUserDtoById(borrower.getUserId());
        borrower.setUserDto(userDto);
        model.addAttribute("borrower", borrower);
        return "borrower-details";
    }

    @GetMapping("/add")
    @Operation(summary = "Show form to create borrower", description = "Displays a form for creating a new borrower.")
    public String showCreateBorrowerForm(Model model) {
        model.addAttribute("borrower", new Borrower());
        return "add-borrower";
    }

    @PostMapping("/add")
    @Operation(summary = "Create borrower", description = "Creates a new borrower with the provided details.")
    public String createBorrower(@ModelAttribute Borrower borrower) {
        getUserDtoById(borrower.getUserId());
        borrowerService.createBorrower(borrower);
        return "redirect:" + gatewayUrl;
    }

    @GetMapping("/{borrowerId}/delete")
    @Operation(summary = "Show form to delete borrower", description = "Displays a confirmation form for deleting a borrower.")
    public String showDeleteBorrowerForm(@PathVariable("borrowerId") Long id, Model model) {
        // Проверка на наличие заемщика с указанным id
        borrowerService.getBorrowerById(id);
        model.addAttribute("borrowerId", id);
        return "delete-borrower";
    }

    @PostMapping("/{borrowerId}/delete")
    @Operation(summary = "Delete borrower", description = "Deletes the borrower with the provided ID.")
    public String deleteBorrower(@PathVariable("borrowerId") Long id) {
        borrowerService.deleteBorrower(id);
        return "redirect:" + gatewayUrl;
    }

    @GetMapping("/{borrowerId}/update")
    @Operation(summary = "Show form to update borrower", description = "Displays a form for updating borrower details.")
    public String showUpdateBorrowerForm(@PathVariable("borrowerId") Long id, Model model) {
        model.addAttribute("borrower", borrowerService.getBorrowerById(id));
        return "update-borrower";
    }

    @PostMapping("/{borrowerId}/update")
    @Operation(summary = "Update borrower", description = "Updates the details of the borrower with the provided ID.")
    public String updateBorrower(@PathVariable("borrowerId") Long id, @ModelAttribute Borrower borrower) {
        getUserDtoById(borrower.getUserId());
        borrowerService.updateBorrower(id, borrower);
        return "redirect:" + gatewayUrl;
    }

    /**
     * Получает объект UserDto по его идентификатору.
     *
     * @param userId идентификатор пользователя
     * @return объект UserDto
     * @throws UserNotFoundException если пользователь с указанным идентификатором не найден
     */
    private UserDto getUserDtoById(Long userId) {
        try {
            return restTemplate.getForObject(userServiceApiUrl, UserDto.class, userId);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException(userId);
            }
            throw ex;
        }
    }
}
