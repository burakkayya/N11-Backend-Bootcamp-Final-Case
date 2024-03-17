package com.burakkaya.commentservice.business.dto.responses;

import com.burakkaya.commonpackage.utils.enums.Rate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentResponse {
    private String id;
    private String text;
    private String userId;
    private String restaurantId;
    private Rate rate;
    private LocalDateTime commentedAt;
}
