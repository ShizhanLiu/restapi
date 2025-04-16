package cs5500.expensetrackapp.restapi.repository;

import cs5500.expensetrackapp.restapi.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository <ProfileEntity, Long> {
}
