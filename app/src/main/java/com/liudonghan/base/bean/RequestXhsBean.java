package com.liudonghan.base.bean;

import java.util.ArrayList;
import java.util.List;

public class RequestXhsBean {

    private String cursorScore = "1.7389805179610028E9";
    private Integer num = 18;
    private Integer refreshType = 3;
    private Integer noteIndex = 38;
    private String unreadBeginNoteId = "";
    private String unreadEndNoteId = "";
    private Integer unreadNoteCount = 0;
    private String category = "homefeed_recommend";
    private String searchKey = "";
    private Integer needNum = 8;
    private Boolean needFilterImage = false;
    private List<String> imageFormats = new ArrayList<>();

    public RequestXhsBean() {
        imageFormats.add("jpg");
        imageFormats.add("webp");
        imageFormats.add("avif");
    }

    public String getCursorScore() {
        return cursorScore;
    }

    public void setCursorScore(String cursorScore) {
        this.cursorScore = cursorScore;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getRefreshType() {
        return refreshType;
    }

    public void setRefreshType(Integer refreshType) {
        this.refreshType = refreshType;
    }

    public Integer getNoteIndex() {
        return noteIndex;
    }

    public void setNoteIndex(Integer noteIndex) {
        this.noteIndex = noteIndex;
    }

    public String getUnreadBeginNoteId() {
        return unreadBeginNoteId;
    }

    public void setUnreadBeginNoteId(String unreadBeginNoteId) {
        this.unreadBeginNoteId = unreadBeginNoteId;
    }

    public String getUnreadEndNoteId() {
        return unreadEndNoteId;
    }

    public void setUnreadEndNoteId(String unreadEndNoteId) {
        this.unreadEndNoteId = unreadEndNoteId;
    }

    public Integer getUnreadNoteCount() {
        return unreadNoteCount;
    }

    public void setUnreadNoteCount(Integer unreadNoteCount) {
        this.unreadNoteCount = unreadNoteCount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Integer getNeedNum() {
        return needNum;
    }

    public void setNeedNum(Integer needNum) {
        this.needNum = needNum;
    }

    public Boolean isNeedFilterImage() {
        return needFilterImage;
    }

    public void setNeedFilterImage(Boolean needFilterImage) {
        this.needFilterImage = needFilterImage;
    }

    public List<String> getImageFormats() {
        return imageFormats;
    }

    public void setImageFormats(List<String> imageFormats) {
        this.imageFormats = imageFormats;
    }
}
