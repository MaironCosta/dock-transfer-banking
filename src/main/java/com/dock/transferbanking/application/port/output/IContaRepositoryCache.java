package com.dock.transferbanking.application.port.output;

import com.dock.transferbanking.adapter.output.repository.cache.conta.ContaCache;
import com.dock.transferbanking.application.domain.ContaDomain;
import com.dock.transferbanking.application.domain.PortadorDomain;

public interface IContaRepositoryCache {

    void set(String key, ContaDomain contaDomain);

    ContaDomain get(String key);

    void remove(String key);
}
