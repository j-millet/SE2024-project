package pl.put.poznan.jsontools.gui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class GuiController {

    @GetMapping
    public String getGui() {
        return "index.html";
    }
}