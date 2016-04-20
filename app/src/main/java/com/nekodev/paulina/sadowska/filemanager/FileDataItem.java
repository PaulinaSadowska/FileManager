package com.nekodev.paulina.sadowska.filemanager;

/**
 * Created by Paulina Sadowska on 10.04.16.
 */
public class FileDataItem {

    private String name;
    private FileType type;
    private String lastModified;
    private String size;
    private boolean readable;
    private String absolutePath;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public FileDataItem() {
        isChecked = false;
    }

    public String getName() {
        return name;
    }

    public FileType getType() {
        return type;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getSize() {
        return size;
    }

    public boolean isReadable() {
        return readable;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }
}
