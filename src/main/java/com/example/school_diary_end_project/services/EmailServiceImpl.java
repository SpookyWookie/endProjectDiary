package com.example.school_diary_end_project.services;

import com.example.school_diary_end_project.entities.GradeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
//   TODO Make the email pretty

    @Autowired
    private JavaMailSender sender;

    @Override
    public void sendTemplateMessage(GradeEntity grade) throws Exception {

        MimeMessage mail = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo(grade.getPupil().getParent().getEmail());
//        TODO set setSubject()
        helper.setSubject(grade.getPupil().getName() + " "+ grade.getPupil().getSurname() +
                " has been graded, subject: " + grade.getSubject().getName());

        String text = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "table, th, td {\n" +
                "    border: 1px solid black;\n" +
                "    padding: 5px;\n" +
                "}\n" +
                "td{\n" +
                "text-align:center;\n" +
                "}\n" +
                "table {\n" +
                "    border-spacing: 5px;\n" +
                "    border-collapse: collapse;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "\n" +
                "<table style=\"width:100%\">\n" +
                "  <tr>\n" +
                "    <th>Pupil</th>\n" +
                "    <th>Teacher</th> \n" +
                "    <th>Subject</th>\n" +
                "    <th>Grade</th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>"+ grade.getPupil().getName() +" "+ grade.getPupil().getSurname() +"</td>\n" +
                "    <td>"+grade.getTeacher().getName() + " " + grade.getTeacher().getSurname() +"</td>\n" +
                "    <td>"+ grade.getSubject().getName() +"</td>\n" +
                "    <td>5</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <th colspan=\"4\">Comment</th>\n" +
                "   \n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td colspan = \"4\"><text-area>"+ grade.getComment() +"</text-area></td>\n" +
                "    \n" +
                "  </tr>\n" +
                "</table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";

        helper.setText(text, true);

        sender.send(mail);
    }
}
