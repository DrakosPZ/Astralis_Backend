package com.Astralis.backend.utils;

import com.Astralis.backend.dto.LoginInformationDTO;
import com.Astralis.backend.dto.UserDTO;
import com.Astralis.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class BuildTestData implements ApplicationRunner {

    @Autowired
    private UserService userService;

    //StartUp Settings
    private static boolean START_UP_DATA_INJECTION = true;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(START_UP_DATA_INJECTION){
            //Startup Data
            UserDTO user = UserDTO.builder()
                    .nickName("DrakoD")
                    .role("MASTER_ADMIN")
                    .build();
            LoginInformationDTO login = new LoginInformationDTO("Drako","Drako","");
            user.setLoginInformation(login);
            userService.save(Optional.of(user));

            user = UserDTO.builder()
                    .nickName("KuroK")
                    .role("ADMIN")
                    .build();
            login = new LoginInformationDTO("Kuro","Kuro","");
            user.setLoginInformation(login);
            userService.save(Optional.of(user));

            user = UserDTO.builder()
                    .nickName("ZuignapZ")
                    .role("USER")
                    .build();
            login = new LoginInformationDTO("Zuignap","Zuignap","");
            user.setLoginInformation(login);
            userService.save(Optional.of(user));
        }
    }
}
