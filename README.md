# hrd-notification
kosa-dktechin 교육중 입실, 퇴실 시간에 SMS로 알림을 주는 프로젝트

## 프로젝트 동기
kosa-dktechin 개발자 양성 교육과정중에 출결관리를 하기 위해 hrd-net 앱을 사용한다.
이 앱으로 매일 아침 9시, 18시에 입실, 퇴실을 찍어야 정상적인 출석이 인정된다.

이 알림 애플리케이션을 개발하기 직전에 퇴실 찍는것을 잊고 귀가하는 중에 퇴실을 안찍었다는 것을 깨달았고 다시 교육장으로 돌아왔다.
퇴실알림이 없고, 퇴실을 안찍었을 경우 생기는 불편함이 너무 크다고 생각했다. 
다시는 퇴실 찍는것을 잊지 않겠다고 다짐하며 이 프로그램을 만들었다.

그리고 개인적으로 네이버 클라우드 플랫폼(NCP)에서 제공하는 Simple & Easy Notification 서비스를 이용해보고 싶었다. 
아직 내 서버에서 다른 서버의 API를 사용했던 경험이 없었기 때문에 이번 기회에 간단하게나마 적용해보면 좋은 경험이 될 것이라고 생각했다.

## 프로젝트 기능
데이터베이스에 등록된 사용자에게 주중 오후 5시 50분(hrd-net 퇴실 인정 시간)에 퇴실 알림 SMS를 전송한다.

![SMS 알림 결과](https://user-images.githubusercontent.com/52393564/233763921-8a3d9cf1-202a-4545-8788-a6097e0777ea.PNG){: width="100"}
![서버 로그](https://user-images.githubusercontent.com/52393564/233793547-71ca8551-eb4d-46db-b09a-b421d3c7298f.png)

## 사용한 기술
- Spring Boot
  - Spring Scheduling
  - JPA
- Naver Cloud Platform(NCP)
  - Simple & Easy Notification Service
- MySQL
