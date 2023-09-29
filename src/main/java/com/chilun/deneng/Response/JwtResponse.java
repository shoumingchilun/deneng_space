package com.chilun.deneng.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @auther 齿轮
 * @create 2023-07-07-9:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse extends BaseResponse {
    private String jwtData;
}
