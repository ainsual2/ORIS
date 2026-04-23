package ru.itis.dis403.spring.security.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.dis403.spring.security.model.Phone;
import ru.itis.dis403.spring.security.service.PersonService;
import ru.itis.dis403.spring.security.service.PhoneService;
import ru.itis.dis403.spring.security.model.Person;

import java.util.List;

@Controller
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;
    private final PhoneService phoneService;

    public PersonController(PersonService personService, PhoneService phoneService) {
        this.personService = personService;
        this.phoneService = phoneService;
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute  ("person", new Person());
        model.addAttribute("allPhones", phoneService.findAll());
        return "form";
    }

    @PostMapping("/save")
    public String savePerson(@ModelAttribute Person person,
                             @RequestParam(required = false) List<Long> phoneIds,
                             @RequestParam(required = false) List<String> newPhones) {

        // 1. Привязываем существующие телефоны
        if (phoneIds != null) {
            for (Long phoneId : phoneIds) {
                phoneService.findById(phoneId).ifPresent(phone -> {
                    person.getPhones().add(phone);
                });
            }
        }

        // 2. Создаем новые телефоны
        if (newPhones != null) {
            for (String num : newPhones) {
                if (num != null && !num.trim().isEmpty()) {
                    Phone phone = new Phone();
                    phone.setNumber(num.trim());
                    // Добавляем связь с обеих сторон (полезно для Hibernate)
                    person.getPhones().add(phone);
                }
            }
        }

        personService.save(person); // Теперь благодаря PERSIST всё сохранится в person_phones
        return "redirect:/persons/all";
    }

    @GetMapping("/all")
    public String listAll(Model model) {
        model.addAttribute("persons", personService.findAll());
        model.addAttribute("allPhones", phoneService.findAll());
        model.addAttribute("phones", personService.findAll());
        return "list";
    }
}