package mfaseehbinnaeem.netlify.app.journalApp.controller;

import mfaseehbinnaeem.netlify.app.journalApp.api.response.WeatherResponse;
import mfaseehbinnaeem.netlify.app.journalApp.entity.User;
import mfaseehbinnaeem.netlify.app.journalApp.service.UserService;
import mfaseehbinnaeem.netlify.app.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;

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

    @GetMapping
    public ResponseEntity<String> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Islamabad");
        String greeting = "Hi " + authentication.getName();

        if (weatherResponse != null) {
            greeting = "Hi " + authentication.getName() + "! Weather feels like " + weatherResponse.getCurrent().getFeelslike() + ".";
        }

        return new ResponseEntity<>(greeting, HttpStatus.OK);
    }
}
