package org.hgq.service;

import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2021-08-30 19:40
 **/
@Service
public class OrderServiceImpl implements OrderService {

    int a;
    @Override
    public String getOrder() {
        return "a";
    }

    @Override
    public int increment() {
        return a++;
    }

}
