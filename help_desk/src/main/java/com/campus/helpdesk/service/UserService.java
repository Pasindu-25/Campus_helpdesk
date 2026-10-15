package com.campus.helpdesk.service;

import com.campus.helpdesk.model.User;
import com.campus.helpdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private List<UserObserver> observers;

    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "userId"));
    }


    private void notifyObservers(User user, String actionType) {
        if (observers != null) {
            for (UserObserver observer : observers) {
                observer.updateUserStatusAndSendEmail(user, actionType);
            }
        }
    }


    public boolean addUser(User user, String creatorRole) {
        if (!"SUPER_ADMIN".equalsIgnoreCase(creatorRole) &&
                ("ADMIN".equalsIgnoreCase(user.getRole()) || "SUPER_ADMIN".equalsIgnoreCase(user.getRole()))) {
            return false;
        }

        user.setAccountStatus("ACTIVE");
        notifyObservers(user, "ACCOUNT_CREATED");
        userRepository.save(user);
        return true;
    }

    public boolean updateUserStatus(int userId, String newStatus, String modifierRole) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if ("SUPER_ADMIN".equalsIgnoreCase(user.getRole()) && !"SUPER_ADMIN".equalsIgnoreCase(modifierRole)) {
                System.out.println("Security Alert: Normal Admin cannot deactivate a Super Admin account!");
                return false;
            }

            user.setAccountStatus(newStatus);
            userRepository.save(user);

            if ("SUSPENDED".equalsIgnoreCase(newStatus)) {
                notifyObservers(user, "ACCOUNT_SUSPENDED");
            }
            return true;
        }
        return false;
    }

    public boolean deleteUser(int userId, String creatorRole) {
        if (!"SUPER_ADMIN".equalsIgnoreCase(creatorRole)) {
            return false;
        }

        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getPasswordHash().equals(oldPassword)) {
                user.setPasswordHash(newPassword);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public void updatePasswordAndRemoveFirstLoginFlag(int userId, String newPassword) {
        Optional<User> optUser = userRepository.findById(userId);

        if (optUser.isPresent()) {
            User user = optUser.get();

            user.setPasswordHash(newPassword);
            user.setFirstLogin(false);
            userRepository.save(user);
        }
    }

    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public String generateNextUniversityId(String role, Integer facultyId, String facultyCode) {
        if ("ADMIN".equalsIgnoreCase(role) || "SUPER_ADMIN".equalsIgnoreCase(role)) {
            return "";
        }

        User lastUser = userRepository.findTopByRoleAndFacultyIdOrderByUniversityIdDesc(role, facultyId);

        if (lastUser == null || lastUser.getUniversityId() == null || lastUser.getUniversityId().isEmpty()) {
            if ("STUDENT".equalsIgnoreCase(role)) {
                return facultyCode + "25100001";
            } else {
                return facultyCode + "1001";
            }
        } else {
            String lastId = lastUser.getUniversityId();
            String numberPart = lastId.replace(facultyCode, "");
            try {
                long nextNum = Long.parseLong(numberPart) + 1;
                return facultyCode + nextNum;
            } catch (NumberFormatException e) {
                return "STUDENT".equalsIgnoreCase(role) ? facultyCode + "25100001" : facultyCode + "1001";
            }
        }
    }
}