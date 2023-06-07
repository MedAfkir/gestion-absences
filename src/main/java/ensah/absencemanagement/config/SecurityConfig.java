package ensah.absencemanagement.config;

import ensah.absencemanagement.filters.AuthenticationFilter;
import ensah.absencemanagement.filters.VisitorFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static ensah.absencemanagement.models.users.User.Role.*;

@Slf4j
@EnableAsync
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final AuthenticationFilter authenticationFilter;
    private final VisitorFilter visitorFilter;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, AuthenticationFilter authenticationFilter, VisitorFilter visitorFilter) {
        this.userDetailsService = userDetailsService;
        this.authenticationFilter = authenticationFilter;
        this.visitorFilter = visitorFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/assets/**", "/login").permitAll();

            auth.requestMatchers("/", "/profil", "/profil/remove-image", "/profil/update-image", "/change-password")
                    .authenticated();
            auth.requestMatchers("/profil/receive-authorisation-demands",
                    "/profil/not-receive-authorisation-demands")
                        .hasAuthority(ENSEIGNANT.getAuthority());

            // ---------------------- ABSENCES ----------------------
            auth.requestMatchers(HttpMethod.GET, "/absences")
                    .authenticated();
            auth.requestMatchers(HttpMethod.GET, "/absences/create")
                    .hasAnyAuthority(CADRE_ADMINISTRATEUR.getAuthority(), ENSEIGNANT.getAuthority());
            auth.requestMatchers(HttpMethod.POST, "/absences")
                    .hasAuthority(ENSEIGNANT.getAuthority());
            auth.requestMatchers(HttpMethod.POST, "/absences/multiple", "/absences/multiple/validate")
                    .hasAuthority(CADRE_ADMINISTRATEUR.getAuthority());
            auth.requestMatchers(HttpMethod.GET, "/absences/*")
                    .authenticated();
            auth.requestMatchers(HttpMethod.POST, "/absences/*/cancel")
                    .hasAuthority(ENSEIGNANT.getAuthority());
            auth.requestMatchers(HttpMethod.POST, "/absences/*/state")
                    .hasAuthority(CADRE_ADMINISTRATEUR.getAuthority());
            auth.requestMatchers(HttpMethod.POST, "/absences/*")
                    .hasAuthority(CADRE_ADMINISTRATEUR.getAuthority());

            // ---------------------- USERS ----------------------
            auth.requestMatchers(HttpMethod.GET, "/users", "/users/create", "/users/*")
                    .hasAuthority(SUPER_ADMIN.getAuthority());
            auth.requestMatchers(HttpMethod.POST, "/users", "/users/*",
                            "/users/*/delete", "/users/*/change-password",
                            "/users/*/disable", "/users/*/enable",
                            "/users/*/lock", "/users/*/unlock",
                            "/users/*/role")
                    .hasAuthority(SUPER_ADMIN.getAuthority());

            auth.requestMatchers(HttpMethod.GET, "/etudiants", "/etudiants/*")
                    .hasAnyAuthority(
                            ENSEIGNANT.getAuthority(),
                            SUPER_ADMIN.getAuthority(),
                            CADRE_ADMINISTRATEUR.getAuthority());
            auth.requestMatchers(HttpMethod.POST, "/etudiants/*")
                    .hasAuthority(SUPER_ADMIN.getAuthority());

            // ---------------------- FILES ----------------------
            auth.requestMatchers(HttpMethod.GET, "/files/**")
                    .authenticated();

            // ---------------------- DEMANDES D'AUTORISATION D'ABSENCE ----------------------
            auth.requestMatchers(HttpMethod.GET, "/demandes-autorisation")
                    .hasAnyAuthority(ENSEIGNANT.getAuthority(), ETUDIANT.getAuthority());
            auth.requestMatchers(HttpMethod.GET, "/demandes-autorisation/create")
                    .hasAuthority(ETUDIANT.getAuthority());
            auth.requestMatchers(HttpMethod.POST, "/demandes-autorisation")
                    .hasAuthority(ETUDIANT.getAuthority());
            auth.requestMatchers(HttpMethod.POST, "/demandes-autorisation/state")
                    .hasAuthority(ENSEIGNANT.getAuthority());

            auth.requestMatchers(HttpMethod.GET, "/enseignants", "/enseignants/*")
                    .hasAnyAuthority(SUPER_ADMIN.getAuthority(), CADRE_ADMINISTRATEUR.getAuthority());

            auth.requestMatchers(HttpMethod.GET, "/cadres-admins", "/cadres-admins/*")
                    .hasAnyAuthority(SUPER_ADMIN.getAuthority());
            auth.requestMatchers(HttpMethod.POST,  "/cadres-admins/*")
                    .hasAuthority(SUPER_ADMIN.getAuthority());

            // ---------------------- PIECES JUSITIFCATIVES ----------------------
            auth.requestMatchers(HttpMethod.POST, "/pieces-justificatives")
                    .hasAnyAuthority(CADRE_ADMINISTRATEUR.getAuthority(), ETUDIANT.getAuthority());
            auth.requestMatchers("/pieces-justificatives/*", "/pieces-justificatives")
                    .authenticated();
            auth.requestMatchers(HttpMethod.POST, "/pieces-justificatives/*/state")
                    .hasAuthority(CADRE_ADMINISTRATEUR.getAuthority());

            // ---------------------- RECLAMATIONS ----------------------
            auth.requestMatchers(HttpMethod.GET, "/reclamations", "/reclamations/*")
                    .hasAnyAuthority(CADRE_ADMINISTRATEUR.getAuthority(), ETUDIANT.getAuthority());
            auth.requestMatchers(HttpMethod.POST, "/reclamations")
                    .hasAuthority(ETUDIANT.getAuthority());
            auth.requestMatchers(HttpMethod.POST, "/reclamations/*/answer")
                    .hasAuthority(CADRE_ADMINISTRATEUR.getAuthority());

            // ---------------------- INSCRIPTIONS ----------------------
            auth.requestMatchers(HttpMethod.GET, "/inscriptions", "/inscriptions/*")
                    .hasAnyAuthority(CADRE_ADMINISTRATEUR.getAuthority(), ETUDIANT.getAuthority());
            auth.requestMatchers(HttpMethod.POST, "/inscriptions")
                    .hasAuthority(ETUDIANT.getAuthority());
            auth.requestMatchers(HttpMethod.POST,
                            "/inscriptions/*/state",
                            "/inscriptions/*/refuse")
                    .hasAuthority(CADRE_ADMINISTRATEUR.getAuthority());

            // ---------------------- STRUCTURE PÉDAGOGIQUE ----------------------
            auth.requestMatchers("/filieres/**", "/matieres/**",
                            "/types-seances/**", "/annees-univ/**",
                            "/modules/**", "/niveaux/**")
                    .hasAuthority(CADRE_ADMINISTRATEUR.getAuthority());
        }).exceptionHandling(configurer -> {
            configurer.accessDeniedHandler((request, response, accessDeniedException) -> {
                log.error(accessDeniedException.getMessage());
                response.setStatus(403);
            });
            configurer.authenticationEntryPoint((request, response, authException) -> {
                log.error("Redirect to /login");
                response.sendRedirect("/login");
            });
        }).addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(visitorFilter, AuthenticationFilter.class)
            .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
