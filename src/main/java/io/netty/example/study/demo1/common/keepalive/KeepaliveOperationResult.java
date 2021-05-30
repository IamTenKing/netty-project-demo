package io.netty.example.study.demo1.common.keepalive;

import io.netty.example.study.demo1.common.OperationResult;
import lombok.Data;

@Data
public class KeepaliveOperationResult extends OperationResult {

    private final long time;

}
