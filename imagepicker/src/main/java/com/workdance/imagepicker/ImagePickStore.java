package com.workdance.imagepicker;

import com.workdance.imagepicker.model.ImageFolder;
import com.workdance.imagepicker.model.ImageItem;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ImagePickStore {
    @Getter
    private ArrayList<ImageItem> pickedImages = new ArrayList<>();
    @Setter
    private List<ImageFolder> imageFolders;
    private int currentImageFolder = 0;
    private boolean compress = true;
    private static ImagePickStore store;

    private ImagePickStore() {
    }

    public synchronized static ImagePickStore getInstance() {
        if (store == null) {
            store = new ImagePickStore();
        }

        return store;
    }

    public void clearSelectedImages() {
        if (pickedImages != null) {
            pickedImages.clear();
        }
    }

    public void addSelectedImageItem(ImageItem imageItem, boolean isAdd) {
        if (isAdd) {
            pickedImages.add(imageItem);
        } else {
            pickedImages.remove(imageItem);
        }
    }

    public ArrayList<ImageItem> getSelectedImages() {
        return pickedImages;
    }

    public int getSelectImageCount() {
        if (pickedImages != null) {
            return pickedImages.size();
        }
        return 0;
    }

    public void setCurrentImageFolderPosition(int mCurrentSelectedImageSetPosition) {
        currentImageFolder = mCurrentSelectedImageSetPosition;
    }

    public ArrayList<ImageItem> getCurrentImageFolderItems() {
        return imageFolders.get(currentImageFolder).getImages();
    }
}
