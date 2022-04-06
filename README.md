# homestagram
인테리어에 관심있는 사람들이 모여, 인테리어 사진들을 올리고 소통할 수 있는 커뮤니티 입니다.
## DB 설계
<img src="https://user-images.githubusercontent.com/73224388/161961146-c0f8add6-aa86-489d-8bc2-59dd954edcba.png" width="800">

## 1. 기술 스택
#### Front-end
<img src="https://img.shields.io/badge/Bootstrap-7952B3?style=flat-square&logo=Bootstrap&logoColor=white"/> <img src="https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=HTML5&logoColor=white"/> <img src="https://img.shields.io/badge/CSS3-1572B6?style=flat-square&logo=CSS3&logoColor=white"/> <img src="https://img.shields.io/badge/Javascript-F7DF1E?style=flat-square&logo=Javascript&logoColor=white"/> <img src="https://img.shields.io/badge/Jquery-0769AD?style=flat-square&logo=Jquery&logoColor=white"/>
#### Back-end
<img src="https://img.shields.io/badge/JAVA-007396?style=flat-square&logo=JAVA&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=Spring Security&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=flat-square&logo=Spring&logoColor=white"/> <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=PostgreSQL&logoColor=white"/> 

## 2. 핵심기능
- 프로필 기능
- 게시물 좋아요, 북마크, 댓글 기능
- 태그 검색 기능
- 게시물 최신순, 좋아요 순 정렬 기능
- 메인페이지 , 조회수 높은 게시물 4개 노출
- 관리자에게 권한 부여

### ✅ 2.1 - 프로필 기능
<img src="https://user-images.githubusercontent.com/73224388/161745220-a3c8e48b-ee7f-41e7-8025-b454c7391858.gif" width="700" height="300">

- Account에 임베디드 값 타입으로 Profile을 생성하였습니다.
- 프로필에 들어가면 팔로잉, 팔로워들을 확인할 수 있습니다.
- 자신이 작성한 게시물, 좋아요한 게시물, 북마크한 게시글을 확인할 수 있습니다. (사진누르면 해당 게시글로 이동)
  - **위의 두 경우 ajax로 화면전환 없이 게시물이 보여지는 부분만 변하도록 하였습니다.**
- 프로필 주인인 경우 프로필 수정(회원정보 수정)이 가능합니다.

### ✅ 2.2 - 게시물 좋아요, 북마크, 댓글 기능
<img src="https://user-images.githubusercontent.com/73224388/161750626-5f30fcb1-dc50-47f8-b204-cefdbe648bd3.gif" width="400" height="300"> <img src="https://user-images.githubusercontent.com/73224388/161751016-043433f2-ec8d-4c97-813d-53176e6b593b.gif" width="400" height="300">

- 게시글을 좋아요 또는 북마크 할 수 있습니다. 누른 즉시 결과가 화면에 반영됩니다. 
- 댓글 작성도, ajax로 등록하도록 구현하여 화면전환없이 반영되도록 하였습니다. 댓글 작성자만이 삭제할 수 있으며 한 화면에 5개씩 나타나도록 구현하였습니다.


### ✅ 2.3 - 태그 검색 기능 
<img src="https://user-images.githubusercontent.com/73224388/161753594-357791c2-cbdb-4442-a754-10923ab660ed.gif" width="500" height="300" > <img src="https://user-images.githubusercontent.com/73224388/161754434-7348dca7-bca4-4808-9666-12c412e7b647.gif" width="300" height="300">

- 게시글 작성 시에 여러 개의 태그를 등록할 수 있습니다. 같은 태그를 작성한 게시글들을 조회할 수 있습니다.
- 태그 검색 방법
  - 검색창에서 태그를 검색.
    - **ajax로 화면전환 없이 게시물이 보여지는 부분만 변하도록 하였습니다.**
  - 게시글에서 태그 클릭
  

### ✅ 2.4 - 게시물 최신순, 좋아요 순 정렬 기능
<img src="https://user-images.githubusercontent.com/73224388/161766153-34aa128d-284f-4443-b125-ffc80c04728a.gif"> 

- 게시글을 8개 단위로 페이징 하였습니다.
- 최신순, 좋아요 순 정렬할 수 있습니다. 게시물 리스트만 바뀌도록 하였습니다.
### ✅ 2.5 - 관리자계정(ROLE_ADMIN)에게 권한 부여
<img src="https://user-images.githubusercontent.com/73224388/161756120-091cb1e6-af0f-4a96-98cf-1c6e3280c7eb.png"> 
<img src="https://user-images.githubusercontent.com/73224388/161755905-f7214629-cb77-458c-a486-d1c96a64223d.png" width="700" height="300">

- 관리자 계정으로 로그인 한 경우 main-navbar에 회원관리 항목이 추가됩니다.
- 회원관리 페이지에서는 모든 회원들을 확인할 수 있으며, 관리자는 회원의 권한변경 및 강제 탈퇴가 가능합니다.
- 관리자만 접속 가능한 URI는 ```SecurityConfig``` 의 ```configure(HttpSecurity http)``` 메서드에 다음과 같이 작성하여 설정하였습니다.
```java 
   protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/","/login*","/sign-up","/postList","/searchTag","/searchTag/orderBy","/find-password").permitAll()
                .mvcMatchers(HttpMethod.GET,"/post/**","/commentList/**","/followerList/**","/followList/**").permitAll()
                .mvcMatchers(HttpMethod.GET,"/profile/**").permitAll()
                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN") //관리자만 접속가능한 페이지
                .anyRequest().authenticated();
```

## 3. 트러블 슈팅
### 3.1 - AJAX POST 전송 시 403에러
Spring Security를 사용하면서 ajax post방식 통신 시에 403에러 발생
- Spring Security의 CsrfFilter는 CSRF를 방지하기 위한 필터로 모든 요청에 랜덤하게 생성된 토큰을 HTTP 파라미터로 요구합니다.
- POST 방식으로 값을 전달하고자 할 때 Csrf 토큰값과 토큰명을 가지고 요청을 해야합니다.
- 스프링의 Form태그로 전송하는 경우, 자동적으로 생성되지만 AJAX를 이용해 POST방식으로 전송하는 경우에는 직접 토큰 값을 포함하여 보내야합니다.
- 하지만, csrf토큰을 포함하지 않은채로 전송하여 403에러가 발생하였습니다.

#### 해결방법
```javascript
//헤더 값에 csrf 토큰 값을 정한 후,
<meta id="_csrf" name="_csrf" th:content="${_csrf.token}"/>
<meta id="_csrf_header" name="_csrf_header" th:content="${_csrf.headerName}"/>

  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");
  
  //방법 1. ajax전송 시 토큰 값을 함께 보내주기.
  $(function() {
    $(document).ajaxSend(function(e, xhr, options) {
      xhr.setRequestHeader(header, token);
    });
  });
  
  //방법 2. ajax전송 시 beforeSend로 함께 보내주기
  beforeSend : function(xhr)
  {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
    xhr.setRequestHeader("${_csrf.headerName}", "${_csrf.token}");
  }
```
- 헤더에 csrf 토큰값을 포함해준 뒤, ajax post 통신 시 토큰 값을 포함하여 전송하도록 하였습니다.


## 4. 회고 및 느낀점
### 4.1 - 프로젝트를 마치며
- Spring Boot를 처음 사용해보면서, 내장된 톰캣을 사용하는 기능이나, 복잡한 설정들을 자동으로 해주는 기능 덕분에 Spring Boot를 사용하지 않았던 때 보다 더 편한 개발을 진행하였습니다.
- JpaRepository 인터페이스를 상속받는 것만으로 SQL에 소모하는 시간을 줄일 수 있어서 편리하게 개발에 임할 수 있었습니다. 또한 메서드에 지정된 표현을 사용하여 네이밍하는 것으로 쿼리가 자동으로 생성되는 경험이 흥미로웠습니다.
- Spring을 강의를 통해 배우고, 학습을 토대로 혼자서 처음부터 끝까지 만들어본 프로젝트였기에 복잡한 기능들을 구현한 것은 아니지만, 기본적인 기능이라도 제대로 만들어보자는 마음가짐으로 임하였습니다. 이후에도 코드를 보다 더 깔끔하게 바꾸는 리팩토링 작업이나, 다양한 기능을 추가해보고 싶습니다.
### 4.2 - 아쉬운 점
- 개발을 하면서 단위 테스트를 초반 이후에 진행하지 못하였습니다. 
- 빠르게 개발을 진행하는것에 급급해 꼼꼼하게 확인하지 못한 불찰입니다.
- 단위 테스트를 하지 않았기 때문에 개발이 진행되면서 기능을 하나 추가할수록, 동작이 되는지를 일일이 화면 단에서 확인하기 위해 서버를 재시작해야 했기 때문에 여간 불편한 일이 아니었습니다.
- TDD에 대한 공부도 게을리하지 않고 다음 프로젝트 진행시에는 단위 테스트를 습관화 하도록 하겠습니다.
