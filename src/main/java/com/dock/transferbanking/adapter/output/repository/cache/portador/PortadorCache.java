package com.dock.transferbanking.adapter.output.repository.cache.portador;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@NoArgsConstructor
@Data
@RedisHash(value = "Portador", timeToLive = 300)
public class PortadorCache {

    @Id
    private String key;

    private String result;

}
