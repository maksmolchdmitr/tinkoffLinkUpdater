package realization;

import java.net.URL;

interface LinkParser{
    boolean canParse(URL url);
    ParsedData parse(URL url);
}
