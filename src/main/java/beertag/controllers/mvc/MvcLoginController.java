package beertag.controllers.mvc;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class MvcLoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/login/error")
    public String showError(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        AuthenticationException e = (AuthenticationException) session
                .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        String message = null;
        if (e != null) {
            message = e.getMessage();
        }
        model.addAttribute("error", message);
        return "login";
    }

}
