package cs5500.expensetrackapp.restapi.service.impl;

import cs5500.expensetrackapp.restapi.dto.ProfileDTO;
import cs5500.expensetrackapp.restapi.entity.ProfileEntity;
import cs5500.expensetrackapp.restapi.exceptions.ItemExistsException;
import cs5500.expensetrackapp.restapi.repository.ProfileRepository;
import cs5500.expensetrackapp.restapi.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {


    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;

    /**
     * it will save the user details to database
     * @param profileDTO
     * @return profileDTO
     */
    @Override
    public ProfileDTO createProfile(ProfileDTO profileDTO){
        if(profileRepository.existsByEmail(profileDTO.getEmail() )){
            throw new ItemExistsException("Profile already exists" + profileDTO.getEmail());
        }
        profileDTO.setPassword(encoder.encode(profileDTO.getPassword()));
        ProfileEntity profileEntity = mapToProfileEntity(profileDTO);
        profileEntity.setProfileId(UUID.randomUUID().toString());
        //TODO: check for the email exists
        profileEntity = profileRepository.save(profileEntity);
        log.info("Printing the profile entity details {}", profileEntity);
        return mapToProfileDTO(profileEntity);
    }
    /**
     * Mapper method to map values from profile entity to profile dto
     * @param profileEntity
     * @return profileDTO
     */
    private ProfileDTO mapToProfileDTO(ProfileEntity profileEntity) {
        return modelMapper.map(profileEntity, ProfileDTO.class);
    }
    /**
     * Mapper method to map values from profile dto to profile entity
     * @param profileDTO
     * @return profileEntity
     */
    private ProfileEntity mapToProfileEntity(ProfileDTO profileDTO) {
        return modelMapper.map(profileDTO, ProfileEntity.class);
    }
}
