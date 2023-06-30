package com.example.demo.constants;

public enum ResponseType {
    SUCCESS("Successfully enrolled in course."),
    FAIL_DEPT_MISMATCH("Course and student departments do not match."),
    ALREADY_ENROLLED("You are already enrolled in this course."),
    NOT_ENROLLED("You are currently not enrolled in any course."),
    INVALID_COURSE("Invalid course."),
    CONTACT_REQ_RECV("Your request is received. A staff member will follow up shortly."),
    CONTACT_REQ_HOLD("You've contacted outside office hours. A ticket is raised for you. Updates will be sent to your email: "),
    INVALID_STUDENT("Invalid student.");

    private final String message;

    ResponseType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}