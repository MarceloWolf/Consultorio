package com.prueba.consultorioMedico.service.user;

import com.prueba.consultorioMedico.auth.AuthResponse;
import com.prueba.consultorioMedico.dto.FullProfessionalDto;
import com.prueba.consultorioMedico.dto.SecretaryDto;
import com.prueba.consultorioMedico.dto.SpecialityDto;
import com.prueba.consultorioMedico.dto.UserDto;
import com.prueba.consultorioMedico.enums.RoleEnum;
import com.prueba.consultorioMedico.model.User;

import java.util.List;

public interface IUserService {
    List<UserDto> findAllUsersByRole(RoleEnum role);
    public AuthResponse createSecretary(SecretaryDto secretaryDto);
    AuthResponse createProfessional(FullProfessionalDto professional, List<SpecialityDto>specilityNames);;
    List<UserDto> findAll();
    UserDto findUserByUsername(String username);
    UserDto findUserByDni(String dni);
    void updatePassword(String username, String password);
    public void reactivateUser(String dni);

}
