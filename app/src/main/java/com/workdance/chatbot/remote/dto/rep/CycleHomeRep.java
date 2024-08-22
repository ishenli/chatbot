package com.workdance.chatbot.remote.dto.rep;

import com.workdance.chatbot.model.Circle;

import java.util.List;

import lombok.Data;

@Data
public class CycleHomeRep {
    private List<Circle> cycleList;
}
