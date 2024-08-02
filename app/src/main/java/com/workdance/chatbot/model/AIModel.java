package com.workdance.chatbot.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AIModel {
    private String name;
    private String code;

    public AIModel(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static List<AIModel> getAIModels() {
        List<AIModel> aiModels = new ArrayList<>();
        aiModels.add(new AIModel("通义千问", "qwen2:7b"));
        aiModels.add(new AIModel("Meta llama", "llama3"));
        aiModels.add(new AIModel("谷歌 gemma", "gemma:7b"));
        return aiModels;
    }
}
