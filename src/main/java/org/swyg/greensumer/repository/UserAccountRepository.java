package org.swyg.greensumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swyg.greensumer.domain.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}
