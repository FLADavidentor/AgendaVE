package com.uam.agendave.model;

import com.uam.agendave.dto.TestDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ApiResponse {
    private boolean success;
    private String message;
    private List<TestDTO> data;
}
