package com.campus.helpdesk.service;

import com.campus.helpdesk.model.User;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService implements UserObserver {

    @Override
    public void updateUserStatusAndSendEmail(User user, String actionType) {
        if ("ACCOUNT_CREATED".equals(actionType)) {
            System.out.println("[EMAIL NOTIFICATION] Sending Welcome Email to: " + user.getEmail() +
                    " | Hello " + user.getFullName() + ", your account is ready.");
        }
        else if ("ACCOUNT_SUSPENDED".equals(actionType)) {
            System.out.println("[EMAIL NOTIFICATION] Sending Suspension Notice to: " + user.getEmail() +
                    " | Your account has been suspended.");
        }
    }
}
