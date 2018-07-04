package mbaapp.services;

//import com.mashape.unirest.http.HttpResponse;
//import com.mashape.unirest.http.JsonNode;
//import com.mashape.unirest.http.Unirest;
import com.mongodb.gridfs.GridFSDBFile;
import mbaapp.core.EssayDraft;
import mbaapp.core.Resume;
import mbaapp.core.User;
import mbaapp.core.UserSchool;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Created by jnag on 5/12/18.
 */
@Controller
public class ResumeService {

    private String API_KEY = "b4b501";

    private String REZ_SCORE_URL = "http://rezscore.com/a/" + API_KEY + "/grade";

    public void runAnalysis(User user, byte[] resumeContents, Resume resume) throws Exception {

        FileInputStream fileInputStream = null;
        File file = new File(resume.getResumeName());
        FileUtils.writeByteArrayToFile(file, resumeContents);
        fileInputStream = new FileInputStream(file);
        fileInputStream.read(resumeContents);
        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("resume", fileBody);
        HttpEntity entity = builder.build();
        HttpPost request = new HttpPost(REZ_SCORE_URL);
        request.setEntity(entity);

        HttpClient client = HttpClientBuilder.create().build();
        try {

            HttpResponse response = client.execute(request);
            InputStream is = response.getEntity().getContent();
            String body = EntityUtils.toString(response.getEntity());
            int code = response.getStatusLine().getStatusCode();
            resume.setAnalysisDone("True");
            extractScore(resume, body);


        } catch (Exception e) {
            e.printStackTrace();
        }

        file.delete();


    }


    private void extractScore(Resume resume, String xml) throws Exception {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document document = docBuilder.parse (new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
        document.getDocumentElement ().normalize ();
        Element root = document.getDocumentElement();
        Element score = (Element)root.getElementsByTagName("score").item(0);
        String brevityScroe = score.getElementsByTagName("brevity_score").item(0).getTextContent();
        String impactScore = score.getElementsByTagName("impact_score").item(0).getTextContent();
        String depthScore = score.getElementsByTagName("depth_score").item(0).getTextContent();
        String percentile = score.getElementsByTagName("percentile").item(0).getTextContent();
        String grade_blurb = score.getElementsByTagName("grade_blurb").item(0).getTextContent();

        Element adviceElement = (Element)root.getElementsByTagName("advice").item(0);
        Element tip = (Element) adviceElement.getElementsByTagName("tip").item(0);
        String advice = tip.getElementsByTagName("long").item(0).getTextContent();

        Element extended = (Element)root.getElementsByTagName("extended").item(0);

        resume.setBrevityScore(brevityScroe);
        resume.setImpactScore(impactScore);
        resume.setDepthScore(depthScore);
        resume.setPercentile(percentile);
        resume.setGradeBlurb(grade_blurb);
        resume.setAdvice(advice);




    }
}
