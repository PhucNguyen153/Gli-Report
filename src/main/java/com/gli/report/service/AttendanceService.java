package com.gli.report.service;

import com.gli.report.entity.Attendance;
import com.gli.report.entity.Student;
import com.gli.report.model.AttendanceRequest;
import com.gli.report.model.AttendanceResponse;
import com.gli.report.repository.AttendanceRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AttendanceService {

    private final AttendanceRepo attendanceRepo;

    public AttendanceService(AttendanceRepo attendanceRepo) {
        this.attendanceRepo = attendanceRepo;
    }

    public List<AttendanceResponse> getAttendanceByTime(AttendanceRequest request) {
        List<Attendance> arrayList = attendanceRepo.findAllByGradeIdAndTime(request.getGradeIds(), request.getStartDate(), request.getEndDate());
        List<AttendanceResponse> result = new ArrayList<>();
        Set<Attendance> uniqueElements = new HashSet<>(arrayList);
        Map<Student, List<Attendance>> mapByStudent = uniqueElements.stream().collect(Collectors.groupingBy(Attendance::getStudent));
        mapByStudent.forEach((s, aLst) -> {
            AttendanceResponse res = new AttendanceResponse();
            res.setHolyName(s.getHolyName());
            res.setFullName(s.getFullName());
            res.setTeacher(aLst.get(0).getGrade().getTeacher());
            res.setGradeName(aLst.get(0).getGrade().getName());
            int weekDay = 0;
            int sunDay = 0;
            int attendClass = 0;

            for (Attendance a: aLst) {
                if (a.getAttendanceDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                    if (a.getType() == 1) {
                        sunDay++;
                    } else {
                        attendClass++;
                    }
                } else {
                    weekDay++;
                }
            }

            res.setWeekDay(weekDay);
            res.setSunday(sunDay);
            res.setAttendClass(attendClass);
            result.add(res);
        });
        return result;
    }
}
