package com.roclh.mainmodule.exceptions;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@Getter
@Builder
public class ErrorBody {
    private int statusCode;
    private String message;
    private String details;
    private StackTraceElement[] stackTrace;
    private LocalDateTime timeStamp;
}
