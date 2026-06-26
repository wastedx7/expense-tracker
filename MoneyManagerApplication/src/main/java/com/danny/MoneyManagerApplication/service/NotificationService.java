package com.danny.MoneyManagerApplication.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.danny.MoneyManagerApplication.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${money.manger.fronted.url}")
    private String frontUrl;

    // @Scheduled(cron = "0 0 22 * * *",zone = "IST")
    // public void sendDailyIncomeExpenseReminder(){
    //     log.info("Job started : senddailyIncomeExpenseReminder()");
    //     List<ProfileEntity> profiles = profileRepository.findAll();
    //     for(ProfileEntity profile : profiles){
    //         String body = "Hi" + profile.getFullName() + ",<br><br>"
    //                         +"This is a frendaly reminder to addd your income and expense for today in Money manger"
    //                         +"<a href="+frontUrl+"style='display:inline-block:padding:10px 20px: background-color:#4CAF50;color:#fff;text-decoration:none;"
    //                         +"<br><br> Best regards,<br>Money Manager Team";

    //             emailService.sendEmail(profile.getEmail(), "Daily Reminder ; add you income and expense",body);
    //     }
    // }

    
    //@Scheduled(cron = "0 * * * * *",zone = "Asia/Kolkata")
    //  @Scheduled(cron = "0 0 23 * * *",zone = "Asia/Kolkata")
    // public void sendDailyExpenseSummary(){
    //     log.info("Job started : sendDailyExpenseSummery()");
    //     List<ProfileEntity> profiles = profileRepository.findAll();
    //     for(ProfileEntity profile : profiles){
    //         List<ExpenseDTO> todaysExpenses = expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now());
    //         if(!todaysExpenses.isEmpty()){
    //             StringBuilder table = new StringBuilder();
    //             table.append("<table style='border-collapse:collapse;width:100%;'>");
    //             table.append("<tr style='background-color:#f2f2f2;'>"
    //                             + "<th style='border:1px solid #ddd;padding:8px;'>S.No</th>"
    //                             + "<th style='border:1px solid #ddd;padding:8px;'>Name</th>"
    //                             + "<th style='border:1px solid #ddd;padding:8px;'>Amount</th>"
    //                             + "</tr>");
    //             int i = 1; 
    //             for (ExpenseDTO expenseDTO : todaysExpenses) {
    //                 table.append("<tr>");
    //                 table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(i++).append("</td>");
    //                 table.append("<td style='border:1px solid #ddd;padding:8px;'>").append(expenseDTO.getName()).append("</td>");
    //                 table.append("<td style='border:1px solid #ddd;padding:8px;'>₹ ").append(expenseDTO.getAmount()).append("</td>");
    //                 table.append("</tr>");
    //             }
    //             table.append("</table>");
    //             String body = "Hi"+profile.getFullName()+",<br/><br/> Here is a summary of your expense for the today:<br/><br/>"+table+"<br/><br/> Best regards, <br/> Money manager Team";
    //             emailService.sendEmail(profile.getEmail(), "Your daily expense summery", body); 
    //         }
    //     }
    //     log.info("Job completed : senddailyExpenseReminder()");
    
    // }
}
