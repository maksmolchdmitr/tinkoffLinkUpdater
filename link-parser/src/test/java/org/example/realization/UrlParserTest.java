package org.example.realization;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UrlParserTest {

    @ParameterizedTest
    @ValueSource(strings = {"https://github.com/sanyarnd/tinkoff-java-course-2022/",
            "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c",
            "https://stackoverflow.com/search?q=unsupported%20link"})
    void parseTest(String link) throws MalformedURLException {
        URL url = new URL(link);
        switch (UrlParser.parse(url)){
            case GithubLinkParser.GithubData githubData -> {
                assertEquals(githubData.getUserAndRepository().user(), "sanyarnd");
                assertEquals(githubData.getUserAndRepository().repository(), "tinkoff-java-course-2022");
            }
            case StackoverflowLinkParser.StackoverflowData stackoverflowData ->
                    assertEquals(stackoverflowData.getQuestionId(), "1642028");
            case UrlParser.EmptyData ignored ->
                    assertEquals(link, "https://stackoverflow.com/search?q=unsupported%20link");
        }
    }
}