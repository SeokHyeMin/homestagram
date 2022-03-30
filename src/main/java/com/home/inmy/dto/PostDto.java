package com.home.inmy.web.dto;

import com.home.inmy.domain.Account;
import com.home.inmy.domain.Post;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class PostDto {

    @NotBlank
    @Length(min = 2, max = 20)
    private String title;

    @NotBlank
    private List<MultipartFile> imageFiles = new ArrayList<>();

    @NotBlank
    private String content;

    private Set<String> tags;

    private Account account;

    private String writer;

    @Builder
    public PostDto(String title, String content, String writer, Account account, List<MultipartFile> imageFiles, Set<String> tags) {
        this.title = title;
        this.content = content;
        this.account = account;
        this.imageFiles = imageFiles;
        this.tags = tags;
        this.writer = writer;
    }

    public Post createPost() {
        return Post.builder()
                .title(title)
                .account(account)
                .imageFiles(new ArrayList<>())
                .postTags(new HashSet<>())
                .writeTime(LocalDateTime.now())
                .content(content)
                .writer(writer)
                .views(0L)
                .likes(0L)
                .build();
    }
}
