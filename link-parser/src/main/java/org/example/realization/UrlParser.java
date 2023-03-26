package org.example.realization;

import java.net.URL;
import java.util.List;

public final class UrlParser {
    public static final class EmptyData extends ParsedData{}
    private final static List<LinkParser> parsers = List.of(
            new GithubLinkParser(),
            new StackoverflowLinkParser()
    );

    /**
     * You can handle ParseData with code:
     * <code>
     * switch (UrlParser.parse(url3)){
     *      case GithubLinkParser.GithubData githubData -> System.out.println(githubData.getUserAndRepository());
     *      case StackoverflowLinkParser.StackoverflowData stackoverflowData -> System.out.println("Question id = " + stackoverflowData.getQuestionId());
     *      case UrlParser.EmptyData emptyData -> System.out.println("Empty Data = " + emptyData);
     * }
     * </code>
     * @param url URL link you want parse
     * @return ParseData depending on link
     */
    public static ParsedData parse(URL url){
        return parsers
                .stream()
                .filter(p -> p.canParse(url))
                .findFirst()
                .map(linkParser -> linkParser.parse(url))
                .orElse(new EmptyData());
    }
}
