package com.herve.SGAE.dtos;

import com.herve.SGAE.enums.TypeCourse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {
    private TypeCourse typeCourse;
    private LocalDateTime dateHour;
    private Integer duration;
    private Long studentId;
    private Long monitorId;

}
