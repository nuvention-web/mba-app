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

//        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + DOMAIN_NAME + "/messages")
//                .basicAuth("api", API_KEY)
//                .queryString("from", FROM)
//                .queryString("to", "jyotishman22@gmail.com")
//                .queryString("subject", subject)
//                .queryString("html", text)
//                .field("attachment", draftFile)
//                .asJson();

//        if(request.getStatus()!=200){
//            throw new Exception("Status returned "+request.getStatus());
//        }
    }




    public void sentActivationEmail(JavaMailSender emailSender, InactiveUser user) throws Exception{

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
        html = html.replace("{replaceCode}", user.getCode());

        helper.setText(html, true);
        helper.setFrom("mail@mail.myapp.mba", "myapp.MBA");
//        FileSystemResource file = new FileSystemResource(draftFile);
//        helper.addAttachment(draftFile.getName(), file);
        emailSender.send(message);

//        HttpResponse<String> request = Unirest.post("https://api.mailgun.net/v3/" + DOMAIN_NAME + "/messages")
//                .basicAuth("api", API_KEY)
//                .queryString("from", FROM)
//                .queryString("to", user.getEmail())
//                .queryString("subject", subject)
//                .queryString("html", getHTML())
//                .asString();
//
//        if(request.getStatus()!=200){
//            throw new Exception("Status returned "+request.getStatus());
//        }


    }
//
//
//    private String getHTML(){
//
//        return "<html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
//                "  <head>\n" +
//                "    <!--[if gte mso 15]>\n" +
//                "                        <xml>\n" +
//                "                                <o:OfficeDocumentSettings>\n" +
//                "                                <o:AllowPNG/>\n" +
//                "                                <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
//                "                                </o:OfficeDocumentSettings>\n" +
//                "                        </xml>\n" +
//                "                        <![endif]-->\n" +
//                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
//                "    <style type=\"text/css\">\n" +
//                "    body {\n" +
//                "    margin:0;\n" +
//                "    padding:0;\n" +
//                "    background: rgb(255,255,255);\n" +
//                "    }\n" +
//                "    table td { \n" +
//                "    border-spacing: 0; \n" +
//                "    border-collapse: collapse; \n" +
//                "    border: 0 none; \n" +
//                "    mso-table-lspace: 0pt; \n" +
//                "    mso-table-rspace: 0pt; \n" +
//                "    }\n" +
//                "    /*+++++++++++++++++ MOBILE ++++++++++++++++++*/\n" +
//                "    @media only screen and (max-width: 620px) {\n" +
//                "    .fusionResponsiveContent {\n" +
//                "        width: 100%!important;\n" +
//                "    }\n" +
//                "    .fusionResponsiveColumn {\n" +
//                "        width: auto!important;\n" +
//                "        display:block;\n" +
//                "        text-align:left;\n" +
//                "    }\n" +
//                "\n" +
//                "    .fusionResponsiveImage {\n" +
//                "        width: 100%!important;\n" +
//                "    }\n" +
//                "    .fusionResponsiveImageTable {\n" +
//                "        padding-bottom: 0!important;\n" +
//                "    }\n" +
//                "    .fusionResponsiveCanvas {\n" +
//                "        padding-top: 0px!important;\n" +
//                "        padding-bottom: 0px!important;\n" +
//                "    }\n" +
//                "    }\n" +
//                "\n" +
//                "    @media only screen and (max-width: 500px) {\n" +
//                "    .fusionResponsiveContent {\n" +
//                "        width: 100%!important;\n" +
//                "    }\n" +
//                "    .fusionResponsiveColumn {\n" +
//                "        width: auto!important;\n" +
//                "        display:block;\n" +
//                "        text-align:left;\n" +
//                "    }\n" +
//                "\n" +
//                "    .fusionResponsiveImage {\n" +
//                "        width: 100%!important;\n" +
//                "    }\n" +
//                "    .fusionResponsiveCanvas {\n" +
//                "        padding-top: 0px!important;\n" +
//                "        padding-bottom: 0px!important;\n" +
//                "    }\n" +
//                "    }\n" +
//                "    </style><!--[if !mso]><!-->\n" +
//                "    <link href=\"https://fonts.googleapis.com/css?family=Roboto:400,400,500,500i,700,700i\" rel=\"stylesheet\">\n" +
//                "    <link href=\"https://fonts.googleapis.com/css?family=Raleway:400,400,500,500i,700,700i\" rel=\"stylesheet\">\n" +
//                "    <link href=\"https://fonts.googleapis.com/css?family=Playfair+Display:400,400,500,500i,700,700i\" rel=\"stylesheet\"><!--<![endif]-->\n" +
//                "    <title></title>\n" +
//                "  </head>\n" +
//                "  <body>\n" +
//                "    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:100%;margin:0px auto;\">\n" +
//                "      <tbody>\n" +
//                "        <tr>\n" +
//                "          <td class=\"fusionResponsiveCanvas\" valign=\"top\" style=\"width:100%;padding-top:15px;padding-bottom:15px;background-color:rgb(208,208,208);background-repeat:no-repeat;background-position:center top;font-family:Raleway, sans-serif;\">\n" +
//                "            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" data-fusion-class=\"LogoContainer\" style=\"width:100%;margin:0px auto;\">\n" +
//                "              <tbody>\n" +
//                "                <tr>\n" +
//                "                  <td valign=\"top\" style=\"width:100%;\">\n" +
//                "                    <table class=\"fusionResponsiveContent\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" style=\"margin:0px auto;width:600px;table-layout:fixed;background-color:rgb(51,51,51);\">\n" +
//                "                      <tbody>\n" +
//                "                        <tr>\n" +
//                "                          <td style='background-color:rgb(51,51,51);padding:17px 8px;background-image:url(\"undefined\");background-position:center top;background-repeat:no-repeat;'>\n" +
//                "                            <table class=\"fusionResponsiveContent\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"width:100%;table-layout:fixed;\">\n" +
//                "                              <tbody>\n" +
//                "                                <tr>\n" +
//                "                                  <td class=\"fusionResponsiveColumn\" style=\"mso-line-height-rule:exactly;width:8px;line-height:0;font-size:0px;\">\n" +
//                "                                    <img src=\"https://ui.icontact.com/assets/1px.png\" width=\"1\" border=\"0\" style=\"display:block;\">\n" +
//                "                                  </td>\n" +
//                "                                  <td valign=\"top\" class=\"fusionResponsiveColumn\" data-fusion-class=\"\" style=\"transition:all 0.2s;width:568px;background-color:transparent;padding:0px;\">\n" +
//                "                                    <div data-aqa=\"block-image\" style=\"overflow:hidden;\">\n" +
//                "                                      <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:100%;\">\n" +
//                "                                        <tbody>\n" +
//                "                                          <tr>\n" +
//                "                                            <td class=\"null\" style=\"padding-bottom:20px;\">\n" +
//                "                                              <table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"margin:auto;\">\n" +
//                "                                                <tbody>\n" +
//                "                                                  <tr>\n" +
//                "                                                    <td>\n" +
//                "                                                      <img src=\"https://staticapp.icpsc.com/icp/loadimage.php/mogile/1749596/4b937ba16f191a7169152bf52029c695/image/png\" class=\"fusionResponsiveImage\" alt=\"\" width=\"147\" height=\"auto\" style=\"display:block;width:147px;height:auto;\">\n" +
//                "                                                    </td>\n" +
//                "                                                  </tr>\n" +
//                "                                                </tbody>\n" +
//                "                                              </table>\n" +
//                "                                            </td>\n" +
//                "                                          </tr>\n" +
//                "                                        </tbody>\n" +
//                "                                      </table>\n" +
//                "                                    </div>\n" +
//                "                                  </td>\n" +
//                "                                  <td class=\"fusionResponsiveColumn\" style=\"mso-line-height-rule:exactly;width:8px;line-height:0;font-size:0px;\">\n" +
//                "                                    <img src=\"https://ui.icontact.com/assets/1px.png\" width=\"1\" border=\"0\" style=\"display:block;\">\n" +
//                "                                  </td>\n" +
//                "                                </tr>\n" +
//                "                              </tbody>\n" +
//                "                            </table>\n" +
//                "                          </td>\n" +
//                "                        </tr>\n" +
//                "                      </tbody>\n" +
//                "                    </table>\n" +
//                "                  </td>\n" +
//                "                </tr>\n" +
//                "              </tbody>\n" +
//                "            </table>\n" +
//                "            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" data-fusion-class=\"Unpadded\" style=\"width:100%;margin:0px auto;\">\n" +
//                "              <tbody>\n" +
//                "                <tr>\n" +
//                "                  <td valign=\"top\" style=\"width:100%;\">\n" +
//                "                    <table class=\"fusionResponsiveContent\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" style=\"margin:0px auto;width:600px;table-layout:fixed;background-color:rgb(255,255,255);\">\n" +
//                "                      <tbody>\n" +
//                "                        <tr>\n" +
//                "                          <td style=\"background-color:rgb(255,255,255);padding:0px 8px;background-position:center top;background-repeat:no-repeat;\">\n" +
//                "                            <table class=\"fusionResponsiveContent\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"width:100%;table-layout:fixed;\">\n" +
//                "                              <tbody>\n" +
//                "                                <tr>\n" +
//                "                                  <td class=\"fusionResponsiveColumn\" style=\"mso-line-height-rule:exactly;width:8px;line-height:0;font-size:0px;\">\n" +
//                "                                    <img src=\"https://ui.icontact.com/assets/1px.png\" width=\"1\" border=\"0\" style=\"display:block;\">\n" +
//                "                                  </td>\n" +
//                "                                  <td valign=\"top\" class=\"fusionResponsiveColumn\" data-fusion-class=\"\" style=\"transition:all 0.2s;width:568px;background-color:transparent;padding:0px;\">\n" +
//                "                                    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" data-fusion-class=\"Content\" style=\"width:100%;\">\n" +
//                "                                      <tbody>\n" +
//                "                                        <tr>\n" +
//                "                                          <td style=\"padding-top:10px;padding-bottom:10px;\">\n" +
//                "                                            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" style=\"margin:0px auto;width:100%;\">\n" +
//                "                                              <tbody>\n" +
//                "                                                <tr>\n" +
//                "                                                  <td style=\"mso-line-height-rule:exactly;font-size:0px;line-height:0px;border-bottom:1px solid rgb(233,233,233);\">\n" +
//                "                                                    &nbsp;\n" +
//                "                                                  </td>\n" +
//                "                                                </tr>\n" +
//                "                                              </tbody>\n" +
//                "                                            </table>\n" +
//                "                                          </td>\n" +
//                "                                        </tr>\n" +
//                "                                      </tbody>\n" +
//                "                                    </table>\n" +
//                "                                  </td>\n" +
//                "                                  <td class=\"fusionResponsiveColumn\" style=\"mso-line-height-rule:exactly;width:8px;line-height:0;font-size:0px;\">\n" +
//                "                                    <img src=\"https://ui.icontact.com/assets/1px.png\" width=\"1\" border=\"0\" style=\"display:block;\">\n" +
//                "                                  </td>\n" +
//                "                                </tr>\n" +
//                "                              </tbody>\n" +
//                "                            </table>\n" +
//                "                          </td>\n" +
//                "                        </tr>\n" +
//                "                      </tbody>\n" +
//                "                    </table>\n" +
//                "                  </td>\n" +
//                "                </tr>\n" +
//                "              </tbody>\n" +
//                "            </table>\n" +
//                "            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" data-fusion-class=\"Main,\" style=\"width:100%;margin:0px auto;\">\n" +
//                "              <tbody>\n" +
//                "                <tr>\n" +
//                "                  <td valign=\"top\" style=\"width:100%;\">\n" +
//                "                    <table class=\"fusionResponsiveContent\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" style=\"margin:0px auto;width:600px;table-layout:fixed;background-color:rgb(255,255,255);\">\n" +
//                "                      <tbody>\n" +
//                "                        <tr>\n" +
//                "                          <td style=\"background-color:rgb(255,255,255);padding:12px 0px;background-position:center top;background-repeat:no-repeat;\">\n" +
//                "                            <table class=\"fusionResponsiveContent\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"width:100%;table-layout:fixed;\">\n" +
//                "                              <tbody>\n" +
//                "                                <tr>\n" +
//                "                                  <td valign=\"top\" class=\"fusionResponsiveColumn\" data-fusion-class=\"\" style=\"transition:all 0.2s;width:600px;background-color:transparent;padding:0px;\">\n" +
//                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;\">\n" +
//                "                                      <tbody>\n" +
//                "                                        <tr>\n" +
//                "                                          <td>\n" +
//                "                                            <div data-fusion-class=\"Headline\" style=\"background-color:transparent;border:0px;display:block;margin-bottom:30px;color:rgb(51,51,51);font-family:Raleway, sans-serif;font-size:15px;text-align:left;\">\n" +
//                "                                              <h1 style='text-align:center;color:rgb(0,0,0);font-size:32px;font-family:\"Playfair Display\", serif;margin-top:0px;margin-bottom:0px;'>\n" +
//                "                                                Hi {replaceName},\n" +
//                "                                              </h1>\n" +
//                "                                            </div>\n" +
//                "                                          </td>\n" +
//                "                                        </tr>\n" +
//                "                                      </tbody>\n" +
//                "                                    </table>\n" +
//                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;\">\n" +
//                "                                      <tbody>\n" +
//                "                                        <tr>\n" +
//                "                                          <td>\n" +
//                "                                            <div data-fusion-class=\"Content\" style=\"background-color:transparent;border:0px;display:block;margin-bottom:30px;color:rgb(51,51,51);font-family:Raleway, sans-serif;font-size:15px;text-align:left;\">\n" +
//                "                                              <p style=\"mso-line-height-rule:exactly;text-align:center;line-height:20px;margin-top:0px;margin-bottom:0px;\">\n" +
//                "                                                Welcome to myapp.MBA!\n" +
//                "                                              </p>\n" +
//                "                                              <p style=\"mso-line-height-rule:exactly;text-align:center;line-height:20px;margin-top:16px;margin-bottom:0px;\">\n" +
//                "                                                Please use the following verification code to verify your account.\n" +
//                "                                              </p>\n" +
//                "                                              <p style=\"mso-line-height-rule:exactly;text-align:center;line-height:16px;margin-top:16px;margin-bottom:0px;\">\n" +
//                "                                                <strong>Verification Code: {replaceCode}</strong>\n" +
//                "                                              </p>\n" +
//                "                                            </div>\n" +
//                "                                          </td>\n" +
//                "                                        </tr>\n" +
//                "                                      </tbody>\n" +
//                "                                    </table>\n" +
//                "                                  </td>\n" +
//                "                                </tr>\n" +
//                "                              </tbody>\n" +
//                "                            </table>\n" +
//                "                          </td>\n" +
//                "                        </tr>\n" +
//                "                      </tbody>\n" +
//                "                    </table>\n" +
//                "                  </td>\n" +
//                "                </tr>\n" +
//                "              </tbody>\n" +
//                "            </table>\n" +
//                "            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" data-fusion-class=\"HeroContainer\" style=\"width:100%;margin:0px auto;\">\n" +
//                "              <tbody>\n" +
//                "                <tr>\n" +
//                "                  <td valign=\"top\" style=\"width:100%;\">\n" +
//                "                    <table class=\"fusionResponsiveContent\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" style=\"margin:0px auto;width:600px;table-layout:fixed;background-color:rgb(255,255,255);\">\n" +
//                "                      <tbody>\n" +
//                "                        <tr>\n" +
//                "                          <td style=\"background-color:rgb(255,255,255);padding:0px;background-position:center top;background-repeat:no-repeat;\">\n" +
//                "                            <table class=\"fusionResponsiveContent\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"width:100%;table-layout:fixed;\">\n" +
//                "                              <tbody>\n" +
//                "                                <tr>\n" +
//                "                                  <td valign=\"top\" class=\"fusionResponsiveColumn\" data-fusion-class=\"\" style=\"transition:all 0.2s;width:600px;background-color:transparent;padding:0px;\">\n" +
//                "                                    <div data-fusion-class=\"\" style=\"overflow:hidden;\">\n" +
//                "                                      <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:100%;\">\n" +
//                "                                        <tbody>\n" +
//                "                                          <tr>\n" +
//                "                                            <td style=\"padding-bottom:16px;\">\n" +
//                "                                              <table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" style=\"margin:0px auto;\">\n" +
//                "                                                <tbody>\n" +
//                "                                                  <tr>\n" +
//                "                                                    <td style=\"text-align:center;background:rgb(51,51,51);border-radius:0px;border-color:rgb(51,51,51);border-style:solid;border-width:1px;padding:10px 20px;\">\n" +
//                "                                                      <a href=\"https://myapp.mba\" style=\"text-decoration:none;color:rgb(255,252,252);font-family:Raleway, sans-serif;font-size:16px;\">Verify Account</a>\n" +
//                "                                                    </td>\n" +
//                "                                                  </tr>\n" +
//                "                                                </tbody>\n" +
//                "                                              </table>\n" +
//                "                                            </td>\n" +
//                "                                          </tr>\n" +
//                "                                        </tbody>\n" +
//                "                                      </table>\n" +
//                "                                    </div>\n" +
//                "                                    <div data-aqa=\"block-image\" style=\"overflow:hidden;\">\n" +
//                "                                      <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:100%;\">\n" +
//                "                                        <tbody>\n" +
//                "                                          <tr>\n" +
//                "                                            <td class=\"null\" style=\"padding-bottom:0px;\">\n" +
//                "                                              <table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"margin:auto;\">\n" +
//                "                                                <tbody>\n" +
//                "                                                  <tr>\n" +
//                "                                                    <td>\n" +
//                "                                                      <img src=\"https://staticapp.icpsc.com/icp/loadimage.php/mogile/1749596/761372650d3e503ca4b2390397552213/image/jpeg\" class=\"fusionResponsiveImage\" alt=\"\" width=\"600\" height=\"auto\" style=\"display:block;width:600px;height:auto;\">\n" +
//                "                                                    </td>\n" +
//                "                                                  </tr>\n" +
//                "                                                </tbody>\n" +
//                "                                              </table>\n" +
//                "                                            </td>\n" +
//                "                                          </tr>\n" +
//                "                                        </tbody>\n" +
//                "                                      </table>\n" +
//                "                                    </div>\n" +
//                "                                  </td>\n" +
//                "                                </tr>\n" +
//                "                              </tbody>\n" +
//                "                            </table>\n" +
//                "                          </td>\n" +
//                "                        </tr>\n" +
//                "                      </tbody>\n" +
//                "                    </table>\n" +
//                "                  </td>\n" +
//                "                </tr>\n" +
//                "              </tbody>\n" +
//                "            </table>\n" +
//                "            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" data-fusion-class=\"Footer,Contrast\" style=\"width:100%;margin:0px auto;\">\n" +
//                "              <tbody>\n" +
//                "                <tr>\n" +
//                "                  <td valign=\"top\" style=\"width:100%;\">\n" +
//                "                    <table class=\"fusionResponsiveContent\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" align=\"center\" style=\"margin:0px auto;width:600px;table-layout:fixed;background-color:rgb(237,237,237);\">\n" +
//                "                      <tbody>\n" +
//                "                        <tr>\n" +
//                "                          <td style=\"background-color:rgb(237,237,237);padding:20px 8px;background-position:center top;background-repeat:no-repeat;\">\n" +
//                "                            <table class=\"fusionResponsiveContent\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"width:100%;table-layout:fixed;\">\n" +
//                "                              <tbody>\n" +
//                "                                <tr>\n" +
//                "                                  <td class=\"fusionResponsiveColumn\" style=\"mso-line-height-rule:exactly;width:8px;line-height:0;font-size:0px;\">\n" +
//                "                                    <img src=\"https://ui.icontact.com/assets/1px.png\" width=\"1\" border=\"0\" style=\"display:block;\">\n" +
//                "                                  </td>\n" +
//                "                                  <td valign=\"top\" class=\"fusionResponsiveColumn\" data-fusion-class=\"\" style=\"transition:all 0.2s;width:568px;background-color:transparent;padding:0px;\">\n" +
//                "                                    <table style=\"width:100%;\">\n" +
//                "                                      <tbody>\n" +
//                "                                        <tr>\n" +
//                "                                          <td>\n" +
//                "                                            <div data-fusion-class=\"Content\" style=\"display:block;margin-bottom:10px;width:100%;\">\n" +
//                "                                              <div style=\"box-sizing:content-box;text-align:center;\">\n" +
//                "                                                <a href=\"https://www.facebook.com/\" style=\"text-decoration:none;cursor:pointer;box-sizing:content-box;\"><img src=\"https://ui.icontact.com/assets/editor-social-icons/black-transparent/facebook.png\" alt=\"Facebook\" height=\"40\" width=\"40\" style=\"height:40px;width:40px;cursor:pointer;box-sizing:content-box;border:0px;\" title=\"Facebook\"></a><a href=\"https://www.twitter.com/\" style=\"text-decoration:none;cursor:pointer;box-sizing:content-box;\"><img src=\"https://ui.icontact.com/assets/editor-social-icons/black-transparent/twitter.png\" alt=\"Twitter\" height=\"40\" width=\"40\" style=\"height:40px;width:40px;cursor:pointer;box-sizing:content-box;border:0px;\" title=\"Twitter\"></a><a href=\"https://www.instagram.com/\" style=\"text-decoration:none;cursor:pointer;box-sizing:content-box;\"><img src=\"https://ui.icontact.com/assets/editor-social-icons/black-transparent/instagram.png\" alt=\"Instagram\" height=\"40\" width=\"40\" style=\"height:40px;width:40px;cursor:pointer;box-sizing:content-box;border:0px;\" title=\"Instagram\"></a><a href=\"https://plus.google.com/\" style=\"text-decoration:none;cursor:pointer;box-sizing:content-box;\"><img src=\"https://ui.icontact.com/assets/editor-social-icons/black-transparent/google.png\" alt=\"Google+\" height=\"40\" width=\"40\" style=\"height:40px;width:40px;cursor:pointer;box-sizing:content-box;border:0px;\" title=\"Google+\"></a>\n" +
//                "                                              </div>\n" +
//                "                                            </div>\n" +
//                "                                          </td>\n" +
//                "                                        </tr>\n" +
//                "                                      </tbody>\n" +
//                "                                    </table>\n" +
//                "                                    <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;\">\n" +
//                "                                      <tbody>\n" +
//                "                                        <tr>\n" +
//                "                                          <td>\n" +
//                "                                            <div data-fusion-class=\"Content\" style=\"background-color:transparent;border:0px;display:block;margin-bottom:20px;color:rgb(51,51,51);font-family:Raleway, sans-serif;font-size:15px;text-align:left;\">\n" +
//                "                                              <p style=\"text-align:center;margin-top:0px;margin-bottom:0px;\">\n" +
//                "                                                FOLLOW US\n" +
//                "                                              </p>\n" +
//                "                                              <p style=\"text-align:center;margin-top:16px;margin-bottom:0px;\">\n" +
//                "                                                © myapp.MBA 2018. All Rights Reserved.\n" +
//                "                                              </p>\n" +
//                "                                              <p class=\"paragraph-spacing-none\" style=\"margin-top:0px;margin-bottom:0px;\">\n" +
//                "                                                <br>\n" +
//                "                                              </p>\n" +
//                "                                              <p style=\"text-align:center;margin-top:16px;margin-bottom:0px;\">\n" +
//                "                                                <br>\n" +
//                "                                              </p>\n" +
//                "                                            </div>\n" +
//                "                                          </td>\n" +
//                "                                        </tr>\n" +
//                "                                      </tbody>\n" +
//                "                                    </table>\n" +
//                "                                  </td>\n" +
//                "                                  <td class=\"fusionResponsiveColumn\" style=\"mso-line-height-rule:exactly;width:8px;line-height:0;font-size:0px;\">\n" +
//                "                                    <img src=\"https://ui.icontact.com/assets/1px.png\" width=\"1\" border=\"0\" style=\"display:block;\">\n" +
//                "                                  </td>\n" +
//                "                                </tr>\n" +
//                "                              </tbody>\n" +
//                "                            </table>\n" +
//                "                          </td>\n" +
//                "                        </tr>\n" +
//                "                      </tbody>\n" +
//                "                    </table>\n" +
//                "                  </td>\n" +
//                "                </tr>\n" +
//                "              </tbody>\n" +
//                "            </table>\n" +
//                "          </td>\n" +
//                "        </tr>\n" +
//                "      </tbody>\n" +
//                "    </table>\n" +
//                "  </body>\n" +
//                "</html>";
//
//    }

}