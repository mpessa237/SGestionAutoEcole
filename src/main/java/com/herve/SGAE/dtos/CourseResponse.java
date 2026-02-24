package com.herve.SGAE.dtos;

import com.herve.SGAE.enums.StatusCourse;
import com.herve.SGAE.enums.TypeCourse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {

    private Long id;
    private TypeCourse typeCourse;
    private LocalDateTime dateHour;
    private Integer duration;
    private StatusCourse statusCourse;
    private List<Long> studentIds;
    private List<String> studentFirstnames;
    private String classroom;
    private Long monitorId;
    private String monitorFirstname;
    private Long vehicleId;

}
