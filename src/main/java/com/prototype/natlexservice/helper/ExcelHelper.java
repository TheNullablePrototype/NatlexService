package com.prototype.natlexservice.helper;

import com.prototype.natlexservice.model.GeoClass;
import com.prototype.natlexservice.model.Section;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ExcelHelper {

    public static Path write(List<Section> sections) throws Exception {

        Path directory = Paths.get("exported-resources");
        Files.createDirectories(directory);
        Path path = directory.resolve(String.format("%d.xls", System.currentTimeMillis()));

        try (Workbook book = new HSSFWorkbook()) {

            final var helper = book.getCreationHelper();
            final var sheet = book.createSheet(WorkbookUtil.createSafeSheetName("sections"));

            var rows = 0;
            var cell = 0;

            final var headerRow = sheet.createRow(rows++);

            createCell(helper, headerRow, cell, "Section name");

            final var columns = sections.stream().mapToInt(section -> section.getClasses().size())
                    .max().orElseThrow(IllegalStateException::new);

            cell = 1;

            for (var index = 1; index <= columns; index++) {
                createCell(helper, headerRow, cell++, String.format("Class %d name", index));
                createCell(helper, headerRow, cell++, String.format("Class %d code", index));
            }

            for (Section section : sections) {
                cell = 0;
                var row = sheet.createRow(rows++);
                createCell(helper, row, cell++, section.getName());
                for (GeoClass clazz : section.getClasses()) {
                    createCell(helper, row, cell++, clazz.getName());
                    createCell(helper, row, cell++, clazz.getCode());
                }
            }

            for (int i = 0; i < 1 + (columns * 2); i++) {
                sheet.autoSizeColumn(i);
            }

            book.write(Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
        }

        return path;
    }

    public static void createCell(CreationHelper helper, Row row, int index, String value) {
        row.createCell(index, CellType.STRING).setCellValue(helper.createRichTextString(value));
    }

    public static List<Section> read(Path path) throws Exception {
        List<Section> sections = new ArrayList<>();

        try (HSSFWorkbook book = new HSSFWorkbook(Files.newInputStream(path, StandardOpenOption.DELETE_ON_CLOSE))) {

            final var sheet = book.getSheetAt(0);

            var skipHeaders = false;

            for (Row row : sheet) {

                if (!skipHeaders) {
                    skipHeaders = true;
                    continue;
                }


                final short start = (short) (row.getFirstCellNum() + 1);
                final short end = row.getLastCellNum();

                final var cell = row.getCell(row.getFirstCellNum());
                if (cell == null)
                    throw new IllegalStateException();

                final var section = new Section(cell.getRichStringCellValue().getString());

                for (short index = start; index < end; index += 2) {
                    final var nameCell = row.getCell(index);
                    final var codeCell = row.getCell(index + 1);
                    if (nameCell == null || codeCell == null)
                        continue;
                    section.addGeoClass(new GeoClass(
                            nameCell.getRichStringCellValue().getString(),
                            codeCell.getRichStringCellValue().getString()
                    ));
                }

                sections.add(section);

            }
        }

        return sections;
    }

}
