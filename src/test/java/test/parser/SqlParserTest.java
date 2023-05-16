package test.parser;

import com.moyu.test.command.Command;
import com.moyu.test.command.SqlParser;
import com.moyu.test.session.ConnectSession;

import java.util.Arrays;

/**
 * @author xiaomingzhang
 * @date 2023/5/16
 */
public class SqlParserTest {


    public static void main(String[] args) {
        testExecSQL("create table xmz_aaa (id int, name varchar(10))");
        testExecSQL("show tables");
    }


    private static void testExecSQL(String sql) {
        ConnectSession connectSession = new ConnectSession("xmz", 0);
        SqlParser sqlParser = new SqlParser(connectSession);
        Command command = sqlParser.prepareCommand(sql);
        String[] exec = command.exec();
        Arrays.asList(exec).forEach(System.out::println);
    }

}
