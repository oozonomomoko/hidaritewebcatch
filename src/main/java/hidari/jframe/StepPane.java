/*
 * Created by JFormDesigner on Thu Oct 17 10:49:48 CST 2019
 */

package hidari.jframe;

import hidari.dto.CatchStep;
import hidari.dto.Operate;
import org.jsoup.internal.StringUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author oozonomomoko
 */
public class StepPane extends JPanel {
    public StepPane() {
        initComponents();
    }

    private void addStep(MouseEvent e) {
        this.getParent().add(new StepPane(), this.getParent().getComponentZOrder(this)+1);
        this.getParent().validate();
    }

    private void delMouseClicked(MouseEvent e) {
        this.setVisible(false);
        this.getParent().validate();
    }

    private void thisMouseEntered(MouseEvent e) {
        this.setBackground(Color.white);
    }

    private void dirBtnMouseClicked(MouseEvent e) {
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(this.getParent())) {
            dir.setText(fileChooser.getSelectedFile().getAbsolutePath());
            dir.setToolTipText(dir.getText());
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        del = new JButton();
        add = new JButton();
        panel1 = new JPanel();
        operateType = new JComboBox();
        downloadPane = new JPanel();
        fileNameFrom = new JComboBox();
        fileTypeFrom = new JComboBox();
        fileType = new JTextField();
        dir = new JTextField();
        dirBtn = new JButton();
        fileName = new JTextField();
        regPane = new JPanel();
        regLabel = new JLabel();
        regSelector = new JTextField();
        regReplace = new JComboBox();
        label6 = new JLabel();
        regSource = new JTextField();
        cssPane = new JPanel();
        cssLabel = new JLabel();
        cssSelector = new JTextField();
        attrType = new JComboBox();
        attrName = new JTextField();
        attrNameLabel = new JLabel();
        pagePane = new JPanel();
        pageLable = new JLabel();
        label7 = new JLabel();
        pageMax = new JTextField();
        pageMin = new JTextField();
        varPane = new JPanel();
        label1 = new JLabel();
        varName = new JTextField();
        label8 = new JLabel();
        varValue = new JTextField();
        varOperate = new JComboBox();
        progress = new JProgressBar();

        //======== this ========
        setBackground(new Color(152, 234, 187));
        setBorder(new LineBorder(new Color(224, 224, 224)));
        setForeground(new Color(204, 204, 204));
        setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                thisMouseEntered(e);
            }
        });
        setLayout(new FlowLayout());

        //---- del ----
        del.setText("-");
        del.setBackground(new Color(239, 190, 190));
        del.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
        del.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                delMouseClicked(e);
            }
        });
        add(del);

        //---- add ----
        add.setText("+");
        add.setBackground(new Color(176, 232, 187));
        add.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
        add.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addStep(e);
            }
        });
        add(add);

        //======== panel1 ========
        {
            panel1.setLayout(null);

            //---- operateType ----
            operateType.setBackground(Color.white);
            operateType.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            operateType.setBorder(null);
            panel1.add(operateType);
            operateType.setBounds(0, 0, 185, 24);

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel1.getComponentCount(); i++) {
                    Rectangle bounds = panel1.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel1.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel1.setMinimumSize(preferredSize);
                panel1.setPreferredSize(preferredSize);
            }
        }
        add(panel1);

        //======== downloadPane ========
        {
            downloadPane.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            downloadPane.setLayout(null);

            //---- fileNameFrom ----
            fileNameFrom.setBackground(Color.white);
            fileNameFrom.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            fileNameFrom.setBorder(null);
            downloadPane.add(fileNameFrom);
            fileNameFrom.setBounds(8, 0, 130, 24);

            //---- fileTypeFrom ----
            fileTypeFrom.setBackground(Color.white);
            fileTypeFrom.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            fileTypeFrom.setBorder(null);
            downloadPane.add(fileTypeFrom);
            fileTypeFrom.setBounds(275, 0, 145, 24);

            //---- fileType ----
            fileType.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            downloadPane.add(fileType);
            fileType.setBounds(430, 0, 75, fileType.getPreferredSize().height);

            //---- dir ----
            dir.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            downloadPane.add(dir);
            dir.setBounds(600, 2, 126, 20);

            //---- dirBtn ----
            dirBtn.setText("\u9009\u62e9\u76ee\u5f55");
            dirBtn.setBackground(new Color(204, 204, 204));
            dirBtn.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            dirBtn.setHorizontalAlignment(SwingConstants.TRAILING);
            dirBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    dirBtnMouseClicked(e);
                }
            });
            downloadPane.add(dirBtn);
            dirBtn.setBounds(515, 0, 80, 24);

            //---- fileName ----
            fileName.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            downloadPane.add(fileName);
            fileName.setBounds(150, 0, 95, 23);

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < downloadPane.getComponentCount(); i++) {
                    Rectangle bounds = downloadPane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = downloadPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                downloadPane.setMinimumSize(preferredSize);
                downloadPane.setPreferredSize(preferredSize);
            }
        }
        add(downloadPane);

        //======== regPane ========
        {
            regPane.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            regPane.setLayout(null);

            //---- regLabel ----
            regLabel.setText("\u6b63\u5219\u8868\u8fbe\u5f0f");
            regLabel.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            regPane.add(regLabel);
            regLabel.setBounds(10, 5, 60, regLabel.getPreferredSize().height);

            //---- regSelector ----
            regSelector.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            regPane.add(regSelector);
            regSelector.setBounds(80, 0, 165, regSelector.getPreferredSize().height);

            //---- regReplace ----
            regReplace.setBackground(Color.white);
            regReplace.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            regReplace.setBorder(null);
            regPane.add(regReplace);
            regReplace.setBounds(275, 0, 145, 24);

            //---- label6 ----
            label6.setText("->");
            label6.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            regPane.add(label6);
            label6.setBounds(446, 4, 20, label6.getPreferredSize().height);

            //---- regSource ----
            regSource.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            regPane.add(regSource);
            regSource.setBounds(529, 0, 200, regSource.getPreferredSize().height);

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < regPane.getComponentCount(); i++) {
                    Rectangle bounds = regPane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = regPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                regPane.setMinimumSize(preferredSize);
                regPane.setPreferredSize(preferredSize);
            }
        }
        add(regPane);

        //======== cssPane ========
        {
            cssPane.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            cssPane.setLayout(null);

            //---- cssLabel ----
            cssLabel.setText("css\u9009\u62e9\u5668");
            cssLabel.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            cssPane.add(cssLabel);
            cssLabel.setBounds(10, 5, 60, cssLabel.getPreferredSize().height);

            //---- cssSelector ----
            cssSelector.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            cssPane.add(cssSelector);
            cssSelector.setBounds(80, 1, 165, cssSelector.getPreferredSize().height);

            //---- attrType ----
            attrType.setBackground(Color.white);
            attrType.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            attrType.setBorder(null);
            cssPane.add(attrType);
            attrType.setBounds(275, 0, 145, 24);

            //---- attrName ----
            attrName.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            cssPane.add(attrName);
            attrName.setBounds(529, 0, 200, attrName.getPreferredSize().height);

            //---- attrNameLabel ----
            attrNameLabel.setText("\u6807\u7b7e\u5c5e\u6027\u540d");
            attrNameLabel.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            cssPane.add(attrNameLabel);
            attrNameLabel.setBounds(new Rectangle(new Point(441, 4), attrNameLabel.getPreferredSize()));

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < cssPane.getComponentCount(); i++) {
                    Rectangle bounds = cssPane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = cssPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                cssPane.setMinimumSize(preferredSize);
                cssPane.setPreferredSize(preferredSize);
            }
        }
        add(cssPane);

        //======== pagePane ========
        {
            pagePane.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            pagePane.setLayout(null);

            //---- pageLable ----
            pageLable.setText("\u751f\u6210\u9875\u7801(\u66ff\u6362{page})\uff0c\u6570\u5b57\u4ece");
            pageLable.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            pagePane.add(pageLable);
            pageLable.setBounds(10, 1, 200, 22);

            //---- label7 ----
            label7.setText("->");
            label7.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            pagePane.add(label7);
            label7.setBounds(446, 4, 20, label7.getPreferredSize().height);

            //---- pageMax ----
            pageMax.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            pageMax.setText("1");
            pagePane.add(pageMax);
            pageMax.setBounds(529, 0, 200, pageMax.getPreferredSize().height);

            //---- pageMin ----
            pageMin.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", Font.PLAIN, 12));
            pageMin.setText("1");
            pagePane.add(pageMin);
            pageMin.setBounds(240, 0, 180, 24);

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < pagePane.getComponentCount(); i++) {
                    Rectangle bounds = pagePane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = pagePane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                pagePane.setMinimumSize(preferredSize);
                pagePane.setPreferredSize(preferredSize);
            }
        }
        add(pagePane);

        //======== varPane ========
        {
            varPane.setLayout(null);

            //---- label1 ----
            label1.setText("\u53d8\u91cf\u540d");
            varPane.add(label1);
            label1.setBounds(222, 2, 40, 20);
            varPane.add(varName);
            varName.setBounds(275, 0, 145, 23);

            //---- label8 ----
            label8.setText("\u53d8\u91cf\u503c");
            varPane.add(label8);
            label8.setBounds(445, 0, 40, 24);
            varPane.add(varValue);
            varValue.setBounds(529, 0, 200, 23);
            varPane.add(varOperate);
            varOperate.setBounds(9, 1, 190, varOperate.getPreferredSize().height);

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < varPane.getComponentCount(); i++) {
                    Rectangle bounds = varPane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = varPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                varPane.setMinimumSize(preferredSize);
                varPane.setPreferredSize(preferredSize);
            }
        }
        add(varPane);

        //---- progress ----
        progress.setStringPainted(true);
        add(progress);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        other();
    }

    private void other() {
        fileChooser = new JFileChooser();

        fileNameFrom.addItem(Operate.DOWNLOAD_FILENAME_URL);
        fileNameFrom.addItem(Operate.DOWNLOAD_FILENAME_RANDOM);
        fileNameFrom.addItem(Operate.DOWNLOAD_FILENAME_SELF);
        fileTypeFrom.addItem(Operate.DOWNLOAD_FILETYPE_URL);
        fileTypeFrom.addItem(Operate.DOWNLOAD_FILETYPE_SELFDEFINED);

        regReplace.addItem(Operate.REG_NOREPLACE);
        regReplace.addItem(Operate.REG_REPLACE);
        regReplace.addItem(Operate.REG_VAR_ADD);

        attrType.addItem(Operate.TAG_ATTR);
        attrType.addItem(Operate.TAG_CONTENT);
        attrType.addItem(Operate.TAG_ALL);

        operateType.addItem(Operate.OPERATE_DOWNLOAD);
        operateType.addItem(Operate.OPERATE_REGRESULT);
        operateType.addItem(Operate.OPERATE_CSSRESULT);
        operateType.addItem(Operate.OPERATE_REG);
        operateType.addItem(Operate.OPERATE_PAGE);
        operateType.addItem(Operate.OPERATE_VAR);
        operateType.addItem(Operate.OPERATE_HEADER);
        operateType.addItem(Operate.OPERATE_INIT);
        operateType.addItemListener(e -> {
            int opType = ((Operate)e.getItem()).getOperateType();
            switch (opType){
                case 0:
                    showObj(true, downloadPane);
                    showObj(false, regPane, cssPane, pagePane, varPane);
                    break;
                case 1:
                case 3:
                    showObj(true, regPane);
                    showObj(false, downloadPane, cssPane, pagePane, varPane);
                    break;
                case 2:
                    showObj(true, cssPane);
                    showObj(false, downloadPane, regPane, pagePane, varPane);
                    break;
                case 4:
                    showObj(true, pagePane);
                    showObj(false, downloadPane, regPane, cssPane, varPane);
                    break;
                case 5:
                case 6:
                case 7:
                    showObj(true, varPane);
                    showObj(false, downloadPane, regPane, cssPane, pagePane);
                    break;
            }
        });

        varOperate.addItem(Operate.VAR_ADD);
//        varOperate.addItem(Operate.VAR_SET);

        showObj(true, downloadPane);
        showObj(false, regPane, cssPane, pagePane, varPane);
    }

    private void showObj(Boolean show, JComponent... components) {
        for (JComponent component : components) {
            component.setVisible(show);
        }
    }


    public String check() {

        switch (operateType.getSelectedIndex()) {
            case 0:
                if (StringUtil.isBlank(dir.getText())) {
                    this.setBackground(Color.ORANGE);
                    return "目录不可为空";
                }
                if (2 == fileNameFrom.getSelectedIndex()
                        && StringUtil.isBlank(fileName.getText())){
                    this.setBackground(Color.ORANGE);
                    return "自定义文件名不可为空";
                }
                if (1 == fileTypeFrom.getSelectedIndex()
                        && StringUtil.isBlank(fileType.getText())){
                    this.setBackground(Color.ORANGE);
                    return "自定义后缀不可为空";
                }
                break;
            case 1:
            case 3:
                if (StringUtil.isBlank(regSelector.getText())) {
                    this.setBackground(Color.ORANGE);
                    return "正则表达式不可为空";
                }
                if (regReplace.getSelectedIndex() == 1 && StringUtil.isBlank(regSource.getText())) {
                    this.setBackground(Color.ORANGE);
                    return "要替换的字符串不可为空";
                }
                if (regReplace.getSelectedIndex() == 2 && StringUtil.isBlank(regSource.getText())) {
                    this.setBackground(Color.ORANGE);
                    return "匹配结果作为变量时请填写变量名";
                }
                break;
            case 2:
                if (StringUtil.isBlank(cssSelector.getText())) {
                    this.setBackground(Color.ORANGE);
                    return "css选择器不可为空";
                }
                if (attrType.getSelectedIndex() == 0 && StringUtil.isBlank(attrName.getText())) {
                    this.setBackground(Color.ORANGE);
                    return "属性名不可为空";
                }
                break;
            case 4:
                if (StringUtil.isBlank(pageMin.getText()) || StringUtil.isBlank(pageMax.getText())) {
                    this.setBackground(Color.ORANGE);
                    return "页码数字错误";
                }
                break;
            case 5:
                if (StringUtil.isBlank(varName.getText()) || StringUtil.isBlank(varValue.getText())) {
                    this.setBackground(Color.ORANGE);
                    return "变量名称和值不可为空";
                }
                break;
        }
        return null;
    }

    private String blank(String src){
        return null == src || src.length() == 0 ? null : src;

    }
    public CatchStep toStep(){
        CatchStep catchStep = new CatchStep();
        catchStep.setOperateType(operateType.getSelectedIndex());

        catchStep.setDownloadDir(blank(dir.getText()));
        catchStep.setFileNameFrom(fileNameFrom.getSelectedIndex());
        catchStep.setFileName(blank(fileName.getText()));
        catchStep.setFileTypeFrom(fileTypeFrom.getSelectedIndex());
        catchStep.setFileType(blank(fileType.getText()));

        catchStep.setRegSelector(blank(regSelector.getText()));
        catchStep.setRegReplace(regReplace.getSelectedIndex());
        catchStep.setRegSource(blank(regSource.getText()));

        catchStep.setCssSelector(blank(cssSelector.getText()));
        catchStep.setAttrType(attrType.getSelectedIndex());
        catchStep.setAttrName(blank(attrName.getText()));

        catchStep.setPageMin(blank(pageMin.getText()));
        catchStep.setPageMax(blank(pageMax.getText()));

        catchStep.setVarOperate(varOperate.getSelectedIndex());
        catchStep.setVarName(blank(varName.getText()));
        catchStep.setVarValue(blank(varValue.getText()));
        return catchStep;
    }

    public void fromStep(CatchStep catchStep) {
        operateType.setSelectedIndex(catchStep.getOperateType());

        dir.setText(catchStep.getDownloadDir());
        fileNameFrom.setSelectedIndex(catchStep.getFileNameFrom());
        fileName.setText(catchStep.getFileName());
        fileTypeFrom.setSelectedIndex(catchStep.getFileTypeFrom());
        fileType.setText(catchStep.getFileType());

        regSelector.setText(catchStep.getRegSelector());
        regReplace.setSelectedIndex(catchStep.getRegReplace());
        regSource.setText(catchStep.getRegSource());

        cssSelector.setText(catchStep.getCssSelector());
        attrType.setSelectedIndex(catchStep.getAttrType());
        attrName.setText(catchStep.getAttrName());

        pageMin.setText(String.valueOf(catchStep.getPageMin()));
        pageMax.setText(String.valueOf(catchStep.getPageMax()));

        varOperate.setSelectedIndex(catchStep.getVarOperate());
        varName.setText(catchStep.getVarName());
        varValue.setText(catchStep.getVarValue());
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JButton del;
    private JButton add;
    private JPanel panel1;
    private JComboBox operateType;
    private JPanel downloadPane;
    private JComboBox fileNameFrom;
    private JComboBox fileTypeFrom;
    private JTextField fileType;
    private JTextField dir;
    private JButton dirBtn;
    private JTextField fileName;
    private JPanel regPane;
    private JLabel regLabel;
    private JTextField regSelector;
    private JComboBox regReplace;
    private JLabel label6;
    private JTextField regSource;
    private JPanel cssPane;
    private JLabel cssLabel;
    private JTextField cssSelector;
    private JComboBox attrType;
    private JTextField attrName;
    private JLabel attrNameLabel;
    private JPanel pagePane;
    private JLabel pageLable;
    private JLabel label7;
    private JTextField pageMax;
    private JTextField pageMin;
    private JPanel varPane;
    private JLabel label1;
    private JTextField varName;
    private JLabel label8;
    private JTextField varValue;
    private JComboBox varOperate;
    private JProgressBar progress;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    private JFileChooser fileChooser;

    public JProgressBar getProgress() {
        return progress;
    }
}
