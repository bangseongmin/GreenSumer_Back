package org.swyg.greensumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swyg.greensumer.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
