package vttp.batch5.ssf.noticeboard.controllers;

// Use this class to write your request handlers

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.services.NoticeService;

import java.io.StringReader;
import java.net.URL;

@Controller
@RequestMapping
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/")
    public String getNotice(Model model) {
        model.addAttribute("notice", new Notice());
        return "notice";
    }

    @PostMapping("/notice")
    public String postNotice(@Valid Notice notice, BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            return "notice";
        }

        ResponseEntity<String> response = noticeService.postToNoticeServer(notice);

        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("id", response.getBody().replace("Notice successfully posted with ID: ", ""));
            return "view2";  // Success view
        } else {
            model.addAttribute("error", response.getBody());
            return "view3";  // Error view
        }

//        model.addAttribute("notice", notice);
//
//        return "notice";

    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        return noticeService.checkHealth();
    }


}
