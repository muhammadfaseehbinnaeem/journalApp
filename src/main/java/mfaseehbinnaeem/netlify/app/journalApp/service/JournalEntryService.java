package mfaseehbinnaeem.netlify.app.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import mfaseehbinnaeem.netlify.app.journalApp.entity.JournalEntry;
import mfaseehbinnaeem.netlify.app.journalApp.entity.User;
import mfaseehbinnaeem.netlify.app.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username) {
        try {
            User user = userService.findByUsername(username);

            journalEntry.setDate(LocalDateTime.now());

            JournalEntry savedJournalEntry = journalEntryRepository.save(journalEntry);

            user.getJournalEntries().add(savedJournalEntry);
            userService.saveUser(user);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("An error occurred while saving the entry", e);
        }
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String username) {
        boolean removed = false;

        try {
            User user = userService.findByUsername(username);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));

            if (removed) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception e) {
            log.error("Error deleting journal entry: ", e);
            throw new RuntimeException("Error deleting journal entry: " + e);
        }

        return removed;
    }
}
