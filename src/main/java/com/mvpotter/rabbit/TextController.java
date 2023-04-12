package com.mvpotter.rabbit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/text")
public class TextController {

    private final TextService textService;

    public TextController(TextService textService) {
        this.textService = textService;
    }

    @GetMapping
    public Event getText() {
        return new Event(textService.getResponse());
    }

    @PutMapping
    public void putText(@RequestBody Event holder) {
        textService.send(holder.getText());
    }

}
