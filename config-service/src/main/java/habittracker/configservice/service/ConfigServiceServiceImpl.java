package habittracker.configservice.service;

import habittracker.configservice.repository.ConfigServiceMongoRepository;
import org.bson.Document;
import org.springframework.stereotype.Service;

@Service
public class ConfigServiceServiceImpl implements ConfigServiceService {
    static ConfigServiceMongoRepository configServiceMongoRepository;

    private ConfigServiceServiceImpl(ConfigServiceMongoRepository configServiceMongoRepository) {
        ConfigServiceServiceImpl.configServiceMongoRepository = configServiceMongoRepository;
    }

    @Override
    public String findConfig(String application, String profiles, String label) {
        Document doc = configServiceMongoRepository.findFirstByLabelAndNameAndProfiles(application, profiles, label);
        return doc.toJson();
    }

    @Override
    public String findConfig(String application, String profiles) {
        Document doc = configServiceMongoRepository.findFirstByNameAndProfiles(application, profiles);
        return doc.toJson();
    }
}
