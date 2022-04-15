package org.hgq.service;

import java.util.Map;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-04-15 17:10
 **/

public interface CollectionDerateService {
    void startProcess(Long id);

    public void audit(Long id, Long userId, Map<String, Object> variables);
}
