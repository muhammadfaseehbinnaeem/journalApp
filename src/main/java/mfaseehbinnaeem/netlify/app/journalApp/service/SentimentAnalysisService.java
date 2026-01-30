package mfaseehbinnaeem.netlify.app.journalApp.service;

import mfaseehbinnaeem.netlify.app.journalApp.entity.JournalEntry;
import mfaseehbinnaeem.netlify.app.journalApp.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SentimentAnalysisService {
    public String getSentiment(User user) {
        List<JournalEntry> journalEntries = user.getJournalEntries();

        String filteredJournalEntries = journalEntries.stream()
                .filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                .map(x -> "<ul>" + x.getContent() + "</ul>")
                .collect(Collectors.joining());

        String sentiment =
                "<!DOCTYPE html>" +
                "<html>" +
                    "<body>" +
                        "<div>" +
                            "<li>" +
                                filteredJournalEntries +
                            "</li>" +
                        "</div>" +
                    "</body>" +
                "</html>";

        return sentiment;
    }
}
