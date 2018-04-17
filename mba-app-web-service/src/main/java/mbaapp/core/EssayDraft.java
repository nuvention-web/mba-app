package mbaapp.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import mbaapp.providers.SchoolInfoDBProvider;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jnag on 2/24/18.
 */
@Controller
public class EssayDraft {

    private String draftName;
    private String id;
    private String contents;
    private String url;
    private Set<String> warningWords;
    private String uploadID;
    private DraftType draftType;
    private List<String> schoolKeywords;
    private boolean schoolKeyWordsFound;

    private List<Review> reviews;

    public static enum DraftType{
        CONTENTS, URL, UPLOAD
    }



    @Autowired
    @Qualifier("mongoSchoolDB")
    SchoolInfoDBProvider schoolInfoDBProvider;

    public EssayDraft() {
         this.id = UUID.randomUUID().toString();
         reviews = new ArrayList<>();
    }

    public void addReview(Review review){
        if(reviews==null) {
            reviews = new ArrayList<>();
        }
        reviews.add(review);
    }

    public List<Review> getReviews() {
        if(reviews == null) {
            reviews = new ArrayList<>();
        }
        return reviews;
    }

    public void setDraftType(DraftType draftType) {
        this.draftType = draftType;
    }

    public DraftType getDraftType() {
        return draftType;
    }

    public String getDraftName() {
        return draftName;
    }

    public String getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }


    public void setDraftName(String draftName) {
        this.draftName = draftName;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getUploadID() {
        return uploadID;
    }

    public void setUploadID(String uploadID) {
        this.uploadID = uploadID;
    }

    public void setUrl(String url) { this.url = url; }

    public String getUrl() { return url; }


    private List<String> getKeyWordsListApplicable(String schoolShortName, HashMap<String, List<String>> allSchoolKeywords){

        List<String> currentSchoolKeywords = allSchoolKeywords.get(schoolShortName);
        List<String> keywordsList = new ArrayList<>();
        for (Map.Entry<String, List<String>> schoolKeyword : allSchoolKeywords.entrySet()) {
            for (String keyword : schoolKeyword.getValue()) {
                if (!currentSchoolKeywords.contains(keyword)) {
                    keywordsList.add(keyword.toUpperCase());
                }
            }
        }
        return keywordsList;

    }

    public void validateEssayDraftContents(String schoolShortName, HashMap<String, List<String>> allSchoolKeyWords) {

        warningWords = new HashSet<>();
        List<String> keywordsList = getKeyWordsListApplicable(schoolShortName, allSchoolKeyWords);
        warningWords = keywordsList.stream().parallel().filter(contents::contains).collect(Collectors.toSet());

        schoolKeywords = allSchoolKeyWords.get(schoolShortName);
        schoolKeyWordsFound = schoolKeywords.stream().parallel().anyMatch(contents.toUpperCase()::contains);

    }

    public void validateEssayDraftUpload(String schoolShortName, HashMap<String, List<String>> allSchoolKeywords,
                                         File file) throws Exception {
        warningWords = new HashSet<>();
        schoolKeyWordsFound = false;
        schoolKeywords = allSchoolKeywords.get(schoolShortName);

        List<String> schoolKeywordsUpper = new ArrayList<>();
        for(String word : schoolKeywords){
            schoolKeywordsUpper.add(word.toUpperCase());
        }

        List<String> keywordsList = getKeyWordsListApplicable(schoolShortName, allSchoolKeywords);

        InputStream targetStream = new FileInputStream(file);
        XWPFDocument document = new XWPFDocument(targetStream);
        List<XWPFParagraph> paragraphs = document.getParagraphs();

        StringBuilder contentsBuilder = new StringBuilder();

        for(int i=0;i<paragraphs.size();i++){
            contentsBuilder.append("<p>");
            String text = paragraphs.get(i).getText().toUpperCase();
            String[] words = text.split("\\W+");
            for(String word : words) {
                warningWords.addAll(keywordsList.stream().parallel().filter(word::equalsIgnoreCase).collect(Collectors.toSet()));
            }
            if(!schoolKeyWordsFound){
                schoolKeyWordsFound = schoolKeywordsUpper.stream().parallel().anyMatch(text.toUpperCase()::contains);
            }
            contentsBuilder.append(paragraphs.get(i).getText()).append("</p>");
        }

        contents = contentsBuilder.toString();
    }

    public List<String> getSchoolKeywords() {
        return schoolKeywords;
    }

    public boolean isSchoolKeyWordsFound() {
        return schoolKeyWordsFound;
    }

    public Set<String> getWarningWords() {
        return warningWords;
    }

    public JSONObject toJSON() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, this);
        JSONObject essayJSON = new JSONObject(stringWriter.toString());
        return essayJSON;
    }
}
