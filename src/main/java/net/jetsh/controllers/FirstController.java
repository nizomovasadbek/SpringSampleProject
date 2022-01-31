package net.jetsh.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/first")
public class FirstController {

    @GetMapping("/hello")
    public String hello(@RequestParam(value="name", required=false) String name ,
                        @RequestParam(value="surname", required=false) String surname,
                        Model model){

        model.addAttribute("message", "Hello, " + name + " " + surname);
        System.out.println("Hello, " + name + " " + surname);

        return "first/hello";
    }

    @GetMapping("/goodbye")
    public String goodBye(){
        return "first/goodbye";
    }

    @GetMapping("/calculator")
    public String calculator(Model model, @RequestParam("a") int a, @RequestParam("b") int b,
                             @RequestParam("action") String action){
        double result=0;
        if(action.equals("multi")){
            result = a*b;
        }
        if(action.equals("add")){
            result = a+b;
        }
        if(action.equals("sub")){
            result = a-b;
        }
        if(action.equals("div")){
            if(b!=0)
            result = a/b;
        }

        model.addAttribute("result", result);

        return "result/result";
    }
}
