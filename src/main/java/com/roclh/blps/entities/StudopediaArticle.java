package com.roclh.blps.entities;

import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "article")
@Getter
@Setter
@NoArgsConstructor
public class StudopediaArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studopedia_article_gen")
    @SequenceGenerator(name = "studopedia_article_gen", sequenceName = "studopedia_article_seq")
    @Column(name = "id", nullable = false)
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @ApiModelProperty(value = "category")
    private Category category;


    @ApiModelProperty(value = "content")
    private String content;


    public StudopediaArticle(String name, String content, Category category) {
        this.name = name;
        this.content = content;
        this.category = category;
    }


}
