package hidari.dto;

import java.util.List;

/**
 * @author 左手掐腰
 * @since 2019/11/04 16:58
 */
public class StepConfig {

    private String proxy;

    private String source;

    private CatchStep steps;

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public CatchStep getSteps() {
        return steps;
    }

    public void setSteps(CatchStep steps) {
        this.steps = steps;
    }
}
