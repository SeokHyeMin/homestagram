package com.home.inmy.post;

import com.home.inmy.domain.Account;
import com.home.inmy.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long>{

    List<Post> findByAccount(Account account);

    Page<Post> findByAccount(Account account, Pageable pageable);

    @Query(value = "select p from Post p join fetch p.account join fetch p.imageFiles", countQuery = "select count(p.id) from Post p")
    Page<Post> findPostAllCountBy(Pageable pageable);

    @Query(
            value = "select p from Post p join fetch p.account join fetch p.imageFiles where p.category = :category",
            countQuery = "select count(p.id) from Post p"
    )
    Page<Post> findByCategory(@Param("category") String category, Pageable pageable);
}
