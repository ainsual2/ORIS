package main.java.ru.itis.dis403.javaservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.dis403.javaservice.service.ImageService;
import ru.itis.dis403.javaservice.service.ImageServiceForNats;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final ImageService imageService;
    private final ImageServiceForNats imageServiceForNats;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/showimg")
    public String showImg(Model model) {
        List<String> imgs = imageService.getImgList();
        List<String> imgs2 = imageServiceForNats.getImgList();
        model.addAttribute("imgs", imgs != null ? imgs : new ArrayList<String>());
        model.addAttribute("imgs2", imgs2 != null ? imgs2 : new ArrayList<String>());
        return "uploadresult";
    }
}

