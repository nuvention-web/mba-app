package mbaapp.services;

import mbaapp.core.EssayDraft;
import mbaapp.core.User;
import mbaapp.core.UserSchool;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.rules.RuleMatch;
import org.springframework.stereotype.Controller;

import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jnag on 5/3/18.
 */
@Controller
public class ProofReadingService {


    public void proofRead(User user, UserSchool school, String essayID, String draftID, EssayDraft draft) throws Exception{
        if(draft.getDraftType()== EssayDraft.DraftType.UPLOAD){
            Pattern pattern = Pattern.compile("<p>(.*?)</p>", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(draft.getContents());
            String jsonResultString = "";
            StringBuilder grammarCheckBuilder = new StringBuilder();
            while (matcher.find()) {
                String paragraph = matcher.group(1);
                grammarCheckBuilder.append("<p>").append(runGrammarCheckOnParagraph(paragraph)).append("</p>");
            }
            draft.setGrammerCheck(grammarCheckBuilder.toString());
        }
        else{
            String contents = draft.getContents();
            String []splits = contents.split("<br>");
            StringBuilder grammarCheckBuilder = new StringBuilder();
            for(String split : splits) {
                    grammarCheckBuilder.append(runGrammarCheckOnParagraph(split)).append("<br>");
            }
            draft.setGrammerCheck(grammarCheckBuilder.toString());
        }
        draft.setGrammarCheckRun("True");
    }

    private String runGrammarCheckOnParagraph(String paragraph) throws Exception {
        JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());

        StringBuilder stringBuilder = new StringBuilder(paragraph);
        List<RuleMatch> matches = langTool.check(paragraph);
        int totalShift=0;
        for (RuleMatch match : matches) {
            int fromPos = match.getFromPos();
            int toPos = match.getToPos();
            String spanStart = MessageFormat.format("<span data-toggle=\"m-popover\" data-placement=\"left\" title=\"{0} Suggestions: {1}\"><u><b>",
                    match.getMessage(), match.getSuggestedReplacements());
            String spanEnd = "</b></u></span>";
            stringBuilder.insert(fromPos + totalShift, spanStart);
            stringBuilder.insert(spanStart.length()+toPos + totalShift, spanEnd);
            totalShift = spanEnd.length() + spanStart.length() + totalShift;
        }

        return stringBuilder.toString();


    }

}
