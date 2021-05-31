package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.DTO.PMBUserDTO;
import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.constants.Response;
import com.paymybuddy.webapp.service.PMBUserService;
import com.paymybuddy.webapp.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    @Autowired
    private PMBUserService pmbUserService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Response register(PMBUserDTO userDTO) {
        if (!userDTO.getPassword().equals(userDTO.getRepeatPassword())) {
            LOGGER.debug("Try to register " + userDTO.getEmail() + " - "
                    + userDTO.getPassword() + " is different from " + userDTO.getRepeatPassword());
            return Response.MISMATCH_PASSWORD;
        }
        if (pmbUserService.getByEmail(userDTO.getEmail()).isPresent()) {
            LOGGER.debug("Try to register " + userDTO.getEmail() + " - existing user mail");
            return Response.EXISTING_USER;
        }
        PMBUser user = new PMBUser();
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        PMBUser createdUser = pmbUserService.saveUser(user);
        LOGGER.debug("User id " + createdUser.getUserId() + ", mail " + createdUser.getEmail() + " created");
        return Response.REGISTERED;
    }


}
