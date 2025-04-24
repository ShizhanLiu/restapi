package cs5500.expensetrackapp.restapi.controller;

import cs5500.expensetrackapp.restapi.dto.ProfileDTO;
import cs5500.expensetrackapp.restapi.io.ProfileRequest;
import cs5500.expensetrackapp.restapi.io.ProfileResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cs5500.expensetrackapp.restapi.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final ModelMapper modelMapper;
    private final ProfileService profileService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ProfileResponse createProfile(@Valid @RequestBody ProfileRequest profileRequest){
        log.info("API /register is called {}", profileRequest);
        ProfileDTO profileDTO = mapToProfileDTO(profileRequest);
        profileDTO = profileService.createProfile(profileDTO);
        log.info("Printing the profile dto details {}", profileDTO);
        return mapToProfileResponse(profileDTO);
    }
    @ResponseStatus(HttpStatus.CREATED)
    private ProfileDTO mapToProfileDTO(ProfileRequest profileRequest) {
        return modelMapper.map(profileRequest, ProfileDTO.class);
    }

    private ProfileResponse mapToProfileResponse(ProfileDTO profileDTO){
        return modelMapper.map(profileDTO, ProfileResponse.class);

    }
}
