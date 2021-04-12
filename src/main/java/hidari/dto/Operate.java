package hidari.dto;

/**
 * @author 左手掐腰
 * @since 2019/10/14 21:10
 */
public enum Operate {
    OPERATE_DOWNLOAD(0, "[下载文件]"),
    OPERATE_REGRESULT(1, "[访问并使用用正则匹配]"),
    OPERATE_CSSRESULT(2, "[访问并使用Selector匹配]"),
    OPERATE_REG(3, "[使用正则匹配]"),
    OPERATE_PAGE(4, "[生成页码]"),
    OPERATE_VAR(5, "[变量操作]"),
    OPERATE_HEADER(6, "[Header]"),

    DOWNLOAD_FILENAME_URL(0, "[从url获取文件名]"),
    DOWNLOAD_FILENAME_RANDOM(1, "[随机文件名]"),
    DOWNLOAD_FILENAME_SELF(3, "[自定义文件名]"),
    DOWNLOAD_FILETYPE_URL(0, "[从url获取文件后缀]"),
    DOWNLOAD_FILETYPE_SELFDEFINED(1, "[自定义文件后缀如(.jpg)]"),

    REG_NOREPLACE(0, "[直接返回匹配结果]"),
    REG_REPLACE(1, "[替换匹配结果到]"),
    REG_VAR_ADD(2, "[将匹配结果作为变量]"),

    TAG_ATTR(0, "[获取标签指定属性值]"),
    TAG_CONTENT(1, "[innerHTML]"),
    TAG_ALL(2, "[outterHTML]"),

    //VAR_SET(1, "[替换变量]"),
    VAR_ADD(0, "[新增变量]");


    private int operateType;
    private String operateName;

    Operate(int operateType, String operateName){
        this.operateType = operateType;
        this.operateName = operateName;
    }

    @Override
    public String toString() {
        return operateType + "." +operateName;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }
}
