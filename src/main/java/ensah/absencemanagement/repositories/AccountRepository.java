package ensah.absencemanagement.repositories;

import ensah.absencemanagement.models.accounts.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsernameAndUserDeleted(String username, boolean deleted);

    List<Account> findByUserDeleted(boolean deleted);

    Integer countByUsernameStartingWith(String username);

}
