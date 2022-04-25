package ru.geekbrains.lesson04springboot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.lesson04springboot.persist.User;
import ru.geekbrains.lesson04springboot.persist.UserRepository;

import javax.validation.Valid;


@RequestMapping("/user")
@Controller
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listPage(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user";
    }

    @GetMapping("/{id}")
    public String form(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userRepository.findById(id));
        return "user_form";
    }

    @GetMapping("/new")
    public String form(Model model) {
    model.addAttribute("user", new User(""));
        return "user_form";
    }

    @PostMapping
    public String save(@Valid User user, BindingResult binding) {
        if (binding.hasErrors()) {
            return "user_form";
        }
        if (!user.getPassword().equals(user.getMatchingPassword())) {
            binding.rejectValue("password", "","Password doesn't match!");
            return "user_form";
        }
        userRepository.save(user);
        return "redirect:/user";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable long id) {
        userRepository.delete(id);
        return "redirect:/user";
    }
}
