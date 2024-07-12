package com.example.recipeRealm.service.impl;

import com.example.recipeRealm.model.User;
import com.example.recipeRealm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final UserService userService;

    @Autowired
    public CurrentUserService(UserService userService) {
        this.userService = userService;
    }

    public boolean isCurrentUserARole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean isCurrentUserMatch(User recipeOrCommentOwner) {
        User currentUser = extractCurrentUser();
        if (currentUser != null && recipeOrCommentOwner != null) {
            return recipeOrCommentOwner.getEmail().equals(extractCurrentUser().getEmail());
        }
        return false;
    }

    public User extractCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return (User) principal;
        } else {
            return userService.findByEmail(principal.toString());
        }
    }
}
