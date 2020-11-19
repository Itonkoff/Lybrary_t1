package com.kofu.brighton.lybrary;

public interface MainActivityCallBacks {
    void setToken(String token);
    String getToken();
    void hideFab();
    void showFab();
    void navigateToNewBook();
    void bookSelected(int id);
}
