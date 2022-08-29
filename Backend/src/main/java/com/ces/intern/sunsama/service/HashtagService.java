package com.ces.intern.sunsama.service;

import com.ces.intern.sunsama.dto.HashtagDTO;
import com.ces.intern.sunsama.entity.HashtagEntity;
import com.ces.intern.sunsama.http.response.HashtagResponse;

import java.util.List;

public interface HashtagService{
    List getAllHashtag();
    HashtagDTO getHashtagById(long id);
    String save(HashtagDTO hashtagRequest);

    HashtagResponse update(HashtagResponse hashtagDTO);
    void delete(Long id);
}
