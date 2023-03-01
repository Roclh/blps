package com.roclh.blps.database;


import com.roclh.blps.entities.Category;
import com.roclh.blps.entities.StudopediaArticle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudopediaDatabase extends JpaRepository<StudopediaArticle, Long>{
    @Query("select distinct s from StudopediaArticle s")
    List<StudopediaArticle> getAllArticles();
    List<StudopediaArticle> findByCategoryEquals(Category category, Pageable pageable);
    List<StudopediaArticle> findByNameContainsIgnoreCase(String name, Pageable pageable);
    Optional<StudopediaArticle> findByNameEqualsIgnoreCase(String name);

    Optional<StudopediaArticle> findByIdEquals(Long id);

}
