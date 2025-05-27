package org.webserv.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.webserv.models.AccountingDay;
import org.webserv.models.User;
import org.webserv.models.UserRole;
import org.webserv.repository.AccountingDayRepository;
import org.webserv.repository.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RangeExcelReportService {

    private final AccountingDayRepository accountingDayRepository;
    private final UserRepository userRepository;

    public RangeExcelReportService(AccountingDayRepository accountingDayRepository, UserRepository userRepository) {
        this.accountingDayRepository = accountingDayRepository;
        this.userRepository = userRepository;
    }

    public byte[] generateExcelForRange(LocalDate from, LocalDate to) throws IOException {
        List<AccountingDay> allDays = accountingDayRepository.findAll();
        List<User> users = userRepository.findAll();

        // Create a map of accountants only
        Map<String, User> accountantMap = users.stream()
                .filter(user -> user.getRole() == UserRole.ACCOUNTANT)
                .collect(Collectors.toMap(User::getEmail, user -> user));

        // Filter accounting days within range AND only for accountants
        List<AccountingDay> filtered = allDays.stream()
                .filter(d -> {
                    LocalDate date = LocalDate.parse(d.getDate());
                    return (date.isEqual(from) || date.isAfter(from)) &&
                            (date.isEqual(to) || date.isBefore(to)) &&
                            accountantMap.containsKey(d.getEmail());
                })
                .collect(Collectors.toList());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Accounting Days");

        // Header
        Row header = sheet.createRow(0);
        String[] cols = {"Email", "First Name", "Last Name", "Date", "Validated"};
        for (int i = 0; i < cols.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(cols[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        int rowNum = 1;
        for (AccountingDay d : filtered) {
            User user = accountantMap.get(d.getEmail());
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(user.getEmail());
            row.createCell(1).setCellValue(user.getFirstName());
            row.createCell(2).setCellValue(user.getLastName());
            row.createCell(3).setCellValue(d.getDate());
            row.createCell(4).setCellValue(d.isValidated() ? "Yes" : "No");
        }

        for (int i = 0; i < cols.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }
}
