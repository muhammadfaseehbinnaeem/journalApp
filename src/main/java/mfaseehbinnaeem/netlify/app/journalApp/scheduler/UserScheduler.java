package mfaseehbinnaeem.netlify.app.journalApp.scheduler;

import mfaseehbinnaeem.netlify.app.journalApp.cache.AppCache;
import mfaseehbinnaeem.netlify.app.journalApp.entity.User;
import mfaseehbinnaeem.netlify.app.journalApp.repository.UserRepositoryImpl;
import mfaseehbinnaeem.netlify.app.journalApp.service.EmailService;
import mfaseehbinnaeem.netlify.app.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserScheduler {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    private AppCache appCache;

    @Scheduled(cron = "0 0 0 * * SUN ")
    public void fetchUsersAndSendSAMail() {
        List<User> users = userRepositoryImpl.getUsersForSA();

        for (User user: users) {
            String sentiment = sentimentAnalysisService.getSentiment(user);

            emailService.sendEmail(user.getEmail(), "Sentiment for Last 7 Days", sentiment);
        }
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void clearAppCache() {
        appCache.init();
    }
}
