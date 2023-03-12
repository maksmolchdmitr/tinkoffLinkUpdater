import realization.GithubLinkParser;
import realization.LinkParser;
import realization.StackoverflowLinkParser;

import java.net.URL;
import java.util.List;
import java.util.Optional;

public final class UrlParser {
    private final List<LinkParser> parsers = List.of(
            new GithubLinkParser(),
            new StackoverflowLinkParser()
    );

    /**
     * Parse given url and return LinkParser with own specified return parameters
     * Using example:
     *<code>
     *      switch (urlParser.parse ( url)){
     *             case GithubLinkParser githubLinkParser ->
     *                     System.out.println(githubLinkParser.getUserAndRepository());
     *             case StackoverflowLinkParser stackoverflowLinkParser ->
     *                     System.out.println("Question id" + stackoverflowLinkParser.getQuestionId());
     *             case null -> System.out.println("Unhandled url");
     *             case default -> throw new IllegalStateException("Unexpected parser");
     * }</code>
     * @return LinkParser from package realization with own specified return parameters
     */
    public LinkParser parse(URL url){
        Optional<LinkParser> optionalLinkParser = parsers.stream().
                filter(p -> p.canParse(url)).findFirst();
        if(optionalLinkParser.isPresent()){
            optionalLinkParser.get().parse(url);
            return optionalLinkParser.get();
        }else {
            return null;
        }
    }
}
