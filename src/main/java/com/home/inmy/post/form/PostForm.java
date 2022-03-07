package com.home.inmy.post.form;

import com.home.inmy.domain.Account;
import com.home.inmy.web.dto.PostDto;
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

    @NotBlank
    private String category;

    private String writer;



    @Builder
    public PostForm(String title, String content, String writer, String category, List<MultipartFile> imageFiles, Set<String> tags) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.writer = writer;
        this.imageFiles = (imageFiles != null) ? imageFiles : new ArrayList<>();
        this.tags = (tags != null) ? tags : new HashSet<>();
    }


    public PostDto createBoardPostDto(Account account) {

        return PostDto.builder()
                .title(title)
                .account(account)
                .category(category)
                .content(content)
                .imageFiles(imageFiles)
                .tags(tags)
                .writer(account.getLoginId())
                .build();
    }

}
