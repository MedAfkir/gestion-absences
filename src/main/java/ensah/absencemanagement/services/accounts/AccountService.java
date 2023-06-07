package ensah.absencemanagement.services.accounts;

import ensah.absencemanagement.dtos.accounts.AccountDTO;
import ensah.absencemanagement.dtos.accounts.AccountMapper;
import ensah.absencemanagement.dtos.accounts.AccountOverviewDTO;
import ensah.absencemanagement.dtos.profil.PasswordUpdateRequest;
import ensah.absencemanagement.exceptions.UnsupportedActionException;
import ensah.absencemanagement.exceptions.not_found.UserNotFoundException;
import ensah.absencemanagement.models.accounts.Account;
import ensah.absencemanagement.models.users.User;
import ensah.absencemanagement.models.users.UserRepository;
import ensah.absencemanagement.repositories.AccountRepository;
import ensah.absencemanagement.utils.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Value("${settings.account.max-connection-attempt}")
    private Integer maxConnectionAttempt;

    @Autowired
    public AccountService(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            AccountRepository accountRepository,
            AccountMapper accountMapper
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    public List<AccountDTO> getAllAccounts() {
        return getAllAccounts(false);
    }

    public List<AccountDTO> getAllAccounts(boolean detailed) {
        List<Account> accounts = accountRepository.findByUserDeleted(false);

        if (detailed)
            return accountMapper.mapWithDetails(accounts);

        return accountMapper.map(accounts);
    }

    public AccountOverviewDTO createAccount(Long userId) {
        User user = findUserById(userId);

        if (user.getAccount() != null) return null;

        // Generate Username
        String fullName = user.getFirstnameFr().toLowerCase() + user.getLastnameFr().toLowerCase();
        int length = accountRepository.countByUsernameStartingWith(fullName);
        String username = length > 0 ? fullName + length : fullName;

        // Generate password
        String password = Random.generateString(10);

        Account account = Account.builder()
                .createdAt(new Date())
                .failureCount(0)
                .active(true)
                .username(username)
                .password(passwordEncoder.encode(password))
                .user(user)
                .build();

        accountRepository.save(account);

        user.setAccount(account);
        userRepository.save(user);

        return AccountOverviewDTO.builder()
                    .username(username)
                    .password(password)
                    .build();
    }

    public void changePassword(Long userId, String password) {
        User user = findUserById(userId);

        if (user.getAccount() == null) {
            throw new UnsupportedActionException("Ce compte ne possède pas de compte");
        }

        user.getAccount().setPassword(passwordEncoder.encode(password));
        user.getAccount().setUpdatedAt(new Date());
        accountRepository.save(user.getAccount());
    }

    public void changePassword(Long userId, PasswordUpdateRequest request) {
        User user = findUserById(userId);

        if (user.getAccount() == null) {
            throw new UnsupportedActionException("Cet(te) utilisateur(e) ne possède pas de compte");
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getAccount().getPassword())) {
            throw new UnsupportedActionException("Mot de passe courant saisie est incorrecte");
        }

        user.getAccount().setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.getAccount().setUpdatedAt(new Date());
        accountRepository.save(user.getAccount());
    }

    public void setActivationAccountState(Long userId, boolean enabled) {
        User user = findUserById(userId);

        if (user.getAccount() == null) {
            throw new UnsupportedActionException("Ce compte ne possède pas de compte");
        }

        user.getAccount().setActive(enabled);
        user.getAccount().setUpdatedAt(new Date());
        accountRepository.save(user.getAccount());
    }

    public void setAccountLockoutState(Long userId, boolean locked) {
        User user = findUserById(userId);

        if (user.getAccount() == null) {
            throw new UnsupportedActionException("Ce compte ne possède pas de compte");
        }

        if (locked) {
            user.getAccount().setFailureCount(maxConnectionAttempt);
        } else {
            user.getAccount().setFailureCount(0);
        }

        user.getAccount().setUpdatedAt(new Date());
        accountRepository.save(user.getAccount());
    }

    private User findUserById(Long userId) {
        return userRepository.findByIdAndDeleted(userId, false)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
