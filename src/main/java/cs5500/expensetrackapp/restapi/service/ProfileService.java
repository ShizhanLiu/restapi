package cs5500.expensetrackapp.restapi.service;

import cs5500.expensetrackapp.restapi.dto.ProfileDTO;


public interface ProfileService {

  /**
   * It will save the user details to database
   * @param profileDTO
   * @return profileDto
   * */
  ProfileDTO createProfile(ProfileDTO profileDTO);
}