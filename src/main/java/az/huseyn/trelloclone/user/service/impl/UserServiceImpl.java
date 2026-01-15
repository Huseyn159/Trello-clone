package az.huseyn.trelloclone.user.service.impl;

import az.huseyn.trelloclone.user.dto.UserRegisterRequestDto;
import az.huseyn.trelloclone.user.dto.UserRegisterResponseDto;
import az.huseyn.trelloclone.user.entity.UserEntity;
import az.huseyn.trelloclone.user.exception.EmailAlreadyExsistsException;
import az.huseyn.trelloclone.user.exception.PasswordMismatchException;
import az.huseyn.trelloclone.user.exception.UsernameTakenException;
import az.huseyn.trelloclone.user.repository.UserRepository;
import az.huseyn.trelloclone.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserRegisterResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto) {

        UserEntity userEntity = new UserEntity();
        boolean existsByUsername = userRepository.existsByUsername(userRegisterRequestDto.getUsername());
        boolean existsByEmail = userRepository.existsByEmail(userRegisterRequestDto.getEmail());

        if (!(userRegisterRequestDto.getPassword().equals(userRegisterRequestDto.getConfirmPassword()))) {
            throw new PasswordMismatchException("Password mismatch");
        }

        if (existsByEmail) {
            throw new EmailAlreadyExsistsException("Email already exists");
        }

        if (existsByUsername) {
           throw new UsernameTakenException("Username already taken");
        }


        userEntity.setUsername(userRegisterRequestDto.getUsername());
        userEntity.setEmail(userRegisterRequestDto.getEmail());
        userEntity.setPassword(userRegisterRequestDto.getPassword());
        userRepository.save(userEntity);

        UserRegisterResponseDto userRegisterResponseDto = new UserRegisterResponseDto();
        userRegisterResponseDto.setUsername(userEntity.getUsername());
        userRegisterResponseDto.setEmail(userEntity.getEmail());
        return userRegisterResponseDto;


    }

    }
