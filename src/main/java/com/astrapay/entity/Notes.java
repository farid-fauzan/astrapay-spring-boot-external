package com.astrapay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notes {
    private int id;
    private String title;
    private String content;

    public Notes(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
