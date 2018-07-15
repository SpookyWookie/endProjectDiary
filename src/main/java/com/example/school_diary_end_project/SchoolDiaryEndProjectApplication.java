package com.example.school_diary_end_project;

import com.example.school_diary_end_project.entities.AdministratorEntity;
import com.example.school_diary_end_project.entities.UserEntity;
import com.example.school_diary_end_project.entities.enums.EUserRole;
import com.example.school_diary_end_project.repositories.AdministratorRepository;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class SchoolDiaryEndProjectApplication /*implements CommandLineRunner*/ {

    @Autowired
    AdministratorRepository adRepo;

    /*@Autowired
    PasswordEncoder encoder;*/

    public static void main(String[] args) {
        SpringApplication.run(SchoolDiaryEndProjectApplication.class, args);
    }

    @Bean
    public TomcatServletWebServerFactory tomcatEmbedded() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
//-1 means unlimited
                ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
            }
        });
        return tomcat;
    }


   /* @Override
    public void run(String... params) throws Exception {
        AdministratorEntity admin = new AdministratorEntity();
        admin.setEmail("jpaslavski@gmail.com");
        admin.setUsername("admin");
        admin.setPassword("admin");
//        admin.setPassword(encoder.encode(admin.getPassword()));
        admin.setRoles(new ArrayList<EUserRole>(Arrays.asList(EUserRole.ROLE_ADMINISTRATOR)));
        adRepo.save(admin);

    }*/
}
