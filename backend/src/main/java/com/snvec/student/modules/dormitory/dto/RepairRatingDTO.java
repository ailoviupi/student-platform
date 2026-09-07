package com.snvec.student.modules.dormitory.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;

public class RepairRatingDTO {

    @NotNull(message = "报修ID不能为空")
    private Long repairId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低1分")
    @Max(value = 5, message = "评分最高5分")
    private Integer rating;

    private String ratingContent;

    public Long getRepairId() { return repairId; }
    public void setRepairId(Long repairId) { this.repairId = repairId; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getRatingContent() { return ratingContent; }
    public void setRatingContent(String ratingContent) { this.ratingContent = ratingContent; }
}
