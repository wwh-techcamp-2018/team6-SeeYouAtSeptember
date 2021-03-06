# team6-SeeYouAtSeptember
# 모두의 배민찬: 내 반찬도 우리 민족이었어
우아한테크캠프 6조의 팀프로젝트를 위한 기획입니다.
본 기획안은 6하원칙(누가, 언제, 어디서, 무엇을, 어떻게, 왜)에 기반하여 작성되었습니다.
## 프로젝트 배경과 목적
배민의 강점이 뭘까? 배민이 잘하는 것이 무엇일까? 디자인, 브랜딩, 푸드테크 플랫폼, 유통망
1. 재야의 고수들 혹은 요리 꿈나무의 반찬을 더 많은 사람들에게 선보일 수 있다.
   1. 우리동네 최씨 할머니네는 깻잎반찬이 기가막히는데~
   2. 창업의 문턱을 낮춘다.
   3. 작은 성취를 할 수 있는 장을 만들어주자.
2. 1인 미디어 시대, 나를 표현하고 나를 만들어가는 수단으로서 요리
   1. 유튜버 개인 방송, 독립 출판, 등등
3. 더 다양한 제품을 사용자에게 + 사실 사람들은 음식에서 "맛" 뿐 아니라 "이야기"도 담기길 원한다.
   1. 80년 전통의 집, 3대가 지키고 있는 라면 레시피, 팔당댐의 초계국수, ...
   2. 이야기를 담은 음식은 언제나 즐겁다.
4. 배민찬에 소비자 참여적인 아이템을 끊임없이 공급해줄 수 있다.
   1. 특정 금액 모금 성공시 배민찬 등재
   2. 유튜브의 배지처럼, 요리왕 비룡의 특급요리사처럼. 여러번 모금에 성공하면 (명예)보상을 준다.
## 일정(프로젝트 프로세스)
| 일   | 월              | 화                                    | 수   | 목   | 금                  | 토   |
| ---- | --------------- | ------------------------------------- | ---- | ---- | ------------------- | ---- |
|      |                 | 31<br />**프로젝트 주제 발의와 선정** | 1    | 2    | 3<br />**마인드맵** | 4    |
| 5    | 6<br />**기획** | 7<br />**기획**                       | 8    | 9    | 10                  | 11   |
| 12   | 13              | 14                                    | 15   | 16   | 17                  | 18   |
| 19   | 20              | 21                                    | 22   | 23   | 24                  | 25   |
| 26   | 27              | 28                                    | 29   | 30   | 31 <br />**발표**   |      |
1. 다양한 프로젝트 주제 발의와 선정 (7/31 화)
   1. 신선식품 브랜드, 제품 모아보기(익스피디아, 다나와)
   2. 편향된 정보가 소비자의 선택에 미치는 영향은?
   3. 신선식품 크라우드 펀딩
2. [마인드맵](https://coggle.it/diagram/W2FP_E8sWHNPxPv9/t/%ED%8C%80-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8) (8/3 금)
3. 기획서 작성(8/6)
4. 요구명세 작성
5. 디자인
6. 구현
7. 테스트
8. 피드백
9. 발표
## 주요 기능
프로젝트 조회
프로젝트 등록
프로젝트 참여(후원, 구매)
## 개발환경
todo: 표로 작성하면 좋을 것 같음. 아이콘이랑 같이
기술 스택, 툴을 나열하자
1. Spring
2. HTML CSS JAVASCRIPT
3. RDS(MySQL)
4. travis ci
5. AWS EC2, S3, code deploy
6. github
7. mindmap coggle
8. I'm port(결제 모듈 시스템)
9. DNS
10. WebSocket
11. tui editer
12. Croppie

## 참조
### 웹사이트 레이아웃
1. [배민찬](https://www.baeminchan.com/)
   1. 친숙한 배민 브랜드 컬러, 전형적인 4단 그리드
   2. 상세페이지 상품 사진 다수
   3. static header
2. [정육각](https://www.jeongyookgak.com/index)
   1. 큼직한 아이템 박스와 3단 그리드 시스템
   2. 인포그래픽: 마우스 오버시 가장 중요한 정보(도축일자 등)를 찰나의 순간에 전달
   3. 정보 전달의 단순함(텍스트, 이미지)
   4. 상세 페이지 단일 대표 사진, 레이아웃
   5. sticky header
### 크라우드 펀딩 
크라우드 펀딩에서 주로 시각화하는 데이터가 무엇인지, 추가, 제거, 발전시킬 만한 것은 없는지를 확인해봅시다.
1. [킥스타터](https://www.kickstarter.com/): 2009년 4월 28일 탄생한 [크라우드 펀딩](https://namu.wiki/w/%ED%81%AC%EB%9D%BC%EC%9A%B0%EB%93%9C%20%ED%8E%80%EB%94%A9) 서비스. 크라우드 펀딩계의 대표주자. 
2. [인디고고](https://www.indiegogo.com/): 2008년 탄생한 크라우드 펀딩을 제공하는 최초의 서비스들 중 하나.
3. [텀블벅](https://tumblbug.com/)
4. [와디즈](https://www.wadiz.kr/web/wmain)
5. [크라우디](https://www.ycrowdy.com/)
