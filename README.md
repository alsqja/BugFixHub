# **Newsfeed Project BY EightByte**
### 개발 관련 정보를 공유하고 질문, 답변을 할 수 있는 커뮤니티
![image](https://github.com/user-attachments/assets/4468113b-03a3-4c30-973f-ebed6c8250bd)


---
## Tech Stack
JAVA : JDK 17

Spring Boot : 3.3.5

IDE : IntelliJ

MySQL : Ver 8+

---

## **API 명세서 URL :**
[API URL](https://documenter.getpostman.com/view/18429295/2sAYBUCXTm)
---

## ERD :
![image](https://github.com/user-attachments/assets/a0cd3722-6cf4-4425-b269-6bcae2c39856)
---

## Wire Frame :
[프로토타입 URL](https://www.figma.com/proto/Ys4GRrolVShlxFbl0q5n6u/8%EC%A1%B0---%EB%89%B4%EC%8A%A4%ED%94%BC%EB%93%9C-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8?node-id=64-733&node-type=canvas&t=mi4F9kCPWVviIoUQ-1&scaling=min-zoom&content-scaling=fixed&page-id=0%3A1&starting-point-node-id=2%3A2)

## 시연 영상
[시연 영상 URL](https://www.youtube.com/watch?v=o0Cf7JTVnGU)

## 팀원

| 이름              | Github 프로필  | 블로그     | 역할 |
| ----------------- | -------------- | ---------- | ---- |
| 김민범 | [alsqja]      | [블로그](https://velog.io/@alsqja2626/posts)  | 팀장 |
| 조현지 | [chohyuun]    | [블로그](https://something-do-it.tistory.com) | 팀원 |
| 백은영 | [beunyeong]   | [블로그](https://beunyeong.tistory.com)       | 팀원 |
| 김동건 | [gimdonggeon] | [블로그](https://codinggeony.tistory.com)     | 팀원 |

[chohyuun]: https://github.com/chohyuun
[alsqja]: https://github.com/alsqja
[beunyeong]: https://github.com/beunyeong
[gimdonggeon]: https://github.com/gimdonggeon

## 수행한 일

### 김민범

- API, ERD 기획 및 설계
- Entity 초기 설정 및 연관관계 생성
- 인증 / 인가 관련 처리 (로그인, 로그아웃, 필터를 사용한 로그인 검증)
- ControllerAdvice 를 활용한 GlobalExceptionHandler 구현
- User 관련 로직 구현
- 리팩토링

### 조현지

- API, ERD 기획 및 설계
- 와이어 프레임 (figma 사용)
- Post 관련 로직 구현
  - 게시글 필수 과제 로직 구현
  - 게시글 필터 추가 도전과제 로직 구현
- 테스트 케이스 작성
- 시연 영상 제작

### 백은영

- API 및 ERD 기획/설계 검토 : 프로젝트 요구사항을 바탕으로 주요 API 및 DB 구조 설계 검토
- 기능 개발 및 검증
   - Friend 기능 개발: 친구 추가, 친구 요청 상태 변경, 친구 전체 조회, 친구 삭제 로직 구현
   - CommentLike 기능 개발: 댓글 좋아요/취소 및 Post/Comment 관계 설정 로직 구현
   - 로직 검증: Postman 및 IntelliJ DB를 통해 구현된 기능 cross 검증
- 협업 및 커뮤니케이션: Code Convention 및 Github Rules 협의
- 소스 관리: 개인 Branch를 생성하고, Pull Request -> Merge하여 팀원들과의 소스 관리 작업 수행
- 문서화 작업: 구현된 코드 주석 보강으로 유지보수성 강화
- 트러블 슈팅 및 정리
   - 프로젝트를 수행하면서 발생한 문제를 해결하고 개인 블로그를 통해 이슈 정리

### 김동건

- API, ERD 기획 및 설계
- Comment 관련 로직 구현
- PostLike 관련 로직 구현
- Comment 관련 요청 및 응답 예시 작성
- Readme 작성 예시 틀 작성
- 전체 테스트 진행
- 테스트 진행 중 오류 발생 시 오류 핸들링

---

## 프로젝트 설계 시 목적 및 목표

---

저희는,

한국형 스택오버플로우라는 뼈대를 세워보자는 목적을 가지고  프로젝트를 진행했습니다.

저희 프로젝트의 목표는 프로그래밍 및 소프트웨어 개발 관련 질문과 답변을 공유하고, 해결책을 제공하는 커뮤니티를 형성하는 것이기에,  처음 설계 단계부터 도전 과제인 댓글 기능을 처음부터 구현하고자 하였습니다.

---

## 특징

---
### User

- 비밀번호는 암호화된 상태로 Database 에 저장됩니다.
- 이메일은 중복될 수 없습니다.

### Post

- 작성 시 ask, info 타입을 분리하여 원하는 카테고리의 글을 작성할 수 있고, 원하는 카테고리의 글을 조회할 수 있습니다.
- 친구들이 작성한 게시글들을 따로 조회할 수 있습니다.
- 원하는 기준으로 정렬된 게시글을 볼 수 있고(좋아요, 수정일자, 생성일자) 기간별 게시글을 조회할 수 있습니다.
- 게시글의 제목을 통한 검색이 가능합니다.

### Comment
- 한 게시글의 모든 댓글을 조회할 수 있습니다.
- 댓글 수정, 삭제는 댓글의 작성자 혹은 게시글의 작성자만 가능합니다.

### Friend
- 본인에게 친구요청을 할 수 없습니다.
- 요청의 Status 를 저장해 응답대기(unChecked), 수락(accepted), 거절(rejected) 를 구분하였습니다.
- Status 를 활용해 친구 목록(accepted), 친구 요청 목록(unChecked) 를 조회할 수 있습니다.

### Like
- 본인의 게시글, 댓글에는 좋아요를 할 수 없습니다.
- 같은 사용자는 게시글, 댓글에 한 번만 좋아요가 가능합니다.

---

## 기능

---
### 공통
- 로그인 후 서버는 클라이언트에게 JSESSIONID를 쿠키에 저장하고, 이후 클라이언트는 서버로 요청을 보낼 때마다 해당 쿠키를 자동으로 서버에 포함하게 하여, 요청마다 자동으로 인증이 이루어집니다.
- 수정, 삭제 시 Session 에 저장된 로그인된 유저 정보를 통해 본인만 수정 / 삭제 할 수 있습니다.
- ControllerAdvice를 이용해 GlobalExceptionHandler 를 구현하여 통일된 에러 Response 를 message, status 등을 활용하여 반환할 수 있도록 했습니다.
- 삭제가 진행될 때 완전히 삭제하지 않고 테이블 마다 deleted 컬럼을 사용하여 Soft Delete 를 구현했습니다.
- 영속성 전이를 활용해 데이터가 삭제될 때 관련된 데이터가 같이 삭제될 수 있도록 했습니다.(ex. 유저가 삭제되면 유저가 작성한 게시글, 댓글 등이 함께 삭제됨)

### User
- 사용자를 Create, Read, Update, Delete 할 수 있습니다.
- 프로필 조회 시 민감한 정보(password) 는 조회되지 않습니다.
- 비밀번호 수정 시, 본인 확인을 위해 현재 비밀번호를 입력하여 올바른 경우에만 수정할 수 있습니다.
- 현재 비밀번호와 동일한 비밀번호로는 변경할 수 없습니다.
- 비밀번호는 최소 8자 이상이며, 대문자, 소문자, 숫자, 특수문자를 최소 1개씩 포함해야 합니다.
- 탈퇴한 사용자의 아이디는 재사용할 수 없습니다.

### Post
- 게시글을 Create, Read, Update, Delete 할 수 있습니다.
- 기본 정렬은 생성일자(created_at) 기준으로 내림차순 정렬합니다.
- page, limit 정보를 전달 받아 페이지네이션을 할 수 있습니다.
- 작성 시 ask, info 타입을 분리하여 원하는 카테고리의 글을 작성할 수 있고, 원하는 카테고리의 글을 조회할 수 있습니다.
- 필터링을 통해 팔로우를 승인한 사용자의 게시글만 확인할 수 있습니다.
- 게시글에 추가된 댓글을 모두 볼 수 있습니다.
- Type 을 Enum 을 사용하여 좀 더 안정적으로 관리했습니다.

### Comment
- 댓글을 Create, Read, Update, Delete 할 수 있습니다.
- 내용만 수정이 가능합니다.

### Friend
- 친구 요청을 보낼 수 있고(Create), 요청을 확인(Read) 후 응답(Update, Delete)할 수 있습니다.
- 뉴스피드에 친구들의 게시글을 모아 볼 수 있습니다.
- Status 를 Enum 을 사용하여 좀 더 안정적으로 관리했습니다.

### Like
- 게시글과 댓글에 좋아요를 남기고 취소할 수 있습니다.

## Trouble Shooting
- [김민범 Trouble Shooting : JPA 삭제 구현 방법](https://velog.io/@alsqja2626/Trouble-Shooting-JPA-%EC%82%AD%EC%A0%9C-%EA%B5%AC%ED%98%84-%EB%B0%A9%EB%B2%95)
- [조현지 Trouble Shooting : JPA를 활용한 join 구현과 Project Trouble Shooting](https://something-do-it.tistory.com/23)
- [백은영 Trouble Shooting : 댓글, 게시글 관계 검증 문제 해결 ](https://beunyeong.tistory.com/106)
- [김동건 Trouble Shooting : Bean Validation 적용 시 Trouble Shooting](https://codinggeony.tistory.com/14)

## 마치며
### 완성 소감
- 김민범 : 대현지 만세!
- 조현지 : 처음 SPRING 팀 프로젝트라서 타인의 코드를 이해하고 협업을 위한 진행방식에 대해 어려움이 많았던것 같습니다. 하지만 팀원 분들 모두 맡은 파트를 잘 진행해 주시고 적극적으로 진행 내용 공유등을 해주셔서 정말 즐겁게 프로젝트를 진행 할 수 있었습니다. 아쉬웠던 점은 다음에 진행하게 된다면 기능 구현을 좀 더 생각한 ERD 작성과 Restful한 API 작성을 고민해보고 진행해야 겠다 생각합니다. 한 주 동안의 프로젝트 였지만 정말 재밌게 코딩하고 많이 배울 수 있었습니다!
- 백은영 : 이번 첫 프로젝트를 진행하면서 개인적으로 많은 도전과 배움의 기회를 얻었습니다. 이전에는 개인 과제를 진행하면서 GitHub를 깊이 활용할 기회가 적었지만, BugFixHub팀과 함께 하면서 각자 Branch를 생성하고, Pull Request부터 Merge까지 진행하는 협업의 기본적인 흐름을 배우고 경험 할 수 있었습니다. 팀 프로젝트 전에는 주어진 과제의 필수 기능만 겨우 완성하곤 했지만, 이번 프로젝트에서 도전 기능까지 성공 할 수 있었습니다. 특히, 제가 미처 생각하지 못한 코드 구현 방법들을 팀원분들이 알려주고 함께 고민할 기회를 만들어준 덕분에 새로운 시각을 배우며 한단계 성장할 수 있었습니다! 앞으로의 프로젝트에서도 이런 경험을 바탕으로 더 나은 결과물을 만들어보고 싶습니다.(BugFixHub 만세)
- 김동건 : 코드 작성 및 코드이해도에 있어 부족한 부분이 많았지만, 잘 이끌어주신 팀장님과 팀원분들이 있으셔서 재미있게 프로젝트를 잘 완성할 수 있었고 너무 감사했습니다. 덕분에 이번 프로젝트에서 GitHub를 통한 협업을 어떻게 진행해야 할 지 알게되었고, 코드를 짜보고, 오류도 수정해보면서 얻어가는 것이 많았습니다. 다만 개인적으로 아쉬운 점은 제가 아직 코드 짜는 부분이나 설계할 때 전체적인 모습을 보는 시야가 많이 부족해 눈앞에 보이는 문제만 해결하다보니, 그 부분을 수정해도 다른 부분에서 문제가 발생하는 경우도 있었고, 아직 ORM에 대한 이해가 부족해서 코드 리팩토링 해야 하는 부분도 많이 발생하는 등의 문제점이 보여 아쉬웠습니다. 다음번에는 이런 부분들을 보완하려 노력하겠습니다.

### 아쉬웠던 점
- **테스트 코드의 필요성** : 파트를 나누어 각자 개발을 진행할 때 문제없이 작동하던 코드들이 각자의 코드가 합쳐지며, 또 서비스가 확장되며 여러가지 문제가 발생했습니다. 또한 포스트맨으로만 테스트를 진행하다보니, 하나의 기능이 추가되면 관련된 모든 요청에 대해 다시 테스트를 진행해야 했습니다. 테스트 코드를 적절히 활용할 수 있었다면 이러한 문제들을 보다 쉽게 해결할 수 있다고 생각됩니다.
- **초기 기획의 중요성** : 초기 기획 단계에서 충분히 많은 고민들을 거치며 기획을 했다고 생각했지만, 서비스를 확장하며 몇몇 문제가 발생했습니다. 더 철저한 기획이 프로젝트의 품질을 높이고, 향후 유지보수를 더욱 효율적으로 할 수 있게 만들 수 있다고 생각합니다.

