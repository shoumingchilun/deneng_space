package com.chilun.deneng.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @auther 齿轮
 * @create 2023-07-05-14:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private String message;
    private ResultCode code = ResultCode.SUCCESS;

    public boolean isSuccess() {
        return code == ResultCode.SUCCESS;
    }
}
