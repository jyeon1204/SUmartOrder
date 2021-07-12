package com.example.audtj.sumarto;

public class Order {
    int menuID;  //메뉴 번호
    String sungshinUniversity; //수정,운정 캠퍼스
    String menuCategory; //메뉴의 카테고리
    String menuName; //메뉴 이름
    int menuPrice; //메뉴의 가격

    public int getMenuID() {
        return menuID;
    }

    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    public String getSungshinUniversity() {
        return sungshinUniversity;
    }

    public void setSungshinUniversity(String sungshinUniversity) {
        this.sungshinUniversity = sungshinUniversity;
    }

    public String getMenuCategory() {
        return menuCategory;
    }

    public void setMenuCategory(String menuCategory) {
        this.menuCategory = menuCategory;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }

    public Order(int menuID, String sungshinUniversity, String menuCategory, String menuName, int menuPrice) {
        this.menuID = menuID;
        this.sungshinUniversity = sungshinUniversity;
        this.menuCategory = menuCategory;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }


    public Order(int menuID, String menuCategory, String menuName, int menuPrice) {
        this.menuID = menuID;
        this.menuCategory = menuCategory;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

}
