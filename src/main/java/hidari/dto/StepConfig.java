package hidari.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 左手掐腰
 * @since 2019/11/04 16:58
 */
@Getter
@Setter
public class StepConfig {

    private String proxy;

    private String source;

    private CatchStep steps;
}
