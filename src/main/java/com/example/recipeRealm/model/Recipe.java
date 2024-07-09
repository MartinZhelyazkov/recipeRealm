package com.example.recipeRealm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@RequiredArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String ingredients;
    private String instructions;
    private String category;
    private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JoinColumn(name = "author_id")
    private User author;
    private Long likes;
    private Long dislikes;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Comment> comments;
    @ManyToMany(mappedBy = "favoriteRecipes")
    @JsonBackReference
    private Set<User> favoriteByUsers;
}
