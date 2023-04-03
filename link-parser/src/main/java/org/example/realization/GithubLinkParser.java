package org.example.realization;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public final class GithubLinkParser implements LinkParser {
    public static final class GithubData extends ParsedData{
        private final UserAndRepository userAndRepository;
        public record UserAndRepository(String user, String repository){}
        public UserAndRepository getUserAndRepository() {
            return userAndRepository;
        }
        private GithubData(UserAndRepository userAndRepository) {
            this.userAndRepository = userAndRepository;
        }
    }
    @Override
    public boolean canParse(URL url) {
        return url.getHost().equals("github.com");
    }

    @Override
    public ParsedData parse(URL url) {
        List<String> args = Arrays.stream(url.getPath().split("/")).filter(s -> s.length()!=0).toList();
        return new GithubData(new GithubData.UserAndRepository(args.get(0), args.get(1)));
    }
}
