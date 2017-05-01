package com.volunteers.areas.articles.models.binding;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class RegisterArticleModel {

    @NotBlank
    @Length(min = 5)
    private String name;

    @NotBlank
    @Length(max = 11, min = 11)
    private String content;

    @NotBlank
    @Length(min = 20)
    private String text;

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
}
