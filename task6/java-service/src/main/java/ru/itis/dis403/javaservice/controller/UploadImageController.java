package ru.itis.dis403.javaservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.dis403.javaservice.service.ImageService;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Controller
public class UploadImageController {

    private final ImageService imageService;

    public UploadImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/upload")
    public String uploadPageRedirect() {
        return "redirect:/";
    }

    @PostMapping(value = "/upload")
    public String uploadImg(Model model, @RequestParam(value = "image", required = false) MultipartFile file) {
        model.addAttribute("imgs", new ArrayList<String>());

        try {
            if (file != null && !file.isEmpty()) {
                System.out.println("получили картинку");
                byte[] imageBytes = file.getBytes();
                long start = System.nanoTime();
                imageService.processImageBlocking(imageBytes);
                long end = System.nanoTime();
                System.out.println("HTTP time: " + (end - start) / 1_000_000 + " ms");
            } else {
                System.out.println("нет изображения");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        return "uploadresult";
    }
}
