package ensah.absencemanagement.handlers;

import ensah.absencemanagement.exceptions.not_found.NotFoundException;
import ensah.absencemanagement.utils.SessionManager;
import ensah.absencemanagement.utils.SessionUser;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;

@ControllerAdvice
public class GlobalControllerHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccess(HttpServletResponse response) throws IOException {
        response.sendError(403);
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "error";
    }

}
