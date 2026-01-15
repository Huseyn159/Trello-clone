package az.huseyn.trelloclone.user.repository;


import az.huseyn.trelloclone.user.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

  boolean existsByUsername(String username);
  boolean existsByEmail(String email);
  Optional<UserEntity> findByEmail(String email);

}
