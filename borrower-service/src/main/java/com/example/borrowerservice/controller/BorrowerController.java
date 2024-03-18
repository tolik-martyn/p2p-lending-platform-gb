package com.example.borrowerservice.controller;

import com.example.borrowerservice.exception.UserNotFoundException;
import com.example.borrowerservice.model.Borrower;
import com.example.borrowerservice.model.UserDto;
import com.example.borrowerservice.service.BorrowerService;
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
public class BorrowerController {
    private final BorrowerService borrowerService;
    private final RestTemplate restTemplate;

    @Value("${gateway.url}")
    private String gatewayUrl;

    @Value("${userservice.url}")
    private String userserviceApiUrl;

    @Autowired
    public BorrowerController(BorrowerService borrowerService, RestTemplate restTemplate) {
        this.borrowerService = borrowerService;
        this.restTemplate = restTemplate;
    }

    @GetMapping
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
    public String getBorrowerById(@PathVariable("borrowerId") Long id, Model model) {
        Borrower borrower = borrowerService.getBorrowerById(id);
        UserDto userDto = getUserDtoById(borrower.getUserId());
        borrower.setUserDto(userDto);
        model.addAttribute("borrower", borrower);
        return "borrower-details";
    }

    @GetMapping("/add")
    public String showCreateBorrowerForm(Model model) {
        model.addAttribute("borrower", new Borrower());
        return "add-borrower";
    }

    @PostMapping("/add")
    public String createBorrower(@ModelAttribute Borrower borrower) {
        getUserDtoById(borrower.getUserId());
        borrowerService.createBorrower(borrower);
        return "redirect:" + gatewayUrl;
    }

    @GetMapping("/{borrowerId}/delete")
    public String showDeleteBorrowerForm(@PathVariable("borrowerId") Long id, Model model) {
        borrowerService.getBorrowerById(id);
        model.addAttribute("borrowerId", id);
        return "delete-borrower";
    }

    @PostMapping("/{borrowerId}/delete")
    public String deleteBorrower(@PathVariable("borrowerId") Long id) {
        borrowerService.deleteBorrower(id);
        return "redirect:" + gatewayUrl;
    }

    @GetMapping("/{borrowerId}/update")
    public String showUpdateBorrowerForm(@PathVariable("borrowerId") Long id, Model model) {
        model.addAttribute("borrower", borrowerService.getBorrowerById(id));
        return "update-borrower";
    }

    @PostMapping("/{borrowerId}/update")
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
            return restTemplate.getForObject(userserviceApiUrl, UserDto.class, userId);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException(userId);
            }
            throw ex;
        }
    }
}
