package mfaseehbinnaeem.netlify.app.journalApp.repository;

import mfaseehbinnaeem.netlify.app.journalApp.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, ObjectId> {
}
