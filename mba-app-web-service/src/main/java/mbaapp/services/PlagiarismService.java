package mbaapp.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import mbaapp.core.EssayDraft;
import mbaapp.core.User;
import mbaapp.core.UserSchool;
import mbaapp.providers.UserDBProvider;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

/**
 * Created by jnag on 5/2/18.
 */
@Controller
public class PlagiarismService {

    @Autowired
    @Qualifier("userMongoDB")
    UserDBProvider userDBProvider;

    private static String COPY_LEAKS_KEY = "99E08FAB-C2A8-40C5-BBE4-E745C96758B6";
    private static String EMAIL = "jyotishman22@gmail.com";

    private String token = null;
    private LocalDate tokenIssued;
    private String URI = "https://api.copyleaks.com";
    private String ACCOUNT_LOGIN = URI + "/v1/account/login-api";
    private String SCAN_FILE = URI + "/v2/education/create-by-file";

    private static final Logger logger = Logger.getLogger(PlagiarismService.class.getName());




    public String getToken(){

        if(token!=null) {
            LocalDate currentTime = LocalDate.now();
            long daysElapsed = ChronoUnit.DAYS.between(tokenIssued, currentTime);
            if(daysElapsed==0) {
                return token;
            }
        }

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post(ACCOUNT_LOGIN)
                    .field("Email", EMAIL)
                    .field("ApiKey", COPY_LEAKS_KEY)
                    .asJson();

            JSONObject body = jsonResponse.getBody().getObject();
            if(jsonResponse.getStatus()== 200) {
                token = jsonResponse.getBody().getObject().getString("access_token");
                tokenIssued = LocalDate.now();
            }
        }
        catch (Exception e){

        }

        return token;

    }



    public void runPlagiarismCheck(User user, UserSchool school, String essayID, String draftID, EssayDraft draft) throws Exception {
        byte[] byteArray = userDBProvider.getEssayDraftUploaded(user, school, essayID, draftID).toByteArray();

        File tempFile = File.createTempFile(draft.getDraftName().split("\\.")[0],"." +
        draft.getDraftName().split("\\.")[1]);
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(byteArray);

        FileOutputStream fileOuputStream = new FileOutputStream(draft.getDraftName());
        fileOuputStream.write(byteArray);


        HttpResponse jsonResponse = Unirest.post(SCAN_FILE)
                //.header("copyleaks-sandbox-mode", "true")
                .header("Authorization", "Bearer "+getToken())
                .field("file", tempFile)
                .asString();

        jsonResponse.getStatus();
        jsonResponse.getBody();

    }


}
