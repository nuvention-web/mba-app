package mbaapp.email;

import com.mashape.unirest.http.JsonNode;
import mbaapp.core.*;
import mbaapp.requests.EmailDraftRequest;
import mbaapp.requests.EssayDraftRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jnag on 4/14/18.
 */
@Controller
public class EmailService {


//    private String DOMAIN_NAME ="sandbox10ae1339337745ed88889375cfcb11fd.mailgun.org";
//    private String API_KEY = "key-1013ff5b4d9ac554d9ac41f1163ff258";

    private String DOMAIN_NAME = "mail.myapp.mba";
    private String API_KEY = "key-1013ff5b4d9ac554d9ac41f1163ff258";

    private final String FROM = "myapp.MBA <mail@mail.myapp.mba>";

    private final String REST_URI = "https://api.mailgun.net/v3/" + DOMAIN_NAME + "/messages";

    public void sendDraftToFriend(JavaMailSender emailSender, EmailDraftRequest draftRequest, User user,
                                  UserSchool userSchool, File draftFile, EssayDraft draft,
                                  String essayID, String draftID, String reviewID) throws Exception {

        String subject = MessageFormat.format("{0} has sent you a draft of his {1} essay to review", user.getName(),
                userSchool.getShortName());

        Resource resource = new ClassPathResource("email/draft.html");
        InputStream resourceInputStream = resource.getInputStream();
        String html = IOUtils.toString(resourceInputStream, StandardCharsets.UTF_8.name());
        html = html.replace("{replaceReviewerName}", draftRequest.getName().split(" ")[0]);
        html = html.replace("{replaceUserName}", user.getName());
        html = html.replace("{replaceUserFirstName}", user.getName().split(" ")[0]);
        html = html.replace("{replacePersonalMessage}", draftRequest.getMessage());
        String reviewURL = MessageFormat.format("https://portal.myapp.mba/feedback/{0}/{1}/{2}/{3}/{4}/reviewer",
                                user.getEmail(), userSchool.getShortName(), essayID, draftID, reviewID);
        html = html.replace("{replaceReviewURL}", reviewURL);

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(draftRequest.getEmail());
        helper.setSubject(subject);
        helper.setText(html, true);
        helper.setFrom("mail@mail.myapp.mba", "myapp.MBA");
        FileSystemResource file = new FileSystemResource(draftFile);
        helper.addAttachment(draftFile.getName(), file);
        emailSender.send(message);

    }


    public void sendForgotPasswordEmail(JavaMailSender emailSender, User user, String password) throws Exception {

        String subject = "Temporary password for your myapp.MBA account";

        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        Resource resource = new ClassPathResource("email/verify.html");
        InputStream resourceInputStream = resource.getInputStream();
        String html = IOUtils.toString(resourceInputStream, StandardCharsets.UTF_8.name());
        html = html.replace("{replaceName}", user.getName().split(" ")[0]);
        html = html.replace("{replaceCode}", "Temporary Password: " + password);
        html = html.replace("{replaceText}","Please use this temporary password to login to your account.");
        html = html.replace("{buttonText}","Login");

        helper.setText(html, true);
        helper.setFrom("mail@mail.myapp.mba", "myapp.MBA");
        emailSender.send(message);

    }

    public void sendActivationEmail(JavaMailSender emailSender, InactiveUser user) throws Exception{

        String subject = "Please verify your myapp.MBA account";

        String text = MessageFormat.format("Hi {0},<p> Welcome to myapp.MBA! Please use this link to verify your account. You will need " +
                        "to provide the following verification code. <p>" +
                        "Verification code: <b>{1}</b>" +
                        "<p>We look forward to working with you through your MBA application process! <p> The MyApp.MBA team<p>", user.getName(), user.getCode());


        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        Resource resource = new ClassPathResource("email/verify.html");
        InputStream resourceInputStream = resource.getInputStream();
        String html = IOUtils.toString(resourceInputStream, StandardCharsets.UTF_8.name());
        html = html.replace("{replaceName}", user.getName().split(" ")[0]);
        html = html.replace("{replaceCode}", "Verification Code: " + user.getCode());
        html = html.replace("{replaceText}","Please use the following verification code to verify your account");
        html = html.replace("{buttonText}","Verify Account");

        helper.setText(html, true);
        helper.setFrom("mail@mail.myapp.mba", "myapp.MBA");
        emailSender.send(message);

    }

}