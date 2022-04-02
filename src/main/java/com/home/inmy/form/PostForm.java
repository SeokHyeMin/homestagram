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
