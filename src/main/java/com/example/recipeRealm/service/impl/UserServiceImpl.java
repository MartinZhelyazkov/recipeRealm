package com.example.recipeRealm.service.impl;

import com.example.recipeRealm.advice.exceptions.EmailAlreadyExistException;
import com.example.recipeRealm.advice.exceptions.RecordNotFoundException;
import com.example.recipeRealm.config.JwtService;
import com.example.recipeRealm.converter.UserConverter;
import com.example.recipeRealm.dto.LoginRequest;
import com.example.recipeRealm.dto.UserRequest;
import com.example.recipeRealm.dto.UserResponse;
import com.example.recipeRealm.model.Recipe;
import com.example.recipeRealm.model.User;
import com.example.recipeRealm.repository.RecipeRepository;
import com.example.recipeRealm.repository.UserRepository;
import com.example.recipeRealm.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserConverter userConverter;

    private final UserRepository userRepository;

    private final RecipeRepository recipeRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final CurrentUserService currentUserService;

    @Autowired
    public UserServiceImpl(UserConverter userConverter, UserRepository userRepository
            , RecipeRepository recipeRepository, AuthenticationManager authenticationManager
            , JwtService jwtService, @Lazy CurrentUserService currentUserService) {
        this.userConverter = userConverter;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.currentUserService = currentUserService;
    }

    @Override
    public UserResponse registerUser(UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistException(String.format("This email %s is already taken", userRequest.getEmail()));
        } else {
            User user = userConverter.toUser(userRequest);
            User savedUser = userRepository.save(user);
            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(savedUser, userResponse);
            return userResponse;
        }
    }

    @Override
    public UserResponse findById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RecordNotFoundException(String.format("User with id %S not found", userId)));
        boolean canFindUser = currentUserService.isCurrentUserMatch(user);
        canFindUser |= currentUserService.isCurrentUserARole("ROLE_ADMIN");
        if (!canFindUser) {
            throw new RecordNotFoundException("This user is not authorize to proceed this operation");
        }
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }

    @Override
    public UserResponse updateUser(UserRequest userRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RecordNotFoundException(String.format("User with id %S not found", userId)));
        boolean canUpdateUser = currentUserService.isCurrentUserMatch(user);
        canUpdateUser |= currentUserService.isCurrentUserARole("ROLE_ADMIN");
        if (!canUpdateUser) {
            throw new RecordNotFoundException("This user is not authorize to proceed this operation");
        }
        Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new EmailAlreadyExistException(String.format("This email %s is already taken", userRequest.getEmail()));
        } else {
            user.setEmail(userRequest.getEmail());
            user.setUsername(userRequest.getUsername());
            User savedUser = userRepository.save(user);
            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(savedUser, userResponse);
            return userResponse;
        }
    }

    @Override
    public void delUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Recipe with id %s not found", userId)));
        boolean canDeleteUser = currentUserService.isCurrentUserMatch(user);
        canDeleteUser |= currentUserService.isCurrentUserARole("ROLE_ADMIN");
        if (!canDeleteUser) {
            throw new RecordNotFoundException("This user is not authorize to proceed this operation");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public Set<Recipe> getAllRecipeForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User with id %s not found", userId)));
        return user.getRecipes();
    }

    @Override
    public UserResponse addToFavorite(Long recipeId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User with id %s not found", userId)));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Recipe with id %s not found", recipeId)));
        user.getFavoriteRecipes().add(recipe);
        userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }

    @Override
    public Set<Recipe> findAllFavoriteRecipes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User with id %s not found", userId)));
        boolean canFindFavorites = currentUserService.isCurrentUserMatch(user);
        canFindFavorites |= currentUserService.isCurrentUserARole("ROLE_ADMIN");
        if (!canFindFavorites) {
            throw new RecordNotFoundException("This user is not authorize to proceed this operation");
        }
        return user.getFavoriteRecipes();
    }

    @Override
    public UserResponse deleteFromFavorites(Long recipeId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("User with id %s not found", userId)));
        boolean canDeleteFromFavorites = currentUserService.isCurrentUserMatch(user);
        canDeleteFromFavorites |= currentUserService.isCurrentUserARole("ROLE_ADMIN");
        if (!canDeleteFromFavorites) {
            throw new RecordNotFoundException("This user is not authorize to proceed this operation");
        }
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Recipe with id %s not found", recipeId)));
        user.getFavoriteRecipes().remove(recipe);
        userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }

    @Override
    public String login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        var user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RecordNotFoundException("User not found or wrong password"));
        return jwtService.generateJwtToken(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RecordNotFoundException("No results found"));
    }

}
