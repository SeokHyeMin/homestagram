package com.home.inmy.form;

import com.home.inmy.domain.entity.Account;
import com.home.inmy.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostForm {

    @NotBlank
    @Length(min = 2, max = 20)
    private String title;

    @NotNull
    private List<MultipartFile> imageFiles;

    @NotBlank
    private String content;

    private Set<String> tags;

    private String writer;



    @Builder
    public PostForm(String title, String content, String writer, List<MultipartFile> imageFiles, Set<String> tags) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.imageFiles = (imageFiles != null) ? imageFiles : new ArrayList<>();
        this.tags = (tags != null) ? tags : new HashSet<>();
    }


    public PostDto createBoardPostDto(Account account) {

        return PostDto.builder()
                .title(title)
                .account(account)
                .content(content)
                .imageFiles(imageFiles)
                .tags(tags)
                .writer(account.getLoginId())
                .build();
    }

}
