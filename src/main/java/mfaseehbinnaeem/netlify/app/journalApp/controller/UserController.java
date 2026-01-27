package mfaseehbinnaeem.netlify.app.journalApp.controller;

import mfaseehbinnaeem.netlify.app.journalApp.entity.User;
import mfaseehbinnaeem.netlify.app.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/id/{userId}")
    public User getUserById(@PathVariable ObjectId userId) {
        Optional<User> user = userService.findById(userId);

        if (user.isPresent()) {
            return user.get();
        }

        return null;
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User oldUser = userService.findByUsername(username);

        oldUser.setUsername(user.getUsername());
        oldUser.setPassword(user.getPassword());
        userService.saveNewUser(oldUser);

        return new ResponseEntity<>(oldUser, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public  ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        userService.deleteByUsername(username);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
