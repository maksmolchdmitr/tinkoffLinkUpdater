package org.example.realization;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public final class StackoverflowLinkParser implements LinkParser {
    public static final class StackoverflowData extends ParsedData{
        private StackoverflowData(String questionId) {
            this.questionId = questionId;
        }
        private final String questionId;
        public String getQuestionId() {
            return questionId;
        }
    }
    @Override
    public boolean canParse(URL url) {
        return url.getHost().equals("stackoverflow.com")&&url.getPath().matches("/questions.*");
    }

    @Override
    public ParsedData parse(URL url) {
        List<String> args = Arrays
                .stream(url.getPath().split("/"))
                .filter(s -> s.length()!=0)
                .toList();
        return new StackoverflowData(args.get(1));
    }
}
