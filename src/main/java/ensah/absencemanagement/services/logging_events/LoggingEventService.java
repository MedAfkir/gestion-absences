package ensah.absencemanagement.services.logging_events;

import ensah.absencemanagement.dtos.logging_events.LoggingEventMapper;
import ensah.absencemanagement.dtos.logging_events.VisitEventDTO;
import ensah.absencemanagement.models.accounts.Account;
import ensah.absencemanagement.models.logging_events.LoggingEvent;
import ensah.absencemanagement.repositories.AccountRepository;
import ensah.absencemanagement.repositories.LoggingEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoggingEventService {

    private final LoggingEventRepository repository;
    private final LoggingEventMapper mapper;
    private final AccountRepository accountRepository;

    @Autowired
    public LoggingEventService(LoggingEventRepository repository, LoggingEventMapper mapper, AccountRepository accountRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.accountRepository = accountRepository;
    }

    public Page<VisitEventDTO> getVisitsByUserId(Long userId, int page) {
        return repository.findByUserIdAndType(
                        userId,
                        LoggingEvent.Type.VISIT,
                        PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdAt"))))
                .map(mapper::mapVisit);
    }

    public List<VisitEventDTO> getVisitsByUserId() {
        return repository.findByType(LoggingEvent.Type.VISIT, Pageable.unpaged())
                .map(mapper::mapVisit)
                .getContent();
    }

    public Page<VisitEventDTO> getVisitsByUserId(int page) {
        return repository.findByType(
                        LoggingEvent.Type.VISIT,
                        PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdAt"))))
                .map(mapper::mapVisit);
    }

    @Async
    public void createVisitEvent(
            Authentication authentication,
            String path,
            Map<String, String[]> parameters,
            String ipAddress
    ) {
        Account account = authentication != null
                ? accountRepository.findByUsernameAndUserDeleted((String) authentication.getPrincipal(), false)
                    .orElse(null)
                : null;

        LoggingEvent event = LoggingEvent.builder()
                .details(mapToDetails(path, parameters))
                .ipAddress(ipAddress)
                .user(account != null ? account.getUser() : null)
                .type(LoggingEvent.Type.VISIT)
                .createdAt(new Date())
                .build();

        repository.save(event);
    }

    @Async
    public void createActionEvent() {}

    private String mapToDetails(String path, Map<String, String[]> parameters) {
        String params = parameters.isEmpty()
                ? "Sans paramètres"
                : parameters.keySet()
                        .stream()
                        .map(s -> s + ": " + Arrays.toString(parameters.get(s)))
                        .collect(Collectors.joining(", "));

        return String.format("Page: %s, Paramètres: %s", path, params);
    }

}
