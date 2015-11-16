package ru.innopolis.dmd.project.innodb;

import ru.innopolis.dmd.project.innodb.db.DBConstants;
import ru.innopolis.dmd.project.innodb.scheme.Column;
import ru.innopolis.dmd.project.innodb.scheme.Table;
import ru.innopolis.dmd.project.innodb.scheme.type.Types;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Timur Kasatkin
 * @date 18.10.15.
 * @email aronwest001@gmail.com
 */
public class PrototypeTableCreator {

    public static void main(String[] args) {
        Column empColumnPk = new Column("id", Types.INT);
        Column studentColumnPk = new Column("id", Types.INT);
//        Student (id, name, email, address)
//        Employee(id, name, designation, Address)
        List<Table> tables = Arrays.asList(
                new Table("Student",
                        Arrays.asList(studentColumnPk),
                        Arrays.asList(studentColumnPk,
                                new Column("name", Types.VARCHAR),
                                new Column("email", Types.VARCHAR),
                                new Column("address", Types.VARCHAR))),
                new Table("Employee",
                        Arrays.asList(empColumnPk),
                        Arrays.asList(empColumnPk,
                                new Column("name", Types.VARCHAR),
                                new Column("designation", Types.VARCHAR),
                                new Column("Address", Types.VARCHAR))));
        tables.forEach(table -> {
            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(DBConstants.TABLES_FILES_DIRECTORY + "/" + table.getName() + ".txt"))) {
                String scheme = table.getColumns().stream()
                        .map(c -> (table.getPrimaryKeys().contains(c) ? "pk" : "") +
                                "[" + c.getName() + "$" + c.getType().getName() + "]")
                        .collect(Collectors.joining("$"));
                writer.write(scheme);
                writer.newLine();
                for (int i = 0; i < DBConstants.PAGES_COUNT; i++) {
                    writer.write(0 + multiplied(' ', 4 + 2 + DBConstants.PAGE_LENGTH - 1));
                    writer.newLine();
                }
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static String multiplied(char c, int count) {
        StringBuilder builder = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            builder.append(c);
        }
        return builder.toString();
    }

}
