package hidari.util;

import hidari.dto.CatchStep;
import hidari.dto.Operate;

import java.util.*;

public final class VarUtil {
    public static String replaceVar(String src, Map<String, String> variables, boolean file) {
        Set<Map.Entry<String, String>> entrys = variables.entrySet();
        for (Map.Entry<String, String> entry : entrys) {
            src = src.replace("{" + entry.getKey() + "}", file ? RegUtil.getLegalName(entry.getValue()) : entry.getValue());
        }
        return src;
    }

    public static String replaceVar(String src, Map<String, String> variables) {
        return replaceVar(src, variables, false);
    }

    public static String replaceFileVar(String src, Map<String, String> variables) {
        return replaceVar(src, variables, true);
    }


    /**
     * 从url获取文件名，包含后缀
     *
     * @param url
     * @param fileNameFrom 文件名获取方式,
     *                     0:从url中获取，重名时加上随机字符命名
     *                     1:随机生成文件名，
     *                     2:自定义，
     * @param fileTypeFrom 文件后缀获取方式
     *                     0：从url中获取
     *                     1：自定义文件后缀
     * @return
     */
    public static String getNameFromUrl(String url, int fileNameFrom, int fileTypeFrom, String definedName, String definedType, Map<String, String> variables) {
        // 1
        if (1 == fileNameFrom && 1 == fileTypeFrom)
            return UUID.randomUUID().toString() + definedType;

        // 从地址中取出文件名称和类型
        String fullNameFromUrl;
        int a = url.lastIndexOf('/');
        int b = url.indexOf('?');
        int c = url.indexOf('#');
        if (-1 == b && -1 == c)
            fullNameFromUrl = url.substring(a + 1);
        else
            fullNameFromUrl = url.substring(a + 1, -1 == b ? c : b);

        // 2
        if (0 == fileNameFrom && 0 == fileTypeFrom)
            return fullNameFromUrl;

        int dotIndex = fullNameFromUrl.lastIndexOf('.');
        String fileName;
        String fileType;
        if (-1 == dotIndex) {
            fileName = fullNameFromUrl;
            fileType = "";
        } else {
            fileName = fullNameFromUrl.substring(0, dotIndex);
            fileType = fullNameFromUrl.substring(dotIndex);
        }

        String finalName;
        if (0 == fileNameFrom)
            finalName = fileName;
        else if (1 == fileNameFrom)
            finalName = UUID.randomUUID().toString();
        else
            finalName = VarUtil.replaceFileVar(definedName, variables);

        return finalName + (0 == fileTypeFrom ? fileType : definedType);
    }

    public static String getNameFromUrl(String url, CatchStep catchStep, Map<String, String> variables) {
        return getNameFromUrl(url, catchStep.getFileNameFrom(), catchStep.getFileTypeFrom(), catchStep.getFileName(), catchStep.getFileType(), variables);
    }

    public static CatchStep stepSetVariables(CatchStep next, Map<String, String> variables, String content, int index) {
        if (null == next)
            return null;
        String logpre = String.join("", Collections.nCopies(index, "  ")) + index++;
        if (next.getOperateType() == Operate.OPERATE_REGRESULT.getOperateType()
                && next.getRegReplace() == Operate.REG_VAR_ADD.getOperateType()) {
            List<String> temp = RegUtil.find(content, next.getRegSelector());
            next.getProgress().setMaximum(1);
            next.getProgress().setValue(1);
            if (temp.size() == 0) {
                Log.error(logpre + Operate.OPERATE_REGRESULT.getOperateName() + "设置变量失败,正则未匹配到字符串");
            } else {
                variables.put(next.getRegSource(), temp.get(0));
                Log.info(logpre + Operate.OPERATE_REGRESULT.getOperateName() + "设置变量成功," + next.getRegSource() + ":" + temp.get(0));
                return stepSetVariables(next.getNext(), variables, content, index);
            }
            return null;
        } else if (next.getOperateType() == Operate.OPERATE_VAR.getOperateType()
                && next.getVarOperate() == Operate.VAR_ADD.getOperateType()) {
            String value = VarUtil.replaceVar(next.getVarValue(), variables);
            variables.put(next.getVarName(), value);
            Log.info(logpre + Operate.OPERATE_VAR.getOperateName() + "新增变量成功," + next.getVarName() + ":" + value);
            next.getProgress().setMaximum(1);
            next.getProgress().setValue(1);
            return stepSetVariables(next.getNext(), variables, null, index);
        }
        return next;
    }
}
