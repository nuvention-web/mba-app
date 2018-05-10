package mbaapp.endpoints;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Created by jnag on 2/24/18.
 */
@Controller
public class ViewController {


    @RequestMapping(value={"/school/**", "/profile/**", "/feedback/**", "/signin", "/404"})
    public String essays() {
        return "/index.html";
    }


}
