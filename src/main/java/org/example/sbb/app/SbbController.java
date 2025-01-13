package org.example.sbb.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sbb")
public class SbbController {

    @GetMapping
    @ResponseBody
    public String hello(){
        return "안녕하세요 sbb에 오신 것을 환영합니다.";
    }
}
