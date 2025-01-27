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
    private final boolean isActive = false;
    private final boolean isNotActive = true;

    @GetMapping(value="/index")
    public String index(Model model) {
        model.addAttribute("requests",appReqService.getAllApplicationRequest());
        model.addAttribute("isActive", isActive);
        model.addAttribute("isNotActive", isNotActive);
        return "index";
    }



    @GetMapping("/add-req")
    public String addReq(Model model) {
        model.addAttribute("isActive", isActive);
        model.addAttribute("isNotActive", isNotActive);
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
        model.addAttribute("isActive", isActive);
        model.addAttribute("isNotActive", isNotActive);
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

    @GetMapping("/new_request")
    public String newRequest(@RequestParam boolean isActive,Model model) {
        if(!isActive) {
            model.addAttribute("requests", appReqService.getAllHandledRequest(isActive));
            model.addAttribute("isActive", isActive);
            model.addAttribute("isNotActive", isNotActive);
            return "new_request";
        }else{
            return "new_request?error";
        }
    }

    @GetMapping("/processed_req")
    public String processedRequest(@RequestParam boolean isNotActive,Model model) {
        
        if (isNotActive) {
            model.addAttribute("requests", appReqService.getAllHandledRequest(isNotActive));
            model.addAttribute("isActive", isActive);
            model.addAttribute("isNotActive", isNotActive);
            return "processed_req";
        }else{
            return "index?error2";
        }
    }

}
