package habittracker.configservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
@NoArgsConstructor
public class Source {
    private Map<String, Object> properties;
}
