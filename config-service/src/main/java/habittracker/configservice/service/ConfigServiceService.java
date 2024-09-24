package habittracker.configservice.service;

public interface ConfigServiceService {
    String findConfig(String application, String profiles, String label);

    String findConfig(String application, String profiles);

}
