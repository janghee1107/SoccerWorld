package idusw.soccerworld.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CommunityController {
    @RequestMapping(method = RequestMethod.GET,value = "/")
    public String goIndex(){
        return "board/index";
    }

    @GetMapping("/board/meme_list")
    public String meme_list(){
        return "board/meme_list";
    }

    @GetMapping("/board/meme_detail")
    public String meme_detail(){
        return "board/meme_detail";
    }

    @GetMapping("/board/free_list")
    public String free_list(){
        return "board/free_list";
    }

    @GetMapping("/board/free_detail")
    public String free_detail(){
        return "board/free_detail";
    }

    @GetMapping("/board/debate_list")
    public String debate_list(){
        return "board/debate_list";
    }

    @GetMapping("/board/debate_detail")
    public String debate_detail(){
        return "board/debate_detail";
    }

    @GetMapping("/board/history_list")
    public String history_list(){
        return "board/history_list";
    }

    @GetMapping("/board/history_detail")
    public String history_detail(){
        return "board/history_detail";
    }

    @GetMapping("/board/prospect_list")
    public String prospect_list(){
        return "board/prospect_list";
    }

    @GetMapping("/board/prospect_detail")
    public String prospect_detail(){
        return "board/prospect_detail";
    }

}
