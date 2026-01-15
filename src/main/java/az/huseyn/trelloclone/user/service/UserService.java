package az.huseyn.trelloclone.user.service;


import az.huseyn.trelloclone.user.dto.UserRegisterRequestDto;
import az.huseyn.trelloclone.user.dto.UserRegisterResponseDto;

public interface UserService {

    UserRegisterResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto);

}
