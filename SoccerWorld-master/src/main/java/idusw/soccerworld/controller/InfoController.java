package idusw.soccerworld.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class InfoController {
    @RequestMapping(method=RequestMethod.GET,value = "info/index")
    public String goIndex(){
        return "info/index";
    }

    @RequestMapping(method=RequestMethod.GET,value = "info/playerinfo")
    public String goplayerinfo(){
        return "info/playerinfo";
    }

    @RequestMapping(method=RequestMethod.GET,value = "info/playerreview")
    public String goplayerreview(){
        return "info/playerreview";
    }

    @RequestMapping(method=RequestMethod.GET,value = "info/teaminfo")
    public String goteaminfo(){
        return "info/teaminfo";
    }

    @RequestMapping(method=RequestMethod.GET,value = "info/teamreview")
    public String goteamreview(){return "info/teamreview"; }
}
