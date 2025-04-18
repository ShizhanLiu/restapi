package cs5500.expensetrackapp.restapi.repository;

import cs5500.expensetrackapp.restapi.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository <ProfileEntity, Long> {
    Optional<ProfileEntity> findByEmail(String email);

    Boolean existsByEmail(String email);
}
