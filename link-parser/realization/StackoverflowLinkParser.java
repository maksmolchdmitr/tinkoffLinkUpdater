package realization;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class StackoverflowLinkParser implements LinkParser {
    private String questionId;
    @Override
    public boolean canParse(URL url) {
        return url.getHost().equals("stackoverflow.com")&&url.getPath().matches("/questions.*");
    }

    @Override
    public void parse(URL url) {
        List<String> args = Arrays.stream(url.getPath().split("/")).filter(s -> s.length()!=0).toList();
        if(args.get(0).equals("questions")){
            questionId = args.get(1);
        }
    }

    public String getQuestionId() {
        return questionId;
    }
}
