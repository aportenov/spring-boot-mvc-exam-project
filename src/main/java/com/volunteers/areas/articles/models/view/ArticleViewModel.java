package com.volunteers.areas.articles.models.view;

import java.util.Date;

public class ArticleViewModel {

    private Long id;

    private String name;

    private String content;

    private Date creationDate;

    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreationDate() {
        return creationDate ;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}

