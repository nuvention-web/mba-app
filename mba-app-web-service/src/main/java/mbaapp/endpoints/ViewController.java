package mbaapp.endpoints;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jnag on 2/24/18.
 */
@Controller
public class ViewController {


    @RequestMapping(value={"/essays/*", "/essays/*/*"})
    public String essays() {
        return "/index.html";
    }


    @RequestMapping(value={"/profile/*"})
    public String index() {
        return "/index.html";
    }

}
