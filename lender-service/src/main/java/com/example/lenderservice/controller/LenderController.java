package com.example.lenderservice.controller;

import com.example.lenderservice.exception.UserNotFoundException;
import com.example.lenderservice.model.Lender;
import com.example.lenderservice.model.UserDto;
import com.example.lenderservice.service.LenderService;
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
public class LenderController {
    private final LenderService lenderService;
    private final RestTemplate restTemplate;

    @Value("${gateway.url}")
    private String gatewayUrl;

    @Value("${userservice.url}")
    private String userserviceApiUrl;

    @Autowired
    public LenderController(LenderService lenderService, RestTemplate restTemplate) {
        this.lenderService = lenderService;
        this.restTemplate = restTemplate;
    }

    @GetMapping
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
    public String getLenderById(@PathVariable("lenderId") Long id, Model model) {
        Lender lender = lenderService.getLenderById(id);
        UserDto userDto = getUserDtoById(lender.getUserId());
        lender.setUserDto(userDto);
        model.addAttribute("lender", lender);
        return "lender-details";
    }

    @GetMapping("/add")
    public String showCreateLenderForm(Model model) {
        model.addAttribute("lender", new Lender());
        return "add-lender";
    }

    @PostMapping("/add")
    public String createLender(@ModelAttribute Lender lender) {
        getUserDtoById(lender.getUserId());
        lenderService.createLender(lender);
        return "redirect:" + gatewayUrl;
    }

    @GetMapping("/{lenderId}/delete")
    public String showDeleteLenderForm(@PathVariable("lenderId") Long id, Model model) {
        lenderService.getLenderById(id);
        model.addAttribute("lenderId", id);
        return "delete-lender";
    }

    @PostMapping("/{lenderId}/delete")
    public String deleteLender(@PathVariable("lenderId") Long id) {
        lenderService.deleteLender(id);
        return "redirect:" + gatewayUrl;
    }

    @GetMapping("/{lenderId}/update")
    public String showUpdateLenderForm(@PathVariable("lenderId") Long id, Model model) {
        model.addAttribute("lender", lenderService.getLenderById(id));
        return "update-lender";
    }

    @PostMapping("/{lenderId}/update")
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
            return restTemplate.getForObject(userserviceApiUrl, UserDto.class, userId);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new UserNotFoundException(userId);
            }
            throw ex;
        }
    }
}
