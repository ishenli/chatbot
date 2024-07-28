package com.workdance.chatbot.core.util;

import java.util.regex.Pattern;

public class MarkdownFormatter {
    private boolean inList = false;
    private boolean inCodeBlock = false;
    private StringBuilder output = new StringBuilder();

    public String getOutput() {
        return output.toString();
    }

    public StringBuilder formatMarkdown(String line) {
        line = line.trim();

        // 处理空行
        if (line.isEmpty()) {
            if (inList) {
                inList = false;
                output.append("\n");
            }
            output.append("\n");
            return output;
        }

        // 处理代码块
        if (line.startsWith("```")) {
            inCodeBlock = !inCodeBlock;
            output.append(line).append("\n");
            return output;
        }

        if (inCodeBlock) {
            output.append(line).append("\n");
            return output;
        }

        // 处理标题
        if (line.startsWith("#")) {
            output.append(line).append("\n\n");
            return output;
        }

        // 处理无序列表
        if (line.startsWith("- ") || line.startsWith("* ") || line.startsWith("+ ")) {
            if (!inList) {
                output.append("\n");
                inList = true;
            }
            output.append(line).append("\n");
            return output;
        }

        // 处理有序列表
        if (Pattern.matches("^\\d+\\.\\s.*", line)) {
            if (!inList) {
                output.append("\n");
                inList = true;
            }
            output.append(line).append("\n");
            return output;
        }

        // 处理普通段落
        if (inList) {
            inList = false;
            output.append("\n");
        }
        output.append(line).append("\n\n");
        return output;
    }
}
