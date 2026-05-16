package ru.itis.dis403.javaservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.dis403.javaservice.service.ImageServiceForNats;

import java.io.IOException;
import java.util.ArrayList;

@Controller
public class UploadImage2Controller {

    private final ImageServiceForNats imageService;

    public UploadImage2Controller(ImageServiceForNats imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/upload2")
    public String upload2PageRedirect() {
        return "redirect:/";
    }

    @PostMapping(value = "/upload2")
    public String uploadImage(Model model,
                              @RequestParam(value = "image", required = false) MultipartFile file) {
        model.addAttribute("imgs", new ArrayList<String>());
        model.addAttribute("imgs2", new ArrayList<String>());

        try {
            if (file != null && !file.isEmpty()) {
                System.out.println("Получили картинку");
                byte[] imageBytes = file.getBytes();
                long start = System.nanoTime();
                imageService.processImage(imageBytes);
                long end = System.nanoTime();
                System.out.println("Nats time: " + (end - start) / 1_000_000 + " ms");
            } else {
                System.out.println("Нет изображения");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "uploadresult";
    }
}
