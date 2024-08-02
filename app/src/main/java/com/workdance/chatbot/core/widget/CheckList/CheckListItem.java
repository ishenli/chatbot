package com.workdance.chatbot.core.widget.CheckList;

public class CheckListItem {
    private String text;
    private String value;
    private boolean isChecked;

    public CheckListItem(String text, String value) {
        this.text = text;
        this.value = value;
        this.isChecked = false;
    }

    public String getText() {
        return text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getValue() {
        return value;
    }
}
