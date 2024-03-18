package com.example.lenderservice.controller;

import com.example.lenderservice.exception.UserNotFoundException;
import com.example.lenderservice.model.Lender;
import com.example.lenderservice.model.UserDto;
import com.example.lenderservice.service.LenderService;
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
@RequestMapping("/lenders")
@Tag(name = "Lender Controller", description = "Endpoints for managing lenders")
public class LenderController {
    private final LenderService lenderService;
    private final RestTemplate restTemplate;

    @Value("${url.gateway}")
    private String gatewayUrl;

    @Value("${url.userservice}")
    private String userServiceApiUrl;

    @Autowired
    public LenderController(LenderService lenderService, RestTemplate restTemplate) {
        this.lenderService = lenderService;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    @Operation(summary = "Get all lenders", description = "Returns a list of all lenders.")
    public String getAllLenders(Model model) {
        List<Lender> lenders = lenderService.getAllLenders();

        lenders.forEach(lender -> {
            UserDto userDto = getUserDtoById(lender.getUserId());
            lender.setUserDto(userDto);
        });

        model.addAttribute("lenders", lenders);
        return "lender-list";
    }

    @GetMapping("/{lenderId}")
    @Operation(summary = "Get lender by ID", description = "Returns details of a lender based on the provided ID.")
    public String getLenderById(@PathVariable("lenderId") Long id, Model model) {
        Lender lender = lenderService.getLenderById(id);
        UserDto userDto = getUserDtoById(lender.getUserId());
        lender.setUserDto(userDto);
        model.addAttribute("lender", lender);
        return "lender-details";
    }

    @GetMapping("/add")
    @Operation(summary = "Show form to create lender", description = "Displays a form for creating a new lender.")
    public String showCreateLenderForm(Model model) {
        model.addAttribute("lender", new Lender());
        return "add-lender";
    }

    @PostMapping("/add")
    @Operation(summary = "Create lender", description = "Creates a new lender with the provided details.")
    public String createLender(@ModelAttribute Lender lender) {
        getUserDtoById(lender.getUserId());
        lenderService.createLender(lender);
        return "redirect:" + gatewayUrl;
    }

    @GetMapping("/{lenderId}/delete")
    @Operation(summary = "Show form to delete lender", description = "Displays a confirmation form for deleting a lender.")
    public String showDeleteLenderForm(@PathVariable("lenderId") Long id, Model model) {
        lenderService.getLenderById(id);
        model.addAttribute("lenderId", id);
        return "delete-lender";
    }

    @PostMapping("/{lenderId}/delete")
    @Operation(summary = "Delete lender", description = "Deletes the lender with the provided ID.")
    public String deleteLender(@PathVariable("lenderId") Long id) {
        lenderService.deleteLender(id);
        return "redirect:" + gatewayUrl;
    }

    @GetMapping("/{lenderId}/update")
    @Operation(summary = "Show form to update lender", description = "Displays a form for updating lender details.")
    public String showUpdateLenderForm(@PathVariable("lenderId") Long id, Model model) {
        model.addAttribute("lender", lenderService.getLenderById(id));
        return "update-lender";
    }

    @PostMapping("/{lenderId}/update")
    @Operation(summary = "Update lender", description = "Updates the details of the lender with the provided ID.")
    public String updateLender(@PathVariable("lenderId") Long id, @ModelAttribute Lender lender) {
        getUserDtoById(lender.getUserId());
        lenderService.updateLender(id, lender);
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
