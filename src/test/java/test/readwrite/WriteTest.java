package test.readwrite;

import com.moyu.test.util.FileUtil;

/**
 * @author xiaomingzhang
 * @date 2023/5/12
 */
public class WriteTest {

    public static void main(String[] args) {
        String filePath = "D:\\mytest\\fileTest\\unfixLen.xmz";
        FileUtil.createFileIfNotExists(filePath);
    }


}
