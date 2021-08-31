package com.gexingw.shop.vo.pms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class PmsProductCategoryAttributeVO {

    private Long categoryId;

    private String categoryName;

//    public List<Attribute> attributes = new ArrayList<>();

    public List<Attribute> baseAttributes = new ArrayList<>();

    public List<Attribute> saleAttributes = new ArrayList<>();

//    public List<Group> groups = new ArrayList<>();
//
//    @Data
//    public static class Group {
//        private Long groupId;
//
//        private String groupName;
//
//        public List<Attribute> baseAttributes = new ArrayList<>();
//
//        public List<Attribute> saleAttributes = new ArrayList<>();
//
//        public Group(Long groupId, String groupName) {
//            this.groupId = groupId;
//            this.groupName = groupName;
//        }
//    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Attribute {
        private Long attributeId;

        private String attributeName;

        private Integer attributeType;

        private Integer inputType;

        private String inputValue;
    }

}
