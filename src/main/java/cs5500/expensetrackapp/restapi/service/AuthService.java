package cs5500.expensetrackapp.restapi.service;

import cs5500.expensetrackapp.restapi.entity.ProfileEntity;
import cs5500.expensetrackapp.restapi.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

  private final ProfileRepository profileRepository;

  public ProfileEntity getLoggedInProfile() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final String email = authentication.getName();
    return profileRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Profile not found for the email "+email));
  }
}