package mfaseehbinnaeem.netlify.app.journalApp.repository;

import mfaseehbinnaeem.netlify.app.journalApp.api.response.WeatherResponse;
import mfaseehbinnaeem.netlify.app.journalApp.entity.ConfigJournalApp;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalApp, ObjectId> {
}
