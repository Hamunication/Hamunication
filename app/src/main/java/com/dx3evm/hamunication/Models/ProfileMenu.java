package com.dx3evm.hamunication.Models;

public class ProfileMenu {
    private String menuTitle;
    private String menuIcon;

    public ProfileMenu(){}
    public ProfileMenu(String menuTitle, String menuIcon) {
        this.menuTitle = menuTitle;
        this.menuIcon = menuIcon;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }
}
