package habittracker.configservice.repository;

import habittracker.configservice.model.Configuration;
import org.bson.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigServiceMongoRepository extends MongoRepository<Configuration, String> {
    Document findFirstByLabelAndNameAndProfiles(String label, String name, String profiles);

    Document findFirstByNameAndProfiles(String name, String profiles);
}

