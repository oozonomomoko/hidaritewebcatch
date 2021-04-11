/*
 * Created by JFormDesigner on Thu Oct 17 11:35:45 CST 2019
 */

package hidari.jframe;

import com.alibaba.fastjson.JSON;
import hidari.CatchStarter;
import hidari.Log;
import hidari.dto.CatchConfig;
import hidari.dto.CatchStep;
import hidari.dto.StepConfig;
import org.jsoup.internal.StringUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class MainFrame extends JFrame {

    public MainFrame() {
        initComponents();
    }

    private void thisComponentResized(ComponentEvent e) {
        this.repaint();
    }

    /**
     * 导入配置
     * @param e
     */
    private void importConfig(MouseEvent e) {
        try {
            StepConfig config = JSON.parseObject(textArea1.getText(), StepConfig.class);
            CatchStep steps = config.getSteps();
            if (null == steps) {
                Log.error("导入配置为空");
                return;
            }
            proxy.setText(config.getProxy());
            source.setText(config.getSource());
            centerPane.removeAll();
            addStepPane(steps);
            centerPane.repaint();
            centerPane.validate();
        } catch (Exception e1) {
            Log.error("请将配置信息粘贴到控制台");
        }
    }

    private void addStepPane(CatchStep step) {
        StepPane stepPane = new StepPane();
        stepPane.fromStep(step);
        centerPane.add(stepPane);
        if (null != step.getNext())
            addStepPane(step.getNext());
    }

    /**
     * 导出配置
     * @param e
     */
    private void exportMouseClicked(MouseEvent e) {
        Component[] components = centerPane.getComponents();
        CatchStep steps = null;
        for (int i=components.length-1; i >=0; i --) {
            Component component = components[i];
            if (component.isVisible()) {
                StepPane stepPane = (StepPane) component;
                CatchStep step = stepPane.toStep();
                if (null != steps) {
                    step.setNext(steps);
                }
                steps = step;
            } else {
                centerPane.remove(component);
            }
        }

        StepConfig config = new StepConfig();
        config.setSteps(steps);
        config.setProxy(proxy.getText());
        config.setSource(source.getText());
        textArea1.setText(JSON.toJSONString(config, true));
    }

    /**
     * 开始执行
     * @param e
     */
    private void startMouseClicked(MouseEvent e) {
        if (!CatchConfig.init(proxy.getText())) {
            Log.error("代理设置错误");
            return;
        }

        if (!StringUtil.isBlank(source.getText())) {
            int stepLen = 0;
            Component[] components = centerPane.getComponents();
            CatchStep steps = null;
            for (int i=components.length-1; i >=0; i --) {
                Component component = components[i];
                if (component.isVisible()) {
                    StepPane stepPane = (StepPane) component;
                    String err = stepPane.check();
                    if (null != err) {
                        Log.error(err);
                        return;
                    }
                    CatchStep step = stepPane.toStep();
                    step.setProgress(stepPane.getProgress());
                    if (null != steps) {
                        step.setNext(steps);
                    }
                    steps = step;
                    stepLen++;
                } else {
                    centerPane.remove(component);
                }
            }
            if (stepLen == 0) {
                Log.error("未新增步骤");
                return;
            }
            try {
                CatchStarter.start(steps, source.getText(), Integer.valueOf(thCount.getText()));
            }catch (Exception e1) {
                e1.printStackTrace();
            }
        } else {
            Log.error("初始地址为空");
        }
    }

    /**
     * 清空控制台
     * @param e
     */
    private void clearConsoleMouseClicked(MouseEvent e) {
        textArea1.setText("");
        if (!CatchStarter.isWorking.get()) {
            // 清空所有进度条
            Component[] components = centerPane.getComponents();
            for (Component component:components) {
                if (component.isVisible()) {
                    StepPane stepPane = (StepPane) component;
                    stepPane.getProgress().setValue(0);
                    stepPane.getProgress().setMaximum(0);
                } else {
                    centerPane.remove(component);
                }
            }
            downloadProgress.setValue(0);
            downloadProgress.setMaximum(0);
        }
    }

    /**
     * 新增步骤
     * @param e
     */
    private void addStepMouseClicked(MouseEvent e) {
        centerPane.add(new StepPane(), 0);
        centerPane.validate();
    }

    private void stopMouseClicked(MouseEvent e) {
        CatchStarter.stop();
    }


    private void initComponents() {


        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        topPanel = new JPanel();
        label1 = new JLabel();
        source = new JTextField();
        start = new JButton();
        label2 = new JLabel();
        proxy = new JTextField();
        addStep = new JButton();
        stop = new JButton();
        exportBtn = new JButton();
        thCount = new JTextField();
        label3 = new JLabel();
        scrollPane1 = new JScrollPane();
        centerPane = new JPanel();
        consolePane = new JScrollPane();
        textArea1 = new JTextArea();
        clearConsole = new JButton();
        downloadProgress = new JProgressBar();
        label4 = new JLabel();
        importBtn = new JButton();

        //======== this ========
        setBackground(Color.white);
        setTitle("\u722c\u866b46.0");
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                thisComponentResized(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== topPanel ========
        {
            topPanel.setOpaque(false);
            topPanel.setBackground(Color.white);
            topPanel.setLayout(null);

            //---- label1 ----
            label1.setText("\u521d\u59cb\u5730\u5740");
            label1.setFont(new Font("Serif", Font.PLAIN, 12));
            topPanel.add(label1);
            label1.setBounds(new Rectangle(new Point(390, 10), label1.getPreferredSize()));

            //---- source ----
            source.setFont(new Font("Serif", Font.PLAIN, 12));
            topPanel.add(source);
            source.setBounds(450, 7, 296, source.getPreferredSize().height);

            //---- start ----
            start.setText("\u5f00\u59cb");
            start.setBackground(UIManager.getColor("Button.background"));
            start.setFont(new Font("Serif", Font.PLAIN, 12));
            start.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    startMouseClicked(e);
                }
            });
            topPanel.add(start);
            start.setBounds(750, 5, 70, 29);

            //---- label2 ----
            label2.setText("\u4ee3\u7406\u8bbe\u7f6e(ip:port)");
            label2.setFont(new Font("Serif", Font.PLAIN, 12));
            topPanel.add(label2);
            label2.setBounds(130, 10, 100, label2.getPreferredSize().height);

            //---- proxy ----
            proxy.setFont(new Font("Serif", Font.PLAIN, 12));
            topPanel.add(proxy);
            proxy.setBounds(240, 7, 140, proxy.getPreferredSize().height);

            //---- addStep ----
            addStep.setText("\u65b0\u589e\u6b65\u9aa4");
            addStep.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    addStepMouseClicked(e);
                }
            });
            topPanel.add(addStep);
            addStep.setBounds(20, 5, 86, 28);

            //---- stop ----
            stop.setText("\u505c\u6b62");
            stop.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    stopMouseClicked(e);
                }
            });
            topPanel.add(stop);
            stop.setBounds(835, 5, 70, 29);

            //---- exportBtn ----
            exportBtn.setText("\u5bfc\u51fa");
            exportBtn.setFont(new Font("Serif", Font.PLAIN, 12));
            exportBtn.setBackground(UIManager.getColor("Button.background"));
            exportBtn.setToolTipText("\u5bfc\u51fa\u5f53\u524d\u914d\u7f6e\u5230\u63a7\u5236\u53f0");
            exportBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    exportMouseClicked(e);
                }
            });
            topPanel.add(exportBtn);
            exportBtn.setBounds(new Rectangle(new Point(1100, 7), exportBtn.getPreferredSize()));

            //---- thCount ----
            thCount.setText("20");
            topPanel.add(thCount);
            thCount.setBounds(960, 10, 40, thCount.getPreferredSize().height);

            //---- label3 ----
            label3.setText("\u5e76\u53d1");
            topPanel.add(label3);
            label3.setBounds(new Rectangle(new Point(930, 15), label3.getPreferredSize()));

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < topPanel.getComponentCount(); i++) {
                    Rectangle bounds = topPanel.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = topPanel.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                topPanel.setMinimumSize(preferredSize);
                topPanel.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(topPanel);
        topPanel.setBounds(0, 0, 1170, 40);

        //======== scrollPane1 ========
        {

            //======== centerPane ========
            {
                centerPane.setBackground(Color.white);
                centerPane.setPreferredSize(new Dimension(890, 400));
                centerPane.setAutoscrolls(true);
                centerPane.setLayout(new FlowLayout(FlowLayout.LEADING));
            }
            scrollPane1.setViewportView(centerPane);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(0, 40, 1175, 450);

        //======== consolePane ========
        {

            //---- textArea1 ----
            textArea1.setBackground(new Color(243, 243, 243));
            consolePane.setViewportView(textArea1);
        }
        contentPane.add(consolePane);
        consolePane.setBounds(1, 515, 1174, 195);

        //---- clearConsole ----
        clearConsole.setText("\u6e05\u7a7a\u63a7\u5236\u53f0");
        clearConsole.setFont(new Font("Serif", Font.PLAIN, 12));
        clearConsole.setBackground(UIManager.getColor("Button.background"));
        clearConsole.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearConsoleMouseClicked(e);
            }
        });
        contentPane.add(clearConsole);
        clearConsole.setBounds(1055, 490, 94, 23);

        //---- downloadProgress ----
        downloadProgress.setMaximum(0);
        contentPane.add(downloadProgress);
        downloadProgress.setBounds(65, 491, 965, 23);

        //---- label4 ----
        label4.setText("\u4e0b\u8f7d\u8fdb\u5ea6");
        contentPane.add(label4);
        label4.setBounds(new Rectangle(new Point(10, 495), label4.getPreferredSize()));

        //---- importBtn ----
        importBtn.setText("\u5bfc\u5165");
        importBtn.setFont(new Font("Serif", Font.PLAIN, 12));
        importBtn.setBackground(UIManager.getColor("Button.background"));
        importBtn.setToolTipText("\u8bf7\u5c06\u914d\u7f6e\u4fe1\u606f\u7c98\u8d34\u5230\u63a7\u5236\u53f0");
        importBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                importConfig(e);
            }
        });
        contentPane.add(importBtn);
        importBtn.setBounds(new Rectangle(new Point(1025, 7), importBtn.getPreferredSize()));

        { // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
        other();
    }

    private void other() {
        centerPane.add(new StepPane());
        CatchStarter.downloadProgress = downloadProgress;
        Log.init(textArea1);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel topPanel;
    private JLabel label1;
    private JTextField source;
    private JButton start;
    private JLabel label2;
    private JTextField proxy;
    private JButton addStep;
    private JButton stop;
    private JButton exportBtn;
    private JTextField thCount;
    private JLabel label3;
    private JScrollPane scrollPane1;
    private JPanel centerPane;
    private JScrollPane consolePane;
    private JTextArea textArea1;
    private JButton clearConsole;
    private JProgressBar downloadProgress;
    private JLabel label4;
    private JButton importBtn;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JFrame frame = new MainFrame();
        frame.setVisible(true);
        frame.setResizable(false);
        try {
            frame.setIconImage(ImageIO.read(MainFrame.class.getResource("/icon.png")));
        } catch (IOException e) {
            Log.error(e);
        }
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
