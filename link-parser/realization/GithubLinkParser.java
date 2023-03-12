package realization;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class GithubLinkParser implements LinkParser {
    public UserAndRepository getUserAndRepository() {
        return userAndRepository;
    }

    public record UserAndRepository(String user, String repository){}

    private UserAndRepository userAndRepository;
    @Override
    public boolean canParse(URL url) {
        return url.getHost().equals("github.com");
    }

    @Override
    public void parse(URL url) {
        List<String> args = Arrays.stream(url.getPath().split("/")).filter(s -> s.length()!=0).toList();
        userAndRepository = new UserAndRepository(args.get(0), args.get(1));
    }
}
