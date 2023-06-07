package ensah.absencemanagement.filters;

import ensah.absencemanagement.dtos.accounts.AccountMapper;
import ensah.absencemanagement.models.accounts.Account;
import ensah.absencemanagement.utils.SessionManager;
import ensah.absencemanagement.utils.SessionUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final AccountMapper accountMapper;

    @Autowired
    public AuthenticationFilter(UserDetailsService userDetailsService, AccountMapper accountMapper) {
        this.userDetailsService = userDetailsService;
        this.accountMapper = accountMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        HttpSession session = request.getSession();
        SessionUser sessionUser = SessionManager.getUserSession(session);

        if (sessionUser != null) {
            try {
                Account account = (Account) userDetailsService.loadUserByUsername(
                        sessionUser.getUsername()
                );

                accountMapper.updateSessionUser(account, sessionUser);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        account.getUsername(),
                        null,
                        account.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (UsernameNotFoundException e) {
                session.invalidate();
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

}
