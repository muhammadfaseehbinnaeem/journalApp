package mfaseehbinnaeem.netlify.app.journalApp.controller;

import mfaseehbinnaeem.netlify.app.journalApp.entity.JournalEntry;
import mfaseehbinnaeem.netlify.app.journalApp.entity.User;
import mfaseehbinnaeem.netlify.app.journalApp.service.JournalEntryService;
import mfaseehbinnaeem.netlify.app.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry journalEntry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            journalEntryService.saveEntry(journalEntry, username);

            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<JournalEntry> allJournals = user.getJournalEntries();

        if (allJournals != null && !allJournals.isEmpty()) {
            return new ResponseEntity<>(allJournals, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id/{journalId}")
    public ResponseEntity<JournalEntry> getJournalById(@PathVariable ObjectId journalId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(journalId)).collect(Collectors.toList());

        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(journalId);

            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{journalId}")
    public ResponseEntity<JournalEntry> updateJournalById(@PathVariable ObjectId journalId, @RequestBody JournalEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(journalId)).collect(Collectors.toList());

        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(journalId);

            if (journalEntry.isPresent()) {
                JournalEntry oldEntry = journalEntry.get();

                oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : oldEntry.getContent());
                journalEntryService.saveEntry(oldEntry);

                return new ResponseEntity<>(oldEntry, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{journalId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId journalId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean removed = journalEntryService.deleteById(journalId, username);

        if (removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
