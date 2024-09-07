package com.example.entity.vo.response;

import lombok.Data;

import java.util.List;

@Data
public class HistoryVO {
    Integer code;
    String msg;
    private HistoryData data;

    @Data
    public static class HistoryData {
        private String today;
        private List<HistoryItem> list;
    }

    @Data
    public static class HistoryItem {
        private String title;
        private String url;
        private String year;
    }
}
