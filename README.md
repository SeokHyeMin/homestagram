# homestagram
인테리어에 관심있는 사람들이 모여, 인테리어 사진들을 올리고 소통할 수 있는 커뮤니티 입니다.

## 1. 기술 스택
#### Front-end
<img src="https://img.shields.io/badge/Bootstrap-7952B3?style=flat-square&logo=Bootstrap&logoColor=white"/> <img src="https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=HTML5&logoColor=white"/> <img src="https://img.shields.io/badge/CSS3-1572B6?style=flat-square&logo=CSS3&logoColor=white"/> <img src="https://img.shields.io/badge/Javascript-F7DF1E?style=flat-square&logo=Javascript&logoColor=white"/> <img src="https://img.shields.io/badge/Jquery-0769AD?style=flat-square&logo=Jquery&logoColor=white"/>
#### Back-end
<img src="https://img.shields.io/badge/JAVA-007396?style=flat-square&logo=JAVA&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=Spring Security&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=flat-square&logo=Spring&logoColor=white"/> <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=flat-square&logo=PostgreSQL=white"/>

## 2. 핵심기능
- 프로필 기능
- 게시물 좋아요, 북마크, 댓글 기능
- 태그 검색 기능
- 게시물 최신순, 좋아요 순 정렬 기능
- 관리자에게 권한 부여

### ✅ 2.1 - 프로필 기능
<img src="https://user-images.githubusercontent.com/73224388/161745220-a3c8e48b-ee7f-41e7-8025-b454c7391858.gif" width="500" height="300">

- 프로필에 들어가면 팔로잉, 팔로워들을 확인할 수 있습니다.
- 자신이 작성한 게시물, 좋아요한 게시물, 북마크한 게시글을 확인할 수 있습니다. (사진누르면 해당 게시글로 이동)
  - **위의 두 경우 ajax로 화면전환 없이 게시물이 보여지는 부분만 변하도록 하였습니다.**
- 프로필 주인인 경우 프로필 수정(회원정보 수정)이 가능합니다.

### ✅ 2.2 - 게시물 좋아요, 북마크, 댓글 기능
<img src="https://user-images.githubusercontent.com/73224388/161750626-5f30fcb1-dc50-47f8-b204-cefdbe648bd3.gif" width="600" height="300"> <img src="https://user-images.githubusercontent.com/73224388/161751016-043433f2-ec8d-4c97-813d-53176e6b593b.gif" width="400" height="300">

- 게시글 조회 화면에서, 해당 게시글을 좋아요, 북마크 할 수 있습니다. 누른 즉시 결과가 화면에 반영됩니다. 
- 댓글 작성도, 화면전환없이 ajax로 등록하도록 구현하였으며, 댓글 작성자만이 삭제할 수 있습니다. 댓글은 한 화면에 5개씩 나타나도록 구현하였습니다.


### ✅ 2.3 - 태그 검색 기능 
<img src="https://user-images.githubusercontent.com/73224388/161753594-357791c2-cbdb-4442-a754-10923ab660ed.gif" width="600" height="300" > <img src="https://user-images.githubusercontent.com/73224388/161754434-7348dca7-bca4-4808-9666-12c412e7b647.gif" width="400" height="300">

- 게시글 작성 시에 여러 개의 태그를 등록할 수 있습니다. 
- 검색창에서 검색 혹은, 게시글에서 태그를 클릭하여 같은 태그를 작성한 게시글들을 조회할 수 있습니다.
  - **ajax로 화면전환 없이 게시물이 보여지는 부분만 변하도록 하였습니다.**

### ✅ 2.4 - 게시물 최신순, 좋아요 순 정렬 기능

### ✅ 2.5 - 관리자에게 권한 부여
<img src="https://user-images.githubusercontent.com/73224388/161756120-091cb1e6-af0f-4a96-98cf-1c6e3280c7eb.png"> 
<img src="https://user-images.githubusercontent.com/73224388/161755905-f7214629-cb77-458c-a486-d1c96a64223d.png" width="700" height="300">

- 관리자 계정으로 로그인 한 경우 main-navbar에 회원관리 항목이 추가됩니다.
- 회원관리 페이지에서는 모든 회원들을 확인할 수 있으며, 관리자 권한을 부여할 수 있으며 회원을 강제 탈퇴시킬 수 있습니다.
- 관리자만 접속 가능한 URI는 SecurityConfig 의 configure(HttpSecurity http) 메서드에 다음과 같이 작성하여 설정하였습니다.
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
