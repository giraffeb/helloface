[![Build Status](https://travis-ci.org/giraffeb/helloface.svg?branch=master)](https://travis-ci.org/giraffeb/helloface)
[![codecov](https://codecov.io/gh/giraffeb/helloface/branch/master/graph/badge.svg)](https://codecov.io/gh/giraffeb/helloface)
[![Coverage Status](https://coveralls.io/repos/github/giraffeb/helloface/badge.svg?branch=master)](https://coveralls.io/github/giraffeb/helloface?branch=master)


## 카톡플러스 친구 with MS emotion api
카카오톡 친구 검색 : devhiki

#### 소개
"인물표정 분석 챗봇"
사용자가 카톡플친에게 사진을 보내면 사진의 인물에 대한 표정 분석을 리턴하는 어플리케이션입니다.

#### 사용기술
1) [카카오톡 플러스 친구 API](https://center-pf.kakao.com/)
2) [MS face api](https://azure.microsoft.com/ko-kr/services/cognitive-services/)
3) [spring boot](https://projects.spring.io/spring-boot/)

#### 라이선스
MIT라이선스를 사용합니다.

---

#### 01. 개발동기
게시판만들기나 기술관점으로만 학습하다보니 재미가 부족했습니다.
1. 기존 기술을 잘 활용해서
2. 재미위주의 바로 접근이 가능한 서비스를 만들고 싶었습니다.

#### 02. 개발환경
1. 최초 windows8, intellij, jdk8환경에서 개발 시작
2. 이후 MacOS로 이전
---


#### 03. 개발하면서 난감했던 점.
1. 이미지 처리를 해본적이 없어서 찾아가면서 처리해야했다.
2. 서버의 성능과 용량이 적어서 서버에 저장되지 않게 하고 싶었다.
3. 카카오 앱등록을 위해서 HTTPS가 지원되는 도메인이 필요했다.
4. 매번 코드 작성 후 업로드하기 어려웠다.
5. gradle 처음 사용하는데 문제가 발생하면 해결하기가 어려웠다.

---

#### 04. 작동 영상/이미지

[![image](https://i9.ytimg.com/vi/J5qVQ1SDPq8/mq1.jpg?sqp=CPCxlOcF&rs=AOn4CLAjJHwZ-JyiNAfYvuK-4zbpeMMing)](https://youtu.be/J5qVQ1SDPq8)

---

#### 05. 상세한 내부 작동원리
SpringBoot app 내부에서
사용자가 이미지를 챗봇으로 보내면
답장으로 링크를 보내는데
이 링크를 누르면

1. 카카오 API를 통해
    1. 사용자가 보낸 이미지의 주소를 얻어옴
    2. 이미지 주소로 어플리케이션 내부로 가져오고
    3. 이미지를 적절한 사이즈로 리사이즈함

---
2. MS Face detection API로 표정 분석
    1. 리사이즈된 이미지를 base64형태로 MS Face API로 보냄
    2. json형태로 이미지에서 얼굴의 위치와 표정분석값을 받음

---
3. 앱 자체적으로
    1. 리사이즈된 이미지와 표정분석된 정보로 이미지에 표시함
    2. 이미지에 그리는 과정으로 결과물도 이미지임
    3. 결과물 출력 

이미지는 서버에 남기지 않고, 서로 교환시엔 Base64로 인코딩된 바이너리를 통해 전송함.

---

#### 08. 내부 구조 그림
이미지를 생활코딩처럼 만들어서 넣어보자.

---

#### 07. 개발환경 문제.
1. jenkins를 사용해서 CI환경을 만듬.
2. gradle을 통해 war를 생성하고 
3. 서버에 설치된 tomcat디렉토리로 복사함.

---

#### 08. 이후 해보고 싶은 것들
01. 배포 자체를 docker를 이용해서 해보고 싶음.
02. kubernate도 학습해보고 싶음.
