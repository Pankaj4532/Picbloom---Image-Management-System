/*package org.omnera.picbloom.config;

import org.omnera.picbloom.model.Account;
import org.omnera.picbloom.service.AccountService;
import org.omnera.picbloom.util.constants.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class SeedData implements CommandLineRunner{

    @Autowired
    private AccountService accountService;

    @Override
    public void run(String... args) throws Exception {

        Account account01 = new Account();
        Account account02 = new Account();

        //inserting values

        account01.setEmail("user@gmail.com");
        account01.setPassword("pass1234");
        account01.setAuthorities(Authority.USER.toString());
        accountService.save(account01);

        account02.setEmail("admin@gmail.com");
        account02.setPassword("pass1234");
        account02.setAuthorities(Authority.ADMIN.toString() +" "+Authority.USER.toString());
        accountService.save(account02);
        
       
    }
    
}
*/