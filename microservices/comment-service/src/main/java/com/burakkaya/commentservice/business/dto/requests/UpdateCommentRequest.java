package com.burakkaya.commentservice.business.dto.requests;

import com.burakkaya.commonpackage.utils.enums.Rate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentRequest {
    private String id;
    private String text;
    private Rate rate;
}
