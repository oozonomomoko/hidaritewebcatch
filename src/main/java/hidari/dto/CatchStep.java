package hidari.dto;

import javax.swing.*;

/**
 * @author 左手掐腰
 * @since 2019/10/11 10:20
 */
public class CatchStep {

    private JProgressBar progress;

    private CatchStep next;

    public CatchStep getNext() {
        return next;
    }

    public void setNext(CatchStep next) {
        this.next = next;
    }

    /*
     * 资源操作方式
     * 0. 访问资源，直接下载resource
     * 1. 访问资源，获取结果 正则匹配
     * 2. 访问资源，获取结果 css选择器匹配
     * 3. 处理资源地址，不访问
     */
    private int operateType;


    // operateType=0. 直接下载配置---------------------------start
    /*
     * 文件存放目录
      */
    private String downloadDir;

    /*
     * 文件名获取方式,
     * 0:从url中获取，重名时加上随机字符命名
     * 1:随机生成文件名，
     */
    private int fileNameFrom;

    private String fileName;

    /*
     * 文件后缀获取方式
     * 0：从url中获取
     * 1：自定义文件后缀
     */
    private int fileTypeFrom;

    /*
     * 文件后缀，如 .jpg .mp4，可为空
     */
    private String fileType;

    // operateType=0. 直接下载配置---------------------------end



    // operateType=1. 正则匹配结果---------------------------start
    /*
     * 正则表达式
     */
    private String regSelector;

    /**
     * 是否替换到regSource中
     */
    private int regReplace;

    /*
     * 使用正则匹配到的字符替换到此字符串中
     */
    private String regSource;
    // operateType=1. 正则匹配结果---------------------------end


    // operateType=2. css选择器结果---------------------------start
    /*
     * css选择器
     */
    private String cssSelector;

    /*
     * 0 获取标签属性
     * 1 获取innerHTML
     * 2 不处理直接返回
     */
    private int attrType;

    /*
     * attrType=0
     */
    private String attrName;
    // operateType=2. css选择器结果---------------------------end

    private String pageMin;

    private String pageMax;

    private int varOperate;

    private String varName;

    private String varValue;

    public int getVarOperate() {
        return varOperate;
    }

    public void setVarOperate(int varOperate) {
        this.varOperate = varOperate;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getVarValue() {
        return varValue;
    }

    public void setVarValue(String varValue) {
        this.varValue = varValue;
    }

    public String getPageMin() {
        return pageMin;
    }

    public void setPageMin(String pageMin) {
        this.pageMin = pageMin;
    }

    public String getPageMax() {
        return pageMax;
    }

    public void setPageMax(String pageMax) {
        this.pageMax = pageMax;
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public String getDownloadDir() {
        return downloadDir;
    }

    public void setDownloadDir(String downloadDir) {
        this.downloadDir = downloadDir;
    }

    public String getRegSelector() {
        return regSelector;
    }

    public void setRegSelector(String regSelector) {
        this.regSelector = regSelector;
    }

    public String getCssSelector() {
        return cssSelector;
    }

    public int getRegReplace() {
        return regReplace;
    }

    public void setRegReplace(int regReplace) {
        this.regReplace = regReplace;
    }

    public void setCssSelector(String cssSelector) {
        this.cssSelector = cssSelector;
    }

    public int getFileNameFrom() {
        return fileNameFrom;
    }

    public void setFileNameFrom(int fileNameFrom) {
        this.fileNameFrom = fileNameFrom;
    }

    public int getFileTypeFrom() {
        return fileTypeFrom;
    }

    public void setFileTypeFrom(int fileTypeFrom) {
        this.fileTypeFrom = fileTypeFrom;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getRegSource() {
        return regSource;
    }

    public void setRegSource(String regSource) {
        this.regSource = regSource;
    }

    public int getAttrType() {
        return attrType;
    }

    public void setAttrType(int attrType) {
        this.attrType = attrType;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public JProgressBar getProgress() {
        return progress;
    }

    public void setProgress(JProgressBar progress) {
        this.progress = progress;
    }
}
