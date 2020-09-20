package com.Astralis.backend.utils;

import com.Astralis.backend.dto.OccasionDto;
import com.Astralis.backend.dto.PersonDto;
import com.Astralis.backend.service.OccasionService;
import com.Astralis.backend.service.PersonService;
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
    private PersonService personService;
    @Autowired
    private OccasionService occasionService;

    //StartUp Settings
    private static boolean START_UP_DATA_INJECTION = false;
    private static boolean TEAM_FILLED = false;
    private static boolean OCCASIONS_GENERATED = false;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(START_UP_DATA_INJECTION){
            //Startup Data
            PersonDto user = PersonDto.builder()
                    .eMail("admin@team.com")
                    .firstName("Admin")
                    .lastName("Admin")
                    .type("TEAMLEADER")
                    .username("Admin")
                    .password("#123abxAd56xad!aklä")
                    .build();
            String id0Identifier = personService.save(Optional.of(user)).get().getIdentifier();
            String id1Identifier = "";
            String id2Identifier = "";
            String id12Identifier = "";

            if(TEAM_FILLED){
                for(int i = 1; i<12; i++){
                    PersonDto test;
                    if(i == 1){
                        test = PersonDto.builder()
                                .eMail("eMail" + i)
                                .firstName("Firstname" + i)
                                .lastName("Lastname" + i)
                                .type("TEAMLEADER")
                                .username("test2")
                                .password("test2")
                                .build();
                        id1Identifier = personService.save(Optional.of(test)).get().getIdentifier();
                        test.setIdentifier(id1Identifier);
                    }else if(i == 2) {
                        test = PersonDto.builder()
                                .eMail("eMail" + i)
                                .firstName("Firstname" + i)
                                .lastName("Lastname" + i)
                                .type("TEAMLEADER")
                                .username("testAB")
                                .password("testAB")
                                .build();
                        id2Identifier = personService.save(Optional.of(test)).get().getIdentifier();
                        test.setIdentifier(id2Identifier);
                    }else if(i >= 10) {
                        test = PersonDto.builder()
                                .eMail("eMail" + i)
                                .firstName("Firstname" + i)
                                .lastName("Lastname" + i)
                                .type("PARTNER")
                                .username("testA"+i)
                                .password("testA"+i)
                                .build();
                        test.setIdentifier(personService.save(Optional.of(test)).get().getIdentifier());
                    }else{
                        test = PersonDto.builder()
                                .eMail("eMail" + i)
                                .firstName("Firstname" + i)
                                .lastName("Lastname" + i)
                                .type("RECRUIT")
                                .build();
                        test.setIdentifier(personService.save(Optional.of(test)).get().getIdentifier());
                    }

                    if(i < 5){
                        personService.addPersonToTeam(id0Identifier, Optional.of(test));
                    }else if(i < 7){
                        personService.addPersonToTeam(id1Identifier, Optional.of(test));
                    }else if(i == 10){
                        personService.addPersonToTeam(id1Identifier, Optional.of(test));
                    }else if(i == 11){
                        personService.addPersonToTeam(id2Identifier, Optional.of(test));
                    }else {
                        personService.addPersonToTeam(id2Identifier, Optional.of(test));
                    }
                }
                PersonDto test = PersonDto.builder()
                        .eMail("eMailLayer2")
                        .firstName("FirstnameLayer2")
                        .lastName("LastnameLayer2")
                        .type("RECRUIT")
                        .build();
                id12Identifier = personService.save(Optional.of(test)).get().getIdentifier();
                test.setIdentifier(id12Identifier);
                personService.addPersonToTeam(id1Identifier, Optional.of(test));
            }

            if(OCCASIONS_GENERATED) {
                OccasionDto occasion1 = OccasionDto.builder()
                        .title("Occasion1")
                        .description("Number 1 Occasion")
                        .start(ZonedDateTime.of(LocalDateTime.of(2020, 8, 2, 10, 10, 10), ZoneId.of("CET")))
                        .end(ZonedDateTime.of(LocalDateTime.of(2020, 8, 4, 10, 12, 10), ZoneId.of("CET")))
                        .duration(null)
                        .place("Wherever")
                        .isPublic(false)
                        .category("Termin")
                        .allUsedComponents(new ArrayList<>())
                        .build();
                occasionService.addOccasionToCalender(
                        personService.findModelByIdentifier(id0Identifier).get().getCalenders().get(0).getIdentifier(),
                        occasionService.save(Optional.of(occasion1))
                );

                OccasionDto occasion2 = OccasionDto.builder()
                        .title("Occasion2")
                        .description("Number 2 Occasion")
                        .start(ZonedDateTime.of(LocalDateTime.of(2020, 8, 2, 10, 20, 10), ZoneId.of("CET")))
                        .end(ZonedDateTime.of(LocalDateTime.of(2020, 8, 2, 10, 22, 10), ZoneId.of("CET")))
                        .duration(null)
                        .place("Wherever")
                        .isPublic(false)
                        .category("Termin")
                        .allUsedComponents(new ArrayList<>())
                        .build();
                occasionService.addOccasionToCalender(
                        personService.findModelByIdentifier(id0Identifier).get().getCalenders().get(0).getIdentifier(),
                        occasionService.save(Optional.of(occasion2))
                );

                OccasionDto occasion3 = OccasionDto.builder()
                        .title("Occasion3")
                        .description("Number 3 Occasion")
                        .start(ZonedDateTime.of(LocalDateTime.of(2020, 8, 3, 10, 10, 10), ZoneId.of("CET")))
                        .end(ZonedDateTime.of(LocalDateTime.of(2020, 8, 3, 10, 12, 10), ZoneId.of("CET")))
                        .duration(null)
                        .place("Wherever")
                        .isPublic(false)
                        .category("Termin")
                        .allUsedComponents(new ArrayList<>())
                        .build();
                occasionService.addOccasionToCalender(
                        personService.findModelByIdentifier(id0Identifier).get().getCalenders().get(0).getIdentifier(),
                        occasionService.save(Optional.of(occasion3))
                );

                OccasionDto occasion4 = OccasionDto.builder()
                        .title("OccasionOfChild1")
                        .description("Number 4 Occasion")
                        .start(ZonedDateTime.of(LocalDateTime.of(2020, 8, 5, 10, 10, 10), ZoneId.of("CET")))
                        .end(ZonedDateTime.of(LocalDateTime.of(2020, 8, 8, 14, 12, 10), ZoneId.of("CET")))
                        .duration(null)
                        .place("Wherever")
                        .isPublic(false)
                        .category("EinstTermin")
                        .allUsedComponents(new ArrayList<>())
                        .build();
                occasionService.addOccasionToCalender(
                        personService.findModelByIdentifier(id1Identifier).get().getCalenders().get(0).getIdentifier(),
                        occasionService.save(Optional.of(occasion4))
                );


                OccasionDto occasion5 = OccasionDto.builder()
                        .title("OccasionOfChild2")
                        .description("Number 5 Occasion")
                        .start(ZonedDateTime.of(LocalDateTime.of(2020, 8, 5, 10, 10, 10), ZoneId.of("CET")))
                        .end(ZonedDateTime.of(LocalDateTime.of(2020, 8, 6, 15, 12, 10), ZoneId.of("CET")))
                        .duration(null)
                        .place("Wherever")
                        .isPublic(false)
                        .category("KDTermin")
                        .allUsedComponents(new ArrayList<>())
                        .build();
                occasionService.addOccasionToCalender(
                        personService.findModelByIdentifier(id2Identifier).get().getCalenders().get(0).getIdentifier(),
                        occasionService.save(Optional.of(occasion5))
                );
            }
        }
    }
}
