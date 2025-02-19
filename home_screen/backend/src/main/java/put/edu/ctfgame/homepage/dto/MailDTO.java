package put.edu.ctfgame.homepage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.edu.ctfgame.homepage.entity.mail.Mail;
import put.edu.ctfgame.homepage.entity.mail.MailState;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailDTO {
    private Long id;
    private String topic;
    private String content;
    private String sender;
    private Boolean read;

    public static MailDTO from(Mail mail, MailState mailState) {
        return MailDTO.builder()
                .id(mail.getId())
                .topic(mail.getTopic())
                .content(mail.getContent())
                .sender(mail.getSender())
                .read(mailState.getRead())
                .build();
    }
}
