package io.netty.example.study.demo1.common.order;

import io.netty.example.study.demo1.common.OperationResult;
import lombok.Data;

@Data
public class OrderOperationResult extends OperationResult {

    private final int tableId;
    private final String dish;
    private final boolean complete;

}
