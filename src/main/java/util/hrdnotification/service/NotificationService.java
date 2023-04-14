package util.hrdnotification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import util.hrdnotification.dto.RequestSmsDto;
import util.hrdnotification.entity.HrdStudent;
import util.hrdnotification.repository.HrdStudentRepository;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    @Value("${sms.from}")
    private String FROM;

    @Value("${ncp.access-key-id}")
    private String NCP_ACCESS_KEY;

    @Value("${ncp.secret-key}")
    private String NCP_SECRET_KEY;

    @Value("${ncp.notification.service-id}")
    private String NCP_SERVICE_ID;

    private String NCP_API_URL = "https://sens.apigw.ntruss.com";

    private final HrdStudentRepository hrdStudentRepository;

    @Scheduled(cron = "0 50 17 * * *")
    public void sendSms() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDate = dateTimeFormatter.format(now);
        int day = now.getDayOfWeek().getValue();
        String dayName = now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREA);

        String logMsg = "\n" +
                "==============================================================\n" +
                formatDate + " " + dayName + "\n";
        if (day >= 6) {
            logMsg += "We do not send notifications on weekends.\n" +
                    "==============================================================\n";
            log.info(logMsg);
            return;
        }
        List<HrdStudent> students = hrdStudentRepository.findAll();

        List<String> nameList = students.stream().map(HrdStudent::getName).collect(Collectors.toList());
        String names = String.join(", ", nameList);
        logMsg += "SMS send to " + names + "\n" +
                "==============================================================\n";
        log.info(logMsg);

        RequestSmsDto requestSmsDto = RequestSmsDto.builder()
                .from(FROM)
                .messages(students.stream().map(student -> RequestSmsDto.Message.builder()
                        .to(student.getPhoneNumber())
                        .build())
                        .collect(Collectors.toList()))
                .build();

        Map<String, Object> response = send(requestSmsDto, new Timestamp(System.currentTimeMillis()).getTime());
        logMsg = "\n==============================================================\n" +
                "statusCode: " + response.get("statusCode") + "\n" +
                "statusName: " + response.get("statusName") + "\n" +
                "==============================================================\n";
        log.info(logMsg);
    }

    private Map<String, Object> send(RequestSmsDto requestSmsDto, long timestamp) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        WebClient webClient = WebClient.builder()
                .baseUrl(NCP_API_URL)
                .defaultHeader("x-ncp-apigw-timestamp", String.valueOf(timestamp))
                .defaultHeader("x-ncp-iam-access-key", NCP_ACCESS_KEY)
                .defaultHeader("x-ncp-apigw-signature-v2", makeSignature(String.valueOf(timestamp), "/sms/v2/services/" + NCP_SERVICE_ID + "/messages", HttpMethod.POST))
                .build();

        Map<String, Object> response = webClient.post()
                .uri("/sms/v2/services/" + NCP_SERVICE_ID + "/messages")
                .bodyValue(requestSmsDto)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        return response;
    }

    private String makeSignature(String timestampString, String urlString, HttpMethod httpMethod) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        String space = " ";					// one space
        String newLine = "\n";					// new line
        String method = httpMethod.name();					// method
        String accessKey = NCP_ACCESS_KEY;			// access key id (from portal or Sub Account)
        String secretKey = NCP_SECRET_KEY;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(urlString)
                .append(newLine)
                .append(timestampString)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));

        return Base64.encodeBase64String(rawHmac);
    }
}
