package com.campus.helpdesk.controller;

import com.campus.helpdesk.model.Faculty;
import com.campus.helpdesk.model.User;
import com.campus.helpdesk.service.FacultyService;
import com.campus.helpdesk.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private com.campus.helpdesk.util.EmailService emailService;

    private User checkAdminAuth(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user != null && ("ADMIN".equalsIgnoreCase(user.getRole()) || "SUPER_ADMIN".equalsIgnoreCase(user.getRole()))) {
            return user;
        }
        return null;
    }

    @GetMapping("/admin/users")
    public String listUsers(HttpSession session, Model model) {
        if (checkAdminAuth(session) == null) return "redirect:/login";

        List<User> userList = userService.getAllUsers();
        List<Faculty> facultyList = facultyService.getAllFaculties();

        model.addAttribute("userList", userList);
        model.addAttribute("facultyList", facultyList);

        return "manage_users";
    }

    @GetMapping("/admin/users/toggleStatus")
    public String toggleStatus(@RequestParam("id") int userId,
                               @RequestParam("status") String currentStatus,
                               HttpSession session) {
        User loggedUser = checkAdminAuth(session);
        if (loggedUser == null) return "redirect:/login";

        String newStatus = "ACTIVE".equalsIgnoreCase(currentStatus) ? "INACTIVE" : "ACTIVE";
        userService.updateUserStatus(userId, newStatus, loggedUser.getRole());

        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/delete")
    public String deleteUser(@RequestParam("id") int userId, HttpSession session) {
        User loggedUser = checkAdminAuth(session);
        if (loggedUser == null) return "redirect:/login";

        try {
            userService.deleteUser(userId, loggedUser.getRole());
            return "redirect:/admin/users";

        } catch (DataIntegrityViolationException e) {
            return "redirect:/admin/users?error=deleteFailed";
        } catch (Exception e) {
            return "redirect:/admin/users?error=deleteFailed";
        }
    }

    @PostMapping("/admin/users/add")
    public String addUser(@RequestParam("fullName") String fullName,
                          @RequestParam("email") String email,
                          @RequestParam("password") String password,
                          @RequestParam("role") String role,
                          @RequestParam(value = "universityId", required = false) String universityId,
                          @RequestParam(value = "facultyId", required = false) Integer facultyId,
                          HttpSession session) {

        User loggedUser = checkAdminAuth(session);
        if (loggedUser == null) return "redirect:/login";

        if ("ADMIN".equalsIgnoreCase(role) || "SUPER_ADMIN".equalsIgnoreCase(role)) {
            universityId = "ADM-" + (10000 + new java.util.Random().nextInt(90000));
            facultyId = null;
        } else if (facultyId == null) {
            facultyId = null;
        }

        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPasswordHash(password);
        newUser.setRole(role);
        newUser.setUniversityId(universityId);
        newUser.setFacultyId(facultyId);

        try {
            userService.addUser(newUser, loggedUser.getRole());

            if (!"SUPER_ADMIN".equalsIgnoreCase(role)) {
                emailService.sendActivationEmail(email, fullName, password);
            }

            return "redirect:/admin/users";

        } catch (DataIntegrityViolationException e) {
            return "redirect:/admin/users?error=duplicateUser";
        }
    }

    @PostMapping("/user/changePassword")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 HttpSession session) {

        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) return "redirect:/login";

        boolean isUpdated = userService.changePassword(loggedUser.getUserId(), oldPassword, newPassword);

        if (isUpdated) {
            session.invalidate();
            return "redirect:/login?msg=PasswordChanged";
        } else {
            String redirectUrl = "redirect:/admin/dashboard";
            if ("STUDENT".equalsIgnoreCase(loggedUser.getRole())) {
                redirectUrl = "redirect:/student/ticket/dashboard";
            } else if ("STAFF".equalsIgnoreCase(loggedUser.getRole())) {
                redirectUrl = "redirect:/staff/dashboard";
            }
            return redirectUrl + "?error=InvalidOldPassword";
        }
    }

    @GetMapping("/user/force-password-change")
    public String showForcePasswordChangePage(HttpSession session) {
        if (session.getAttribute("loggedUser") == null) return "redirect:/login";
        return "force_password_change";
    }

    @PostMapping("/user/update-first-password")
    public String updateFirstPassword(@RequestParam("newPassword") String newPassword, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) return "redirect:/login";

        userService.updatePasswordAndRemoveFirstLoginFlag(loggedUser.getUserId(), newPassword);

        loggedUser.setFirstLogin(false);
        session.setAttribute("loggedUser", loggedUser);

        if ("STAFF".equals(loggedUser.getRole())) {
            return "redirect:/staff/dashboard";
        } else if ("STUDENT".equals(loggedUser.getRole())) {
            return "redirect:/student/ticket/dashboard";
        } else {
            return "redirect:/admin/dashboard";
        }
    }


    @GetMapping("/admin/users/checkEmail")
    @ResponseBody
    public String checkEmail(@RequestParam("email") String email) {
        boolean exists = userService.isEmailExists(email);
        return exists ? "true" : "false";
    }

    @GetMapping("/admin/users/generateId")
    @ResponseBody
    public String generateId(@RequestParam("role") String role,
                             @RequestParam("facultyId") Integer facultyId,
                             @RequestParam("facultyCode") String facultyCode) {
        return userService.generateNextUniversityId(role, facultyId, facultyCode);
    }
}