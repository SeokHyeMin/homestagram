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
<img src="https://user-images.githubusercontent.com/73224388/161745220-a3c8e48b-ee7f-41e7-8025-b454c7391858.gif">

- 프로필에 들어가면 팔로잉, 팔로워들을 확인할 수 있습니다.
- 자신이 작성한 게시물, 좋아요한 게시물, 북마크한 게시글을 확인할 수 있습니다. (사진누르면 해당 게시글로 이동)
  - **위의 두 경우 ajax로 화면전환 없이 게시물이 보여지는 부분만 변하도록 하였습니다.**
- 프로필 주인인 경우 프로필 수정(회원정보 수정)이 가능합니다.

### ✅ 2.2 - 게시물 좋아요, 북마크, 댓글 기능
<img src="https://user-images.githubusercontent.com/73224388/161750626-5f30fcb1-dc50-47f8-b204-cefdbe648bd3.gif"> 

게시글 조회 화면에서, 해당 게시글을 좋아요, 북마크 할 수 있습니다. 누른 즉시 결과가 화면에 반영됩니다. 

<img src="https://user-images.githubusercontent.com/73224388/161751016-043433f2-ec8d-4c97-813d-53176e6b593b.gif">

댓글 작성도, 화면전환없이 ajax로 등록하도록 구현하였으며, 댓글 작성자만이 삭제할 수 있습니다. 댓글은 한 화면에 5개씩 나타나도록 구현하였습니다.


### ✅ 2.3 - 태그 검색 기능

### ✅ 2.4 - 게시물 최신순, 좋아요 순 정렬 기능

### ✅ 2.5 - 관리자에게 권한 부여

## 3. 트러블 슈팅
