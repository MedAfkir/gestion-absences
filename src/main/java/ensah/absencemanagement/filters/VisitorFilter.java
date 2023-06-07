package ensah.absencemanagement.filters;

import ensah.absencemanagement.services.logging_events.LoggingEventService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class VisitorFilter extends OncePerRequestFilter {

    private final LoggingEventService loggingEventService;

    @Autowired
    public VisitorFilter(LoggingEventService loggingEventService) {
        this.loggingEventService = loggingEventService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if (!servletPath.startsWith("/assets") && request.getMethod().equals("GET")) {
            String ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null) ipAddress = request.getRemoteAddr();

            loggingEventService.createVisitEvent(
                    SecurityContextHolder.getContext().getAuthentication(),
                    servletPath,
                    request.getParameterMap(),
                    ipAddress
            );
        }

        filterChain.doFilter(request, response);
    }

}
