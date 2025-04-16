package cs5500.expensetrackapp.restapi.service;

import cs5500.expensetrackapp.restapi.dto.ProfileDTO;

public interface ProfileService {
    /**
     * it will save the user details to database
     * @param profileDTO
     * @return profileDTO
     */
    ProfileDTO createProfile(ProfileDTO profileDTO);
}
