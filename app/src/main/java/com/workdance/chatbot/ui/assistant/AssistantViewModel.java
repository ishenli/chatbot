package com.workdance.chatbot.ui.assistant;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.workdance.chatbot.api.AssistantClient;
import com.workdance.chatbot.api.ChatClient;
import com.workdance.chatbot.core.dto.OperateResult;
import com.workdance.chatbot.core.util.FileUtils;
import com.workdance.chatbot.model.Assistant;
import com.workdance.chatbot.model.Brain;

import java.util.List;

public class AssistantViewModel extends ViewModel {
    private static final String TAG = "AssistantViewModel";
    public MutableLiveData<Assistant> assistant = new MutableLiveData<>();
    public MutableLiveData<List<Brain>> brains = new MutableLiveData<>();
    public void setBrains(List<Brain> brains) {
        this.brains.setValue(brains);
    }

    public LiveData<List<Brain>> getBrains() {
        return brains;
    }

    public void loadBrains() {

    }

    public LiveData<OperateResult<Boolean>> addAssistant(Assistant assistant) {
        return AssistantClient.addAssistant(assistant);
    }

    public LiveData<String> updateAssistantAvatar(String imagePath) {
        MutableLiveData<String> resultLiveData = new MutableLiveData<>();
        byte[] content = FileUtils.readFile(imagePath);
        if (content != null) {
            ChatClient.uploadFile(imagePath, "头像上传").observeForever(response -> {
                if (response.isSuccess()) {
                    Assistant assistant1 = new Assistant();
                    assistant1.setLogo(FileUtils.getStaticFilePath(response.getResult()));
                    Log.d(TAG, "updateAssistantAvatar: " + assistant1.getLogo());
                    assistant.setValue(assistant1);
                    resultLiveData.setValue(response.getResult());
                }
            });
        }
        return resultLiveData;
    }
}
