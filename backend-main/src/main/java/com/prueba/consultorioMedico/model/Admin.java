package com.prueba.consultorioMedico.model;

import com.prueba.consultorioMedico.enums.AccountStateEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Entity
@Getter
@Setter
@SuperBuilder
public class Admin extends User{
    private static Admin adminInstance;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private List<AdminUser> users;


    public Admin(){
        users = new ArrayList<>();
    }

    public static synchronized Admin getInstance(){
        if(adminInstance == null){
            adminInstance = new Admin();
        }
        return adminInstance;
    }

    public boolean addUser(String userName, String password){
        if(users.size()<2){
            users.add(new AdminUser(userName,password));
            return true;
        }
        return false;

    }


}
