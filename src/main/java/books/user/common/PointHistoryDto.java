package books.user.common;

import lombok.Data;

@Data
public class PointHistoryDto {
    private String createTime;
    private String historyDetail;
    private int pointChange;
    private int changeResult;
    private char using;

    public PointHistoryDto(Builder builder) {
        this.createTime = builder.createTime;
        this.historyDetail = builder.historyDetail;
        this.pointChange = builder.pointChange;
        this.changeResult = builder.changeResult;
        this.using = builder.using;
    }

    public static class Builder {
        private String createTime = "";
        private String historyDetail = "";
        private int pointChange = 0;
        private int changeResult = 0;
        private char using = '0';

        public Builder createTime(String createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder historyDetail(String historyDetail) {
            this.historyDetail = historyDetail;
            return this;
        }

        public Builder pointChange(int pointChange) {
            this.pointChange = pointChange;
            return this;
        }

        public Builder changeResult(int changeResult) {
            this.changeResult = changeResult;
            return this;
        }

        public Builder using(boolean using) {
            if(using) this.using = '-';
            else this.using = '+';
            return this;
        }

        public PointHistoryDto build() {
            return new PointHistoryDto(this);
        }
    }
}
