package io.netty.example.study.demo1.common.auth;

import io.netty.example.study.demo1.common.OperationResult;
import lombok.Data;

@Data
public class AuthOperationResult extends OperationResult {

    private final boolean passAuth;

}
