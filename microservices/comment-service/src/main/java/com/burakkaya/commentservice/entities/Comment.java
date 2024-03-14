package com.burakkaya.commentservice.entities;

import com.burakkaya.commonpackage.utils.enums.Rate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "invoices")
public class Comment {
    @Id
    private String id;
    private String text;
    private String userId;
    private String restaurantId;
    private Rate rate;
    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime commentedAt;
}
