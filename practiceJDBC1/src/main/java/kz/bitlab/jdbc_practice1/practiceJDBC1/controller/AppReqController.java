package kz.bitlab.jdbc_practice1.practiceJDBC1.controller;

import kz.bitlab.jdbc_practice1.practiceJDBC1.model.ApplicationRequest;
import kz.bitlab.jdbc_practice1.practiceJDBC1.service.AppReqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AppReqController {

    private final AppReqService appReqService;

    @GetMapping(value="/index")
    public String index(Model model) {
        model.addAttribute("requests",appReqService.getAllApplicationRequest());
        return "index";
    }



    @GetMapping("/add-req")
    public String addReq() {
        return "add-req";
    }

    @PostMapping("save_req")
    public String save_req(@RequestParam(name = "reqName") String userName,
                           @RequestParam(name = "reqCourse") String courseName,
                           @RequestParam(name ="reqComment") String comment,
                           @RequestParam(name ="reqPhone") String phone){
        ApplicationRequest req = ApplicationRequest.builder()
                .id(null)
                .userName(userName)
                .courseName(courseName)
                .commentary(comment)
                .phone(phone)
                .handled(false)
                .build();
        boolean statuc = appReqService.addRequest(req);
        if (statuc) {
            return "redirect:/index";
        }else {
            return "redirect:/add-req?error";
        }
    }

    @GetMapping("/details")
    public String details(Model model, @RequestParam(name="id") Long id) {
        model.addAttribute("requests",appReqService.getRequestById(id));
        return "details";
    }

    @PostMapping("/change_req")
    public String checkHandle(@RequestParam(name = "id") Long id ,
                              @RequestParam(name = "handled") boolean handled,Model model) {
        if (!handled) {
            handled = true;
            model.addAttribute("requests",appReqService.updateReqHandled(id,handled));
            return "redirect:/index";
        }else{
            return "redirect:/details?error";
        }
    }

    @PostMapping("delete_req")
    public String deleteReq(@RequestParam(name = "id") Long reqID) {
        boolean status = appReqService.deleteRequestById(reqID);
        if (status) {
            return "redirect:/index";
        }else{
            return "redirect:/add-req?error";
        }
    }

    @GetMapping("new_request")
    public String newRequest(Model model) {
        System.out.println(">>>>>>>>>>>>>");
        //boolean statuc = true;
        model.addAttribute("requests",appReqService.getAllNewRequest());
        return "new_request";
    }
}
