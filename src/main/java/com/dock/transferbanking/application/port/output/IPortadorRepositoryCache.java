package com.dock.transferbanking.application.port.output;

import com.dock.transferbanking.application.domain.PortadorDomain;

public interface IPortadorRepositoryCache {

    void set(String key, PortadorDomain portadorDomain);

    PortadorDomain get(String key);

    void remove(String key);
}
