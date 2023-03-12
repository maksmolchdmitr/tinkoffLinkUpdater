package realization;

import java.net.URL;

public interface LinkParser {
    boolean canParse(URL url);
    void parse(URL url);
}
