package com.campus.helpdesk.service;

import com.campus.helpdesk.model.User;

public interface UserObserver {

    void updateUserStatusAndSendEmail(User user, String actionType);

}
