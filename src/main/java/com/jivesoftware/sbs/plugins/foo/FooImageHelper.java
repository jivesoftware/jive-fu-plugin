package com.jivesoftware.sbs.plugins.foo;

import java.util.ArrayList;
import java.util.List;

import com.jivesoftware.community.Image;
import com.jivesoftware.community.ImageManager;
import com.jivesoftware.community.JiveIterator;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.NotFoundException;
import com.jivesoftware.community.lifecycle.JiveApplication;

public class FooImageHelper {

    private ImageManager imageManager;

    public static void deleteImage(Image image) {
        JiveApplication.getContext().getImageManager().deleteImage(image);
    }

    public void saveImages(JiveObject resource, JiveIterator<Image> images) {
        imageManager.saveImages(resource, images);
    }

    public List<Long> getImages(long id, int objectType) {
        try {
            return imageManager.getImageIDs(objectType, id);
        } catch (NotFoundException e) {
            return new ArrayList<Long>();
        }
    }

    public void setImageManager(ImageManager imageManager) {
        this.imageManager = imageManager;
    }
}
