package mbaapp.email;

import mbaapp.core.EssayDraft;
import mbaapp.core.Review;
import mbaapp.core.User;
import mbaapp.core.UserSchool;
import mbaapp.requests.EmailDraftRequest;
import mbaapp.requests.EssayDraftRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jnag on 4/14/18.
 */
@Controller
public class EmailService {





    public void sendDraftToFriend(JavaMailSender emailSender, EmailDraftRequest draftRequest, User user,
                                  UserSchool userSchool, File draftFile, EssayDraft draft,
                                  String essayID, String draftID, String reviewID) throws Exception {

        String subject = MessageFormat.format("{0} has sent you a draft of his {1} essay to review", user.getName(),
                userSchool.getShortName());

        String text = MessageFormat.format("Hi {0},<p> You have received an essay from {1} to review. Please use this " +
                        "<a href=\"http://myapp.mba/mba/users/{2}/school/{3}/essay/{4}/draft/{5}/review/{6}\">personalized link</a></p>" +
                        " to review the essay and leave some feedback. The essay is also included as an attachment to this email. <p>" +
                        "<p>Thank you! <p> The MyApp.MBA team<p>",
                draftRequest.getName(), user.getName(), user.getEmail(), userSchool.getShortName(), essayID, draftID, reviewID);

        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(draftRequest.getEmail());
        helper.setSubject(subject);
        helper.setText(text, true);
        helper.setFrom("myapps.mba@gmail.com", "MyApp.MBA");
        FileSystemResource file = new FileSystemResource(draftFile);
        helper.addAttachment(draftFile.getName(), file);
        emailSender.send(message);
    }


}