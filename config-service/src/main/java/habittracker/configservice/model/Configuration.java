package habittracker.configservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Document(collection = "properties")
@Getter
@Setter
@NoArgsConstructor
public class Configuration {
    @Id
    private BigInteger id;
    private String label;
    private String name;
    private String[] profiles;
    private String state;
    private int version;
    private PropertySource[] propertySource;

    public Configuration(BigInteger id, String label, String name, String[] profiles, String state, int version, PropertySource[] propertySource) {
        this.id = id;
        this.label = label;
        this.name = name;
        this.profiles = profiles;
        this.state = state;
        this.version = version;
        this.propertySource = propertySource;
    }

}
