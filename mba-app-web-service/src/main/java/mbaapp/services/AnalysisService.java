package mbaapp.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import mbaapp.core.EssayDraft;
import mbaapp.core.User;
import mbaapp.core.UserSchool;
import mbaapp.providers.UserDBProvider;
import org.decimal4j.util.DoubleRounder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * Created by jnag on 5/3/18.
 */
@Controller
public class AnalysisService {

    private static final String AZURE_KEY = "106de97e1328482fad6038726fb14e3b";

    private static final String SENTIMENT_API = "https://eastus.api.cognitive.microsoft.com/text/analytics/v2.0/sentiment";
    private static final String KEY_PHRASES_API = "https://eastus.api.cognitive.microsoft.com/text/analytics/v2.0/keyPhrases";


    @Autowired
    @Qualifier("userMongoDB")
    UserDBProvider userDBProvider;


    public void runAnalysis(User user, UserSchool school, String essayID, String draftID, EssayDraft draft) throws Exception {

        JSONArray documentArray = new JSONArray();
        JSONObject document = new JSONObject();
        document.put("id", 1);
        String contents = draft.getContents();
        if (draft.getDraftType() == EssayDraft.DraftType.UPLOAD) {
            contents = contents.replaceAll("<p>", "");
            contents = contents.replaceAll("</p>", "");
        }
        document.put("text", contents);
        documentArray.put(document);

        JSONObject requestBody = new JSONObject();
        requestBody.put("documents", documentArray);

        HttpResponse<JsonNode> sentimentResponse = Unirest.post(SENTIMENT_API)
                .header("Ocp-Apim-Subscription-Key", AZURE_KEY)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .asJson();

        int sentimentResponseStatus = sentimentResponse.getStatus();
        if(sentimentResponseStatus==200) {
            JSONObject body = sentimentResponse.getBody().getObject();

            double sentimentScore = body.getJSONArray("documents").getJSONObject(0).getDouble("score");
            double scorePercent = sentimentScore*100;
            scorePercent = DoubleRounder.round(scorePercent, 2);
            draft.setSentimentScore(scorePercent);

        }

        HttpResponse<JsonNode> keyWordsResponse = Unirest.post(KEY_PHRASES_API)
                .header("Ocp-Apim-Subscription-Key", AZURE_KEY)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .asJson();

        int keyWordsResponseStatus = keyWordsResponse.getStatus();
        if(keyWordsResponseStatus==200) {
            JSONObject body = keyWordsResponse.getBody().getObject();
            JSONArray keyPhrasesArray = body.getJSONArray("documents").getJSONObject(0).getJSONArray("keyPhrases");
            StringBuilder keyWords = new StringBuilder();
            for(int i=0; i<keyPhrasesArray.length(); i++) {
                keyWords = keyWords.length() == 0 ? keyWords.append(keyPhrasesArray.getString(i)) :
                        keyWords.append(", " + keyPhrasesArray.getString(i));
            }
            draft.setKeyPhrases(keyWords.toString());
        }

        draft.setAnalysisRun("True");


    }
}


