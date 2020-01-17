package org.mmo.engine.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 */
public class FileUtil {

    private final static Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 循环扫描,获得文件
     *
     * @param files
     * @param file
     * @param includes
     */
    public static void getRfFiles(List<File> files, File file, String[] includes) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (File f : listFiles) {
                getRfFiles(files, f, includes);
            }
        } else {
            for (String include : includes) {
                if (file.getName().endsWith(include)) {
                    files.add(file);
                    break;
                }
            }

        }
    }

    /**
     * 创建目录
     *
     * @param dir
     */
    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }

    /**
     * 创建文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static boolean createFile(File file) throws IOException {
        if (!file.exists()) {
            if (file.getParentFile() != null) {
                makeDir(file.getParentFile());
            }
        }
        return file.createNewFile();
    }

    public static void getFiles(String file, List<File> sourceFileList, final String endName, Predicate<String> condition) {
        File sourceFile = new File(file);
        getFiles(sourceFile, sourceFileList, endName, condition);
    }

    /**
     * 查找该目录下的所有的 endName 文件
     *
     * @param sourceFile ,单文件或者目录
     * @param sourceFileList 返回目录所包含的所有文件包括子目录
     * @param endName
     * @param condition
     */
    public static void getFiles(File sourceFile, List<File> sourceFileList, final String endName, Predicate<String> condition) {
        if (sourceFile.exists() && sourceFileList != null) {
            if (sourceFile.isDirectory()) {
                File[] childrenFiles = sourceFile.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        if (pathname.isDirectory()) {
                            return true;
                        } else {
                            return pathname.getAbsolutePath().endsWith(endName);
                        }
                    }
                });
                for (File childFile : childrenFiles) {
                    getFiles(childFile, sourceFileList, endName, condition);
                }
            } else if (condition == null || condition.test(sourceFile.getAbsolutePath())) {
                sourceFileList.add(sourceFile);
            }
        }
    }
    //</editor-fold>

//    /**
//     * 获取xml的实例
//     *
//     * @param <T>
//     * @param path
//     * @param fileName
//     * @param configClass
//     * @return
//     */
//    public static <T extends Object> T getConfigXML(String path, String fileName, Class<T> configClass) {
//        T ob = null;
//        fileName = path + File.separatorChar + fileName;
//        if (!new File(fileName).exists()) {
//            return ob;
//        }
//        Serializer serializer = new Persister();
//        try {
//            ob = serializer.read(configClass, new File(fileName));
//        } catch (Exception ex) {
//            log.error("文件" + fileName + "配置有误", ex);
//        }
//        return ob;
//    }

    public static String readTxtFile(String path, String fileName, String encoding) {
        return readTxtFile(path + File.separatorChar + fileName, encoding);
    }

    public static String readTxtFile(String filePath) {
    	return readTxtFile(filePath, "UTF-8");
    }
    
    public static String readTxtFile(String filePath, String encoding) {
        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                StringBuilder sb;
                try (
                        InputStreamReader read = new InputStreamReader(
                                new FileInputStream(file), encoding) //考虑到编码格式
                        ) {
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    sb = new StringBuilder();
                    while ((lineTxt = bufferedReader.readLine()) != null) {
                        sb.append(lineTxt).append(Symbol.ENTER);
                    }
                }
                return sb.toString();
            } else {
                log.warn("文件{}配置有误,找不到指定的文件", file);
            }
        } catch (Exception e) {
            log.error("读取文件内容出错", e);
        }
        return null;
    }

    public static boolean writeTxtFile(String content, File fileName, String encoding) throws Exception {
        FileOutputStream o = null;
        try {
            o = new FileOutputStream(fileName);
            o.write(content.getBytes(encoding));
            o.close();
            return true;
        } catch (Exception e) {
            log.error("写入文件内容出错", e);
        }
        return false;
    }

    public static String getMainPath() {
        String path = null;
        File file = new File(System.getProperty("user.dir"));
        if ("target".equals(file.getName())) {
            path = file.getPath() + File.separatorChar + "config";
        } else {
            path = file.getPath() + File.separatorChar + "target" + File.separatorChar + "config";
        }
        return path;
    }
    
    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            log.info("删除文件失败:" + fileName + "不存在！");
            return false;
        } else if (file.isFile()) {
            return deleteFile(fileName);
        } else {
            return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
//                log.info("删除文件" + fileName + "成功！");
                return true;
            } else {
                log.info("删除文件" + fileName + "失败！");
                return false;
            }
        } else {
            log.info("删除文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            log.info("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            log.info("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
//            log.info("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }
}
