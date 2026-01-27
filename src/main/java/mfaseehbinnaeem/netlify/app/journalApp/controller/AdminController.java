package mfaseehbinnaeem.netlify.app.journalApp.controller;

import mfaseehbinnaeem.netlify.app.journalApp.entity.User;
import mfaseehbinnaeem.netlify.app.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @PostMapping("createAdminUser")
    public ResponseEntity<User> createAdminUser(@RequestBody User user) {
        try {
            userService.saveAdmin(user);

            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userService.getAll();

        if (allUsers != null && !allUsers.isEmpty()) {
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
