package com.contact.portfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.contact.portfolio.model.Contact;
import com.contact.portfolio.repo.ContactRepository;

@Controller
public class ContactController {

    @Autowired
    private ContactRepository contactRepo;

    @Autowired
    private JavaMailSender mailSender;

    // 🟢 Redirect root (/) to /contact
    @GetMapping("/")
    public String redirectToForm() {
        return "redirect:/contact";
    }

    // 🟢 Show contact form
    @GetMapping("/contact")
    public String showContactForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "contact"; // will load contact.html from templates/
    }

    // 🟢 Handle form submission
    @PostMapping("/contact")
    public String submitContactForm(@ModelAttribute Contact contact, Model model) {
        try {
            // Save to DB
            contactRepo.save(contact);

            // Send email
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo("borhadeparth24@gmail.com");
            msg.setSubject("Message from " + contact.getName());
            msg.setText("Name: " + contact.getName() + "\n" +
                        "Email: " + contact.getEmail() + "\n" +
                        "Mobile: " + contact.getMobile() + "\n" +
                        "Subject: " + contact.getSubject() + "\n\n" +
                        "Message:\n" + contact.getMessage());

            mailSender.send(msg);

            model.addAttribute("successMsg", "Message sent and saved!");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", "Something went wrong.");
        }
        return "contact";
    }
}
