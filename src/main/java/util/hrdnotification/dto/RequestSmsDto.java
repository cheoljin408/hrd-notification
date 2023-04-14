package util.hrdnotification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestSmsDto {

    @Builder.Default
    private String type = "SMS";
    @Builder.Default
    private String content = "kosa-dktechin\n퇴실 알림";
    private String from;
    private List<Message> messages;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Message {
        private String to;
    }
}
